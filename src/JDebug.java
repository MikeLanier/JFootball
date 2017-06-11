public class JDebug
{
	static JGame m_game = null;

	public JDebug(JGame game)
	{
		m_game = game;
	}

	public void parse(String stuff)
	{
		System.out.println(stuff);
		stuff = stuff.replace(".", "");

		String[] tokens = stuff.split(",");

		Integer intgr;

		if(tokens.length>1)
		{
			switch(tokens[0])
			{
				case "sb":
					switch(tokens[1])
					{
						case "oh":
							JGame.data.offense = JGame.HOME;
							JGame.data.defense = JGame.VISITOR;
							JGame.scoreboard.updateTeams();
							JGame.scoreboard.updateDownAndDistance();
							break;

						case "ov":
							JGame.data.offense = JGame.VISITOR;
							JGame.data.defense = JGame.HOME;
							JGame.scoreboard.updateTeams();
							JGame.scoreboard.updateDownAndDistance();
							break;

						case "sh":
							if(tokens.length>2)
							{
								intgr = new Integer(tokens[2]);
								JGame.data.score[JGame.HOME][JGame.data.quarter] = intgr.intValue();
								JGame.scoreboard.updateScore();
							}
							break;

						case "sv":
							if(tokens.length>2)
							{
								intgr = new Integer(tokens[2]);
								JGame.data.score[JGame.VISITOR][JGame.data.quarter] = intgr.intValue();
								JGame.scoreboard.updateScore();
							}
							break;

						case "q":
							if(tokens.length>2)
							{
								intgr = new Integer(tokens[2]);
								JGame.data.quarter = intgr.intValue();
								JGame.scoreboard.updateScore();
							}
							break;

						case "d":
							if(tokens.length>2)
							{
								intgr = new Integer(tokens[2]);
								JGame.data.down = intgr.intValue();
								JGame.scoreboard.updateDownAndDistance();
							}
							break;

						case "yl":
							if(tokens.length>2)
							{
								intgr = new Integer(tokens[2]);
								JGame.data.yardline = intgr.intValue();
								JGame.scoreboard.updateDownAndDistance();
							}
							break;

						case "tg":
							if(tokens.length>2)
							{
								intgr = new Integer(tokens[2]);
								JGame.data.togo = intgr.intValue();
								JGame.scoreboard.updateDownAndDistance();
							}
							break;

						case "t":
							if(tokens.length>2)
							{
								intgr = new Integer(tokens[2]);
								JGame.data.time = intgr.intValue();
								JGame.scoreboard.updateTime();
							}
							break;

						case "toh":
							if(tokens.length>2)
							{
								intgr = new Integer(tokens[2]);
								JGame.data.timeouts[JGame.HOME] = intgr.intValue();
								JGame.scoreboard.updateTimeouts();
							}
							break;

						case "tov":
							if(tokens.length>2)
							{
								intgr = new Integer(tokens[2]);
								JGame.data.timeouts[JGame.VISITOR] = intgr.intValue();
								JGame.scoreboard.updateTimeouts();
							}
							break;
					}
					break;

				case "d":
					intgr = new Integer(tokens[1]);
					JGame.data.down = intgr.intValue();
					JGame.scoreboard.updateDownAndDistance();
					break;

				case "q":
					intgr = new Integer(tokens[1]);
					JGame.data.quarter = intgr.intValue();
					JGame.scoreboard.update();
					break;

				case "yl":
					intgr = new Integer(tokens[1]);
					JGame.data.yardline = intgr.intValue();
					JGame.scoreboard.updateDownAndDistance();
					JGame.field.draw();
					break;

				case "tg":
					intgr = new Integer(tokens[1]);
					JGame.data.togo = intgr.intValue();
					JGame.scoreboard.updateDownAndDistance();
					JGame.field.draw();
					break;

				case "t":
					intgr = new Integer(tokens[1]);
					JGame.data.time = intgr.intValue();
					JGame.scoreboard.updateTime();
					break;

				case "toh":
					intgr = new Integer(tokens[1]);
					JGame.data.timeouts[JGame.HOME] = intgr.intValue();
					JGame.scoreboard.updateTimeouts();
					break;

				case "tov":
					intgr = new Integer(tokens[1]);
					JGame.data.timeouts[JGame.VISITOR] = intgr.intValue();
					JGame.scoreboard.updateTimeouts();
					break;

				case "ds": // dice stack
					intgr = new Integer(tokens[1]);
					//if(intgr.intValue()>=10 && intgr.intValue()<=39)
					{
						JGame.diceStack.add(intgr);
						JGame.field.draw();
					}
					break;

				case "p":
					intgr = new Integer(tokens[1]);
					JOptions.setOption(intgr.intValue());
					break;
			}
		}
		else
		{
			switch(tokens[0])
			{
				case "oh":
					JGame.data.offense = JGame.HOME;
					JGame.data.defense = JGame.VISITOR;
					JGame.scoreboard.updateTeams();
					JGame.scoreboard.updateDownAndDistance();
					JGame.field.draw();
					break;

				case "ov":
					JGame.data.offense = JGame.VISITOR;
					JGame.data.defense = JGame.HOME;
					JGame.scoreboard.updateTeams();
					JGame.scoreboard.updateDownAndDistance();
					JGame.field.draw();
					break;

				case "r":
					JGame.runPlay();
					JGame.resultStackDebug.clear();
					JGame.field.draw();
					break;

				case "on":
					JOptions.normal();
					break;

				case "ok":
					JOptions.kickoff();
					break;

				case "tcho":
					JTeamCard card = new JTeamCard(JGame.data.teams[JGame.HOME], true);
					card.show();
					break;

				case "tchd":
					card = new JTeamCard(JGame.data.teams[JGame.HOME], false);
					card.show();
					break;

				case "tcvo":
					card = new JTeamCard(JGame.data.teams[JGame.VISITOR], true);
					card.show();
					break;

				case "tcvd":
					card = new JTeamCard(JGame.data.teams[JGame.VISITOR], false);
					card.show();
					break;

				default:
					JResult result = new JResult();
					result.Interpret(tokens[0]);
					result.Play(JGame.options.selectedPlayOffense());
					JGame.resultStackDebug.add(result);

					//JDrive drive = new JDrive( JGame.data.yardline, result, JGame.data.offense );
					//JGame.data.driveStack.add(0, drive);

					JGame.field.draw();
					break;
			}
		}
	}
}
