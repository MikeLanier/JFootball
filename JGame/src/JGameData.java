import java.util.ArrayList;

public class JGameData
{
	public int	offense = JGame.HOME;
	public int	defense = JGame.VISITOR;

	public int	quarter = 1;
	public int	down = 1;
	public int	togo = 10;
	public int	yardline = 60;
	public int	time = 900;

	public JTeam[]	teams = new JTeam[2];
	public int[][]	score = {{0,0,0,0,0}, {0,0,0,0,0}};
	public int[]	timeouts = {3,3};

	public JStats[]	stats = new JStats[2];

	public ArrayList<JDrive> driveStack = new ArrayList<JDrive>();

	public JGameData()
	{
		stats[0] = new JStats();
		stats[1] = new JStats();
	}

	public JGameData(JGameData data)
	{
		stats[0] = new JStats();
		stats[1] = new JStats();
		copy(data);
	}

	public void copy(JGameData data)
	{
		offense = data.offense;
		defense = data.defense;

		quarter = data.quarter;
		down = data.down;
		togo = data.togo;
		yardline = data.yardline;
		time = data.time;

		for(int i = 0; i < 2; i++)
		{
			stats[i].copy(data.stats[i]);
			teams[i] = data.teams[i];
			timeouts[i] = data.timeouts[i];

			for(int j=0; j<5; j++)
			{
				score[i][j] = data.score[i][j];
			}
		}

		driveStack.clear();

		for(int i=0; i<data.driveStack.size(); i++)		driveStack.add(data.driveStack.get(i));
	}

	public String toString()
	{
		String s = "Gamedata:" +
				"\n    offense: " + offense +
				"\n    defense: " + defense +
				"\n    quarter: " + quarter +
				"\n    down: " +	down +
				"\n    togo: " + togo +
				"\n    yardline: " + yardline +
				"\n    time: " + time;

		for(int i = 0; i < 2; i++)
		{
			//stats[i].copy(data.stats[i]);
			//teams[i] = data.teams[i];
			//timeouts[i] = data.timeouts[i];

			for(int j=0; j<5; j++)
			{
				s = s + "\n    score[" + i + "][" + j + "]: " + score[i][j];
			}
		}

		return s;
	}

	public void log()
	{
		JLog.writeln("GameData:");
		JLog.addindent();
		JLog.writeln("offense: " + offense);
		JLog.writeln("defense: " + defense);
		JLog.writeln("quarter: " + quarter);
		JLog.writeln("down: " +	down);
		JLog.writeln("togo: " + togo);
		JLog.writeln("yardline: " + yardline);
		JLog.writeln("time: " + time);

		for(int i = 0; i < 2; i++)
		{
			//stats[i].copy(data.stats[i]);
			//teams[i] = data.teams[i];
			//timeouts[i] = data.timeouts[i];

			JLog.writeln("score: " + score[i][0] + ", " + score[i][1] + ", " + score[i][2] + ", " + score[i][3] + ", " + score[i][4]);
		}
		JLog.subindent();
	}
}
