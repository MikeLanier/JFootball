import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class JPanel extends GridPane
{
	double m_width = 0;
	double m_height = 0;

	public JPanel(Color color)
	{
		widthProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth)
			{
				SetWidth(newSceneWidth.doubleValue());
				//System.out.println("Width: " + newSceneWidth);
			}
		});

		heightProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight)
			{
				SetHeight(newSceneHeight.doubleValue());
				//System.out.println("Height: " + newSceneHeight);
			}
		});

		setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
		setBackground(new Background(new BackgroundFill(color,null,null)));
	}

	public void SetWidth(double width)
	{
		if(width != m_width)
		{
			m_width = width;

			if(m_height != 0 && m_width != 0 )
				SetSize(m_width, m_height);
		}
	}

	public void SetHeight(double height)
	{
		if(height != m_height)
		{
			m_height = height;

			if(m_height != 0 && m_width != 0 )
				SetSize(m_width, m_height);
		}
	}

	public void SetSize(double width, double height)
	{
	}
}
