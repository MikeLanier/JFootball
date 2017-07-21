import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.prefs.Preferences;

public class Main extends Application
{
	private Stage primaryStage;


	@Override
	public void start(Stage _primaryStage) throws Exception
	{
		primaryStage = _primaryStage;

		primaryStage.setTitle("JGame");

		JGame game = new JGame();
		Scene scene = new Scene(game, 1400, 800);

		// Get frame size and location from the users preferences
		Preferences userPrefs = Preferences.userNodeForPackage(getClass());
		double x = userPrefs.getDouble("stage.x", 100);
		double y = userPrefs.getDouble("stage.y", 100);
		double w = userPrefs.getDouble("stage.width", 1400);
		double h = userPrefs.getDouble("stage.height", 800);

		primaryStage.setScene(scene);
		primaryStage.setX(x);
		primaryStage.setY(y);
		primaryStage.setWidth(w);
		primaryStage.setHeight(h);
		primaryStage.show();
	}

	@Override
	public void stop()
	{
		// when the app closes, save the frame size and location
		Preferences userPrefs = Preferences.userNodeForPackage(getClass());
		userPrefs.putDouble("stage.x", primaryStage.getX());
		userPrefs.putDouble("stage.y", primaryStage.getY());
		userPrefs.putDouble("stage.width", primaryStage.getWidth());
		userPrefs.putDouble("stage.height", primaryStage.getHeight());
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
