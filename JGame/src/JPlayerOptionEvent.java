import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class JPlayerOptionEvent extends Event
{
	//final JPlayerOptionEvents
	public static final EventType<JPlayerOptionEvent> OPTION_1 = new EventType(ANY, "OPTION_1");
	public static final EventType<JPlayerOptionEvent> OPTION_2 = new EventType(ANY, "OPTION_2");
	public static final EventType<JPlayerOptionEvent> OPTION_3 = new EventType(ANY, "OPTION_3");
	public static final EventType<JPlayerOptionEvent> OPTION_4 = new EventType(ANY, "OPTION_4");
	public static final EventType<JPlayerOptionEvent> OPTION_5 = new EventType(ANY, "OPTION_5");

	public JPlayerOptionEvent()
	{
		this(OPTION_1);
	}

	public JPlayerOptionEvent(EventType<? extends Event> arg0)
	{
		super(arg0);
	}

	public JPlayerOptionEvent(Object arg0, EventTarget arg1, EventType<? extends Event> arg2)
	{
		super(arg0, arg1, arg2);
	}
}