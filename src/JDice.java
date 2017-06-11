import java.util.Random;

public class JDice
{
	Random rand = new Random();

	public JDice()
	{
	}

	int[] greenDice		= { 0, 0, 0, 0, 1, 2};
	int[] redDice		= { 1, 2, 1, 1, 2, 3};
	int[] blackDice		= { 3, 2, 1, 2, 3, 3};
	int[] white4Dice	= { 1, 2, 0, 0, 3, 4};
	int[] white5Dice	= { 0, 1, 3, 2, 4, 5};

	public int roll(int max)
	{
		return rand.nextInt(max);
	}

	public int green()
	{
		int r = rand.nextInt(6);
		r = greenDice[r];

		if(JGame.diceStack.size()>0)
		{
			r = JGame.diceStack.get(0).intValue();
			JGame.diceStack.remove(JGame.diceStack.get(0));
		}

		return r;
	}

	public int red()
	{
		int r = rand.nextInt(6);
		r = redDice[r];

		if(JGame.diceStack.size()>0)
		{
			r = JGame.diceStack.get(0).intValue();
			JGame.diceStack.remove(JGame.diceStack.get(0));
		}

		return r;
	}

	public int black()
	{
		int r = rand.nextInt(6);
		r =  blackDice[r];

		if(JGame.diceStack.size()>0)
		{
			r = JGame.diceStack.get(0).intValue();
			JGame.diceStack.remove(JGame.diceStack.get(0));
		}

		return r;
	}

	public int white4()
	{
		int r = rand.nextInt(6);
		r = white4Dice[r];

		if(JGame.diceStack.size()>0)
		{
			r = JGame.diceStack.get(0).intValue();
			JGame.diceStack.remove(JGame.diceStack.get(0));
		}

		return r;
	}

	public int white5()
	{
		int r = rand.nextInt(6);
		r = white5Dice[r];

		if(JGame.diceStack.size()>0)
		{
			r = JGame.diceStack.get(0).intValue();
			JGame.diceStack.remove(JGame.diceStack.get(0));
		}

		return r;
	}

	///////////////////////////////////////////////////////////////////
	public int offense()
	{
		Integer roll = 0;

		if(JGame.diceStack.size()>0)
		{
			roll = JGame.diceStack.get(0).intValue();
			JGame.diceStack.remove(JGame.diceStack.get(0));
		}
		else
		{
			roll = black() * 10 + white4() + white5();
		}

		JLog.writeln("Dice: offense: " + roll.toString());
		return roll.intValue();
	}

	///////////////////////////////////////////////////////////////////
	public int DS()
	{
		return black()*10 + white4() + white5();
	}

	public int T1()
	{
		return offense();
	}

	public int T2()
	{
		return offense()+offense();
	}

	public int T3()
	{
		return offense()+offense()+offense();
	}

	///////////////////////////////////////////////////////////////////
	public int defense()
	{
		return green() + red();
	}

	///////////////////////////////////////////////////////////////////
	public int toss()
	{
		return rand.nextInt(2);
	}
}
