import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class JGame extends GridPane
{
	static Color	LIGHTGREEN = Color.rgb(197,240,185);
	static Color	WHITE = Color.rgb(255,255,255);

	public enum Plays
	{
		// offensive plays on the game chart
		LinePlunge(0),
		Counter(1),
		EndReverse(2),
		Draw(3),
		Option(4),
		Screen(5),
		SprintOut(6),
		Bootleg(7),
		Dropback(8),
		Breakaway(9),
		QuarterbackRun(10),
		QuarterbackTrapped(11),
		Kickoff(12),
		KickoffReturn(13),
		Punt(14),
		PuntReturn(15),
		InterceptionReturn(16),
		FieldGoal(17),

		// defensive plays on the game chart
		Standard(18),
		ShortGaps(19),
		ShortWide(20),
		ShortPass(21),
		LongPass(22),
		Blitz(23),

		// hybrid offensive plays
		FakePunt(24),
		FakeFG(25),
		Kneel(26),
		QBSneak(27),
		Spike(28),
		OnsidesKick(29),
		SquibKick(30),
		SquibKickReturn(31),

		Undefined(99);

		private int value;

		Plays(int value)
		{
			this.value = value;
		}
	};

	static int HOME = 0;
	static int VISITOR = 1;

	static JGameData data = new JGameData();

	static JDice dice = new JDice();

	static JScoreboard scoreboard = new JScoreboard(Color.WHITE);
	static JField field = new JField(Color.GRAY);
	static JOutput output = new JOutput(Color.WHITE);
	static JStatsTableView stats = new JStatsTableView();
	static JOptions options = new JOptions(Color.WHITE);

	static int timeUsed = 0;

	int pad = 5;

	public static ArrayList<JResult> resultStackDebug = new ArrayList<JResult>();
	public static ArrayList<JResult> resultStack = new ArrayList<JResult>();
	public static ArrayList<Integer> diceStack = new ArrayList<Integer>();

	static String squibKick[] =
			{
					"0", // 10
					"22", // 11
					"20", // 12
					"6", // 13
					"DEF15", // 14
					"OFF15", // 15
					"21", // 16
					"23", // 17
					"24", // 18
					"B", // 19
					"4", // 20
					"17", // 21
					"5", // 22
					"7", // 23
					"8", // 24
					"13", // 25
					"F+10", // 26
					"16", // 27
					"3", // 28
					"1", // 29
					"18", // 30
					"14", // 31
					"9", // 32
					"11", // 33
					"10", // 34
					"10", // 35
					"12", // 36
					"15", // 37
					"19", // 38
					"2", // 39
			};

	static JResult squibKickResults[] = new JResult[squibKick.length];

	public JGame()
	{
		JLog.create();

		double[] percentWidth = {60, 20, 20};
		double[] percentHeight = {12,53,22,13};
//		double[] percentWidth = {80, 20};
//		double[] percentHeight = {10,65,12,13};

		for(int i=0; i<squibKick.length; i++)
		{
			squibKickResults[i] = new JResult(squibKick[i]);
		}

		setGridLinesVisible(true);

		// constraints for fixed size grid cells
		ColumnConstraints[] col = new ColumnConstraints[percentWidth.length];

		for(int i=0; i<percentWidth.length; i++)
		{
			col[i] = new ColumnConstraints();
			col[i].setPercentWidth(percentWidth[i]);
		}
		getColumnConstraints().addAll(col);

		RowConstraints[] row = new RowConstraints[percentHeight.length];

		for(int i=0; i<percentHeight.length; i++)
		{
			row[i] = new RowConstraints();	row[i].setPercentHeight(percentHeight[i]);
		}
		getRowConstraints().addAll(row);

		add(scoreboard, 0, 0);
		add(field, 0, 1);
		add(output, 0, 2);
		add(options, 0, 3);
		add(stats, 1, 0, 1, 4);

		JDebug debug = new JDebug(this);
		JOptions.debugText.textProperty().addListener((observable, oldValue, newValue) ->
		{
			if(newValue.contains("."))
			{
				debug.parse(newValue);
			}
		});

		JTeam home = new JTeam("C:\\Mike\\Football\\JFootball\\Teams\\CSV\\2012 Alabama.csv");
		JTeam visitor = new JTeam("C:\\Mike\\Football\\JFootball\\Teams\\CSV\\2010 Auburn.csv");

		data.teams[HOME] = home;
		data.teams[VISITOR] = visitor;

		//JOptions.normal();
	}

	static BorderStroke createBorderStroke(Color color, Boolean top, Boolean right, Boolean bottom, Boolean left, BorderWidths borderWidths)
	{
		return	new BorderStroke(	color, color, color, color,
				top ? BorderStrokeStyle.SOLID : BorderStrokeStyle.NONE,
				right ? BorderStrokeStyle.SOLID : BorderStrokeStyle.NONE,
				bottom ? BorderStrokeStyle.SOLID : BorderStrokeStyle.NONE,
				left ? BorderStrokeStyle.SOLID : BorderStrokeStyle.NONE,
				null,
				borderWidths,
				null );
	}

	public static int playIndex( Plays play )
	{
		if(play == Plays.LinePlunge) return 0;
		if(play == Plays.Counter) return 1;
		if(play == Plays.EndReverse) return 2;
		if(play == Plays.Draw) return 3;
		if(play == Plays.Option) return 4;
		if(play == Plays.Screen) return 5;
		if(play == Plays.SprintOut) return 6;
		if(play == Plays.Bootleg) return 7;
		if(play == Plays.Dropback) return 8;
		if(play == Plays.Breakaway) return 9;
		if(play == Plays.QuarterbackRun) return 10;
		if(play == Plays.QuarterbackTrapped) return 11;
		if(play == Plays.Kickoff) return 12;
		if(play == Plays.KickoffReturn) return 13;
		if(play == Plays.Punt) return 14;
		if(play == Plays.PuntReturn) return 15;
		if(play == Plays.InterceptionReturn) return 16;
		if(play == Plays.FieldGoal) return 17;

		return 99;
	}

	static ArrayList<JSpot>	_spotStack = new ArrayList<>();
	static ArrayList<JSpot>	_penaltyStack = new ArrayList<>();

	static JGame.Plays	offensivePlay = JGame.Plays.Undefined;
	static JGame.Plays	defensivePlay = JGame.Plays.Undefined;

	public static void runPlay()
	{
		JLog.writeln(data.toString());

		// create an initial spot from the line of scrimmage.
		// Clear the spot and penalty stacks
		JSpot spot = new JSpot(data, null);
		_spotStack.clear();
		_penaltyStack.clear();

		_spotStack.add(spot);

		offensivePlay = JOptions.selectedPlayOffense();
		defensivePlay = JOptions.selectedPlayDefense();

		JOutput.text.getChildren().clear();

		do
		{
			// get a result for the given offensive/defensive play
			JResult result = new JResult( offensivePlay, defensivePlay );

			// create a new spot from the result, starting from the "after the fact"
			// gamedata of the previous spot.
			spot = new JSpot(spot.dataAfter, result);

			// if the result was a penalty, add the spot to the penalty stack
			// add all to the spot stack.
			if(spot.actionPenalty())
				_penaltyStack.add(new JSpot(spot));

			_spotStack.add(new JSpot(spot));

			if(spot.actionPenalty())
				spot.dataAfter.copy(spot.dataBefore);
		}
		while(!spot.actionBallDown());

		// if there was a penalty, format the question and options from the penalty stack and show
		// the question dialog
		if(_penaltyStack.size()>0)
		{
			JPenaltyDialog dialog = new JPenaltyDialog(_penaltyStack, _spotStack);
			dialog.showAndWait();

			data.copy(dialog.returnSelection().dataAfter);
			if(dialog.accepted())
			{
				data.driveStack.add(0, new JDrive(dialog.returnSelection().dataBefore.yardline,
						dialog.returnSelection().dataBefore.yardline - dialog.returnSelection().dataAfter.yardline,
						dialog.returnSelection().result,
						dialog.returnSelection().dataAfter.offense));
			}
		}
		else
		{
			data.copy(spot.dataAfter);
		}

		data.time -= timeUsed;

		if(data.time <= 0)
		{
			data.quarter++;
			data.time = 900;

			if(data.quarter>4)
			{
				(new JMessage("Game Over","")).showAndWait();
			}
		}

		JGame.Plays defPlay = JPlayCaller.Defense(data);
		JOptions.setOption(defPlay.value);
		scoreboard.update();
	}

	static String formatYardline(double yardline, int offense, int defense)
	{
		Number n;
		String s;

		if(yardline > 50 )
		{
			n = (int)(100-yardline);
			s = "the " + JGame.data.teams[offense].name + " " + n.toString() + " yard line";
		}

		else if(yardline < 50 )
		{
			n = (int)yardline;
			s = "the " + JGame.data.teams[defense].name + " " + n.toString() + " yard line";
		}

		else
		{
			s = " midfield";
		}

		return s;
	}

	static String formatDown(int down)
	{
		if(down == 2)	return "2nd down";
		else if(down == 3)	return "3rd down";
		else if(down == 4)	return "4th down";
		else return "1st down";
	}

	static String downAndDistance(JGameData data)
	{
		String d = "1st";
		if(data.down == 2)	d = "2nd";	else
		if(data.down == 3)	d = "3rd";	else
		if(data.down == 4)	d = "4th";

		Number n = data.togo;
		String tg = n.toString();
		if(data.togo >= data.yardline)	tg = "goal";

		return d + " and " + tg + " " + data.teams[data.offense].name + " at " + where(data);
	}

	static String where(JGameData data)
	{
		Number n = 0;
		String ay = "";
		if(data.yardline > 50 )
		{
			n = 100-data.yardline;
			ay = "the " + data.teams[data.offense].name + " " + n.toString() + " yard line.";
		}

		else if(data.yardline < 50 )
		{
			n = data.yardline;
			ay = "the " + data.teams[data.defense].name + " " + n.toString() + " yard line.";
		}

		else
		{
			ay = " midfield";
		}

		return ay;
	}
}
