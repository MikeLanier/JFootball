//import javafx.event.Event;
import javafx.event.EventHandler;
//import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

//import javax.management.Notification;

public class JString extends TextFlow
{
	private class JText extends Text
	{
		private Color _color = Color.BLACK;

		private EventType<JPlayerOptionEvent> _optionEvent = null;
		public void optionEvent(EventType<JPlayerOptionEvent> optionEvent) { _optionEvent = optionEvent; }

		public JText(String str)
		{
			super(str);

			setOnMouseEntered(new EventHandler<MouseEvent>()
			{
				public void handle(MouseEvent mouseEvent)
				{
					if(_optionEvent != null)
					{
						JText txt = (JText) mouseEvent.getSource();
						if(txt != null)
						{
							txt.setFill(Color.GRAY);
							txt.setUnderline(true);
						}
					}
				}
			});

			setOnMouseExited(new EventHandler<MouseEvent>()
			{
				public void handle(MouseEvent mouseEvent)
				{
					if(_optionEvent != null)
					{
						JText txt = (JText) mouseEvent.getSource();
						if(txt != null)
						{
							txt.setFill(_color);
							txt.setUnderline(false);
						}
					}
				}
			});
		}

		public void setColor(Color color)
		{
			setFill(_color=color);
		}
	}

	Font font = new Font(16);
	@SuppressWarnings("unused")
	private Color _color;

	public JString()
	{
	}

	public void addSegment(String str, Color color, Boolean bold, EventType<JPlayerOptionEvent> optionEvent)
	{
		JText text1 = new JText(str);
		text1.setColor(_color=color);
		text1.optionEvent(optionEvent);
		text1.setFont(font);

		if(optionEvent!=null)
		{
			text1.setOnMousePressed(new EventHandler<MouseEvent>()
			{
				public void handle(MouseEvent mouseEvent)
				{
					JText txt = (JText)mouseEvent.getSource();
					System.out.println("X: " + mouseEvent.getX() + " Y: " + mouseEvent.getY());
					JPlayerOptionEvent save = new JPlayerOptionEvent(txt._optionEvent);
					fireEvent(save);
				}
			});
		}

		getChildren().add(text1);
	}
}
