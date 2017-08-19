import javafx.scene.paint.Color;

//import java.util.ArrayList;

/*
	Spot will process the result and decide what to do next.
	The first call itself recursively until a tackle
 */
public class JSpot
{
	// given the current game data and a result, compute the new game data
	// from the result

	public JGameData dataBefore = null;
	public JGameData dataAfter = null;
	public JResult result = null;

	public static enum Action
	{
		spotBall,
		ballLive,
		ballDown,		// result of the play was tackle.  Nothing else to do
		intReturn,		// interception return
		kickoff,		// run kickoff after a score
		kickoffReturn,	// kickoff return
		penalty,
		puntReturn,		// punt return
		question,		// as the play a question
		turnover,		// turn the ball over to the defense
	};

	// actions to be performed
	private Action _action = Action.spotBall;
	public Boolean actionBallDown() { return _action == Action.ballDown ? true : false; }
	public Boolean actionIntReturn() { return _action == Action.intReturn ? true : false; }
	public Boolean actionKickoff()  { return _action == Action.kickoff  ? true : false; }
	public Boolean actionKickoffReturn() { return _action == Action.kickoffReturn ? true : false; }
	public Boolean actionPuntReturn() { return _action == Action.puntReturn ? true : false; }
	public Boolean actionPenalty() { return _action == Action.penalty ? true : false; }
	public Boolean actionQuestion() { return _action == Action.question ? true : false; }
	public Boolean actionTurnover() { return _action == Action.turnover ? true : false; }

	public void log()
	{
		JLog.writeln("SPOT");
		JLog.addindent();
		dataBefore.log();
		if(result != null) result.log();
		dataAfter.log();
		JLog.writeln("action: " + _action);
		JLog.subindent();
	}

	public JSpot(JSpot spot)
	{
		dataBefore = new JGameData(spot.dataBefore);
		dataAfter = new JGameData(spot.dataAfter);

		if(spot.result == null)
			result = null;
		else
			result = new JResult(spot.result);

		_action = spot._action;
	}
	
	public JSpot(JGameData data, JResult _result)
	{
		// create a before and after "game data" from the
		// current/given game data.
		dataBefore = new JGameData(data);
		dataAfter = new JGameData(data);

		result = _result;

		if(result != null) process();
	}

	private int abs(int a) { return a<0 ? -a : a; }

