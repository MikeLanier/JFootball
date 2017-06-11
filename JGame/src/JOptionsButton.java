import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class JOptionsButton extends Button
{
	private Background normalBackground = new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), null));
	private Background hoverBackground = new Background(new BackgroundFill(Color.YELLOW, new CornerRadii(5), null));
	private Background selectBackground = new Background(new BackgroundFill(Color.ORANGE, new CornerRadii(5), null));

	private Boolean _mouseOver = false;

	private JGame.Plays	_play = JGame.Plays.Undefined;
	public JGame.Plays Play() { return _play; }

	private Boolean	_selected = false;
	public Boolean Selected() { return _selected; }
	public void Select()
	{
		if(_selected)
		{
			if(_mouseOver)
				setBackground(hoverBackground);
			else
				setBackground(normalBackground);

			_selected = false;
		}
		else
		{
			setBackground(selectBackground);
			_selected = true;
		}
	}

	public JOptionsButton(String title, JGame.Plays play)
	{
		super(title);

		_play = play;

		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(0,5,0,5));
		setBackground(normalBackground);
		setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));

		addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent e)
			{
				_mouseOver = true;

				if(!_selected)
					setBackground(hoverBackground);
			}
		});

		addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent e)
			{
				_mouseOver = false;

				if(!_selected)
					setBackground(normalBackground);
			}
		});

		/*
		addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent e)
			{
				if(_selected)
				{
					setBackground(hoverBackground);
					_selected = false;
				}
				else
				{
					setBackground(selectBackground);
					_selected = true;
				}
			}
		});
		*/
	}
}
