import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

//import java.awt.*;

import static com.sun.javafx.tk.Toolkit.getToolkit;

// found that putting the Canvas in a StackPane and binding it to the height/width properties doesn't work well.
// the StackPane sizes to the Canvas and can never get smaller.  What I did that does work, I get the parent,
// from that I get the row/column percent of the cell the Canvas is in, listen for the parent to change size
// and use the percent to compute the size of the Canvas.  This is not great in that this class needs to know
// that the parent is a GridPane and which cell it is attached to.  Got to be a better way.

public class JField extends Canvas
{
	static double	m_dFieldLength = 300;
	static double	m_dEndZoneDepth = 30;
	static double	m_dFieldWidth = 160;
	static double	m_dHashMarkOffsetY = 60;
	static double	m_dHashMarkWidth = 2;
	static double	m_dLineWidth = 4/12;
	static double	m_dYardNumberHeight = 10;
	static double	m_dYardNumberWidth = 6;
	static double	m_dYardNumberOffsetY = 6 + m_dYardNumberHeight;
	static double	m_dSkirtWidth = 2;
	static double	m_dTotalWidth = m_dLineWidth * 4 + m_dSkirtWidth * 2 + m_dEndZoneDepth * 2 + m_dFieldLength;
	static double	m_dTotalHeight = m_dLineWidth * 4 + m_dSkirtWidth * 2 + m_dFieldWidth;

	static double	m_canvasWidth = 0;
	static double	m_canvasHeight = 0;

	public JField(Color color)
	{
		super();

		// listen for when the parent is set
		parentProperty().addListener(evt -> parentSet());
	}

	// stuff to save when the parent is set
	JGame m_parent = null;
	RowConstraints rowConstraint;
	ColumnConstraints columnConstraint;

	private void parentSet()
	{
		// get the parent
		m_parent = (JGame)getParent();

		// listen for when the parent size changes
		m_parent.widthProperty().addListener(evt -> draw());
		m_parent.heightProperty().addListener(evt -> draw());

		// get the column and row constraints for the gridpane cell the
		// field is assigned to.  That would be <0,1>
		columnConstraint = m_parent.getColumnConstraints().get(0);
		rowConstraint = m_parent.getRowConstraints().get(1);
	}

	private double trunk(double d)
	{
		return (double)((int)d);
	}

//	private double computeScale()
//	{
//		double sw = m_canvasWidth / m_dTotalWidth;
//		double sh = m_canvasHeight / m_dTotalHeight;
//		double s = sw < sh ? sw : sh;
//		s = trunk(s);
//
//		return s;
//	}

	private Rectangle centerRectangle(double width, double height)
	{
		double dw = (m_canvasWidth - width) / 2;
		double dh = (m_canvasHeight - height) / 2;

		return new Rectangle(dw, dh, width, height);
	}

	Rectangle fieldRange;		// range box around the field
	double scale;				// scale at which stuff is drawn

	public void draw()
	{
		// get the size of the parent
		m_canvasWidth = m_parent.getWidth();
		m_canvasHeight = m_parent.getHeight();
		JLog.writeln(m_canvasWidth + "," + m_canvasHeight);

		// compute the size of the GridPane cell
		m_canvasWidth = m_canvasWidth * columnConstraint.getPercentWidth() / 100;
		m_canvasHeight = m_canvasHeight * rowConstraint.getPercentHeight() / 100;

		// set the size of the field
		setWidth(m_canvasWidth);
		setHeight(m_canvasHeight);

		// compute the scale
		double sw = m_canvasWidth / m_dTotalWidth;
		double sh = m_canvasHeight / m_dTotalHeight;
		scale = trunk(sw < sh ? sw : sh);

		Rectangle range = centerRectangle((m_dTotalWidth * scale), (m_dTotalHeight * scale));
		fieldRange = centerRectangle(((m_dFieldLength + m_dEndZoneDepth * 2) * scale), (m_dFieldWidth * scale));

		// draw the field
		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0, 0, m_canvasWidth, m_canvasHeight);

		// fill the panel with a light green then draw a black border around it
		gc.setFill(JGame.LIGHTGREEN);
		gc.fillRoundRect(0, 0, m_canvasWidth, m_canvasHeight, 10, 10);

		gc.setStroke(Color.BLACK);
		gc.strokeRoundRect(0, 0, m_canvasWidth, m_canvasHeight, 10, 10);

		gc.setLineWidth(2);        // a line width of 2 seems to defeat the antialiasing

		// draw a dark green skirt around the field
		gc.setFill(Color.DARKGREEN);
		gc.setStroke(Color.WHITE);
		gc.fillRect(range.getX(), range.getY(), range.getWidth(), range.getHeight());
		gc.strokeRect(range.getX(), range.getY(), range.getWidth(), range.getHeight());