	private void process()
	{
		_action = Action.ballDown;

		if( result.DEF() || result.DEFX() ||
			result.OFF() || result.OFFX() ||
			result.PI())
		{
			int yards = result.Yards();

			if(result.OFF() || result.OFFX())
			{
				int halfDist = (100 - dataAfter.yardline)/2;
				if(yards > halfDist)
				{
					result.OtherModifier(JResult.OtherModifiers.HALF_DISTANCE);
					dataAfter.yardline += halfDist;
					dataAfter.togo += halfDist;
					dataAfter.stats[dataAfter.offense].Penalty(abs(halfDist));
				}
				else
				{
					dataAfter.yardline += yards;
					dataAfter.togo += yards;
					dataAfter.stats[dataAfter.offense].Penalty(abs(yards));
				}
			}
			else
			{
				int halfDist = dataAfter.yardline/2;
				if(yards > halfDist)
				{
					result.OtherModifier(JResult.OtherModifiers.HALF_DISTANCE);
					dataAfter.yardline -= halfDist;
					dataAfter.togo -= halfDist;
					dataAfter.stats[dataAfter.defense].Penalty(abs(halfDist));
				}
				else
				{
					dataAfter.yardline -= yards;
					dataAfter.togo -= yards;
					dataAfter.stats[dataAfter.defense].Penalty(abs(yards));
				}
			}

			if(dataAfter.togo <= 0)
			{
				dataAfter.down = 1;
				dataAfter.togo = 10;
				result.OtherModifier(JResult.OtherModifiers.FD);
			}

			_action = Action.penalty;
		}

		else if( result.B())
		{
			String s = dataAfter.teams[dataAfter.offense].name + " breaks into the open and ";
			JOutput.text.addSegment(s, Color.BLACK, false, null);
			
			JGame.offensivePlay = JGame.Plays.Breakaway;
			_action = Action.ballLive;
		}

		else if( result.BLP())
		{
			String s = dataAfter.teams[dataAfter.offense].name + "'s quarterback rolls out and ";
			JOutput.text.addSegment(s, Color.BLACK, false, null);

			JGame.offensivePlay = JGame.Plays.Bootleg;
			_action = Action.ballLive;
		}

		else if( result.SOP())
		{
			String s = dataAfter.teams[dataAfter.offense].name + "'s quarterback sprints out and ";
			JOutput.text.addSegment(s, Color.BLACK, false, null);

			JGame.offensivePlay = JGame.Plays.SprintOut;
			_action = Action.ballLive;
		}

		else if( result.QT())
		{
			String s = dataAfter.teams[dataAfter.offense].name + "'s quarterback is trapped and ";
			JOutput.text.addSegment(s, Color.BLACK, false, null);

			JGame.offensivePlay = JGame.Plays.QuarterbackTrapped;
			_action = Action.ballLive;
		}

		else if( result.QR())
		{
			String s = dataAfter.teams[dataAfter.offense].name + "'s quarterback pulls it down and ";
			JOutput.text.addSegment(s, Color.BLACK, false, null);

			JGame.offensivePlay = JGame.Plays.QuarterbackRun;
			_action = Action.ballLive;
		}

		else if(result.Play() == JGame.Plays.Kickoff)
		{
			Kickoff();
		}

		else if(result.Play() == JGame.Plays.SquibKick)
		{
			SquibKick();
		}

		else if(result.Play() == JGame.Plays.SquibKickReturn)
		{
			Return();
		}

		else if(result.Play() == JGame.Plays.OnsidesKick)
		{
			OnsidesKick();
		}

		else if(result.Play() == JGame.Plays.InterceptionReturn ||
				result.Play() == JGame.Plays.KickoffReturn ||
				result.Play() == JGame.Plays.PuntReturn)
		{
			Return();
		}

		else if(result.Play() == JGame.Plays.Punt)
		{
			Punt();
		}

		else if(result.Play() == JGame.Plays.LinePlunge ||
				result.Play() == JGame.Plays.Counter ||
				result.Play() == JGame.Plays.EndReverse ||
				result.Play() == JGame.Plays.Draw ||
				result.Play() == JGame.Plays.Option)
		{
			Rush(true);
		}
		else if(result.Play() == JGame.Plays.QuarterbackRun ||
				result.Play() == JGame.Plays.QuarterbackTrapped ||
				result.Play() == JGame.Plays.Option ||
				(result.Play() == JGame.Plays.Breakaway && result.B_RUSH()))
		{
			Rush(true);
		}

		else
		if( result.Play() == JGame.Plays.Screen ||
			result.Play() == JGame.Plays.SprintOut ||
			result.Play() == JGame.Plays.Bootleg ||
			result.Play() == JGame.Plays.Dropback ||
			(result.Play() == JGame.Plays.Breakaway && result.B_PASS()))
		{
			Pass();
		}
	}

