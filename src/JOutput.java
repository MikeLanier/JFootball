import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.paint.Color;

public class JOutput extends JPanel
{
	static JString text = new JString();
	static JString text2 = new JString();
	public JOutput(Color color)
	{
		super(color);

		double[] percentWidth = {60, 40};
		ColumnConstraints[] col = new ColumnConstraints[percentWidth.length];

		for(int i=0; i<percentWidth.length; i++)
		{
			col[i] = new ColumnConstraints();
			col[i].setPercentWidth(percentWidth[i]);
		}
		getColumnConstraints().addAll(col);

		this.setPadding(new Insets(10,10,10,10));

		text.addSegment("This is red text. ", Color.RED, false, null);
		text.addSegment("This is blue text. ", Color.BLUE, false, null);
		text.addSegment("This is green text. ", Color.GREEN, false, JPlayerOptionEvent.OPTION_1);
		text.addSegment("This is purple text.\n", Color.PURPLE, false, null);
		text.addSegment("This is orange text. ", Color.ORANGE, false, JPlayerOptionEvent.OPTION_2);
		text.addSegment("This is black text. ", Color.BLACK, false, null);
		text.addSegment("This is brown text. ", Color.BROWN, false, JPlayerOptionEvent.OPTION_3);
		text.addSegment("This is red text. ", Color.RED, false, null);
		add(text, 0, 0);

		text2.addSegment("This is red text. ", Color.RED, false, null);
		text2.addSegment("This is blue text. ", Color.BLUE, false, null);
		text2.addSegment("This is green text. ", Color.GREEN, false, JPlayerOptionEvent.OPTION_1);
		text2.addSegment("This is purple text.\n", Color.PURPLE, false, null);
		text2.addSegment("This is orange text. ", Color.ORANGE, false, JPlayerOptionEvent.OPTION_2);
		text2.addSegment("This is black text. ", Color.BLACK, false, null);
		text2.addSegment("This is brown text. ", Color.BROWN, false, JPlayerOptionEvent.OPTION_3);
		text2.addSegment("This is red text. ", Color.RED, false, null);
		add(text2, 1, 0);

		// I'll probably move this somewhere else.  Now sure yet how this will fit into the flow

		// JString currently allows for up to 5 options in one string.  Create an event handler for
		// each option.
		addEventHandler(JPlayerOptionEvent.OPTION_1, new EventHandler<JPlayerOptionEvent>()
		{
			@Override
			public void handle(JPlayerOptionEvent event)
			{
				System.out.println("Option 1 selected");
			}
		});

		addEventHandler(JPlayerOptionEvent.OPTION_2, new EventHandler<JPlayerOptionEvent>()
		{
			@Override
			public void handle(JPlayerOptionEvent event)
			{
				System.out.println("Option 2 selected");
			}
		});

		addEventHandler(JPlayerOptionEvent.OPTION_3, new EventHandler<JPlayerOptionEvent>()
		{
			@Override
			public void handle(JPlayerOptionEvent event)
			{
				System.out.println("Option 3 selected");
			}
		});

		addEventHandler(JPlayerOptionEvent.OPTION_4, new EventHandler<JPlayerOptionEvent>()
		{
			@Override
			public void handle(JPlayerOptionEvent event)
			{
				System.out.println("Option 4 selected");
			}
		});

		addEventHandler(JPlayerOptionEvent.OPTION_4, new EventHandler<JPlayerOptionEvent>()
		{
			@Override
			public void handle(JPlayerOptionEvent event)
			{
				System.out.println("Option 5 selected");
			}
		});
	}
}
