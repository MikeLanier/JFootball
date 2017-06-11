/**
 * Created by MikeL on 11/26/2016.
 */
public class JPlayCaller
{
	static int SITUATION_0	= 0;	// Own 10
	static int SITUATION_1	= 1;	// 1st & 10
	static int SITUATION_2	= 2;	// 2nd & 6+
	static int SITUATION_3	= 3;	// 2nd & 5-
	static int SITUATION_4	= 4;	// 3rd & 5+
	static int SITUATION_5	= 5;	// 3rd & 4-
	static int SITUATION_6	= 6;	// 4th & 4+
	static int SITUATION_7	= 7;	// 4th & 3-
	static int SITUATION_8	= 8;	// Opp 10

	static JGame.Plays[][]	defenseCalls =
	{
		{ JGame.Plays.ShortPass, JGame.Plays.ShortGaps, JGame.Plays.ShortGaps, JGame.Plays.ShortPass, JGame.Plays.Standard, JGame.Plays.Blitz, JGame.Plays.ShortGaps, JGame.Plays.Standard, JGame.Plays.ShortGaps, JGame.Plays.ShortPass, JGame.Plays.ShortWide, JGame.Plays.ShortGaps, JGame.Plays.ShortPass, JGame.Plays.Undefined },	// Own 10
		{ JGame.Plays.ShortGaps, JGame.Plays.Standard, JGame.Plays.ShortPass, JGame.Plays.ShortGaps, JGame.Plays.Blitz, JGame.Plays.ShortPass, JGame.Plays.ShortGaps, JGame.Plays.ShortWide, JGame.Plays.ShortPass, JGame.Plays.Standard, JGame.Plays.Undefined, JGame.Plays.ShortGaps, JGame.Plays.ShortWide, JGame.Plays.Undefined },	// 1st & 10
		{ JGame.Plays.Standard, JGame.Plays.ShortPass, JGame.Plays.LongPass, JGame.Plays.ShortWide, JGame.Plays.Undefined, JGame.Plays.Standard, JGame.Plays.Blitz, JGame.Plays.ShortPass, JGame.Plays.Undefined, JGame.Plays.ShortWide, JGame.Plays.Standard, JGame.Plays.LongPass, JGame.Plays.ShortPass, JGame.Plays.ShortGaps },	// 2nd & 6+
		{ JGame.Plays.Undefined, JGame.Plays.Blitz, JGame.Plays.ShortGaps, JGame.Plays.ShortPass, JGame.Plays.Standard, JGame.Plays.ShortGaps, JGame.Plays.ShortPass, JGame.Plays.Standard, JGame.Plays.ShortWide, JGame.Plays.Undefined, JGame.Plays.ShortGaps, JGame.Plays.ShortPass, JGame.Plays.Standard, JGame.Plays.Blitz },	// 2nd & 5-
		{ JGame.Plays.ShortPass, JGame.Plays.LongPass, JGame.Plays.Standard, JGame.Plays.LongPass, JGame.Plays.LongPass, JGame.Plays.Blitz, JGame.Plays.LongPass, JGame.Plays.LongPass, JGame.Plays.LongPass, JGame.Plays.ShortPass, JGame.Plays.Standard, JGame.Plays.ShortWide, JGame.Plays.LongPass, JGame.Plays.Undefined },	// 3rd & 5+
		{ JGame.Plays.Standard, JGame.Plays.ShortGaps, JGame.Plays.ShortPass, JGame.Plays.ShortPass, JGame.Plays.ShortGaps, JGame.Plays.ShortWide, JGame.Plays.Standard, JGame.Plays.ShortGaps, JGame.Plays.ShortGaps, JGame.Plays.Blitz, JGame.Plays.ShortPass, JGame.Plays.ShortWide, JGame.Plays.ShortGaps, JGame.Plays.Undefined },	// 3rd & 4-
		{ JGame.Plays.ShortPass, JGame.Plays.Standard, JGame.Plays.ShortWide, JGame.Plays.Blitz, JGame.Plays.Standard, JGame.Plays.ShortPass, JGame.Plays.Undefined, JGame.Plays.Standard, JGame.Plays.LongPass, JGame.Plays.ShortPass, JGame.Plays.Blitz, JGame.Plays.Undefined, JGame.Plays.Standard, JGame.Plays.LongPass },	// 4th & 4+
		{ JGame.Plays.Standard, JGame.Plays.ShortPass, JGame.Plays.ShortPass, JGame.Plays.ShortGaps, JGame.Plays.ShortWide, JGame.Plays.ShortGaps, JGame.Plays.ShortPass, JGame.Plays.ShortPass, JGame.Plays.Standard, JGame.Plays.ShortGaps, JGame.Plays.Blitz, JGame.Plays.ShortPass, JGame.Plays.ShortGaps, JGame.Plays.Undefined },	// 4th & 3-
		{ JGame.Plays.ShortWide, JGame.Plays.ShortGaps, JGame.Plays.Standard, JGame.Plays.Blitz, JGame.Plays.ShortPass, JGame.Plays.Undefined, JGame.Plays.ShortGaps, JGame.Plays.ShortPass, JGame.Plays.Standard, JGame.Plays.ShortGaps, JGame.Plays.ShortWide, JGame.Plays.Standard, JGame.Plays.ShortPass, JGame.Plays.Blitz }	// Opp 10
	};

