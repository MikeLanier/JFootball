import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

// 0, "", ""
// 1, "Score", ""
// 2, "First Downs", ""
// 3, "Rush", "Yards"
// 4, "", "Att"
// 5, "", "TD"
// 6, "Pass", "Yards"
// 7, "", "Att"
// 8, "", "Complete"
// 9, "", "TD"
// 10, "", "Int"
// 11, "Total", "Yards"
// 12, "", "Plays"
// 13, "Fumb/Lost", ""
// 14, "Punt", "Yards"
// 15, "", "Att"
// 16, "", "BK"
// 17, "Punt", "Yards"
// 18, "Return", "Att"
// 19, "", "TD"
// 20, "Kickoff", "Yards"
// 21, "Return", "Att"
// 22, "", "TD"
// 23, "Int", "Yards"
// 24, "Return", "Att"
// 25, "", "TD"
// 26, "FG", "Att"
// 27, "", "Made"
// 28, "", "Length"
// 29, "Penalty", "Yards"
// 30, "", "Num"

// if I make this a subclass of FlowPane, then create a GridPane internally
// stuff seems to resize better.
public class JStatsTreeTable extends JPanel
{
	int pad = 5;

	double[] percentWidth = {30, 30, 20, 20};
	double[] percentHeight = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};

	String[][] labelText =
	{
			{ "",			"",			"HOME","VIS"},
			{ "Score",		"",			"0",	"0"},
			{ "First Downs","",			"0",	"0"},
			{ "Rush",		"Yards",	"0",	"0"},
			{ "",			"Att",		"0",	"0"},
			{ "",			"TD",		"0",	"0"},
			{ "Pass",		"Yards",	"0",	"0"},
			{ "",			"Att",		"0",	"0"},
			{ "",			"Complete",	"0",	"0"},
			{ "",			"TD",		"0",	"0"},
			{ "",			"Int",		"0",	"0"},
			{ "Total",		"Yards",	"0",	"0"},
			{ "",			"Plays",	"0",	"0"},
			{ "Fumb/Lost",	"",			"0",	"0"},
			{ "Punt",		"Yards",	"0",	"0"},
			{ "",			"Att",		"0",	"0"},
			{ "",			"BK",		"0",	"0"},
			{ "Punt",		"Yards",	"0",	"0"},
			{ "Return",		"Att",		"0",	"0"},
			{ "",			"TD",		"0",	"0"},
			{ "Kickoff",	"Yards",	"0",	"0"},
			{ "Return",		"Att",		"0",	"0"},
			{ "",			"TD",		"0",	"0"},
			{ "Int",		"Yards",	"0",	"0"},
			{ "Return",		"Att",		"0",	"0"},
			{ "",			"TD",		"0",	"0"},
			{ "FG",			"Att",		"0",	"0"},
			{ "",			"Made",		"0",	"0"},
			{ "",			"Length",	"0",	"0"},
			{ "Penalty",	"Yards",	"0",	"0"},
			{ "",			"Num",		"0",	"0"},
	};

	Label[][] labels = new Label[percentWidth.length][percentHeight.length];

	public JStatsTreeTable(Color color)
	{
		super(color);

		setBackground(new Background(new BackgroundFill(JGame.LIGHTGREEN, new CornerRadii(5), null)));

		Background background = new Background(new BackgroundFill(Color.WHITE, null, null));

		setPadding(new Insets(pad,pad,pad,pad));

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
		Font font = new Font(12);

		Border border0010 = new Border(JGame.createBorderStroke(Color.BLACK, false, false, true, false, new BorderWidths(1)));
		Border border0110 = new Border(JGame.createBorderStroke(Color.BLACK, false, true, true, false, new BorderWidths(1)));
		Border border0111 = new Border(JGame.createBorderStroke(Color.BLACK, false, true, true, true, new BorderWidths(1)));

		for(int i=0; i<percentWidth.length; i++)
		{
			for(int j=0; j<percentHeight.length; j++)
			{
				Label label = new Label(labelText[j][i]);

				if(j==0)
					label.setBorder(border0010);
				else
				{
					label.setBackground(background);
					if(i == 0)
						label.setBorder(border0111);
					else
						label.setBorder(border0110);
				}

				label.setFont(font);
				label.setPadding(new Insets(0,5,0,5));

				if(i>1)
				{
					label.setAlignment(Pos.CENTER);
				}

				labels[i][j] = label;
				add(label,i,j);
			}
		}
	}

	public void SetSize(double width, double height)
	{
		if(JGame.data.teams[JGame.HOME] != null)
		{
			labels[2][0].setText(JGame.data.teams[JGame.HOME].abrv);
		}

		if(JGame.data.teams[JGame.VISITOR] != null)
		{
			labels[3][0].setText(JGame.data.teams[JGame.VISITOR].abrv);
		}

		width = width - pad - pad;
		height = height - pad - pad;

		for(int i=0; i<percentWidth.length; i++)
		{
			for(int j=0; j<percentHeight.length; j++)
			{
				labels[i][j].setPrefSize(width * percentWidth[i] / 100, height * percentHeight[j] / 100);
			}
		}
	}
}
