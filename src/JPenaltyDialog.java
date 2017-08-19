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

import java.util.ArrayList;

public class JPenaltyDialog extends Stage
{
	private VBox _box = new VBox();
	private Label _title = new Label("");
	private VBox _questions = new VBox();
	private Scene _scene = new Scene(_box);
	private Integer _numQuestions = 0;
	private Font font = new Font(16);

//	private class JPenaltyButton extends Button
//	{
//		public JSpot	spot;
//	}

	// the question button contains the question string and the
	// spot at which to place the ball if accepted
	private class JPenaltyQuestionButton extends Button
	{
		JSpot spot = null;
		Boolean accept = false;

		public JPenaltyQuestionButton(String _question, JSpot _spot, Boolean _accept)
		{
			super(_question);
			spot = _spot;
			accept = _accept;
		}
	}

	public JPenaltyDialog(ArrayList<JSpot> penaltyStack, ArrayList<JSpot> spotStack)
	{
		// initialize the dialog
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

		// format the title and questions
		/*
		JLog.writeln("spotStack ---------------------------------");
		JLog.addindent();
		for(int i=0; i<spotStack.size(); i++)
		{
			spotStack.get(i).log();
		}
		JLog.subindent();
		*/

		//JLog.writeln("penaltyStack ---------------------------------");
		//JLog.addindent();

		int who = -1;			// who the penalty was on.  if equal 2, offsetting
		String title = "";		// title to display on the dialog
		ArrayList<JPenaltyQuestionButton> questions = new ArrayList<>();	// array of questions to ask

		int n = penaltyStack.size()-1;
		for(int i=0; i<=n; i++)
		{
			//penaltyStack.get(i).log();

			// format the title
			if(i==0)
				title = title + "These is ";
			else if(i==n)
				title = title + ", and ";
			else
				title = title + ", ";

			title = title + formatPenaltyTitle(penaltyStack.get(i));

			// format the question
			questions.add(new JPenaltyQuestionButton(formatPenaltyQuestion(penaltyStack.get(i)), penaltyStack.get(i), true));

			// determine if we have offsetting penalties. 'who' will contain the index of the team on which the
			// penalty was called.  If we have penalties on both, 'who' will be 2
			if(penaltyStack.get(i).result.DEF() || penaltyStack.get(i).result.DEFX() || penaltyStack.get(i).result.PI())
			{
				if(who == penaltyStack.get(i).dataBefore.offense)
					who = 2;
				else
					who = penaltyStack.get(i).dataBefore.defense;
			}
			else
			{
				if(who == penaltyStack.get(i).dataBefore.defense)
					who = 2;
				else
					who = penaltyStack.get(i).dataBefore.offense;
			}
		}
		//JLog.subindent();

		if(who == 2)
		{
			// if we have offsetting penalties, replay the down
			addTitle(title + ".\nThe penalties offset. Repeat " + JGame.formatDown(spotStack.get(0).dataBefore.down));
			addQuestion(new JPenaltyQuestionButton("OK", spotStack.get(0), false));
		}
		else
		{
			// add the title and accept questions
			addTitle(title);
			for(int i=0; i<questions.size(); i++)
				addQuestion(questions.get(i));

			// add the decline question
			addQuestion(new JPenaltyQuestionButton(formatPenaltyDecline(spotStack.get(spotStack.size()-1)),spotStack.get(spotStack.size()-1), false));
		}
	}

	private JSpot _selection = null;
	public JSpot returnSelection() { return _selection; }

	private Boolean _accept = null;
	public Boolean accepted() { return _accept; }

	private String formatPenaltyTitle(JSpot spot)
	{
		String title;
		title = "a " + spot.result.Yards() + " yard penalty on ";
		if(spot.result.DEF() || spot.result.DEFX() || spot.result.PI())
			title = title + spot.dataAfter.teams[spot.dataBefore.defense].name;
		else
			title = title + spot.dataAfter.teams[spot.dataBefore.offense].name;

		return title;
	}

	private String formatPenaltyQuestion(JSpot spot)
	{
		String question;
		question = "Move the ball ";
		if(spot.result.HALF_DISTANCE())
			question = question + "half the distance to the goal. ";
		else
		{
			question = question + spot.result.Yards() +
					" yards to the " +
					JGame.formatYardline(spot.dataAfter.yardline, spot.dataAfter.offense, spot.dataAfter.defense);
			question = question + "\n" + JGame.downAndDistance(spot.dataAfter);
		}

		return question;
	}

	private String formatPenaltyDecline(JSpot spot)
	{
		String decline;
		decline = "Decline the penalty.\n";
		decline = decline + JGame.downAndDistance(spot.dataAfter);

		return decline;
	}

	public void addTitle(String title)
	{
		_title.setText(title);
	}

	public void addQuestion(JPenaltyQuestionButton questionButton)
	{
		questionButton.setUserData(_numQuestions);
		questionButton.setFont(font);

		_questions.getChildren().add(questionButton);

		questionButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				JPenaltyQuestionButton b = (JPenaltyQuestionButton) event.getSource();
				_selection = b.spot;
				_accept = b.accept;
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
		@SuppressWarnings("unused")
		private int _selectedQuestion = -1;

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

	public class JPenaltyDialogJString extends Stage
	{
		private JString _questions = new JString();
		private Scene _scene = new Scene(_questions);
		private int _numQuestions = 0;

		public JPenaltyDialogJString()
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