	private int Clock()
	{
		if(result.OB() &&
			((dataBefore.time <= 120 && dataBefore.quarter == 2) ||
			 (dataBefore.time <= 300 && dataBefore.quarter == 4)))
			return 10;
		else
			return 30;
	}
	private void Rush(Boolean normal)
	{
		// keep the ball on the field
		int yards = result.Yards();
		int yardline = dataAfter.yardline - yards;
		if(yardline <= 0) yards = dataAfter.yardline;
		if(yardline > 100) yards = dataAfter.yardline - 100;

		// build the play by play string
		String s;

		// for normal plays, prefix the play by play with the offensive teams name.
		if(normal)
			s = dataAfter.teams[dataAfter.offense].name + " runs the ball for ";
		else
			s = "runs the ball for ";

		JOutput.text.addSegment(s, Color.BLACK, false, null);

		if(yards > 0)
		{
			s = "a gain of " + yards + " yards.";
			JOutput.text.addSegment(s, Color.GREEN, false, null);
		}
		else if(yards < 0)
		{
			s = "a loss of " + yards + " yards.";
			JOutput.text.addSegment(s, Color.RED, false, null);
		}
		else
		{
			s = "no gain.";
			JOutput.text.addSegment(s, Color.BLACK, false, null);
		}

		// create a drive for the result
		dataAfter.driveStack.add(0, new JDrive(dataAfter.yardline, yards, result, dataAfter.offense));

		// move the ball, update togo and yards, update stats
		dataAfter.stats[dataAfter.offense].Rush(yards);
		dataAfter.yardline -= yards;
		dataAfter.togo -= yards;
		dataAfter.down++;

		// check for touchdown, safety and first down
		if(Touchdown())
		{
			s = s + ". Touchdown";
			JOutput.text.addSegment(s, Color.BLUE, false, null);
			dataAfter.stats[dataAfter.offense].RushTouchdowns();
		}

		else if(Safety())	{}
		else if(FirstDown()){}
		else if(Fumble())	{}
		else
		{
			JGame.timeUsed = Clock();
			BallGoesOverOnDowns();
		}
	}

	private void Pass()
	{
		if(result.INT())
		{
			Interception();
			return;
		}

		// keep the ball on the field
		int yards = result.Yards();
		int yardline = dataAfter.yardline - yards;
		if(yardline<=0)	yards = dataAfter.yardline;
		if(yardline>100)	yards = dataAfter.yardline - 100;

		// build the play by play string
		String s = dataAfter.teams[dataAfter.offense].name + " passes the ball ";
		JOutput.text.addSegment(s, Color.BLACK, false, null);

		if(result.INC())
		{
			s = "incomplete.";
			JOutput.text.addSegment(s, Color.GREEN, false, null);
			dataAfter.stats[dataAfter.offense].PassIncomplete();
			dataAfter.stats[dataAfter.offense].TotalPlays();
		}
		else if(yards > 0)
		{
			s = "for a gain of " + yards + " yards.";
			JOutput.text.addSegment(s, Color.GREEN, false, null);
			dataAfter.stats[dataAfter.offense].Pass(yards);
		}
		else if(yards < 0)
		{
			s = "for a loss of " + yards + " yards.";
			JOutput.text.addSegment(s, Color.RED, false, null);
			dataAfter.stats[dataAfter.offense].Pass(yards);
		}
		else
		{
			s = "for no gain.";
			JOutput.text.addSegment(s, Color.BLACK, false, null);
			dataAfter.stats[dataAfter.offense].Pass(yards);
		}

		// create a drive for the result
		dataAfter.driveStack.add(0, new JDrive(dataAfter.yardline, yards, result, dataAfter.offense));

		dataAfter.yardline -= yards;
		dataAfter.togo -= yards;
		dataAfter.down++;

		if(Touchdown())
		{
			s = s + ". Touchdown";
			JOutput.text.addSegment(s, Color.BLUE, false, null);
			dataAfter.stats[dataAfter.offense].PassTouchdowns();
		}

		else if(Safety())	{}
		else if(FirstDown()){}
		else if(Fumble())	{}
		else
		{
			JGame.timeUsed = Clock();
		}

		BallGoesOverOnDowns();

		// copy the adjusted gamedata to the in play game data
		JGame.data.copy(dataAfter);
	}

