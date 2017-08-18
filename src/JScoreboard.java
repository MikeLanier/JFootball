import javafx.event.EventHandler;
//import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class JScoreboard extends JPanel
{
	// first and last column is padding so as to center the scoreboard
	private double[] percentWidth = {8, 20, 4, 4, 4, 4, 4, 40, 8, 4, 8};
	private double[] percentHeight = {30,35,35};

	// the labels for the scoreboard
	private Label nameTitle = new Label("");
	private Label nameHome = new Label("Northwestern State");
	private Label nameVisitor = new Label("Visitor");

	private Label scoreTitle = new Label("");
	private Label scoreHome = new Label("0");
	private Label scoreVisitor = new Label("0");

	private Label scoreQ1Title = new Label("1");
	private Label scoreQ1Home = new Label("0");
	private Label scoreQ1Visitor = new Label("0");

	private Label scoreQ2Title = new Label("2");
	private Label scoreQ2Home = new Label("0");
	private Label scoreQ2Visitor = new Label("0");

	private Label scoreQ3Title = new Label("3");
	private Label scoreQ3Home = new Label("0");
	private Label scoreQ3Visitor = new Label("0");

	private Label scoreQ4Title = new Label("4");
	private Label scoreQ4Home = new Label("0");
	private Label scoreQ4Visitor = new Label("0");

	private	Label[] scoreLabelHome = {scoreHome, scoreQ1Home, scoreQ2Home, scoreQ3Home, scoreQ4Home};
	private	Label[] scoreLabelVisitor = {scoreVisitor, scoreQ1Visitor, scoreQ2Visitor, scoreQ3Visitor, scoreQ4Visitor};

	private Label downTitle = new Label("Down and Distance");
	private Label down = new Label("1st and 10 at the home 40");

	private Label timeTitle = new Label("Time");
	private Label time = new Label("15:00");

	private Label timeoutsTitle = new Label("TO");
	private Label timeoutsHome = new Label("3");
	private Label timeoutsVisitor = new Label("3");

	private int pad = 5;
	private int[] colIndex = {1,2,3,4,5,6,7,8,9,10,11};		// which column a label goes in

	private Background whiteBackground = new Background(new BackgroundFill(Color.WHITE, null, null));
	private Background yellowBackground = new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null));

	public JScoreboard(Color color)
	{
		super(color);

		nameHome.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(event.isShiftDown())
				{
					JTeamCard card = new JTeamCard(JGame.data.teams[JGame.HOME], true);
					card.showAndWait();
				}
				else
				{
					JTeamTree tree = new JTeamTree();
					Point2D point = nameHome.localToScreen(0.0, 0.0);
					tree.setX(point.getX() - pad);
					tree.setY(point.getY() + nameHome.getHeight());
					tree.showAndWait();

					System.out.println("["+tree.selectedTeamFilename()+"]");
					JTeam team = new JTeam(tree.selectedTeamFilename());
					JGame.data.teams[JGame.HOME] = team;
					JGame.scoreboard.update();
					JGame.boxScore.update();
					JGame.field.draw();
				}
			}
		});

		nameVisitor.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(event.isShiftDown())
				{
					JTeamCard card = new JTeamCard(JGame.data.teams[JGame.VISITOR], true);
					card.showAndWait();
				}
				else
				{
					JTeamTree tree = new JTeamTree();
					Point2D point = nameVisitor.localToScreen(0.0, 0.0);
					tree.setX(point.getX() - pad);
					tree.setY(point.getY() + nameVisitor.getHeight());
					tree.showAndWait();

					System.out.println("["+tree.selectedTeamFilename()+"]");
					JTeam team = new JTeam(tree.selectedTeamFilename());
					JGame.data.teams[JGame.VISITOR] = team;
					JGame.scoreboard.update();
					JGame.boxScore.update();
					JGame.field.draw();
				}
			}
		});

		setBackground(new Background(new BackgroundFill(JGame.LIGHTGREEN, new CornerRadii(5), null)));

