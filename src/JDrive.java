import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

public class JDrive
{
	private double _yardStart = 0;
	private double _yards = 0;
	private int _offense = 0;

	private JResult _result = null;
	public void Result(JResult result) { _result = result; }

	static double _height = 10;
	static double Height() { return _height; }

	public JDrive(double yardStart, double yards, JResult result, int offense)
	{
		_yardStart = yardStart;
		_yards = yards;
		_result = result;
		_offense = offense;
	}

	public JDrive(JDrive drive)
	{
		_yardStart = drive._yardStart;
		_yards = drive._yards;
		_result = drive._result;
		_offense = drive._offense;
	}

	public void draw(GraphicsContext gc, double x, double y, double scale)
	{
		try
		{
			double x1 = 0;
			double x2 = 0;

			if(_offense == JGame.HOME)
			{
				x1 = x + (100 - _yardStart) * scale * 3;
				x2 = x + (100 - (_yardStart - _yards)) * scale * 3;
			}
			else
			{
				x1 = x + _yardStart * scale * 3;
				x2 = x + (_yardStart - _yards) * scale * 3;
			}

			gc.setLineCap(StrokeLineCap.BUTT);

			if(_result.DEF() || _result.DEFX() || _result.OFF() || _result.OFFX())
			{
				gc.setLineDashes(0);
				gc.setStroke(Color.YELLOW);
			}
			else
			{
				if(_result.Play() == JGame.Plays.Screen ||
						_result.Play() == JGame.Plays.SprintOut ||
						_result.Play() == JGame.Plays.Bootleg ||
						_result.Play() == JGame.Plays.Dropback ||
						(_result.Play() == JGame.Plays.Breakaway && _result.B_PASS()))
				{
					gc.setLineDashes(scale, scale);
				}
				else
				{
					gc.setLineDashes(0);
				}

				if(_result.Play() == JGame.Plays.Kickoff ||
						_result.Play() == JGame.Plays.OnsidesKick)
				{
					gc.setStroke(Color.LAVENDER);
					gc.setLineDashes(scale, scale);
				}
				else if(_result.Play() == JGame.Plays.KickoffReturn)
				{
					gc.setStroke(Color.LAVENDER);
				}
				else if(_result.Play() == JGame.Plays.Punt)
				{
					gc.setStroke(Color.LIGHTBLUE);
				}
				else if(_result.Play() == JGame.Plays.PuntReturn)
				{
					gc.setStroke(Color.LIGHTBLUE);
				}
				else if(_result.Play() == JGame.Plays.InterceptionReturn)
				{
					gc.setStroke(Color.AQUAMARINE);
				}
				else if(_result.Play() == JGame.Plays.FieldGoal)
				{
					gc.setStroke(Color.BLUE);
				}
				else
				{
					if(_yards < 0)
						gc.setStroke(Color.RED);
					else
						gc.setStroke(Color.GREENYELLOW);
				}
			}

			if(_result.INC())
			{
				double d = scale + 1;
				gc.setLineDashes(0);
				gc.setLineWidth(2);
				gc.setStroke(Color.RED);
				gc.strokeLine(x1 - d, y - d, x2 + d, y + d);
				gc.strokeLine(x1 - d, y + d, x2 + d, y - d);
			}
			else
			{
				gc.setLineWidth(4);

				gc.strokeLine(x1, y, x2, y);

				if(_result.F())
				{
					if(_result.F_LOST())
						gc.setFill(Color.RED);
					else
						gc.setFill(Color.GREENYELLOW);

					double d = scale + 1;
					gc.fillOval(x2 - d, y - d, d * 2, d * 2);
				}

				else if(_result.INT())
				{
					gc.setFill(Color.RED);
					gc.setStroke(Color.WHITE);

					double d = scale + 2;
					double xPoints[] = {x2 - d, x2, x2 + d, x2};
					double yPoints[] = {y, y - d, y, y + d};

					gc.fillPolygon(xPoints, yPoints, 4);
				}

				else if(_result.FD())
				{
					gc.setFill(Color.GREENYELLOW);
					gc.setStroke(Color.WHITE);

					if(_offense == JGame.HOME)
					{
						double d = scale + 2;
						double xPoints[] = {x2, x2, x2 + d, x2};
						double yPoints[] = {y, y - d, y, y + d};

						gc.fillPolygon(xPoints, yPoints, 4);
					}
					else
					{
						double d = scale + 2;
						double xPoints[] = {x2 - d, x2, x2, x2};
						double yPoints[] = {y, y - d, y, y + d};

						gc.fillPolygon(xPoints, yPoints, 4);
					}
				}
			}

			gc.setLineDashes(0);
		}
		catch(Exception e)
		{
			System.out.println("Exception caught JDrive.draw");
		}
	}
}