	private void Interception()
	{
		// Interception in the field of play, start a return
		// Interception in the endzone, ask if the defense wants to try a return
		//		if they try a return, but fail to get out of the endzone, it's a touchback
		// Interception beyond the endline will be 9 yards deep.

		int yards = result.Yards();
		int yardline = dataAfter.yardline - yards;

		// If the interception is beyond the endline, put it 9 yards deep
		if(yardline < -9)	yardline = -9;
		yards = dataAfter.yardline - yardline;

		Integer i = new Integer(-yardline);

		// build the play by play string
		String s = dataAfter.teams[dataAfter.offense].name + " passes the ball ";
		JOutput.text.addSegment(s, Color.BLACK, false, null);

		s = "and is intercepted ";
		JOutput.text.addSegment(s, Color.RED, false, null);

		// create a drive for the result
		dataAfter.driveStack.add(0, new JDrive(dataAfter.yardline, yards, result, dataAfter.offense));

		dataAfter.yardline -= yards;
		dataAfter.stats[dataAfter.offense].PassInterception();
		dataAfter.stats[dataAfter.offense].PassIncomplete();	// charge offense with pass attempt

		Turnover();

		if(yardline < 0)
		{
			s = i.toString() + " yards deep in the endzone.";
			JOutput.text.addSegment(s, Color.BLACK, false, null);

			s = dataAfter.teams[dataAfter.offense].name + ", you have intercepted the pass, " + i.toString() + " yards deep in the endzone. Do you want to...";
			JDialogStage dialog = new JDialogStage();
			dialog.addTitle(s);
			dialog.addQuestion("Attempt a return, or");
			dialog.addQuestion("Down the ball for a touchback.");
			dialog.showAndWait();
		}
		else
		{
			s = " at the " + JGame.formatYardline(dataAfter.yardline, dataAfter.offense, dataAfter.defense);
			JOutput.text.addSegment(s, Color.BLACK, false, null);
			JGame.offensivePlay = JGame.Plays.InterceptionReturn;
			JGame.defensivePlay = JGame.Plays.Undefined;
			_action = Action.ballLive;
		}
	}

	private Boolean Touchdown()
	{
		if(dataAfter.yardline <= 0)
		{
			JGame.timeUsed = 10;

			dataAfter.yardline = 60;
			dataAfter.togo = 10;
			dataAfter.down = 1;
			dataAfter.score[dataAfter.offense][0] += 7;
			dataAfter.score[dataAfter.offense][dataAfter.quarter] += 7;
			JOptions.kickoff();

			return true;
		}
		else
		{
			return false;
		}
	}

	private Boolean Safety()
	{
		if(dataAfter.yardline >= 100)
		{
			JOutput.text.addSegment(" Safety.", Color.BLUE, false, null);

			dataAfter.stats[dataAfter.offense].TotalSafeties();
			dataAfter.togo = 10;
			dataAfter.down = 1;

			JGame.timeUsed = 10;
			dataAfter.yardline = 80;
			dataAfter.score[dataAfter.defense][0] += 2;
			dataAfter.score[dataAfter.defense][dataAfter.quarter] += 2;
			JOptions.kickoff();

			return true;
		}
		else
		{
			return false;
		}
	}

	// Fumbles and Blocked Kicks
	//		Move the ball forward (+) or backward (-) the number of yards shown. The erring team now rolls
	//		the offensive dice and refers to the FUMBLE line on the lower right edge of its Offensive Team
	//		Chart, to see whether they recover the free ball or lose it.

	//		The opponents are entitled to an INTERCEPTION RETURN, from the spot of recovery, of ANY
	//		blocked kick lost by the kicking team (a recovery attempt total of 39 is an automatic touchdown)
	//		and fumbles which are lost due to recovery attempt totals of 37, 38, or 39.

	//		The offensive team is entitled to an INTERCEPTION RETURN, from the spot of recovery, of
	//		ANY blocked kick recovered by the kicking team at or behind the line of scrimmage with recovery
	//		attempt totals of 17, 18, or 19 or Fumbles which are recovered with recovery attempt totals of 17,
	//		18, or 19. Otherwise, the next play begins from the spot of the recovery. If the offense recovers on
	//		4th down but fails to make first down yardage, the defense takes possession anyway.

