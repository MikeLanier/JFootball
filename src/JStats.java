import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class JStats
{
	public IntegerProperty points = new SimpleIntegerProperty(0);

	public void Points(int _points)	{	points.set(points.get()+_points);	}

	public IntegerProperty first_downs = new SimpleIntegerProperty(0);

	public void FirstDown()    {   first_downs.set(first_downs.get()+1);}

	public IntegerProperty total_yards = new SimpleIntegerProperty(0);
	public IntegerProperty total_plays = new SimpleIntegerProperty(0);
	public IntegerProperty total_touchdowns = new SimpleIntegerProperty(0);
	public IntegerProperty total_safeties = new SimpleIntegerProperty(0);
	public IntegerProperty total_fumbles = new SimpleIntegerProperty(0);
	public IntegerProperty total_fumbles_lost = new SimpleIntegerProperty(0);

	public void TotalYards(int yards)
	{
		total_yards.set(total_yards.get()+yards);
		total_plays.set(total_plays.get()+1);
	}

	public void TotalPlays()
	{
		total_plays.set(total_plays.get()+1);
	}

	public void TotalTouchdowns()   {   total_touchdowns.set(total_touchdowns.get()+1);    }
	public void TotalSafeties()     {   total_safeties.set(total_safeties.get()+1);  }

	public void TotalFumbles(Boolean lost)
	{
		total_fumbles.set(total_fumbles.get()+1);
		if(lost)    total_fumbles_lost.set(total_fumbles_lost.get()+1);
	}

	public IntegerProperty rush_yards = new SimpleIntegerProperty(0);
	public IntegerProperty rush_carries = new SimpleIntegerProperty(0);
	public IntegerProperty rush_touchdowns = new SimpleIntegerProperty(0);
	//public IntegerProperty rush_fumbles = new SimpleIntegerProperty(0);
	//public IntegerProperty rush_fumbles_lost = new SimpleIntegerProperty(0);

	public void Rush(int yards)
	{
		rush_yards.set(rush_yards.get()+yards);
		rush_carries.set(rush_carries.get()+1);
		TotalYards(yards);
	}

	public void RushTouchdowns()
	{
		rush_touchdowns.set(rush_touchdowns.get()+1);
		TotalTouchdowns();
	}

	//public void RushFumble(Boolean lost)
	//{
	//	rush_fumbles.set(rush_fumbles.get()+1);
	//	if(lost)    rush_fumbles_lost.set(rush_fumbles_lost.get()+1);
	//	TotalFumbles(lost);
	//}

	public IntegerProperty pass_yards = new SimpleIntegerProperty(0);
	public IntegerProperty pass_attempts = new SimpleIntegerProperty(0);
	public IntegerProperty pass_completions = new SimpleIntegerProperty(0);
	public IntegerProperty pass_touchdowns = new SimpleIntegerProperty(0);
	//public IntegerProperty pass_fumbles = new SimpleIntegerProperty(0);
	//public IntegerProperty pass_fumbles_lost = new SimpleIntegerProperty(0);
	public IntegerProperty pass_interceptions = new SimpleIntegerProperty(0);
	public IntegerProperty pass_sacks = new SimpleIntegerProperty(0);

	public void Pass(int yards)
	{
		pass_yards.set(pass_yards.get()+yards);
		pass_attempts.set(pass_attempts.get()+1);
		pass_completions.set(pass_completions.get()+1);
		TotalYards(yards);
	}

	public void PassIncomplete()
	{
		pass_attempts.set(pass_attempts.get()+1);
	}

	public void PassTouchdowns()
	{
		pass_touchdowns.set(pass_touchdowns.get()+1);
		TotalTouchdowns();
	}

	//public void PassFumble(Boolean lost)
	//{
	//	pass_fumbles.set(pass_fumbles.get()+1);
	//	if(lost)    pass_fumbles_lost.set(pass_fumbles_lost.get()+1);
	//	TotalFumbles(lost);
	//}

	public void PassInterception()	{	pass_interceptions.set(pass_interceptions.get()+1);	}
	public void Sack() { pass_sacks.set(pass_sacks.get()+1); }

	public IntegerProperty interception_returns = new SimpleIntegerProperty(0);
	public IntegerProperty interception_return_yards = new SimpleIntegerProperty(0);
	public IntegerProperty interception_return_touchdowns = new SimpleIntegerProperty(0);
	//public IntegerProperty interception_return_fumbles = new SimpleIntegerProperty(0);
	//public IntegerProperty interception_return_fumbles_lost = new SimpleIntegerProperty(0);

	public void InterceptionReturn(int yards)
	{
		interception_returns.set(interception_returns.get()+1);
		interception_return_yards.set(interception_return_yards.get()+yards);
	}

	public void InterceptionReturnTouchdowns()    { interception_return_touchdowns.set(interception_return_touchdowns.get()+1); }

	//public void InterceptionReturnFumble(Boolean lost)
	//{
	//	interception_return_fumbles.set(interception_return_fumbles.get()+1);
	//	if(lost)    interception_return_fumbles_lost.set(interception_return_fumbles_lost.get()+1);
	//	TotalFumbles(lost);
	//}

	public IntegerProperty punts = new SimpleIntegerProperty(0);
	public IntegerProperty punt_yards = new SimpleIntegerProperty(0);
	public IntegerProperty punts_returned = new SimpleIntegerProperty(0);
	public IntegerProperty punts_blocked = new SimpleIntegerProperty(0);

	public void Punt(int yards)
	{
		punts.set(punts.get()+1);
		punt_yards.set(punt_yards.get()+yards);
	}

	public void PuntReturned()  { punts_returned.set(punts_returned.get()+1); }

	public void PuntBlocked()
	{
		punts_blocked.set(punts_blocked.get()+1);
	}

	public IntegerProperty punt_returns = new SimpleIntegerProperty(0);
	public IntegerProperty punt_return_yards = new SimpleIntegerProperty(0);
	public IntegerProperty punt_return_touchdowns = new SimpleIntegerProperty(0);
	//public IntegerProperty punt_return_fumbles = new SimpleIntegerProperty(0);
	//public IntegerProperty punt_return_fumbles_lost = new SimpleIntegerProperty(0);

	public void PuntReturn(int yards)
	{
		punt_returns.set(punt_returns.get()+1);
		punt_return_yards.set(punt_return_yards.get()+yards);
	}

	public void PuntReturnTouchdown()
	{
		punt_return_touchdowns.set(punt_return_touchdowns.get()+1);
		TotalTouchdowns();
	}

	//public void PuntReturnFumble(Boolean lost)
	//{
	//	punt_return_fumbles.set(punt_return_fumbles.get()+1);
	//	if(lost)    punt_return_fumbles_lost.set(punt_return_fumbles_lost.get()+1);
	//	TotalFumbles(lost);
	//}

	public IntegerProperty kickoffs = new SimpleIntegerProperty(0);
	public IntegerProperty kickoff_yards = new SimpleIntegerProperty(0);
	public IntegerProperty kickoffs_returned = new SimpleIntegerProperty(0);

	public void Kickoff(int yards)
	{
		kickoffs.set(kickoffs.get()+1);
		kickoff_yards.set(kickoff_yards.get()+yards);
	}

	public void KickoffReturned()   { kickoffs_returned.set(kickoffs_returned.get()+1); }

	public IntegerProperty kickoff_returns = new SimpleIntegerProperty(0);
	public IntegerProperty kickoff_return_yards = new SimpleIntegerProperty(0);
	public IntegerProperty kickoff_return_touchdowns = new SimpleIntegerProperty(0);
	//public IntegerProperty kickoff_return_fumbles = new SimpleIntegerProperty(0);
	//public IntegerProperty kickoff_return_fumbles_lost = new SimpleIntegerProperty(0);

	public void KickoffReturn(int yards)
	{
		kickoff_returns.set(kickoff_returns.get()+1);
		kickoff_return_yards.set(kickoff_return_yards.get()+yards);
	}

	public void KickoffReturnTouchdown()    { kickoff_return_touchdowns.set(kickoff_return_touchdowns.get()+1); }

	//public void KickoffReturnFumble(Boolean lost)
	//{
	//	kickoff_return_fumbles.set(kickoff_return_fumbles.get()+1);
	//	if(lost)    kickoff_return_fumbles_lost.set(kickoff_return_fumbles_lost.get()+1);
	//	TotalFumbles(lost);
	//}

	public IntegerProperty fieldgoal_attempts = new SimpleIntegerProperty(0);
	public IntegerProperty fieldgoals_made = new SimpleIntegerProperty(0);
	public IntegerProperty fieldgoals_blocked = new SimpleIntegerProperty(0);

	public void Fieldgoal(Boolean made)
	{
		fieldgoal_attempts.set(fieldgoal_attempts.get()+1);
		if(made)    fieldgoals_made.set(fieldgoals_made.get()+1);
	}

	public void FieldgoalBlocked()  { fieldgoals_blocked.set(fieldgoals_blocked.get()+1); }

	public IntegerProperty extrapoint_attempts = new SimpleIntegerProperty(0);
	public IntegerProperty extrapoints_made = new SimpleIntegerProperty(0);
	public IntegerProperty extrapoints_blocked = new SimpleIntegerProperty(0);

	public void ExtraPoint(Boolean made)
	{
		extrapoint_attempts.set(extrapoint_attempts.get()+1);
		if(made)    extrapoints_made.set(extrapoints_made.get()+1);
	}

	public void ExtraPointBlocked() { extrapoints_blocked.set(extrapoints_blocked.get()+1); }

	public IntegerProperty penalties = new SimpleIntegerProperty(0);
	public IntegerProperty penalty_yards = new SimpleIntegerProperty(0);

	public void Penalty(int yards)
	{
		penalties.set(penalties.get()+1);
		penalty_yards.set(penalty_yards.get()+yards);
	}

	public JStats()
	{
	}

	public void copy(JStats stats)
	{
		points.set(stats.points.get());

		first_downs.set(stats.first_downs.get());

		total_yards.set(stats.total_yards.get());
		total_plays.set(stats.total_plays.get());
		total_touchdowns.set(stats.total_touchdowns.get());
		total_safeties.set(stats.total_safeties.get());
		total_fumbles.set(stats.total_fumbles.get());
		total_fumbles_lost.set(stats.total_fumbles_lost.get());

		rush_yards.set(stats.rush_yards.get());
		rush_carries.set(stats.rush_carries.get());
		rush_touchdowns.set(stats.rush_touchdowns.get());
		//rush_fumbles.set(stats.rush_fumbles.get());
		//rush_fumbles_lost.set(stats.rush_fumbles_lost.get());

		pass_yards.set(stats.pass_yards.get());
		pass_attempts.set(stats.pass_attempts.get());
		pass_completions.set(stats.pass_completions.get());
		pass_touchdowns.set(stats.pass_touchdowns.get());
		//pass_fumbles.set(stats.pass_fumbles.get());
		//pass_fumbles_lost.set(stats.pass_fumbles_lost.get());
		pass_interceptions.set(stats.pass_interceptions.get());
		pass_sacks.set(stats.pass_sacks.get());

		interception_returns.set(stats.interception_returns.get());
		interception_return_yards.set(stats.interception_return_yards.get());
		interception_return_touchdowns.set(stats.interception_return_touchdowns.get());
		//interception_return_fumbles.set(stats.interception_return_fumbles.get());
		//interception_return_fumbles_lost.set(stats.interception_return_fumbles_lost.get());

		punts.set(stats.punts.get());
		punt_yards.set(stats.punt_yards.get());
		punts_returned.set(stats.punts_returned.get());
		punts_blocked.set(stats.punts_blocked.get());

		punt_returns.set(stats.punt_returns.get());
		punt_return_yards.set(stats.punt_return_yards.get());
		punt_return_touchdowns.set(stats.punt_return_touchdowns.get());
		//punt_return_fumbles.set(stats.punt_return_fumbles.get());
		//punt_return_fumbles_lost.set(stats.punt_return_fumbles_lost.get());

		kickoffs.set(stats.kickoffs.get());
		kickoff_yards.set(stats.kickoff_yards.get());
		kickoffs_returned.set(stats.kickoffs_returned.get());

		kickoff_returns.set(stats.kickoff_returns.get());
		kickoff_return_yards.set(stats.kickoff_return_yards.get());
		kickoff_return_touchdowns.set(stats.kickoff_return_touchdowns.get());
		//kickoff_return_fumbles.set(stats.kickoff_return_fumbles.get());
		//kickoff_return_fumbles_lost.set(stats.kickoff_return_fumbles_lost.get());

		fieldgoal_attempts.set(stats.fieldgoal_attempts.get());
		fieldgoals_made.set(stats.fieldgoals_made.get());
		fieldgoals_blocked.set(stats.fieldgoals_blocked.get());

		extrapoint_attempts.set(stats.extrapoint_attempts.get());
		extrapoints_made.set(stats.extrapoints_made.get());
		extrapoints_blocked.set(stats.extrapoints_blocked.get());

		penalties.set(stats.penalties.get());
		penalty_yards.set(stats.penalty_yards.get());
	}
}
