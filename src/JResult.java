//import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

import static com.sun.javafx.tk.Toolkit.getToolkit;

public class JResult
{
	public enum ActionModifiers
	{
		Undefined,

		QT,			// Quarterback Trapped
		QR,			// Quarterback Runs
		FG,			// Shanked Punt
		NG,			// Fieldgoal No Good
		SOP,		// Sprintout Pass
		BLP,		// Bootleg Pass
		INC,		// Incomplete Pass

		INT,		// Interception
		F,			// Fumble
		BK,			// Blocked Kick
		PI,			// Pass Interference
		OFF,		// Offensive Penalty
		DEF,		// Defensive Penalty
		OFFX,		// Offensive Deadball Penalty
		DEFX,		// Defensive Deadball Penalty
	}

	public enum YardageModifiers
	{
		Undefined,

		DS_GAIN,	// Variable Yards Gained Direct Sum
		DS_LOSS,	// Variable Yards Lost Direct Sum
		X_GAIN,		// Variable Yards Gained
		X_LOSS,		// Variable Yards Lost
		B,			// Breakaway
		TD,			// Touchdown
		KO,			// Booming Punt
		T1,			// Variable Yards Gained T1
		T2,			// Variable Yards Gained T2
		T3,			// Variable Yards Gained T3
		S,			//
		R,
	}

	public enum OtherModifiers
	{
		Undefined,

		IO,			// Ignore Other
		OB,			// Out of bounds
		PNR,		// Punt not returned
		RECOVERED,  // on side kick recovered
		B_RUSH,
		B_PASS,
		F_LOST,
		F_RECOVERED,
		FD,			// First down
		HALF_DISTANCE,	// penalty is marked half the distance to the goal
	}

	public enum Who
	{
		offense,
		defense,
		combined,
		undefined
	}

	private YardageModifiers _yardageModifier;
	private ActionModifiers _actionModifier;
	private ArrayList<OtherModifiers> _otherModifiers = new ArrayList<>();
	private String _source = "";
	private Who _who = Who.undefined;

	private int _yards;
	public int Yards() { return _yards; }
	public void OtherModifier(OtherModifiers modifier) { _otherModifiers.add(modifier); }

	private JGame.Plays _play;
	public void Play(JGame.Plays play) { _play = play; }
	public JGame.Plays Play() { return _play; }

	public String toString()
	{
		String s = "";
		s = s + "Result:";
		s = s + "\n    Yards: " + _yards;
		s = s + "\n    Play : " + _play;
		if(_yardageModifier != YardageModifiers.Undefined)	s = s + "\n    yMod : " + _yardageModifier;
		if(_actionModifier != ActionModifiers.Undefined)	s = s + "\n    aMod : " + _actionModifier;

		for(int i=0; i<_otherModifiers.size(); i++)
		{
			if(i==0)
				s = s + "\n    oMods: " + _otherModifiers.get(i);
			else
				s = s + "\n         : " + _otherModifiers.get(i);
		}

		s = s + "\n    Who  : " + _who;

		return s;
	}
	public void log()
	{
		JLog.writeln("Result:");
		JLog.writeln("    Yards: " + _yards);
		JLog.writeln("    Play : " + _play);
		if(_yardageModifier != YardageModifiers.Undefined)	JLog.writeln("    yMod : " + _yardageModifier);
		if(_actionModifier != ActionModifiers.Undefined)	JLog.writeln("    aMod : " + _actionModifier);

		for(int i=0; i<_otherModifiers.size(); i++)
		{
			if(i==0)
				JLog.writeln("    oMods: " + _otherModifiers.get(i));
			else
				JLog.writeln("         : " + _otherModifiers.get(i));
		}

		JLog.writeln("    Who  : " + _who);
	}

	public JResult(String result)
	{
		this.Interpret(result);
	}

	public JResult(JResult result)
	{
		this.copy(result);
	}

	public JResult()
	{
		_yards = 0;
		_play = JGame.Plays.Undefined;

		_yardageModifier = YardageModifiers.Undefined;
		_actionModifier = ActionModifiers.Undefined;
		//_otherModifier = OtherModifiers.Undefined;
	}

