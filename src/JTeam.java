import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JTeam
{
	public String name = "";
	public String nickname = "";
	public String year = "";
	public String filename = "";
	public String helmet = "";
	public String abrv = "";
	public int fumble = 0;
	
	public JResult[][]	offenseResults = new JResult[30][18];
	public JResult[][]	defenseResults = new JResult[30][18];

	/*
	public JTeam(String name, String year, String filename, String helmet)
	{
		this.name = name;
		this.nickname = "";
		this.year = year;
		this.filename = filename;
		this.helmet = helmet;
		this.abrv = "";

		for(int i=0; i<30; i++)
			for(int j=0; j<18; j++)
			{
				offenseResults[i][j] = new JResult();
				defenseResults[i][j] = new JResult();
			}
	}
	*/

	public JTeam(String filename)
	{
		this.name = "";
		this.nickname = "";
		this.year = "";
		this.filename = filename;
		this.helmet = "";
		this.abrv = "";

		for(int i=0; i<30; i++)
			for(int j=0; j<18; j++)
			{
				offenseResults[i][j] = new JResult();
				defenseResults[i][j] = new JResult();
			}

		try
		{
			load();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("FileNotFoundException");
		}
		catch(IOException e)
		{
			System.out.println("IOException");
		}
	}

	@SuppressWarnings("unused")
	private Boolean	_loaded = false;
	public void load() throws IOException
	{
		FileReader fr = new FileReader(filename);
		if( fr != null )
		{
			BufferedReader br = new BufferedReader(fr);
			if(br != null)
			{
				Integer intgr = null;
				String line = br.readLine();
				String[] items = line.split(",");
				System.out.println(line);

				name = items[1];
				nickname = items[2];
				year = items[0];
				if(items.length > 6) helmet = items[6];
				intgr = new Integer(items[3]);	fumble = intgr.intValue();

				helmet = helmet.replace("c:\\Mike\\Football\\", "");

				if(items.length > 7) abrv = items[7];
				if( abrv.length() == 0 ) abrv = name.substring(0,3);

				line = br.readLine();

				for(int r=0; r<30; r++)
				{
					line = br.readLine();
					items = line.split(",");

					offenseResults[r][JGame.playIndex(JGame.Plays.LinePlunge)].Interpret(items[1]);
					offenseResults[r][JGame.playIndex(JGame.Plays.Counter)].Interpret(items[2]);
					offenseResults[r][JGame.playIndex(JGame.Plays.EndReverse)].Interpret(items[3]);
					offenseResults[r][JGame.playIndex(JGame.Plays.Draw)].Interpret(items[4]);
					offenseResults[r][JGame.playIndex(JGame.Plays.Option)].Interpret(items[5]);
					offenseResults[r][JGame.playIndex(JGame.Plays.Screen)].Interpret(items[6]);
					offenseResults[r][JGame.playIndex(JGame.Plays.SprintOut)].Interpret(items[7]);
					offenseResults[r][JGame.playIndex(JGame.Plays.Bootleg)].Interpret(items[8]);
					offenseResults[r][JGame.playIndex(JGame.Plays.Dropback)].Interpret(items[9]);

					offenseResults[r][JGame.playIndex(JGame.Plays.Breakaway)].Interpret(items[10]);
					offenseResults[r][JGame.playIndex(JGame.Plays.QuarterbackRun)].Interpret(items[11]);
					offenseResults[r][JGame.playIndex(JGame.Plays.QuarterbackTrapped)].Interpret(items[12]);
				}

				line = br.readLine();

				for(int r=0; r<30; r++)
				{
					line = br.readLine();
					items = line.split(",");

					defenseResults[r][JGame.playIndex(JGame.Plays.LinePlunge)].Interpret(items[1]);
					defenseResults[r][JGame.playIndex(JGame.Plays.Counter)].Interpret(items[2]);
					defenseResults[r][JGame.playIndex(JGame.Plays.EndReverse)].Interpret(items[3]);
					defenseResults[r][JGame.playIndex(JGame.Plays.Draw)].Interpret(items[4]);
					defenseResults[r][JGame.playIndex(JGame.Plays.Option)].Interpret(items[5]);
					defenseResults[r][JGame.playIndex(JGame.Plays.Screen)].Interpret(items[6]);
					defenseResults[r][JGame.playIndex(JGame.Plays.SprintOut)].Interpret(items[7]);
					defenseResults[r][JGame.playIndex(JGame.Plays.Bootleg)].Interpret(items[8]);
					defenseResults[r][JGame.playIndex(JGame.Plays.Dropback)].Interpret(items[9]);

					offenseResults[r][JGame.playIndex(JGame.Plays.Kickoff)].Interpret(items[10]);
					offenseResults[r][JGame.playIndex(JGame.Plays.KickoffReturn)].Interpret(items[11]);
					offenseResults[r][JGame.playIndex(JGame.Plays.Punt)].Interpret(items[12]);
					if(items.length > 13) offenseResults[r][JGame.playIndex(JGame.Plays.PuntReturn)].Interpret(items[13]);
					if(items.length > 14) offenseResults[r][JGame.playIndex(JGame.Plays.InterceptionReturn)].Interpret(items[14]);
					if(items.length > 15) offenseResults[r][JGame.playIndex(JGame.Plays.FieldGoal)].Interpret(items[15]);
				}
				_loaded = true;
			}
			
			br.close();
		}
		/*
		public void Load()
		{
			if(!m_bLoaded)
			{
				try
				{
					// open the list file
					TextReader teamFile = new StreamReader(m_filename);
					if(teamFile != null)
					{
						string line = teamFile.ReadLine();
						//System.Diagnostics.Debug.WriteLine(line);

						ArrayList items = new ArrayList();
						FB.Utils.Parse(line, items);

						School = items[1];
						Name = items[2];
						Year = items[0];
						Helmet = Helmets.GetImage(School);

						Fumble = System.Convert.ToInt32(items[3]);
						if(items.Count > 7) Abrv = items[7];

						if( Abrv.Count() == 0 ) Abrv = School.Substring(0,3);

						line = teamFile.ReadLine();
						//System.Diagnostics.Debug.WriteLine(line);

						for(int r=0; r<30; r++)
						{
							line = teamFile.ReadLine();
							//System.Diagnostics.Debug.WriteLine(line);

							items.Clear();
							FB.Utils.Parse(line, items);

							offenseResults[r][JGame.playIndex(JGame.Plays.LinePlunge)].Interpret(items[1]);
							offenseResults[r][JGame.playIndex(JGame.Plays.Counter)].Interpret(items[2]);
							offenseResults[r][JGame.playIndex(JGame.Plays.EndReverse)].Interpret(items[3]);
							offenseResults[r][JGame.playIndex(JGame.Plays.Draw)].Interpret(items[4]);
							offenseResults[r][JGame.playIndex(JGame.Plays.Option)].Interpret(items[5]);
							offenseResults[r][JGame.playIndex(JGame.Plays.Screen)].Interpret(items[6]);
							offenseResults[r][JGame.playIndex(JGame.Plays.SprintOut)].Interpret(items[7]);
							offenseResults[r][JGame.playIndex(JGame.Plays.Bootleg)].Interpret(items[8]);
							offenseResults[r][JGame.playIndex(JGame.Plays.Dropback)].Interpret(items[9]);

							offenseResults[r][JGame.playIndex(JGame.Plays.Breakaway)].Interpret(items[10]);
							offenseResults[r][JGame.playIndex(JGame.Plays.QuarterbackRun)].Interpret(items[11]);
							offenseResults[r][JGame.playIndex(JGame.Plays.QuarterbackTrapped)].Interpret(items[12]);
						}

						line = teamFile.ReadLine();
						//System.Diagnostics.Debug.WriteLine(line);

						for(int r=0; r<30; r++)
						{
							line = teamFile.ReadLine();
							//System.Diagnostics.Debug.WriteLine(line);

							items.Clear();
							FB.Utils.Parse(line, items);

							defenseResults[r][JGame.playIndex(JGame.Plays.LinePlunge)].Interpret(items[1]);
							defenseResults[r][JGame.playIndex(JGame.Plays.Counter)].Interpret(items[2]);
							defenseResults[r][JGame.playIndex(JGame.Plays.EndReverse)].Interpret(items[3]);
							defenseResults[r][JGame.playIndex(JGame.Plays.Draw)].Interpret(items[4]);
							defenseResults[r][JGame.playIndex(JGame.Plays.Option)].Interpret(items[5]);
							defenseResults[r][JGame.playIndex(JGame.Plays.Screen)].Interpret(items[6]);
							defenseResults[r][JGame.playIndex(JGame.Plays.SprintOut)].Interpret(items[7]);
							defenseResults[r][JGame.playIndex(JGame.Plays.Bootleg)].Interpret(items[8]);
							defenseResults[r][JGame.playIndex(JGame.Plays.Dropback)].Interpret(items[9]);

							offenseResults[r][JGame.playIndex(JGame.Plays.Kickoff)].Interpret(items[10]);
							offenseResults[r][JGame.playIndex(JGame.Plays.KickoffReturn)].Interpret(items[11]);
							offenseResults[r][JGame.playIndex(JGame.Plays.Punt)].Interpret(items[12]);
							if(items.Count > 13) offenseResults[r][JGame.playIndex(JGame.Plays.PuntReturn)].Interpret(items[13]);
							if(items.Count > 14) offenseResults[r][JGame.playIndex(JGame.Plays.InterceptionReturn)].Interpret(items[14]);
							if(items.Count > 15) offenseResults[r][JGame.playIndex(JGame.Plays.FieldGoal)].Interpret(items[15]);
						}
						_loaded = true;
					}
				} catch
				{
					System.Diagnostics.Debug.WriteLine("An error occured while reading " + _filename);
				}

			} // if(!_loaded)

		} // public void Load()
		*/
	}
}