		// draw the field background green
		gc.setFill(Color.DARKGREEN);
		gc.fillRect(fieldRange.getX(), fieldRange.getY(), fieldRange.getWidth(), fieldRange.getHeight());
		gc.strokeRect(fieldRange.getX(), fieldRange.getY(), fieldRange.getWidth(), fieldRange.getHeight());

		// draw yard lines every 5 yards
		double x = fieldRange.getX() + m_dEndZoneDepth * scale;
		for(int i = 0; i < 21; i++)
		{
			gc.strokeLine(x, fieldRange.getY(), x, fieldRange.getY() + fieldRange.getHeight());
			x += 15 * scale;
		}

		// draw hash marks every yard
		x = fieldRange.getX() + m_dEndZoneDepth * scale;
		for(int i = 0; i < 101; i++)
		{
			gc.strokeLine(x, fieldRange.getY() + m_dHashMarkOffsetY * scale, x, fieldRange.getY() + (m_dHashMarkOffsetY + m_dHashMarkWidth) * scale);
			gc.strokeLine(x, fieldRange.getY() + range.getHeight() - m_dHashMarkOffsetY * scale, x, fieldRange.getY() + range.getHeight() - (m_dHashMarkOffsetY + m_dHashMarkWidth) * scale);
			x += 3 * scale;
		}

		x = fieldRange.getX() + m_dEndZoneDepth * scale + scale;
		double y = fieldRange.getY() + m_dFieldWidth * scale - m_dYardNumberHeight * scale - m_dYardNumberOffsetY * scale;
		double y2 = fieldRange.getY() + m_dYardNumberOffsetY * scale + m_dYardNumberHeight * scale;

		// draw the yard numbers
		Font font = new Font("Impact", 32);
		gc.setFont(font);
		gc.setFill(Color.WHITE);

		String[] yardNumbers = {"G", "G", "1", "0", "2", "0", "3", "0", "4", "0", "5", "0", "4", "0", "3", "0", "2", "0", "1", "0", "G", "G"};
		for(int i = 0; i < yardNumbers.length; i += 2)
		{
			if(i > 0)
			{
				x = fieldRange.getX() + m_dEndZoneDepth * scale + i * 15 * scale - scale;
				gc.setTextAlign(TextAlignment.RIGHT);
				gc.fillText(yardNumbers[i], x, y);

				x = fieldRange.getX() + m_dEndZoneDepth * scale + i * 15 * scale - scale;
				gc.setTextAlign(TextAlignment.LEFT);

				gc.save();
				gc.translate(x, y2);
				gc.rotate(180);
				gc.fillText(yardNumbers[i + 1], 0, 0);
				gc.restore();
			}

			if(i < (yardNumbers.length - 2))
			{
				x = fieldRange.getX() + m_dEndZoneDepth * scale + i * 15 * scale + scale;
				gc.setTextAlign(TextAlignment.LEFT);
				gc.fillText(yardNumbers[i + 1], x, y);

				x = fieldRange.getX() + m_dEndZoneDepth * scale + i * 15 * scale + scale;
				gc.setTextAlign(TextAlignment.RIGHT);

				gc.save();
				gc.translate(x, y2);
				gc.rotate(180);
				gc.fillText(yardNumbers[i], 0, 0);
				gc.restore();
			}
		}

		// draw the team names in the endzones.  Home on left, visitor on right
		font = new Font("Impact", 48);
		gc.setFont(font);
		if(JGame.data.teams[JGame.HOME] != null)
		{
			float width = getToolkit().getFontLoader().computeStringWidth(JGame.data.teams[JGame.HOME].name, gc.getFont());
			float xheight = getToolkit().getFontLoader().getFontMetrics(gc.getFont()).getXheight();

			x = fieldRange.getX() + m_dEndZoneDepth * scale / 2 + xheight / 2;
			y = fieldRange.getY() + m_dFieldWidth * scale / 2 + width / 2;
			gc.setFill(Color.WHITE);
			gc.save();
			gc.translate(x, y);
			gc.rotate(270);
			gc.fillText(JGame.data.teams[JGame.HOME].name, 0, 0);
			gc.restore();
		}