//		double width;
//		double height;

		// define the grid for the scoreboard labels
		// set the padding around the cells of the grid
		setPadding(new Insets(0,pad,pad*2,pad));
		//setGridLinesVisible(true);

		// constraints for fixed size grid cells
		ColumnConstraints[] col = new ColumnConstraints[percentWidth.length];

		for(int i=0; i<percentWidth.length; i++)
		{
			col[i] = new ColumnConstraints();
			col[i].setPercentWidth(percentWidth[i]);
		}
		getColumnConstraints().addAll(col);

		RowConstraints[] row = new RowConstraints[percentHeight.length];

		for(int i=0; i<percentHeight.length; i++)
		{
			row[i] = new RowConstraints();	row[i].setPercentHeight(percentHeight[i]);
		}
		getRowConstraints().addAll(row);

		// draw a border around the grid, set the initial size of the grid
		setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));

		// font for the text in the labels
		Font font = new Font(15);

		// labels for the team names
		nameTitle.setFont(font);
		nameHome.setFont(font);
		nameVisitor.setFont(font);

		Border border1111 = new Border(JGame.createBorderStroke(Color.BLACK, true, true, true, true, new BorderWidths(1)));
		Border border0111 = new Border(JGame.createBorderStroke(Color.BLACK, false, true, true, true, new BorderWidths(1)));
		Border border1011 = new Border(JGame.createBorderStroke(Color.BLACK, true, false, true, true, new BorderWidths(1)));
		Border border0011 = new Border(JGame.createBorderStroke(Color.BLACK, false, false, true, true, new BorderWidths(1)));

		nameHome.setBorder(border1011);
		nameVisitor.setBorder(border0011);

		nameTitle.setPadding(new Insets(1,1,1,10));
		nameHome.setPadding(new Insets(1,1,1,10));
		nameVisitor.setPadding(new Insets(1,1,1,10));

		nameHome.setBackground(yellowBackground);
		nameVisitor.setBackground(whiteBackground);

		add(nameTitle, colIndex[0], 0);
		add(nameHome, colIndex[0], 1);
		add(nameVisitor, colIndex[0], 2);

		// labels for the score
		scoreTitle.setFont(font);
		scoreHome.setFont(font);
		scoreVisitor.setFont(font);

		scoreTitle.setAlignment(Pos.CENTER);
		scoreHome.setAlignment(Pos.CENTER);
		scoreVisitor.setAlignment(Pos.CENTER);

		scoreHome.setBorder(border1011);
		scoreVisitor.setBorder(border0011);

		scoreHome.setBackground(whiteBackground);
		scoreVisitor.setBackground(whiteBackground);

		add(scoreTitle, colIndex[1], 0);
		add(scoreHome, colIndex[1], 1);
		add(scoreVisitor, colIndex[1], 2);

		// labels for 1st quarter score
		scoreQ1Title.setFont(font);
		scoreQ1Home.setFont(font);
		scoreQ1Visitor.setFont(font);

		scoreQ1Title.setAlignment(Pos.CENTER);
		scoreQ1Home.setAlignment(Pos.CENTER);
		scoreQ1Visitor.setAlignment(Pos.CENTER);

		scoreQ1Home.setBorder(border1011);
		scoreQ1Visitor.setBorder(border0011);

		scoreQ1Home.setBackground(whiteBackground);
		scoreQ1Visitor.setBackground(whiteBackground);

		add(scoreQ1Title, colIndex[2], 0);
		add(scoreQ1Home, colIndex[2], 1);
		add(scoreQ1Visitor, colIndex[2], 2);

		// labels for 2nd quarter score
		scoreQ2Title.setFont(font);
		scoreQ2Home.setFont(font);
		scoreQ2Visitor.setFont(font);

		scoreQ2Title.setAlignment(Pos.CENTER);
		scoreQ2Home.setAlignment(Pos.CENTER);
		scoreQ2Visitor.setAlignment(Pos.CENTER);

		scoreQ2Home.setBorder(border1011);
		scoreQ2Visitor.setBorder(border0011);

		scoreQ2Home.setBackground(whiteBackground);
		scoreQ2Visitor.setBackground(whiteBackground);

		add(scoreQ2Title, colIndex[3], 0);
		add(scoreQ2Home, colIndex[3], 1);
		add(scoreQ2Visitor, colIndex[3], 2);

		// labels for 3rd quarter score
		scoreQ3Title.setFont(font);
		scoreQ3Home.setFont(font);
		scoreQ3Visitor.setFont(font);

		scoreQ3Title.setAlignment(Pos.CENTER);
		scoreQ3Home.setAlignment(Pos.CENTER);
		scoreQ3Visitor.setAlignment(Pos.CENTER);

		scoreQ3Home.setBorder(border1011);
		scoreQ3Visitor.setBorder(border0011);

		scoreQ3Home.setBackground(whiteBackground);
		scoreQ3Visitor.setBackground(whiteBackground);

		add(scoreQ3Title, colIndex[4], 0);
		add(scoreQ3Home, colIndex[4], 1);
		add(scoreQ3Visitor, colIndex[4], 2);

		// labels for 4th quarter score
		scoreQ4Title.setFont(font);
		scoreQ4Home.setFont(font);
		scoreQ4Visitor.setFont(font);

		scoreQ4Title.setAlignment(Pos.CENTER);
		scoreQ4Home.setAlignment(Pos.CENTER);
		scoreQ4Visitor.setAlignment(Pos.CENTER);

		scoreQ4Home.setBorder(border1011);
		scoreQ4Visitor.setBorder(border0011);

		scoreQ4Home.setBackground(whiteBackground);

		scoreQ4Visitor.setBackground(whiteBackground);

		add(scoreQ4Title, colIndex[5], 0);
		add(scoreQ4Home, colIndex[5], 1);
		add(scoreQ4Visitor, colIndex[5], 2);

		// labels for down and distance
		down.setWrapText(true);

		downTitle.setFont(font);
		down.setFont(font);

		downTitle.setAlignment(Pos.CENTER);
		down.setAlignment(Pos.CENTER);

		down.setBorder(border1011);

		down.setBackground(whiteBackground);

		add(downTitle, colIndex[6], 0);
		add(down, colIndex[6], 1, 1, 2);

		// labels for the time
		timeTitle.setFont(font);
		time.setFont(font);

		timeTitle.setAlignment(Pos.CENTER);
		time.setAlignment(Pos.CENTER);

		time.setBorder(border1011);
		time.setBackground(whiteBackground);

		add(timeTitle, colIndex[7], 0);
		add(time, colIndex[7], 1, 1, 2);

		// labels for timeouts
		timeoutsTitle.setFont(font);
		timeoutsHome.setFont(font);
		timeoutsVisitor.setFont(font);

		timeoutsTitle.setAlignment(Pos.CENTER);
		timeoutsHome.setAlignment(Pos.CENTER);
		timeoutsVisitor.setAlignment(Pos.CENTER);

		timeoutsHome.setBorder(border1111);
		timeoutsVisitor.setBorder(border0111);

		timeoutsHome.setBackground(whiteBackground);
		timeoutsVisitor.setBackground(whiteBackground);

		add(timeoutsTitle, colIndex[8], 0);
		add(timeoutsHome, colIndex[8], 1);
		add(timeoutsVisitor, colIndex[8], 2);
	}

	public void SetSize(double width, double height)
	{
		// adjust the size of the labels based on the new height and width of the panel.  Need
		// to do this so the label text will position correctly.
		nameTitle.setPrefSize(width * percentWidth[colIndex[0]] / 100, height * percentHeight[0] / 100);
		nameHome.setPrefSize(width * percentWidth[colIndex[0]] / 100, height * percentHeight[1] / 100);
		nameVisitor.setPrefSize(width * percentWidth[colIndex[0]] / 100, height * percentHeight[2] / 100);

		scoreTitle.setPrefSize(width * percentWidth[colIndex[1]] / 100, height * percentHeight[0] / 100);
		scoreHome.setPrefSize(width * percentWidth[colIndex[1]] / 100, height * percentHeight[1] / 100);
		scoreVisitor.setPrefSize(width * percentWidth[colIndex[1]] / 100, height * percentHeight[2] / 100);

		scoreQ1Title.setPrefSize(width * percentWidth[colIndex[2]] / 100, height * percentHeight[0] / 100);
		scoreQ1Home.setPrefSize(width * percentWidth[colIndex[2]] / 100, height * percentHeight[1] / 100);
		scoreQ1Visitor.setPrefSize(width * percentWidth[colIndex[2]] / 100, height * percentHeight[2] / 100);

		scoreQ2Title.setPrefSize(width * percentWidth[colIndex[3]] / 100, height * percentHeight[0] / 100);
		scoreQ2Home.setPrefSize(width * percentWidth[colIndex[3]] / 100, height * percentHeight[1] / 100);
		scoreQ2Visitor.setPrefSize(width * percentWidth[colIndex[3]] / 100, height * percentHeight[2] / 100);

		scoreQ3Title.setPrefSize(width * percentWidth[colIndex[4]] / 100, height * percentHeight[0] / 100);
		scoreQ3Home.setPrefSize(width * percentWidth[colIndex[4]] / 100, height * percentHeight[1] / 100);
		scoreQ3Visitor.setPrefSize(width * percentWidth[colIndex[4]] / 100, height * percentHeight[2] / 100);

		scoreQ4Title.setPrefSize(width * percentWidth[colIndex[5]] / 100, height * percentHeight[0] / 100);
		scoreQ4Home.setPrefSize(width * percentWidth[colIndex[5]] / 100, height * percentHeight[1] / 100);
		scoreQ4Visitor.setPrefSize(width * percentWidth[colIndex[5]] / 100, height * percentHeight[2] / 100);

		downTitle.setPrefSize(width * percentWidth[colIndex[6]] / 100, height * percentHeight[0] / 100);
		down.setPrefSize(width * percentWidth[colIndex[6]] / 100, height * (percentHeight[1]+percentHeight[2]) / 100);

		timeTitle.setPrefSize(width * percentWidth[colIndex[7]] / 100, height * percentHeight[0] / 100);
		time.setPrefSize(width * percentWidth[colIndex[7]] / 100, height * (percentHeight[1]+percentHeight[2]) / 100);

		timeoutsTitle.setPrefSize(width * percentWidth[colIndex[8]] / 100, height * percentHeight[0] / 100);
		timeoutsHome.setPrefSize(width * percentWidth[colIndex[8]] / 100, height * percentHeight[1] / 100);
		timeoutsVisitor.setPrefSize(width * percentWidth[colIndex[8]] / 100, height * percentHeight[2] / 100);

		update();
	}

	public void update()
	{
		updateTeams();
		updateScore();
		updateDownAndDistance();
		updateTime();
		updateTimeouts();
	}

	public void updateTeams()
	{
		if(JGame.data.teams[JGame.VISITOR] != null)
		{
			nameVisitor.setText(JGame.data.teams[JGame.VISITOR].name);
		}

		if(JGame.data.teams[JGame.HOME] != null)
		{
			nameHome.setText(JGame.data.teams[JGame.HOME].name);
		}

		if(JGame.data.offense == JGame.HOME)
		{
			nameHome.setBackground(yellowBackground);
			nameVisitor.setBackground(whiteBackground);
		}
		else
		{
			nameHome.setBackground(whiteBackground);
			nameVisitor.setBackground(yellowBackground);
		}
	}

	public void updateScore()
	{
		int sHome = 0;
		int sVisitor = 0;
		Number n = 0;

		for(int i = 1; i <= 4; i++)
		{
			if(i <= JGame.data.quarter)
			{
				n = JGame.data.score[JGame.HOME][i];	scoreLabelHome[i].setText(n.toString());	sHome += n.intValue();
				n = JGame.data.score[JGame.VISITOR][i];	scoreLabelVisitor[i].setText(n.toString());	sVisitor += n.intValue();
			}
			else
			{
				scoreLabelHome[i].setText("");
				scoreLabelVisitor[i].setText("");
			}
		}

		n = sHome;		scoreLabelHome[0].setText(n.toString());
		n = sVisitor;	scoreLabelVisitor[0].setText(n.toString());
	}

	public void updateDownAndDistance()
	{
		String d = "1st";
		if(JGame.data.down == 2)	d = "2nd";	else
		if(JGame.data.down == 3)	d = "3rd";	else
		if(JGame.data.down == 4)	d = "4th";

		Number n = JGame.data.togo;
		String tg = n.toString();
		if(JGame.data.togo >= JGame.data.yardline)	tg = "goal";

		String ay = "";

		if(JGame.data.yardline > 50 )
		{
			n = 100-JGame.data.yardline;
			ay = "the " + JGame.data.teams[JGame.data.offense].name + " " + n.toString() + " yard line.";
		}

		else if(JGame.data.yardline < 50 )
		{
			n = JGame.data.yardline;
			ay = "the " + JGame.data.teams[JGame.data.defense].name + " " + n.toString() + " yard line.";
		}

		else
		{
			ay = " midfield";
		}

		down.setText(d + " and " + tg + " at " + ay);
	}

	public void updateTime()
	{
		int itime = JGame.data.time;
		int min = itime/60;
		int sec = itime-min*60;
		Number nbrMin = min;
		Number nbrSec = sec;
		String tm = "";
		if(sec<10)
			tm = nbrMin.toString() + ":0" + nbrSec.toString();
		else
			tm = nbrMin.toString() + ":" + nbrSec.toString();

		time.setText(tm);
	}

	public void updateTimeouts()
	{
		Number n = JGame.data.timeouts[JGame.HOME];	timeoutsHome.setText(n.toString());
		n = JGame.data.timeouts[JGame.VISITOR];	timeoutsVisitor.setText(n.toString());
	}
}