	static JGame.Plays[][]	wildcardCalls =
	{
		{ JGame.Plays.ShortGaps, JGame.Plays.ShortWide, JGame.Plays.Standard, JGame.Plays.ShortPass },	//	1st	
		{ JGame.Plays.Standard, JGame.Plays.ShortPass, JGame.Plays.ShortGaps, JGame.Plays.ShortWide },	//	2nd	
		{ JGame.Plays.Blitz, JGame.Plays.LongPass, JGame.Plays.ShortPass, JGame.Plays.Standard },	//	3rd	
		{ JGame.Plays.ShortPass, JGame.Plays.Standard, JGame.Plays.LongPass, JGame.Plays.ShortGaps },	//	4th	
		{ JGame.Plays.Standard, JGame.Plays.ShortPass, JGame.Plays.ShortGaps, JGame.Plays.ShortPass },	//	Own 10	
		{ JGame.Plays.ShortGaps, JGame.Plays.Blitz, JGame.Plays.ShortPass, JGame.Plays.Standard },	//	Off 10	
	};

	static JGame.Plays Defense( JGameData data )
	{
		int	situation = 0;

		if( data.yardline <= 10 )	situation = SITUATION_0; else	// Own 10
		if( data.yardline >= 90 )	situation = SITUATION_8; else	// Opp 10
		if( data.down == 1 )		situation = SITUATION_1; else	// 1st & 10
		if( data.down == 2 && data.togo >= 6 )	situation = SITUATION_2; else	// 2nd & 6+
		if( data.down == 2 && data.togo <= 5 )	situation = SITUATION_3; else	// 2nd & 5-
		if( data.down == 3 && data.togo >= 5 )	situation = SITUATION_4; else	// 3rd & 5+
		if( data.down == 3 && data.togo <= 4 )	situation = SITUATION_5; else	// 3rd & 4-
		if( data.down == 4 && data.togo >= 4 )	situation = SITUATION_6; else	// 4th & 4+
		if( data.down == 4 && data.togo <= 3 )	situation = SITUATION_7;		// 4th & 3-

		int roll = JGame.dice.roll(14);

		JGame.Plays	call = defenseCalls[situation][roll];

		if( call == JGame.Plays.Undefined )
		{
			situation = 0;

			if( data.yardline <= 10 )	situation = SITUATION_4; else	// Own 10
			if( data.yardline >= 90 )	situation = SITUATION_5; else	// Opp 10
			if( data.down == 1 )		situation = SITUATION_0; else	// 1st
			if( data.down == 2 )		situation = SITUATION_1; else	// 2nd
			if( data.down == 3 )		situation = SITUATION_2; else	// 3rd
			if( data.down == 4 )		situation = SITUATION_3;		// 4th

			roll = JGame.dice.roll(4);

			call = wildcardCalls[situation][roll];
		}

		return call;
	}
}