	public JResult(JGame.Plays playOffense, JGame.Plays playDefense)
	{
		JLog.writeln("JResult: " + playOffense + ", " + playDefense);
		JLog.addindent();
		if(playOffense == JGame.Plays.OnsidesKick)
		{
			// ONSIDE: Ball travels 12 yards. Kicking teams rolls offensive dice. Kicking Team recovers on totals of 13-20.
			//         Receiving Team recovers on 10-12,21-39. No return.

			_yards = 12;
			_play = JGame.Plays.OnsidesKick;

			_yardageModifier = YardageModifiers.Undefined;
			_actionModifier = ActionModifiers.Undefined;
			//_otherModifier = OtherModifiers.Undefined;

			int roll = JGame.dice.offense();
			if(roll >= 13 && roll <= 20)
			{
				_otherModifiers.add(OtherModifiers.RECOVERED);
			}
		}

		else if(playOffense == JGame.Plays.SquibKick)
		{
			// SQUIB: Ball travels 40 yards. REceiving team rolls offensive dice and consults Squib Kickoff
			//        Return Column for return yardage

			_yards = 40;
			_play = JGame.Plays.SquibKick;

			_yardageModifier = YardageModifiers.Undefined;
			_actionModifier = ActionModifiers.Undefined;
			//_otherModifier = OtherModifiers.Undefined;
		}

		else if(playOffense == JGame.Plays.SquibKickReturn)
		{
			// SQUIB: Ball travels 40 yards. REceiving team rolls offensive dice and consults Squib Kickoff
			//        Return Column for return yardage
			//squibKickResults[]
			int roll = JGame.dice.offense() - 10;
			copy(JGame.squibKickResults[roll]);
			_play = JGame.Plays.SquibKickReturn;
		}

		else
		{
			JResult resultOffense = GetResultOffense(playOffense);
			if(resultOffense.DEF() ||
					resultOffense.DEFX() ||
					resultOffense.OFF() ||
					resultOffense.OFFX() ||
					resultOffense.PI())
			{
				// Yards	OFF-S	DEF-S	OFF-R	DEF-R
				// -----	-----	-----	-----	-----
				// 5		10-32	13-34	10-25	10-24
				// 5X*		None	35-36	None	25-26
				// 10		33-35	10-12	26-34	27-29
				// 15*		36-39	37-39	35-39	30-39

				int o = JGame.dice.offense();
				resultOffense._yards = 5;

				if(resultOffense.S())
				{
					if(resultOffense.OFF() || resultOffense.OFFX())
					{
						if(o >= 10 && 0 <= 21)	resultOffense._yards = 5;
						if(o >= 33 && 0 <= 35)	resultOffense._yards = 10;
						if(o >= 36 && 0 <= 39)	resultOffense._yards = 15;
					}
					else
					{
						if(o >= 13 && 0 <= 36)	resultOffense._yards = 5;
						if(o >= 10 && 0 <= 12)	resultOffense._yards = 10;
						if(o >= 37 && 0 <= 39)	resultOffense._yards = 15;
					}
				}

				else if(resultOffense.R())
				{
					if(resultOffense.OFF() || resultOffense.OFFX())
					{
						if(o >= 10 && 0 <= 25)	resultOffense._yards = 5;
						if(o >= 26 && 0 <= 34)	resultOffense._yards = 10;
						if(o >= 35 && 0 <= 39)	resultOffense._yards = 15;
					}
					else
					{
						if(o >= 10 && 0 <= 26)	resultOffense._yards = 5;
						if(o >= 27 && 0 <= 29)	resultOffense._yards = 10;
						if(o >= 30 && 0 <= 39)	resultOffense._yards = 15;
					}
				}

				copy(resultOffense);
			}

			else if(playDefense != JGame.Plays.Undefined)
			{
				JResult resultDefense = GetResultDefense(playOffense, playDefense);
				JGame.resultStack.add(resultOffense);
				JGame.resultStack.add(resultDefense);

				resultOffense.log();
				resultDefense.log();
				copy(Priority(resultOffense, resultDefense));
				_who = Who.combined;
			}
			else
			{
				copy(resultOffense);
			}
		}

		if(F())
		{
			if(JGame.dice.offense() > JGame.data.teams[JGame.data.offense].fumble)
				OtherModifier(OtherModifiers.F_LOST);
			else
				OtherModifier(OtherModifiers.F_RECOVERED);
		}

		log();
		JGame.resultStack.add(this);
		JLog.subindent();
	}
	