		if(JGame.data.teams[JGame.VISITOR] != null)
		{
			float width = getToolkit().getFontLoader().computeStringWidth(JGame.data.teams[JGame.VISITOR].name, gc.getFont());
			float xheight = getToolkit().getFontLoader().getFontMetrics(gc.getFont()).getXheight();

			x = fieldRange.getX() + m_dEndZoneDepth * scale + m_dFieldLength * scale + m_dEndZoneDepth * scale / 2 - xheight / 2;
			y = fieldRange.getY() + m_dFieldWidth * scale / 2 - width / 2;
			gc.setFill(Color.WHITE);
			gc.save();
			gc.translate(x, y);
			gc.rotate(90);
			gc.fillText(JGame.data.teams[JGame.VISITOR].name, 0, 0);
			gc.restore();
		}

		// update everything else
		drawResults(gc);
		drawDice(gc);
		drawLineOfScrimmage(gc);
		drawLineToMake(gc);
		drawDrives(gc);
		drawBall(gc);
	}

	private void drawResults(GraphicsContext gc)
	{

		double x1 = fieldRange.getX() + 10;
		double y1 = fieldRange.getY() + m_dFieldWidth*scale - 10 - JResult.height - 42;

		for(int i=0; i<JGame.resultStackDebug.size(); i++)
		{
			JGame.resultStackDebug.get(i).draw(gc, x1, y1);
			x1 += JResult.width + 10;
		}

		for(int i=0; i<JGame.resultStack.size(); i++)
		{
			JGame.resultStack.get(i).draw(gc, x1, y1);
			x1 += JResult.width + 10;
		}
	}

	private void drawDice(GraphicsContext gc)
	{

		double x1 = fieldRange.getX() + 10;
		double y1 = fieldRange.getY() + m_dFieldWidth*scale - 42;

		for(int i=0; i<JGame.diceStack.size(); i++)
		{
			gc.setStroke(Color.BLACK);
			gc.setFill(Color.WHITE);
			gc.fillRect(x1, y1, 32, 32);
			gc.strokeRect(x1, y1, 32, 32 );

			gc.setFill(Color.BLACK);
			gc.setFont(Font.font(16));
			gc.fillText(JGame.diceStack.get(i).toString(), x1+2, y1+20);
			x1 += 42;
		}
	}

	private void drawDrives(GraphicsContext gc)
	{
		double	y = fieldRange.getY() + m_dFieldWidth * scale / 2 - JDrive.Height();
		double	x = fieldRange.getX() + m_dEndZoneDepth * scale;

		int n = JGame.data.driveStack.size();
		for(int i=0; i<JGame.data.driveStack.size(); i++)
		{
			JGame.data.driveStack.get(i).draw(gc, x, y, scale);
			y -= JDrive.Height() + 2;
		}

		if(n>5)
		{
			JGame.data.driveStack.remove(n-1);
		}
	}

	static double ballsize = 5;
	private void drawBall(GraphicsContext gc)
	{
		double	y = fieldRange.getY() + m_dFieldWidth * scale / 2;
		double	x = fieldRange.getX() + m_dEndZoneDepth * scale;

		if(JGame.data.offense == JGame.HOME)
		{
			x = x + (100-JGame.data.yardline) * scale * 3;

			double[] xPoints = {x, x - ballsize * 2, x - ballsize * 2};
			double[] yPoints = {y, y - ballsize, y + ballsize};

			gc.setFill(Color.BROWN);
			gc.fillPolygon(xPoints, yPoints, 3);
		}
		else
		{
			x = x + (JGame.data.yardline) * scale * 3;

			double[] xPoints = {x, x + ballsize * 2, x + ballsize * 2};
			double[] yPoints = {y, y - ballsize, y + ballsize};

			gc.setFill(Color.BROWN);
			gc.fillPolygon(xPoints, yPoints, 3);
		}
	}

	private void drawLineOfScrimmage(GraphicsContext gc)
	{
		double	y1 = fieldRange.getY();
		double	y2 = fieldRange.getY() + m_dFieldWidth * scale;
		double	x = fieldRange.getX() + m_dEndZoneDepth * scale;

		if(JGame.data.offense == JGame.HOME)
			x = x + (100-JGame.data.yardline) * scale * 3;
		else
			x = x + (JGame.data.yardline) * scale * 3;

		gc.setStroke(Color.BLUE);
		gc.strokeLine(x, y1, x, y2);
	}

	private void drawLineToMake(GraphicsContext gc)
	{
		double	y1 = fieldRange.getY();
		double	y2 = fieldRange.getY() + m_dFieldWidth * scale;
		double	x = fieldRange.getX() + m_dEndZoneDepth * scale;

		if(JGame.data.offense == JGame.HOME)
			x = x + (100-(JGame.data.yardline-JGame.data.togo)) * scale * 3;
		else
			x = x + (JGame.data.yardline-JGame.data.togo) * scale * 3;

		gc.setStroke(Color.YELLOW);
		gc.strokeLine(x, y1, x, y2);
	}
}
