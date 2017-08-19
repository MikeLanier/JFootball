import javafx.event.EventHandler;
import javafx.geometry.Insets;
//import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JDialogStage extends Stage
{
	private VBox _box = new VBox();
	private Label _title = new Label("");
	private VBox _questions = new VBox();
	private Scene _scene = new Scene(_box);
	private Integer _numQuestions = 0;
	private Font font = new Font(16);

	private Integer _selectedQuestion = -1;

	public Integer answer()
	{
		return _selectedQuestion;
	}

	public JDialogStage()
	{
		_box.getChildren().add(_title);
		_box.getChildren().add(_questions);

		_numQuestions = 0;

		//initModality(Modality.WINDOW_MODAL);
		initModality(Modality.APPLICATION_MODAL);

		_title.setPadding(new Insets(10, 10, 10, 10));
		_title.setWrapText(true);
		_title.setFont(font);

		_questions.setPadding(new Insets(10, 10, 10, 10));
		_questions.setSpacing(10);

		_box.setBackground(new Background(new BackgroundFill(JGame.LIGHTGREEN, null, null)));
		_box.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));

		initStyle(StageStyle.UNDECORATED);
		setScene(_scene);
	}

	public void addTitle(String title)
	{
		_title.setText(title);
	}

	public void addQuestion(String question)
	{
		Button button = new Button(question);
		button.setUserData(_numQuestions);
		button.setFont(font);

		_questions.getChildren().add(button);

		button.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				Button b = (Button) event.getSource();
				_selectedQuestion = (Integer) b.getUserData();
				close();
			}
		});

		_numQuestions++;
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////
	// classes for alternate dialog
	public class JDialog extends Dialog<Integer>
	{
		private VBox _box = new VBox();
		private Label _title = new Label("");
		private VBox _questions = new VBox();
		private int _numQuestions = 0;
//		private Integer _selectedQuestion = -1;

		public JDialog()
		{
			_box.getChildren().add(_title);
			_box.getChildren().add(_questions);

			getDialogPane().setContent(_box);
		}

		public void addTitle(String title)
		{
			_title.setText(title);
		}

		public void addQuestion(String question)
		{
			Button button = new Button(question);
			Integer i = _numQuestions++;
			button.setUserData(i);

			button.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent event)
				{
					Button b = (Button) event.getSource();
//					Parent p = b.getParent();
					System.out.println(b.getText());
					_selectedQuestion = (Integer) b.getUserData();
				}
			});

			_questions.getChildren().add(button);
		}
	}

	public class JDialogStageJString extends Stage
	{
		private JString _questions = new JString();
		private Scene _scene = new Scene(_questions);
		private int _numQuestions = 0;

		public JDialogStageJString()
		{
			_numQuestions = 0;
			initModality(Modality.APPLICATION_MODAL);

			_questions.setPadding(new Insets(10, 10, 10, 10));
			setScene(_scene);

			addEventHandler(JPlayerOptionEvent.OPTION_1, new EventHandler<JPlayerOptionEvent>()
			{
				@Override
				public void handle(JPlayerOptionEvent event)
				{
					System.out.println("Option 1 selected");
					close();
				}
			});

			addEventHandler(JPlayerOptionEvent.OPTION_2, new EventHandler<JPlayerOptionEvent>()
			{
				@Override
				public void handle(JPlayerOptionEvent event)
				{
					System.out.println("Option 2 selected");
					close();
				}
			});

			addEventHandler(JPlayerOptionEvent.OPTION_3, new EventHandler<JPlayerOptionEvent>()
			{
				@Override
				public void handle(JPlayerOptionEvent event)
				{
					System.out.println("Option 3 selected");
					close();
				}
			});

			addEventHandler(JPlayerOptionEvent.OPTION_4, new EventHandler<JPlayerOptionEvent>()
			{
				@Override
				public void handle(JPlayerOptionEvent event)
				{
					System.out.println("Option 4 selected");
					close();
				}
			});

			addEventHandler(JPlayerOptionEvent.OPTION_4, new EventHandler<JPlayerOptionEvent>()
			{
				@Override
				public void handle(JPlayerOptionEvent event)
				{
					System.out.println("Option 5 selected");
					close();
				}
			});
		}

		public void addTitle(String title)
		{
			_questions.addSegment(title, Color.BLACK, false, null);
		}

		public void addQuestion(String question)
		{
			if(_numQuestions == 0) _questions.addSegment(question, Color.BLACK, false, JPlayerOptionEvent.OPTION_1);
			if(_numQuestions == 1) _questions.addSegment(question, Color.BLACK, false, JPlayerOptionEvent.OPTION_2);
			if(_numQuestions == 2) _questions.addSegment(question, Color.BLACK, false, JPlayerOptionEvent.OPTION_3);
			if(_numQuestions == 3) _questions.addSegment(question, Color.BLACK, false, JPlayerOptionEvent.OPTION_4);
			if(_numQuestions == 4) _questions.addSegment(question, Color.BLACK, false, JPlayerOptionEvent.OPTION_5);

			_numQuestions++;
		}
	}
}