	private void copy(JResult result)
	{
		_yards = result._yards;
		_play = result._play;

		_yardageModifier = result._yardageModifier;
		_actionModifier = result._actionModifier;

		for(int i=0; i<result._otherModifiers.size(); i++)
		{
			_otherModifiers.add(result._otherModifiers.get(i));
		}

		_source = result._source;
	}

	static Boolean Validate( String str )
	{
		str = str.replace( "0", "" );
		str = str.replace( "1", "" );
		str = str.replace( "2", "" );
		str = str.replace( "3", "" );
		str = str.replace( "4", "" );
		str = str.replace( "5", "" );
		str = str.replace( "6", "" );
		str = str.replace( "7", "" );
		str = str.replace( "8", "" );
		str = str.replace( "9", "" );
		str = str.replace( "+", "" );
		str = str.replace( "-", "" );
		str = str.replace( " ", "" );

		if( str.length() > 0 )
			return false;
		else
			return true;
	}

	public void Interpret(String str)
	{
		_source = str;

		try
		{
			str = str.toUpperCase();

			i = -1;
			if((i = str.indexOf("(")) >= 0)
			{
				str = str.replace("(", "");
				str = str.replace(")", "");

				_otherModifiers.add(OtherModifiers.IO);
				Interpret(str);
			}

			else if((i = str.indexOf("[")) >= 0)
			{
				str = str.replace("[", "");
				str = str.replace("]", "");

				_otherModifiers.add(OtherModifiers.IO);
				Interpret(str);
			}

			else if((i = str.indexOf("*")) >= 0)
			{
				str = str.replace("*", "");

				_otherModifiers.add(OtherModifiers.OB);
				Interpret(str);
			}

			else if((i = str.indexOf("DEFX")) >= 0)
			{
				str = str.replace("DEFX", "");
				_actionModifier = ActionModifiers.DEFX;
				Interpret(str);
			}

			else if((i = str.indexOf("OFFX")) >= 0)
			{
				str = str.replace("OFFX", "");
				_actionModifier = ActionModifiers.OFFX;
				Interpret(str);
			}

			else if((i = str.indexOf("BLP")) >= 0)
			{
				_actionModifier = ActionModifiers.BLP;
			}

			else if((i = str.indexOf("DEF")) >= 0)
			{
				str = str.replace("DEF", "");
				_actionModifier = ActionModifiers.DEF;
				Interpret(str);
			}

			else if((i = str.indexOf("INC")) >= 0)
			{
				_actionModifier = ActionModifiers.INC;
			}

			else if((i = str.indexOf("INT")) >= 0)
			{
				str = str.replace("INT", "");
				_actionModifier = ActionModifiers.INT;
				Interpret(str);
			}

			else if((i = str.indexOf("OFF")) >= 0)
			{
				str = str.replace("OFF", "");
				_actionModifier = ActionModifiers.OFF;
				Interpret(str);
			}

			else if((i = str.indexOf("SOP")) >= 0)
			{
				_actionModifier = ActionModifiers.SOP;
			}

			else if((i = str.indexOf("BK")) >= 0)
			{
				str = str.replace("BK", "");
				_actionModifier = ActionModifiers.BK;
				Interpret(str);
			}

			else if((i = str.indexOf("FG")) >= 0)
			{
				_actionModifier = ActionModifiers.FG;
				_yards = -1;
			}

			else if((i = str.indexOf("KO")) >= 0)
			{
				_yardageModifier = YardageModifiers.KO;
				_yards = 1;
			}

			else if((i = str.indexOf("NG")) >= 0)
			{
				_actionModifier = ActionModifiers.NG;
				_yards = -1;
			}

			else if((i = str.indexOf("PI")) >= 0)
			{
				str = str.replace("PI", "");
				_actionModifier = ActionModifiers.PI;
				Interpret(str);
			}

			else if((i = str.indexOf("QR")) >= 0)
			{
				_actionModifier = ActionModifiers.QR;
			}

			else if((i = str.indexOf("QT")) >= 0)
			{
				_actionModifier = ActionModifiers.QT;
			}

			else if((i = str.indexOf("T1")) >= 0)
			{
				_yardageModifier = YardageModifiers.T1;
				_yards = 1;
			}

			else if((i = str.indexOf("T2")) >= 0)
			{
				_yardageModifier = YardageModifiers.T2;
				_yards = 1;
			}

			else if((i = str.indexOf("T3")) >= 0)
			{
				_yardageModifier = YardageModifiers.T3;
				_yards = 1;
			}

			else if((i = str.indexOf("TD")) >= 0)
			{
				_yardageModifier = YardageModifiers.TD;
				_yards = 1;
			}

			else if((i = str.indexOf("B")) >= 0)
			{
				_yardageModifier = YardageModifiers.B;
				_yards = 1;
			}

			else if((i = str.indexOf("F")) >= 0)
			{
				str = str.replace("F", "");
				_actionModifier = ActionModifiers.F;
				Interpret(str);
			}

			else if((i = str.indexOf("-DS")) >= 0)
			{
				_yardageModifier = YardageModifiers.DS_LOSS;
				_yards = -1;
			}

			else if((i = str.indexOf("DS")) >= 0)
			{
				_yardageModifier = YardageModifiers.DS_GAIN;
				_yards = 1;
			}

			else if((i = str.indexOf("-X")) >= 0)
			{
				_yardageModifier = YardageModifiers.X_LOSS;
				_yards = -1;
			}

			else if((i = str.indexOf("X")) >= 0)
			{
				_yardageModifier = YardageModifiers.X_GAIN;
				_yards = 1;
			}

			else if((i = str.indexOf("S")) >= 0)
			{
				_yardageModifier = YardageModifiers.S;
				_yards = 1;
			}

			else if((i = str.indexOf("R")) >= 0)
			{
				_yardageModifier = YardageModifiers.R;
				_yards = 1;
			}

			else if((i = str.indexOf("O")) >= 0)
			{
				str = str.replace("O", "");
				Interpret(str);
			}

			else if(!Validate(str))
			{
				@SuppressWarnings("unused")
				String s = "Uninterpreted value \"";
				s += str;
				s += "\" found in results data.";
			}

			else
			{
//				Number n;
				try
				{
					if(str.length() > 0)
					{
						str = str.replace("+", "");
						str = str.replace(" ", "");
						if(str.length() > 0)
							_yards = Integer.parseInt(str);
						else
							_yards = 0;
					}
					else
						_yards = 0;
				}
				catch(Exception e)
				{
					System.out.println("Results.cs: Exception thrown: String [" + str + "]");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Results.cs: Exception caught");
		}
	}

	public void Record(String recordFilename,int id)
	{
	}

	public void Playback(String recordFilename,int id)
	{
	}

	public Boolean QT() { return (_actionModifier == ActionModifiers.QT); }
	public Boolean QR() { return (_actionModifier == ActionModifiers.QR); }
	public Boolean FG() { return (_actionModifier == ActionModifiers.FG); }
	public Boolean NG() { return (_actionModifier == ActionModifiers.NG); }
	public Boolean SOP() { return (_actionModifier == ActionModifiers.SOP); }
	public Boolean BLP() { return (_actionModifier == ActionModifiers.BLP); }
	public Boolean INC() { return (_actionModifier == ActionModifiers.INC); }

	public Boolean INT() { return (_actionModifier == ActionModifiers.INT); }
	public Boolean F() { return (_actionModifier == ActionModifiers.F); }
	public Boolean BK() { return (_actionModifier == ActionModifiers.BK); }
	public Boolean PI() { return (_actionModifier == ActionModifiers.PI); }
	public Boolean DEF() { return (_actionModifier == ActionModifiers.DEF); }
	public Boolean OFF() { return (_actionModifier == ActionModifiers.OFF); }
	public Boolean DEFX() { return (_actionModifier == ActionModifiers.DEFX); }
	public Boolean OFFX() { return (_actionModifier == ActionModifiers.OFFX); }

	public Boolean DS()
	{
		return (_yardageModifier == YardageModifiers.DS_GAIN) || (_yardageModifier == YardageModifiers.DS_LOSS);
	}
	public Boolean DS_GAIN() { return (_yardageModifier == YardageModifiers.DS_GAIN); }
	public Boolean DS_LOSS() { return (_yardageModifier == YardageModifiers.DS_LOSS); }
	public Boolean X()
	{
		return (_yardageModifier == YardageModifiers.X_GAIN) || (_yardageModifier == YardageModifiers.X_LOSS);
	}
	public Boolean X_GAIN() { return (_yardageModifier == YardageModifiers.X_GAIN); }
	public Boolean X_LOSS() { return (_yardageModifier == YardageModifiers.X_LOSS); }
	public Boolean B() { return (_yardageModifier == YardageModifiers.B); }
	public Boolean TD() { return (_yardageModifier == YardageModifiers.TD); }
	public Boolean KO() { return (_yardageModifier == YardageModifiers.KO); }
	public Boolean T1() { return (_yardageModifier == YardageModifiers.T1); }
	public Boolean T2() { return (_yardageModifier == YardageModifiers.T2); }
	public Boolean T3() { return (_yardageModifier == YardageModifiers.T3); }
	public Boolean S() { return (_yardageModifier == YardageModifiers.S); }
	public Boolean R() { return (_yardageModifier == YardageModifiers.R); }

	public Boolean IO()
	{
		for(int i=0; i<_otherModifiers.size(); i++)
		{
			if(_otherModifiers.get(i) == OtherModifiers.IO)
			{
				return true;
			}
		}
		return false;
	}

	public Boolean OB()
	{
		for(int i=0; i<_otherModifiers.size(); i++)
		{
			if(_otherModifiers.get(i) == OtherModifiers.OB)
			{
				return true;
			}
		}
		return false;
	}

	public Boolean PNR()
	{
		for(int i=0; i<_otherModifiers.size(); i++)
		{
			if(_otherModifiers.get(i) == OtherModifiers.PNR)
			{
				return true;
			}
		}
		return false;
	}

	public Boolean RECOVERED()
	{
		for(int i=0; i<_otherModifiers.size(); i++)
		{
			if(_otherModifiers.get(i) == OtherModifiers.RECOVERED)
			{
				return true;
			}
		}
		return false;
	}

	public Boolean B_PASS()
	{
		for(int i=0; i<_otherModifiers.size(); i++)
		{
			if(_otherModifiers.get(i) == OtherModifiers.B_PASS)
			{
				return true;
			}
		}
		return false;
	}

	public Boolean B_RUSH()
	{
		for(int i=0; i<_otherModifiers.size(); i++)
		{
			if(_otherModifiers.get(i) == OtherModifiers.B_RUSH)
			{
				return true;
			}
		}
		return false;
	}

	public Boolean F_LOST()
	{
		for(int i=0; i<_otherModifiers.size(); i++)
		{
			if(_otherModifiers.get(i) == OtherModifiers.F_LOST)
			{
				return true;
			}
		}
		return false;
	}

	public Boolean F_RECOVERED()
	{
		for(int i=0; i<_otherModifiers.size(); i++)
		{
			if(_otherModifiers.get(i) == OtherModifiers.F_RECOVERED)
			{
				return true;
			}
		}
		return false;
	}

	public Boolean FD()
	{
		for(int i=0; i<_otherModifiers.size(); i++)
		{
			if(_otherModifiers.get(i) == OtherModifiers.FD)
			{
				return true;
			}
		}
		return false;
	}

	public Boolean HALF_DISTANCE()
	{
		for(int i=0; i<_otherModifiers.size(); i++)
		{
			if(_otherModifiers.get(i) == OtherModifiers.HALF_DISTANCE)
			{
				return true;
			}
		}
		return false;
	}

	public static JResult GetResultDefense(JGame.Plays playOffense, JGame.Plays playDefense)
	{
		JLog.writeln("GetResultDefense: " + playOffense + ", " + playDefense);
		JLog.addindent();
		JResult result = null;
		int roll = 0;
		int p = 0;
		int d = 0;

		try
		{
			if(playDefense != JGame.Plays.Undefined)
			{
				// if there are debug results in the list use those first.  Get the first
				// result then pop it off the list.
				if(JGame.resultStackDebug.size() > 0)
				{
					result = JGame.resultStackDebug.get(0);
					JGame.resultStackDebug.remove(0);
				}
				else
				{
					roll = JGame.dice.defense() - 1;
					p = JGame.playIndex(playOffense);

					if(playDefense == JGame.Plays.Standard) d = 0;
					else if(playDefense == JGame.Plays.ShortGaps) d = 5;
					else if(playDefense == JGame.Plays.ShortWide) d = 10;
					else if(playDefense == JGame.Plays.ShortPass) d = 15;
					else if(playDefense == JGame.Plays.LongPass) d = 20;
					else if(playDefense == JGame.Plays.Blitz) d = 25;

					result = JGame.data.teams[JGame.data.defense].defenseResults[roll + d][p];
				}

				result._play = playOffense;
			}
		}
		catch(Exception e)
		{
			String msg = "roll = " + roll +
							"\np = " + p +
							"\nd = " + d;
			(new JMessage("Exception caught in GetResultDefense", msg)).showAndWait();
		}

		result._who = Who.defense;
		result.log();
		JLog.subindent();
		return result;
	}

	public static JResult GetResultOffense(JGame.Plays playOffense)
	{
		JLog.writeln("GetResultOffense: " +playOffense);
		JLog.addindent();
		JResult result = null;
		int p = 0;
		int roll = 0;

		try
		{
			// if there are debug results in the list use those first.  Get the first
			// result then pop it off the list.
			if(JGame.resultStackDebug.size() > 0)
			{
				result = JGame.resultStackDebug.get(0);
				JGame.resultStackDebug.remove(0);
				result._play = playOffense;
			}
			else
			{
				p = JGame.playIndex(playOffense);
				roll = JGame.dice.offense() - 10;
				result = JGame.data.teams[JGame.data.offense].offenseResults[roll][p];

				result._play = playOffense;
			}

			// if the result is a penalty, process it, add it to the spots list and
			// get another result
			if(result.PI() ||
				result.DEF() ||
				result.DEFX() ||
				result.OFF() ||
				result.OFFX())
			{
				if(result.X_GAIN())
				{
					result._yards = 5;
				} else if(result.X_LOSS())
				{
					result._yards = -5;
				}
			}
			else if(result.DS())
			{
				if(result.DS_GAIN())
					result._yards = JGame.dice.DS();
				else
					result._yards = -JGame.dice.DS();
			}
			else if(result.X())
			{
				if(result.X_GAIN())
					result._yards = 40 - JGame.dice.offense();
				else
					result._yards = JGame.dice.DS() - 40;
			}
			else if(result.T1())
			{
				result._yards = JGame.dice.offense();
			}
			else if(result.T2())
			{
				result._yards = JGame.dice.offense() +
						JGame.dice.offense();
			}
			else if(result.T3())
			{
				result._yards = JGame.dice.offense() +
						JGame.dice.offense() +
						JGame.dice.offense();
			}

			else if(result.TD())
			{
				result._yards = 120;
			}
		}
		catch(Exception e)
		{
			String msg = "roll = " + roll +
					"\np = " + p +
					"\nresult = " + result;
			(new JMessage("Exception caught in GetResultOffense", msg)).showAndWait();
		}

		result._who = Who.offense;
		result.log();
		JLog.subindent();
		return result;
	}

	public static JResult Priority(JResult resultOffense,JResult resultDefense)
	{
		JResult result = new JResult(resultOffense);

		if(resultOffense.IO())
		{
			if(resultDefense.IO())
			{
				result.copy(resultDefense);
			}
			else
			{
				result.copy(resultOffense);
			}
		}

		else if(resultOffense.TD())
		{
			if(resultDefense.IO() ||
					resultDefense.INT() ||
					resultDefense.F() ||
					resultDefense.INC())
			{
				result.copy(resultDefense);
			}

			else if(resultDefense.QR())
			{
				result.copy(new JResult(JGame.Plays.QuarterbackRun, JGame.Plays.Undefined));
			}

			else if(resultDefense.QT())
			{
				result.copy(new JResult(JGame.Plays.QuarterbackTrapped, JGame.Plays.Undefined));
			}

			else
			{
				result.copy(resultOffense);
			}
		}

		else if(resultOffense.B())
		{
			if(resultDefense.IO() ||
				resultDefense.INT() ||
				resultDefense.F() ||
				resultDefense.INC())
			{
				result.copy(resultDefense);
			}

			else if(resultDefense.QR())
			{
				result.copy(new JResult(JGame.Plays.QuarterbackRun, JGame.Plays.Undefined));
			}

			else if(resultDefense.QT())
			{
				result.copy(new JResult(JGame.Plays.QuarterbackTrapped, JGame.Plays.Undefined));
			}

			else
			{
				JGame.Plays play = result.Play();
				result.copy(new JResult(JGame.Plays.Breakaway, JGame.Plays.Undefined));
				result._yards += resultDefense._yards;
				//result.Play(play);

				if(play == JGame.Plays.Screen ||
						play == JGame.Plays.SprintOut ||
						play == JGame.Plays.Bootleg ||
						play == JGame.Plays.Dropback)
					result.OtherModifier(OtherModifiers.B_PASS);
				else
					result.OtherModifier(OtherModifiers.B_RUSH);
			}
		}

		else if(resultOffense.QR())
		{
			if(resultDefense.IO() ||
					resultDefense.INT() ||
					resultDefense.F() ||
					resultDefense.INC())
			{
				result.copy(resultDefense);
			}

			else if(resultDefense.QR())
			{
				result.copy(new JResult(JGame.Plays.QuarterbackRun, JGame.Plays.Undefined));
			}

			else if(resultDefense.QT())
			{
				result.copy(new JResult(JGame.Plays.QuarterbackTrapped, JGame.Plays.Undefined));
			}

			else
			{
				result.copy(new JResult(JGame.Plays.QuarterbackRun, JGame.Plays.Undefined));
				result._yards += resultDefense._yards;
			}
		}

		else if(resultOffense.QT())
		{
			if(resultDefense.IO() ||
					resultDefense.INT() ||
					resultDefense.F() ||
					resultDefense.INC())
			{
				result.copy(resultDefense);
			}

			else if(resultDefense.QR())
			{
				result.copy(new JResult(JGame.Plays.QuarterbackRun, JGame.Plays.Undefined));
			}

			else if(resultDefense.QT())
			{
				result.copy(new JResult(JGame.Plays.QuarterbackTrapped, JGame.Plays.Undefined));
			}

			else
			{
				result.copy(new JResult(JGame.Plays.QuarterbackTrapped, JGame.Plays.Undefined));
			}
		}

		else if(resultOffense.INC())
		{
			if(resultDefense.IO() ||
					resultDefense.INT() ||
					resultDefense.F() ||
					resultDefense.INC())
			{
				result.copy(resultDefense);
			}

			else if(resultDefense.QR())
			{
				result.copy(new JResult(JGame.Plays.QuarterbackRun, JGame.Plays.Undefined));
			}

			else if(resultDefense.QT())
			{
				result.copy(new JResult(JGame.Plays.QuarterbackTrapped, JGame.Plays.Undefined));
			}

			else
			{
				result.copy(resultOffense);
			}
		}

		else if(resultOffense.INT())
		{
		}

		else if(resultOffense.F())
		{
		}

		else if(resultOffense.SOP())
		{
			result.copy(new JResult(JGame.Plays.SprintOut, JGame.Plays.Undefined));
		}

		else if(resultOffense.BLP())
		{
			result.copy(new JResult(JGame.Plays.Bootleg, JGame.Plays.Undefined));
		}

		else
		{
			if(resultDefense.IO() ||
					resultDefense.INT() ||
					resultDefense.F() ||
					resultDefense.INC())
			{
				result.copy(resultDefense);
			}

			else if(resultDefense.QR())
			{
				result.copy(new JResult(JGame.Plays.QuarterbackRun, JGame.Plays.Undefined));
			}

			else if(resultDefense.QT())
			{
				result.copy(new JResult(JGame.Plays.QuarterbackTrapped, JGame.Plays.Undefined));
			}

			else
			{
				result.copy(resultOffense);
				result._yards += resultDefense._yards;
			}
		}

		return result;
	}

	static int	width = 64;
	static int	height = 32;
	@SuppressWarnings("unused")
	private int i;
	public void draw(GraphicsContext gc, double x, double y)
	{
		draw(gc, x, y, 1.0, false);
	}

	public void draw(GraphicsContext gc, double x, double y, double scale, Boolean source)
	{
		gc.setStroke(Color.BLACK);

		Number n = _yards;
		String tx = n.toString();

		if(QT())
		{
			gc.setFill(Color.RED);
			tx = "QT";
		}
		else if(QR())
		{
			gc.setFill(Color.WHITE);
			tx = "QR";
		}
		else if(SOP())
		{
			gc.setFill(Color.WHITE);
			tx = "SOP";
		}
		else if(BLP())
		{
			gc.setFill(Color.WHITE);
			tx = "BLP";
		}
		else if(INC())
		{
			gc.setFill(Color.rgb(36,22,15));
			tx = "INC";
		}
		else if(INT())
		{
			gc.setFill(Color.RED);
			if(_yards<0)
				tx = "INT" + n.toString();
			else
				tx = "INT+" + n.toString();
		}
		else if(F())
		{
			gc.setFill(Color.RED);
			if(_yards<0)
				tx = "F" + n.toString();
			else
				tx = "F+" + n.toString();
		}
		else if(BK())
		{
			gc.setFill(Color.RED);
			if(_yards<0)
				tx = "BK" + n.toString();
			else
				tx = "BK+" + n.toString();
		}
		else if(PI())
		{
			gc.setFill(Color.YELLOW);
			tx = "PI " + n.toString();
		}
		else if(DEF())
		{
			gc.setFill(Color.YELLOW);
			tx = "DEF " + n.toString();
		}
		else if(OFF())
		{
			gc.setFill(Color.YELLOW);
			tx = "OFF " + n.toString();
		}
		else if(DEFX())
		{
			gc.setFill(Color.YELLOW);
			tx = "DEFX " + n.toString();
		}
		else if(OFFX())
		{
			gc.setFill(Color.YELLOW);
			tx = "OFFX " + n.toString();
		}
		else if(DS())
		{
			gc.setFill(Color.GREEN);
			tx = "DS";
		}
		else if(DS_GAIN())
		{
			gc.setFill(Color.GREEN);
			tx = "DS";
		}
		else if(DS_LOSS())
		{
			gc.setFill(Color.RED);
			tx = "DS";
		}
		else if(X())
		{
			gc.setFill(Color.GREEN);
			tx = "X";
		}
		else if(X_GAIN())
		{
			gc.setFill(Color.GREEN);
			tx = "X";
		}
		else if(X_LOSS())
		{
			gc.setFill(Color.RED);
			tx = "X";
		}
		else if(B())
		{
			gc.setFill(Color.GREEN);
			tx = "B";
		}
		else if(TD())
		{
			gc.setFill(Color.GREEN);
			tx = "TD";
		}
		else if(KO())
		{
			gc.setFill(Color.GREEN);
			tx = "KO";
		}
		else if(T1())
		{
			gc.setFill(Color.GREEN);
			tx = "T1";
		}
		else if(T2())
		{
			gc.setFill(Color.GREEN);
			tx = "T2";
		}
		else if(T3())
		{
			gc.setFill(Color.GREEN);
			tx = "T3";
		}
		else if(S())
		{
		}
		else if(R())
		{
		}
		else
		{
			if(_yards < 0)
				gc.setFill(Color.RED);
			else if(_yards > 0)
				gc.setFill(Color.GREEN);
			else
				gc.setFill(Color.WHITE);
		}

		if(IO())
		{
			tx = "(" + tx + ")";
		}
		else if(OB())
		{
			tx = "*" + tx;
		}
		else if(PNR())
		{
			tx = "*" + tx;
		}

		if(source) tx = _source;

		gc.setLineWidth(1);
		gc.fillRect(x, y, width*scale, height*scale);

		gc.setStroke(Color.CYAN);
		if(_who == Who.offense)		gc.setStroke(Color.BLUE);	else
		if(_who == Who.defense)		gc.setStroke(Color.ORANGE);	else
		if(_who == Who.combined)	gc.setStroke(Color.BLACK);

		gc.strokeRect(x, y, width*scale, height*scale);

		gc.setFont(new Font("Arial Narrow", 16*scale));
		float widthTx = getToolkit().getFontLoader().computeStringWidth(tx, gc.getFont());
		float heightTx = getToolkit().getFontLoader().getFontMetrics(gc.getFont()).getXheight();

		double xCenter = width*scale/2 + x;
		double yCenter = height*scale/2 + y;

		double xOffset = xCenter - widthTx*scale/2;
		double yOffset = yCenter + heightTx*scale/2;

		gc.setFill(Color.BLACK);

		gc.fillText( tx, xOffset, yOffset);
	}
}