	private Boolean BlockedKick()
	{
		if(result.BK())
		{
			int yards = result.Yards();

			dataAfter.driveStack.add(0, new JDrive(dataAfter.yardline, yards, result, dataAfter.offense));

			dataAfter.yardline -= yards;
			dataAfter.togo -= yards;
			dataAfter.down++;

			if(JGame.dice.offense() > JGame.data.teams[JGame.data.offense].fumble)
			{
				JOutput.text.addSegment(" the kick is blocked, recovered by the defense. ", Color.RED, false, null);
				dataAfter.stats[dataAfter.offense].PuntBlocked();
				Turnover();
				return true;
			}
			else
			{
				JOutput.text.addSegment(" the kick is blocked, recovered by the offense. ", Color.RED, false, null);
				dataAfter.stats[dataAfter.offense].PuntBlocked();
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	private Boolean Fumble()
	{
		if(result.F())
		{
			if(result.F_LOST())
			{
				JOutput.text.addSegment(" the ball is fumbled, recovered by the defense. ", Color.RED, false, null);
				dataAfter.stats[dataAfter.offense].TotalFumbles(true);
				Turnover();
				return true;
			}
			else
			{
				JOutput.text.addSegment(" the ball is fumbled, recovered by the offense. ", Color.RED, false, null);
				dataAfter.stats[dataAfter.offense].TotalFumbles(false);
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	private Boolean FirstDown()
	{
		if(dataAfter.togo <= 0)
		{
			if(Fumble())
			{
				JGame.timeUsed = 20;
			}
			else
			{
				JOutput.text.addSegment(" Firstdown.", Color.BLUE, false, null);

				dataAfter.stats[dataAfter.offense].FirstDown();
				dataAfter.togo = 10;
				dataAfter.down = 1;
				result.OtherModifier(JResult.OtherModifiers.FD);

				JGame.timeUsed = 20;
			}

			return true;
		}
		else
		{
			return false;
		}
	}

	private Boolean BallGoesOverOnDowns()
	{
		if(dataAfter.down > 4)
		{
			JOutput.text.addSegment(" The ball goes over on downs.", Color.RED, false, null);
			Turnover();

			return true;
		}
		else
		{
			return false;
		}
	}

	private void Turnover()
	{
		int o = dataAfter.offense;
		dataAfter.offense = dataAfter.defense;
		dataAfter.defense = o;
		dataAfter.togo = 10;
		dataAfter.down = 1;
		dataAfter.yardline = 100 - dataAfter.yardline;

		JGame.timeUsed = 10;
	}

	public String downAndDistance()
	{
		String d = "1st";
		if(JGame.data.down == 2)	d = "2nd";	else
		if(JGame.data.down == 3)	d = "3rd";	else
		if(JGame.data.down == 4)	d = "4th";

		Number n = dataAfter.togo;
		String tg = n.toString();
		if(dataAfter.togo >= dataAfter.yardline)	tg = "goal";

		return d + " and " + tg + " " + dataAfter.teams[dataAfter.offense].name + " at " + where();
	}

	public String where()
	{
		Number n = 0;
		String ay = "";
		if(dataAfter.yardline > 50 )
		{
			n = 100-dataAfter.yardline;
			ay = "the " + dataAfter.teams[dataAfter.offense].name + " " + n.toString() + " yard line.";
		}

		else if(dataAfter.yardline < 50 )
		{
			n = dataAfter.yardline;
			ay = "the " + dataAfter.teams[dataAfter.defense].name + " " + n.toString() + " yard line.";
		}

		else
		{
			ay = " midfield";
		}

		return ay;
	}

	private void SquibKick()
	{
		int yards = result.Yards();
		String s = dataAfter.teams[dataAfter.offense].name + " kicking off, kicks a squib, fielded at the ";
		JOutput.text.addSegment(s, Color.BLACK, false, null);

		dataAfter.driveStack.add(0, new JDrive(dataAfter.yardline, yards, result, dataAfter.offense));
		dataAfter.yardline -= yards;

		s = where() + "\n";
		JOutput.text.addSegment(s, Color.BLACK, false, null);

		Turnover();

		JGame.offensivePlay = JGame.Plays.SquibKickReturn;
		JGame.defensivePlay = JGame.Plays.Undefined;
		_action = Action.ballLive;
	}

	private void OnsidesKick()
	{
		int yards = result.Yards();
		String s = dataAfter.teams[dataAfter.offense].name + " attempts an onsides kick.\n";
		JOutput.text.addSegment(s, Color.BLACK, false, null);

		dataAfter.driveStack.add(0, new JDrive(dataAfter.yardline, yards, result, dataAfter.offense));
		dataAfter.yardline -= yards;

		if(result.RECOVERED())
		{
			s = "Recovered by the kicking team.\n";
			JOutput.text.addSegment(s, Color.GREEN, false, null);
		}
		else
		{
			s = "Recovered by the return team.\n";
			JOutput.text.addSegment(s, Color.BLUE, false, null);

			Turnover();
		}
	}

	private void Kickoff()
	{
		int yards = result.Yards();

		String s;

		if((dataAfter.yardline - yards) < -10 ) yards = dataAfter.yardline + 10;

		dataAfter.driveStack.add(0, new JDrive(dataAfter.yardline, yards, result, dataAfter.offense));
		dataAfter.yardline -= yards;

		Turnover();

		if(dataAfter.yardline < 100)
		{
			s = "The " + dataAfter.teams[dataAfter.defense].name + " kicksoff travels " + yards + " yards to " + where() + "\n";
			JOutput.text.addSegment(s, Color.BLACK, false, null);

			dataAfter.togo = 10;
			dataAfter.down = 1;

			JGame.offensivePlay = JGame.Plays.KickoffReturn;
			JGame.defensivePlay = JGame.Plays.Undefined;
			_action = Action.ballLive;
		}

		else if(dataAfter.yardline < 110)
		{
			int y = (int)(dataAfter.yardline - 100);

			s = dataAfter.teams[dataAfter.defense].name + " kicks the ball " + y + " yards deep in the endzone.\n";
			JOutput.text.addSegment(s, Color.BLACK, false, null);

			s = "The kickoff is fielded " + y + " yards deep in the endzone. " + dataAfter.teams[dataAfter.offense].name + ", do you want to...";
			JDialogStage dialog = new JDialogStage();

			dialog.addTitle(s);
			dialog.addQuestion("Attempt a return, or");
			dialog.addQuestion("Down the ball for a touchback.");
			dialog.showAndWait();

			if(dialog.answer() == 0)
			{
				dataAfter.togo = 10;
				dataAfter.down = 1;

				JGame.offensivePlay = JGame.Plays.KickoffReturn;
				JGame.defensivePlay = JGame.Plays.Undefined;
				_action = Action.ballLive;
			}
			else
			{
				dataAfter.yardline = 80;
				dataAfter.togo = 10;
				dataAfter.down = 1;

				s = "Touchback. " + downAndDistance();
				JOutput.text.addSegment(s, Color.BLACK, false, null);
			}

		}

		else
		{
			dataAfter.yardline = 80;
			dataAfter.togo = 10;
			dataAfter.down = 1;

			s = dataAfter.teams[dataAfter.defense].name + " kicks the ball through the endzone. Touchback.\n" + downAndDistance();
			JOutput.text.addSegment(s, Color.BLACK, false, null);
		}

		JOptions.normal();
	}

	private void Return()
	{
		int yards = result.Yards();

		if(yards > dataAfter.yardline)	yards = dataAfter.yardline;

		String s;

		// for normal plays, prefix the play by play with the offensive teams name.
		if(result.Play() == JGame.Plays.PuntReturn)
		{
			s = dataAfter.teams[dataAfter.offense].name + " returns the punt " + yards + " yards";
			dataAfter.stats[dataAfter.offense].PuntReturn(yards);
		}
		else if(result.Play() == JGame.Plays.InterceptionReturn)
		{
			s = dataAfter.teams[dataAfter.offense].name + " returns the interception " + yards + " yards";
			dataAfter.stats[dataAfter.offense].InterceptionReturn(yards);
		}
		else
		{
			s = dataAfter.teams[dataAfter.offense].name + " returns the kick " + yards + " yards";
			dataAfter.stats[dataAfter.offense].KickoffReturn(yards);
		}

		// create a drive for the result
		dataAfter.driveStack.add(0, new JDrive(dataAfter.yardline, yards, result, dataAfter.offense));

		dataAfter.yardline -= yards;
		dataAfter.togo = 10;
		dataAfter.down = 1;
		JGame.timeUsed = result.OB() ? 10 : 30;

		if(Touchdown())
		{
			if(result.Play() == JGame.Plays.PuntReturn)
			{
				dataAfter.stats[dataAfter.offense].PuntReturnTouchdown();
			}
			else if(result.Play() == JGame.Plays.InterceptionReturn)
			{
				dataAfter.stats[dataAfter.offense].InterceptionReturnTouchdowns();
			}
			else
			{
				dataAfter.stats[dataAfter.offense].KickoffReturnTouchdown();
			}

			s = s + " for a ";
			JOutput.text.addSegment(s, Color.BLACK, false, null);
			s = "touchdown";
			JOutput.text.addSegment(s, Color.BLUE, false, null);
		}

		else if(Fumble())
		{
			s = s + " to " + where() + "\n" + downAndDistance();
			JOutput.text.addSegment(s, Color.BLACK, false, null);
		}

		else
		{
			s = s + " to " + where() + "\n" + downAndDistance();
			JOutput.text.addSegment(s, Color.BLACK, false, null);
		}
	}

	private void Punt()
	{
		int yards = result.Yards();
		if(yards > dataAfter.yardline)	yards = dataAfter.yardline;

		if(BlockedKick())
		{

		}
		else
		{
			String s;

			// for normal plays, prefix the play by play with the offensive teams name.
			s = dataAfter.teams[dataAfter.offense].name + " punts the ball " + yards + " yards ";
			JOutput.text.addSegment(s, Color.BLACK, false, null);

			if(result.PNR() || result.OB())
			{
				dataAfter.driveStack.add(0, new JDrive(dataAfter.yardline, yards, result, dataAfter.offense));

				dataAfter.stats[dataAfter.offense].Punt(yards);

				dataAfter.yardline -= yards;

				s = "to " + where() + " No return\n";
				JOutput.text.addSegment(s, Color.BLACK, false, null);

				Turnover();
				dataAfter.togo = 10;
				dataAfter.down = 1;

				_action = Action.ballDown;
			}
			else if((dataAfter.yardline - yards) > 0)
			{
				dataAfter.driveStack.add(0, new JDrive(dataAfter.yardline, yards, result, dataAfter.offense));

				dataAfter.stats[dataAfter.offense].Punt(yards);

				dataAfter.yardline -= yards;

				s = "to " + where() + "\n";
				JOutput.text.addSegment(s, Color.BLACK, false, null);

				Turnover();
				dataAfter.togo = 10;
				dataAfter.down = 1;

				JGame.offensivePlay = JGame.Plays.PuntReturn;
				JGame.defensivePlay = JGame.Plays.Undefined;
				_action = Action.ballLive;
			}

			else
			{
				s = "into the endzone. Touchback.\n";

				yards = dataBefore.yardline;

				dataAfter.stats[dataAfter.offense].Punt(yards);

				// create a drive for the result
				dataAfter.driveStack.add(0, new JDrive(dataAfter.yardline, yards, result, dataAfter.offense));

				Turnover();
				dataAfter.yardline = 80;
				dataAfter.togo = 10;
				dataAfter.down = 1;

				s = s + downAndDistance();
				JOutput.text.addSegment(s, Color.BLACK, false, null);
			}
		}
	}
}
