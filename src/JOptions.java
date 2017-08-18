import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class JOptions extends JPanel
{
	static JOptionsButton optionLinePlunge = new JOptionsButton("0-Line Plunge", JGame.Plays.LinePlunge);
	static JOptionsButton optionCounter = new JOptionsButton("1-Counter", JGame.Plays.Counter);
	static JOptionsButton optionEndReverse = new JOptionsButton("2-End Reverse", JGame.Plays.EndReverse);
	static JOptionsButton optionDraw = new JOptionsButton("3-Draw", JGame.Plays.Draw);

	static JOptionsButton optionOption = new JOptionsButton("4-Option", JGame.Plays.Option);
	static JOptionsButton optionKneel = new JOptionsButton("26-Kneel", JGame.Plays.Kneel);
	static JOptionsButton optionQBSneak = new JOptionsButton("27-QB Sneak", JGame.Plays.QBSneak);
	static JOptionsButton optionSpike = new JOptionsButton("28-Spike", JGame.Plays.Spike);

	static JOptionsButton optionScreen = new JOptionsButton("5-Screen", JGame.Plays.Screen);
	static JOptionsButton optionSprintOut = new JOptionsButton("6-Sprint Out", JGame.Plays.SprintOut);
	static JOptionsButton optionBootleg = new JOptionsButton("7-Bootleg", JGame.Plays.Bootleg);
	static JOptionsButton optionDropBack = new JOptionsButton("8-Drop Back", JGame.Plays.Dropback);

	static JOptionsButton optionPunt = new JOptionsButton("14-Punt", JGame.Plays.Punt);
	static JOptionsButton optionFakePunt = new JOptionsButton("24-Fake Punt", JGame.Plays.FakePunt);
	static JOptionsButton optionFieldgoal = new JOptionsButton("17-Fieldgoal", JGame.Plays.FieldGoal);
	static JOptionsButton optionFakeFG = new JOptionsButton("25-Fake FG", JGame.Plays.FakeFG);

	static JOptionsButton optionStandard = new JOptionsButton("18-Standard", JGame.Plays.Standard);
	static JOptionsButton optionShortGaps = new JOptionsButton("19-Short Gaps", JGame.Plays.ShortGaps);
	static JOptionsButton optionShortWide = new JOptionsButton("20-Short Wide", JGame.Plays.ShortWide);
	static JOptionsButton optionShortPass = new JOptionsButton("21-Short Pass", JGame.Plays.ShortPass);

	static JOptionsButton optionLongPass = new JOptionsButton("22-Long Pass", JGame.Plays.LongPass);
	static JOptionsButton optionBlitz = new JOptionsButton("23-Blitz", JGame.Plays.Blitz);

	static JOptionsButton optionKickoff = new JOptionsButton("12-Kickoff", JGame.Plays.Kickoff);
	static JOptionsButton optionOnsidesKick = new JOptionsButton("29-Onsides Kick", JGame.Plays.OnsidesKick);
	static JOptionsButton optionSquibKick = new JOptionsButton("30-Squib Kick", JGame.Plays.SquibKick);

	static void setOption(int option)
	{
		if(option == 0)     setOffenseOption(optionLinePlunge); else
		if(option == 1)     setOffenseOption(optionCounter); else
		if(option == 2)     setOffenseOption(optionEndReverse); else
		if(option == 3)     setOffenseOption(optionDraw); else
		if(option == 4)     setOffenseOption(optionOption); else
		if(option == 26)    setOffenseOption(optionKneel); else
		if(option == 27)    setOffenseOption(optionQBSneak); else
		if(option == 28)    setOffenseOption(optionSpike); else
		if(option == 5)     setOffenseOption(optionScreen); else
		if(option == 6)     setOffenseOption(optionSprintOut); else
		if(option == 7)     setOffenseOption(optionBootleg); else
		if(option == 8)     setOffenseOption(optionDropBack); else
		if(option == 14)    setOffenseOption(optionPunt); else
		if(option == 24)    setOffenseOption(optionFakePunt); else
		if(option == 17)    setOffenseOption(optionFieldgoal); else
		if(option == 25)    setOffenseOption(optionFakeFG); else

		if(option == 18	)	setDefenseOption(optionStandard); else
		if(option == 19	)	setDefenseOption(optionShortGaps); else
		if(option == 20	)	setDefenseOption(optionShortWide); else
		if(option == 21	)	setDefenseOption(optionShortPass); else
		if(option == 22	)	setDefenseOption(optionLongPass); else
		if(option == 23	)	setDefenseOption(optionBlitz); else

		if(option == 12	)	setKickoffOption(optionKickoff); else
		if(option == 29	)	setKickoffOption(optionOnsidesKick); else
		if(option == 30	)	setKickoffOption(optionSquibKick);
	}

	private static void setOffenseOption(JOptionsButton button)
	{
		if(_selectedOffense != null) _selectedOffense.Select();
		_selectedOffense = button;
		if(_selectedOffense != null) _selectedOffense.Select();
	}

	private static void setDefenseOption(JOptionsButton button)
	{
		if(_selectedDefense != null) _selectedDefense.Select();
		_selectedDefense = button;
		if(_selectedDefense != null) _selectedDefense.Select();
	}

	private static void setKickoffOption(JOptionsButton button)
	{
		if(_selectedKickoff != null) _selectedKickoff.Select();
		_selectedKickoff = button;
		if(_selectedKickoff != null) _selectedKickoff.Select();
	}

	public static TextField debugText = new TextField();

	static JOptionsButton[][] buttonsNormal =
	{
		{	optionLinePlunge,	optionCounter,		optionEndReverse,	optionDraw	},
		{	optionOption,		optionKneel,		optionQBSneak,		optionSpike	},
		{	optionScreen,		optionSprintOut,	optionBootleg,		optionDropBack	},
		{	optionPunt,			optionFakePunt,		optionFieldgoal,	optionFakeFG	},
		{	null,				null,				null,				null	},
		{	optionStandard,		optionShortGaps,	optionShortWide,	optionShortPass	},
		{	optionLongPass,		optionBlitz,		null,				null	},
	};

	static JOptionsButton[][] buttonsKickoff =
	{
		{	optionKickoff,		optionOnsidesKick,	optionSquibKick,	null	},
		{	null,				null,				null,				null	},
		{	null,				null,				null,				null	},
		{	null,				null,				null,				null	},
		{	null,				null,				null,				null	},
		{	null,				null,				null,				null	},
		{	null,				null,				null,				null	},
	};


	static double[] percentWidth = {16, 16, 16, 16, 4, 16, 16};
	static double[] percentHeight = {25,25,25,25};

	static int pad = 5;

	static enum Mode
	{
		normal,
		kickoff
	}
	static Mode _mode = Mode.kickoff;

	static JOptions	me;
	public JOptions(Color color)
	{
		super(color);

		me = this;

		setBackground(new Background(new BackgroundFill(JGame.LIGHTGREEN, new CornerRadii(5), null)));
		setPadding(new Insets(pad,pad,pad,pad));

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

		Font font = new Font(14);

		for(int i=0; i<percentHeight.length; i++)
		{
			for(int j = 0; j < percentWidth.length; j++)
			{
				if(buttonsNormal[j][i] != null)
				{
					buttonsNormal[j][i].setFont(font);
					add(buttonsNormal[j][i], j, i);

					buttonsNormal[j][i].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
					{
						@Override
						public void handle(MouseEvent e)
						{
							onButtonClicked((JOptionsButton)e.getSource());
						}
					});
				}

				if(buttonsKickoff[j][i] != null)
				{
					buttonsKickoff[j][i].setFont(font);

					buttonsKickoff[j][i].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
					{
						@Override
						public void handle(MouseEvent e)
						{
							onButtonClicked((JOptionsButton)e.getSource());
						}
					});
				}
			}
		}

		debugText.setPadding(new Insets(0,5,0,5));
		debugText.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
		add(debugText,6,3);

		setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));

		setOption(0);
		setOption(18);
		setOption(12);

		if(_mode == Mode.kickoff)
			kickoff();
		else
			normal();
		//normal();
	}

	private static JOptionsButton _selectedOffense = null;
	private static JOptionsButton _selectedDefense = null;
	private static JOptionsButton _selectedKickoff = null;

	// when in kickoff mode, return the kickoff play selection, else return the offensive play selection
	public static JGame.Plays selectedPlayOffense()
	{
		if(_mode == Mode.kickoff)
			return (_selectedKickoff == null) ? JGame.Plays.Undefined : _selectedKickoff.Play();
		else
			return (_selectedOffense == null) ? JGame.Plays.Undefined : _selectedOffense.Play();
	}

	// when in kickoff mode, return return the undefined play, else the defensive play selection
	public static JGame.Plays selectedPlayDefense()
	{
		if(_mode == Mode.kickoff)
			return JGame.Plays.Undefined;
		else
			return (_selectedDefense == null) ? JGame.Plays.Undefined : _selectedDefense.Play();
	}

	public static JGame.Plays selectedPlayKickoff()	{ return (_selectedKickoff == null) ? JGame.Plays.Undefined : _selectedKickoff.Play(); }

	public void onButtonClicked(JOptionsButton button)
	{
		if(button.Play() == JGame.Plays.Kickoff ||
			button.Play() == JGame.Plays.SquibKick ||
			button.Play() == JGame.Plays.OnsidesKick)
		{
			if(_selectedKickoff != null) _selectedKickoff.Select();
			_selectedKickoff = button;
			if(_selectedKickoff != null) _selectedKickoff.Select();

			JGame.resultStack.clear();
			JGame.runPlay();
			JGame.field.draw();
		}
		else if(button.Play() == JGame.Plays.Standard ||
				button.Play() == JGame.Plays.ShortGaps ||
				button.Play() == JGame.Plays.ShortWide ||
				button.Play() == JGame.Plays.ShortPass ||
				button.Play() == JGame.Plays.LongPass ||
				button.Play() == JGame.Plays.Blitz )
		{
			if(_selectedDefense != null)	_selectedDefense.Select();
			_selectedDefense = button;
			if(_selectedDefense != null)	_selectedDefense.Select();
		}
		else
		{
			if(_selectedOffense != null)	_selectedOffense.Select();
			_selectedOffense = button;
			if(_selectedOffense != null)	_selectedOffense.Select();

			JGame.resultStack.clear();
			JGame.runPlay();
			JGame.field.draw();
		}
	}

	public void SetSize(double width, double height)
	{
		width = width - pad - pad;
		height = height - pad - pad;

		for(int i=0; i<percentHeight.length; i++)
		{
			for(int j=0; j<percentWidth.length; j++)
			{
				if(buttonsNormal[j][i] != null)
				{
					buttonsNormal[j][i].setPrefSize(width * percentWidth[j] / 100, height * percentHeight[i] / 100);
				}
				if(buttonsKickoff[j][i] != null)
				{
					buttonsKickoff[j][i].setPrefSize(width * percentWidth[j] / 100, height * percentHeight[i] / 100);
				}
			}
		}

		debugText.setPrefSize(width * percentWidth[5] / 100, height * percentHeight[3] / 100);
	}

	static void kickoff()
	{
		me.getChildren().clear();
		for(int i=0; i<percentHeight.length; i++)
		{
			for(int j = 0; j < percentWidth.length; j++)
			{
				if(buttonsKickoff[j][i] != null)
				{
					me.add(buttonsKickoff[j][i], j, i);
				}
			}
		}
		me.add(debugText,6,3);
		_mode = Mode.kickoff;
	}

	static void normal()
	{
		me.getChildren().clear();
		for(int i=0; i<percentHeight.length; i++)
		{
			for(int j = 0; j < percentWidth.length; j++)
			{
				if(buttonsNormal[j][i] != null)
				{
					me.add(buttonsNormal[j][i], j, i);
				}
			}
		}
		me.add(debugText,6,3);
		_mode = Mode.normal;
	}
}
