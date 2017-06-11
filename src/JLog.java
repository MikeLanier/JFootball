import java.io.*;

public class JLog
{
	private static int _indent = 0;

	public JLog()
	{
	}

	static void create()
	{
		try
		{
			_create();
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

	static void _create() throws IOException
	{
		try
		{
			// Create file
			FileWriter fstream = new FileWriter("src\\LOG.txt", false);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("JGame log....");
			out.newLine();
			out.close();
		}
		catch (Exception e)
		{//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	static void writeln(String str)
	{
		try
		{
			_writeln(str);
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

	static void addindent()
	{
		writeln("{");
		_indent++;
	}

	static void subindent()
	{
		_indent--;
		writeln("}");
	}

	static void _writeln(String str) throws IOException
	{
		try
		{
			// Create file
			FileWriter fstream = new FileWriter("src\\LOG.txt", true);
			BufferedWriter out = new BufferedWriter(fstream);

			for(int i=0; i<_indent; i++)
			{
				out.write("    ");
			}

			out.write(str);
			out.newLine();
			out.close();
		}
		catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
