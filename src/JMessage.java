import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class JMessage extends Stage
{
	private VBox _box = new VBox();
	private Scene _scene = new Scene(_box);
	private Label _title = new Label("");
	private Label _message = new Label("");
	private Font fontTitle = new Font(24);
	private Font fontMessage = new Font(16);

	public JMessage(String title, String message)
	{
		_box.getChildren().add(_title);
		_box.getChildren().add(_message);

		initModality(Modality.APPLICATION_MODAL);

		_title.setPadding(new Insets(10, 10, 10, 10));
		_title.setWrapText(true);
		_title.setFont(fontTitle);
		_title.setText(title);

		_message.setPadding(new Insets(10, 10, 10, 10));
		_message.setWrapText(true);
		_message.setFont(fontMessage);
		_message.setText(message);

		setScene(_scene);
	}
}
