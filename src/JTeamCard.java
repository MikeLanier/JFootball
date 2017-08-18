//import com.sun.prism.Graphics;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
//import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.sun.javafx.tk.Toolkit.getToolkit;

public class JTeamCard extends Stage
{
	private double _scale = .75;
	private Canvas _canvas = new Canvas(JResult.width*_scale*36+20,JResult.height*_scale*90 + 120);
	private ScrollPane _pane = new ScrollPane();
	private Scene _scene = new Scene(_pane);

	double scale = 1.0;
	double baseWidth = JResult.width * scale;
	double baseHeight = JResult.height * scale;

	double[] offColumn = { .6, 1, 1, 1, 1, 1, 1, 1, 1, 1, .6, 1, 1, 1, .6 };
	double[] offHeight = { 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

	double[] defColumn = { 2, .6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, .6, 1, 1, 1, 1, 1, 1, .6 };
	double[] defHeight = { 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

	private Font _font = new Font("Arial Narrow", 12);

	private void box(GraphicsContext gc, String tx, double x, double y, double width, double height)
	{
		gc.setLineWidth(1);
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.BLACK);
		gc.fillRect(x, y, width, height);
		gc.strokeRect(x, y, width, height);

		gc.setFont(_font);
		float widthTx = getToolkit().getFontLoader().computeStringWidth(tx, gc.getFont());
		float heightTx = getToolkit().getFontLoader().getFontMetrics(gc.getFont()).getXheight();

		double xCenter = width/2 + x;
		double yCenter = height/2 + y;

		double xOffset = xCenter - widthTx/2;
		double yOffset = yCenter + heightTx/2;

		gc.setFill(Color.BLACK);

		gc.fillText(tx, xOffset, yOffset);
	}

	public JTeamCard(JTeam team, Boolean offense)
	{
		initModality(Modality.APPLICATION_MODAL);
		//initStyle(StageStyle.UNDECORATED);
		_pane.setContent(_canvas);
		_pane.setPrefSize(400, 400);
		setScene(_scene);

		GraphicsContext gc = _canvas.getGraphicsContext2D();

		double x = 10;
		double y = 10;
		double[] size = {0,0};

		cardOffense(gc, team, x, y, size);

		System.out.println("width: " + size[0]);
		System.out.println("height: " + size[1]);

		cardDefense(gc, team, size[0]+10, y, size);
	}

	private void cardOffense(GraphicsContext gc, JTeam team, double x, double y, double[] size)
	{
		String offTitles[] = { "Line\nPlunge", "Counter", "End +\nReverse", "Draw", "Option", "Screen", "Sprint\nOut", "Bootleg", "Drop\nBack", "B", "QR", "QT"};

		Integer intgr = null;

		double xLeft = x;

		{
			x = xLeft + JResult.width * _scale * 1;

			x = x + JResult.height * _scale;
			double x2 = x + JResult.width * _scale * 4.5;
			double x3 = x + JResult.width * _scale * 9;

			double yOff = (JResult.height * .5 * _scale);

			gc.setFont(new Font(16));
			gc.setStroke(Color.BLACK);
			gc.setLineDashes(5, 5);
			gc.strokeLine(x, y + yOff, x+60, y + (JResult.height * .5 * _scale));
			gc.strokeLine(x+160, y + yOff, x2, y + (JResult.height * .5 * _scale));

			gc.strokeLine(x2, y + yOff, x2+60, y + (JResult.height * .5 * _scale));
			gc.strokeLine(x2+160, y + yOff, x3, y + (JResult.height * .5 * _scale));
			gc.setLineDashes(0);

			gc.strokeLine(x, y+2, x, y+yOff+yOff-2);
			gc.strokeLine(x2, y+2, x2, y+yOff+yOff-2);
			gc.strokeLine(x3, y+2, x3, y+yOff+yOff-2);

			gc.strokeLine(x+5, y + yOff - 3, x, y + yOff);
			gc.strokeLine(x+5, y + yOff + 3, x, y + yOff);
			gc.strokeLine(x2-5, y + yOff - 3, x2, y + yOff);
			gc.strokeLine(x2-5, y + yOff + 3, x2, y + yOff);

			gc.strokeLine(x2+5, y + yOff - 3, x2, y + yOff);
			gc.strokeLine(x2+5, y + yOff + 3, x2, y + yOff);
			gc.strokeLine(x3-5, y + yOff - 3, x3, y + yOff);
			gc.strokeLine(x3-5, y + yOff + 3, x3, y + yOff);

			gc.fillText("Rushing plays", x+60, y+yOff+5);
			gc.fillText("Passing plays", x2+60, y+yOff+5);

			y = y + (JResult.height * _scale);
		}

		{
			x = xLeft + JResult.width * _scale * 1;

			x = x + JResult.height * _scale;

			for(int j = 0; j <= 8; j++)
			{
				intgr = new Integer(j+1);
				box( gc, intgr.toString(), x, y, JResult.width*_scale, JResult.width*.75*_scale);
				x = x + (JResult.width * _scale);
			}

			y = y + (JResult.width*.75 * _scale);
		}

		{
			x = xLeft + JResult.width * _scale * 1;

			box(gc, "#", x, y, JResult.height * _scale, JResult.width*_scale);
			x = x + JResult.height * _scale;

			for(int j = 0; j <= 8; j++)
			{
				box( gc, offTitles[j], x, y, JResult.width*_scale, JResult.width*.75*_scale);
				x = x + (JResult.width * _scale);
			}

			x = x + (JResult.height * _scale);

			for(int j = 9; j <= 11; j++)
			{
				box( gc, offTitles[j], x, y, JResult.width*_scale, JResult.width*.75*_scale);
				x = x + (JResult.width * _scale);
			}

			box(gc, "#", x, y, JResult.height * _scale, JResult.width*_scale);
			y = y + (JResult.width*.75 * _scale);
		}


		for(int i=0; i<30; i++)
		{
			x = xLeft + JResult.width * _scale * 1;

			intgr = new Integer(i+10);
			box(gc, intgr.toString(), x, y, JResult.height * _scale, JResult.height * _scale);
			x = x + JResult.height * _scale;

			for(int j = 0; j <= 8; j++)
			{
				team.offenseResults[i][j].draw(gc, x, y, _scale, false);

				x = x + (JResult.width * _scale);
			}

			x = x + (JResult.height * _scale);

			for(int j = 9; j <= 11; j++)
			{
				team.offenseResults[i][j].draw(gc, x, y, _scale, false);

				x = x + (JResult.width * _scale);
			}

			box(gc, intgr.toString(), x, y, JResult.height * _scale, JResult.height * _scale);
			x = x + JResult.height * _scale;
			y = y + (JResult.height * _scale);
		}

		//gc.setStroke(Color.BLACK);
		//gc.strokeLine(0, 0, x+10,y+10);
		//gc.strokeLine(x+10,0,0,y+10);

		//_canvas.setWidth(x+10);
		//_canvas.setHeight(y+10);
		//_pane.setPrefSize(x+10+32, y+10);

		size[0] = x + 10;
		size[1] = y + 10;

		gc.setFont(new Font(36));
		gc.setFill(Color.BLACK);

		gc.save();
		gc.translate(JResult.width * _scale * 1, JResult.height * _scale * 26);
		gc.rotate(270);
		gc.fillText(team.year + "  " + team.name, 0, 0);
		gc.restore();

		gc.save();
		gc.translate(JResult.width * _scale * 1, JResult.height * _scale * 11);
		gc.rotate(270);
		gc.fillText("offense", 0, 0);
		gc.restore();
	}

	private void cardDefense(GraphicsContext gc, JTeam team, double x, double y, double[] size)
	{
		String defTitles[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "Kickoff", "Kickoff\nReturn", "Punt", "Punt\nReturn", "Int.\nReturn", "Field\nGoal"};

		String defLabels[] = {"Standard","Short\nGaps","Short\nWide","Short\nPass","Long\nPass","Blitz"};

		Integer intgr = null;

		double xLeft = x;

		{

			gc.setFont(new Font(36));
			gc.setFill(Color.BLACK);

			gc.save();
			gc.translate(xLeft - 10 + JResult.width * _scale * 1, JResult.height * _scale * 26);
			gc.rotate(270);
			gc.fillText(team.year + "  " + team.nickname, 0, 0);
			gc.restore();

			gc.save();
			gc.translate(xLeft - 10 + JResult.width * _scale * 1, JResult.height * _scale * 11);
			gc.rotate(270);
			gc.fillText("defense", 0, 0);
			gc.restore();

		}
		{
			double y2 =  y + (JResult.height * _scale) + (JResult.width*.75 * _scale) + (JResult.width*.75 * _scale); //(JResult.height * .5 * _scale) + (JResult.height * _scale) + (JResult.width*.75 * _scale);
			for(int i=0; i<defLabels.length; i++)
			{
				box( gc, defLabels[i], x+JResult.width * _scale, y2, JResult.width*_scale, JResult.height * _scale * 5);
				y2 = y2 + (JResult.height * _scale * 5);
			}

			xLeft += (JResult.width*_scale);
		}
		{
			x = xLeft + JResult.width * _scale * 1;

			x = x + JResult.height * _scale;
			double x2 = x + JResult.width * _scale * 4.5;
			double x3 = x + JResult.width * _scale * 9;

			double yOff = (JResult.height * .5 * _scale);

			gc.setFont(new Font(16));
			gc.setStroke(Color.BLACK);
			gc.setLineDashes(5, 5);
			gc.strokeLine(x, y + yOff, x+60, y + (JResult.height * .5 * _scale));
			gc.strokeLine(x+160, y + yOff, x2, y + (JResult.height * .5 * _scale));

			gc.strokeLine(x2, y + yOff, x2+60, y + (JResult.height * .5 * _scale));
			gc.strokeLine(x2+160, y + yOff, x3, y + (JResult.height * .5 * _scale));
			gc.setLineDashes(0);

			gc.strokeLine(x, y+2, x, y+yOff+yOff-2);
			gc.strokeLine(x2, y+2, x2, y+yOff+yOff-2);
			gc.strokeLine(x3, y+2, x3, y+yOff+yOff-2);

			gc.strokeLine(x+5, y + yOff - 3, x, y + yOff);
			gc.strokeLine(x+5, y + yOff + 3, x, y + yOff);
			gc.strokeLine(x2-5, y + yOff - 3, x2, y + yOff);
			gc.strokeLine(x2-5, y + yOff + 3, x2, y + yOff);

			gc.strokeLine(x2+5, y + yOff - 3, x2, y + yOff);
			gc.strokeLine(x2+5, y + yOff + 3, x2, y + yOff);
			gc.strokeLine(x3-5, y + yOff - 3, x3, y + yOff);
			gc.strokeLine(x3-5, y + yOff + 3, x3, y + yOff);

			gc.fillText("Rushing plays", x+60, y+yOff+5);
			gc.fillText("Passing plays", x2+60, y+yOff+5);

			y = y + (JResult.height * _scale);
		}

		{
			x = xLeft + JResult.width * _scale * 1;

			x = x + JResult.height * _scale;

			for(int j = 0; j <= 8; j++)
			{
				intgr = new Integer(j+1);
				box( gc, intgr.toString(), x, y, JResult.width*_scale, JResult.width*.75*_scale);
				x = x + (JResult.width * _scale);
			}

			y = y + (JResult.width*.75 * _scale);
		}

		{
			x = xLeft + JResult.width * _scale * 1;

			box(gc, "#", x, y, JResult.height * _scale, JResult.width*_scale);
			x = x + JResult.height * _scale;

			for(int j = 0; j <= 8; j++)
			{
				box( gc, defTitles[j], x, y, JResult.width*_scale, JResult.width*.75*_scale);
				x = x + (JResult.width * _scale);
			}

			x = x + (JResult.height * _scale);

			for(int j = 9; j <= 14; j++)
			{
				box( gc, defTitles[j], x, y, JResult.width*_scale, JResult.width*.75*_scale);
				x = x + (JResult.width * _scale);
			}

			box(gc, "#", x, y, JResult.height * _scale, JResult.width*_scale);
			y = y + (JResult.width*.75 * _scale);
		}


		for(int i=0; i<30; i++)
		{
			x = xLeft + JResult.width * _scale * 1;

			intgr = new Integer((i%5)+1);
			box(gc, intgr.toString(), x, y, JResult.height * _scale, JResult.height * _scale);
			x = x + JResult.height * _scale;

			for(int j = 0; j <= 8; j++)
			{
				team.defenseResults[i][j].draw(gc, x, y, _scale, false);

				x = x + (JResult.width * _scale);
			}

			x = x + (JResult.height * _scale);

			for(int j = 12; j <= 17; j++)
			{
				team.offenseResults[i][j].draw(gc, x, y, _scale, false);

				x = x + (JResult.width * _scale);
			}

			intgr = new Integer(i+10);
			box(gc, intgr.toString(), x, y, JResult.height * _scale, JResult.height * _scale);
			x = x + JResult.height * _scale;
			y = y + (JResult.height * _scale);
		}

		//gc.setStroke(Color.BLACK);
		//gc.strokeLine(0, 0, x+10,y+10);
		//gc.strokeLine(x+10,0,0,y+10);

		_canvas.setWidth(x+10);
		_canvas.setHeight(y+10);
		_pane.setPrefSize(x+10+32, y+10);

		size[0] = x + 10;
		size[1] = y + 10;
	}
}
