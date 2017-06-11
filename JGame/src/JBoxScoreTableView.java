import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class JBoxScoreTableView extends TableView
{
	public class Stuff
	{
		public SimpleStringProperty _title;
		public SimpleStringProperty  _subtitle;
		public IntegerProperty _hValue;
		public IntegerProperty _vValue;

		public Stuff( String title, String subtitle, IntegerProperty hValue, IntegerProperty vValue )
		{
			_title = new SimpleStringProperty(title);
			_subtitle = new SimpleStringProperty(subtitle);
			_hValue = hValue;
			_vValue = vValue;

			//_title.addListener((e) -> System.out.println("_title change to " + _title.get()));
			//_subtitle.addListener((e) -> System.out.println("_title change to " + _subtitle.get()));
			//_hValue.addListener((e) -> System.out.println("_title change to " + _hValue.get()));
			//_vValue.addListener((e) -> System.out.println("_title change to " + _vValue.get()));
		}

		public StringProperty _titleProperty() { return _title; }
		public StringProperty _subtitleProperty() { return _subtitle; }
		public IntegerProperty _hValueProperty() { return _hValue; }
		public IntegerProperty _vValueProperty() { return _vValue; }
	}

	private TableColumn<Stuff, String> col1 = new TableColumn<>("");
	private TableColumn<Stuff, String> col2 = new TableColumn<>("");
	private TableColumn<Stuff, String> col3 = new TableColumn<>("HOME");
	private TableColumn<Stuff, String> col4 = new TableColumn<>("VIS");

	public JBoxScoreTableView()
	{
		ObservableList<Stuff> data = FXCollections.observableArrayList
				(
						new Stuff("Score", "", JGame.data.stats[JGame.HOME].points, JGame.data.stats[JGame.VISITOR].points),

						new Stuff("First Downs", "", JGame.data.stats[JGame.HOME].first_downs, JGame.data.stats[JGame.VISITOR].first_downs),

						new Stuff("Total", "Yards", JGame.data.stats[JGame.HOME].total_yards, JGame.data.stats[JGame.VISITOR].total_yards),
						new Stuff("", "Plays", JGame.data.stats[JGame.HOME].total_plays, JGame.data.stats[JGame.VISITOR].total_plays),
						new Stuff("", "TDs", JGame.data.stats[JGame.HOME].total_touchdowns, JGame.data.stats[JGame.VISITOR].total_touchdowns),
						new Stuff("", "Safeties", JGame.data.stats[JGame.HOME].total_safeties, JGame.data.stats[JGame.VISITOR].total_safeties),
						new Stuff("", "Fumbles", JGame.data.stats[JGame.HOME].total_fumbles, JGame.data.stats[JGame.VISITOR].total_fumbles),
						new Stuff("", "F.. Lost", JGame.data.stats[JGame.HOME].total_fumbles_lost, JGame.data.stats[JGame.VISITOR].total_fumbles_lost),

						new Stuff("Rush", "Yards", JGame.data.stats[JGame.HOME].rush_yards, JGame.data.stats[JGame.VISITOR].rush_yards),
						new Stuff("", "Carries", JGame.data.stats[JGame.HOME].rush_carries, JGame.data.stats[JGame.VISITOR].rush_carries),
						new Stuff("", "TDs", JGame.data.stats[JGame.HOME].rush_touchdowns, JGame.data.stats[JGame.VISITOR].rush_touchdowns),
						//new Stuff("", "Fumbles", JGame.data.stats[JGame.HOME].rush_fumbles, JGame.data.stats[JGame.VISITOR].rush_fumbles),
						//new Stuff("", "F.. Lost", JGame.data.stats[JGame.HOME].rush_fumbles_lost, JGame.data.stats[JGame.VISITOR].rush_fumbles_lost),

						new Stuff("Pass", "Yards", JGame.data.stats[JGame.HOME].pass_yards, JGame.data.stats[JGame.VISITOR].pass_yards),
						new Stuff("", "Attempts", JGame.data.stats[JGame.HOME].pass_attempts, JGame.data.stats[JGame.VISITOR].pass_attempts),
						new Stuff("", "Completions", JGame.data.stats[JGame.HOME].pass_completions, JGame.data.stats[JGame.VISITOR].pass_completions),
						new Stuff("", "TDs", JGame.data.stats[JGame.HOME].pass_touchdowns, JGame.data.stats[JGame.VISITOR].pass_touchdowns),
						//new Stuff("", "Fumbles", JGame.data.stats[JGame.HOME].pass_fumbles, JGame.data.stats[JGame.VISITOR].pass_fumbles),
						//new Stuff("", "F.. Lost", JGame.data.stats[JGame.HOME].pass_fumbles_lost, JGame.data.stats[JGame.VISITOR].pass_fumbles_lost),
						new Stuff("", "INT", JGame.data.stats[JGame.HOME].pass_interceptions, JGame.data.stats[JGame.VISITOR].pass_interceptions),
						new Stuff("", "Sacks", JGame.data.stats[JGame.HOME].pass_sacks, JGame.data.stats[JGame.VISITOR].pass_sacks),

						new Stuff("INT Returns", "", JGame.data.stats[JGame.HOME].interception_returns, JGame.data.stats[JGame.VISITOR].interception_returns),
						new Stuff("", "Yards", JGame.data.stats[JGame.HOME].interception_return_yards, JGame.data.stats[JGame.VISITOR].interception_return_yards),
						new Stuff("", "TDs", JGame.data.stats[JGame.HOME].interception_return_touchdowns, JGame.data.stats[JGame.VISITOR].interception_return_touchdowns),
						//new Stuff("", "Fumbles", JGame.data.stats[JGame.HOME].interception_return_fumbles, JGame.data.stats[JGame.VISITOR].interception_return_fumbles),
						//new Stuff("", "F.. Lost", JGame.data.stats[JGame.HOME].interception_return_fumbles_lost, JGame.data.stats[JGame.VISITOR].interception_return_fumbles_lost),

						new Stuff("Punts", "", JGame.data.stats[JGame.HOME].punts, JGame.data.stats[JGame.VISITOR].punts),
						new Stuff("", "Yards", JGame.data.stats[JGame.HOME].punt_yards, JGame.data.stats[JGame.VISITOR].punt_yards),
						new Stuff("", "Returned", JGame.data.stats[JGame.HOME].punts_returned, JGame.data.stats[JGame.VISITOR].punts_returned),
						new Stuff("", "Blocked", JGame.data.stats[JGame.HOME].punts_blocked, JGame.data.stats[JGame.VISITOR].punts_blocked),

						new Stuff("Punt Returns", "", JGame.data.stats[JGame.HOME].punt_returns, JGame.data.stats[JGame.VISITOR].punt_returns),
						new Stuff("", "Yards", JGame.data.stats[JGame.HOME].punt_return_yards, JGame.data.stats[JGame.VISITOR].punt_return_yards),
						new Stuff("", "TDs", JGame.data.stats[JGame.HOME].punt_return_touchdowns, JGame.data.stats[JGame.VISITOR].punt_return_touchdowns),
						//new Stuff("", "Fumbles", JGame.data.stats[JGame.HOME].punt_return_fumbles, JGame.data.stats[JGame.VISITOR].punt_return_fumbles),
						//new Stuff("", "F.. Lost", JGame.data.stats[JGame.HOME].punt_return_fumbles_lost, JGame.data.stats[JGame.VISITOR].punt_return_fumbles_lost),

						//new Stuff("Kickoffs", "", JGame.data.stats[JGame.HOME].kickoffs, JGame.data.stats[JGame.VISITOR].kickoffs),
						//new Stuff("", "Yards", JGame.data.stats[JGame.HOME].kickoff_yards, JGame.data.stats[JGame.VISITOR].kickoff_yards),
						//new Stuff("", "Returned", JGame.data.stats[JGame.HOME].kickoffs_returned, JGame.data.stats[JGame.VISITOR].kickoffs_returned),

						new Stuff("Kickoff Returns", "", JGame.data.stats[JGame.HOME].kickoff_returns, JGame.data.stats[JGame.VISITOR].kickoff_returns),
						new Stuff("", "Yards", JGame.data.stats[JGame.HOME].kickoff_return_yards, JGame.data.stats[JGame.VISITOR].kickoff_return_yards),
						new Stuff("", "TDs", JGame.data.stats[JGame.HOME].kickoff_return_touchdowns, JGame.data.stats[JGame.VISITOR].kickoff_return_touchdowns),
						//new Stuff("", "Fumbles", JGame.data.stats[JGame.HOME].kickoff_return_fumbles, JGame.data.stats[JGame.VISITOR].kickoff_return_fumbles),
						//new Stuff("", "F.. Lost", JGame.data.stats[JGame.HOME].kickoff_return_fumbles_lost, JGame.data.stats[JGame.VISITOR].kickoff_return_fumbles_lost),

						new Stuff("Fieldgoals", "Attempts", JGame.data.stats[JGame.HOME].fieldgoal_attempts, JGame.data.stats[JGame.VISITOR].fieldgoal_attempts),
						new Stuff("", "Made", JGame.data.stats[JGame.HOME].fieldgoals_made, JGame.data.stats[JGame.VISITOR].fieldgoals_made),
						new Stuff("", "Blocked", JGame.data.stats[JGame.HOME].fieldgoals_blocked, JGame.data.stats[JGame.VISITOR].fieldgoals_blocked),

						new Stuff("Extrapoints", "Attempts", JGame.data.stats[JGame.HOME].extrapoint_attempts, JGame.data.stats[JGame.VISITOR].extrapoint_attempts),
						new Stuff("", "Made", JGame.data.stats[JGame.HOME].extrapoints_made, JGame.data.stats[JGame.VISITOR].extrapoints_made),
						new Stuff("", "Blocked", JGame.data.stats[JGame.HOME].extrapoints_blocked, JGame.data.stats[JGame.VISITOR].extrapoints_blocked),

						new Stuff("Penalties", "", JGame.data.stats[JGame.HOME].penalties, JGame.data.stats[JGame.VISITOR].penalties),
						new Stuff("", "Yards", JGame.data.stats[JGame.HOME].penalty_yards, JGame.data.stats[JGame.VISITOR].penalty_yards)
				);

		setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
		setBackground(new Background(new BackgroundFill(JGame.LIGHTGREEN, new CornerRadii(5), null)));
		setPadding(new Insets(5,5,5,5));

		itemsProperty().setValue(data);

		col1.setCellValueFactory(new PropertyValueFactory<Stuff, String>("_title"));
		col2.setCellValueFactory(new PropertyValueFactory<Stuff, String>("_subtitle"));
		col3.setCellValueFactory(new PropertyValueFactory<Stuff, String>("_hValue"));
		col4.setCellValueFactory(new PropertyValueFactory<Stuff, String>("_vValue"));

		col3.setStyle( "-fx-alignment: CENTER;");
		col4.setStyle( "-fx-alignment: CENTER;");

		getColumns().addAll(col1, col2, col3, col4);

		widthProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth)
			{
				double width = newSceneWidth.doubleValue() - 10 - 25;
				col1.setPrefWidth(width * .3);
				col2.setPrefWidth(width * .3);
				col3.setPrefWidth(width * .2);
				col4.setPrefWidth(width * .2);
				update();
			}
		});
	}

	public void update()
	{
		col3.setText(JGame.data.teams[JGame.HOME].abrv);
		col4.setText(JGame.data.teams[JGame.VISITOR].abrv);
	}
}
