//import com.sun.javafx.geom.BaseBounds;
//import com.sun.javafx.geom.transform.BaseTransform;
//import com.sun.javafx.jmx.MXNodeAlgorithm;
//import com.sun.javafx.jmx.MXNodeAlgorithmContext;
//import com.sun.javafx.sg.prism.NGNode;
import javafx.event.EventHandler;
//import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
//import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class JTeamTree extends Stage
{
	private class JTeamTreeGroupNode
	{
	}

	private class JTeamTreeGroup extends JTeamTreeGroupNode
	{
		String m_name;
		@SuppressWarnings("unused")
		String m_image;

		@SuppressWarnings("unused")
		ArrayList<JTeamTreeGroupNode> m_nodes;

		public JTeamTreeGroup(String name, String image)
		{
			m_name = name;
			m_image = image;
			m_nodes = new ArrayList<JTeamTreeGroupNode>();
		}

//		public void add(JTeamTreeGroupNode node)
//		{
//			m_nodes.add(node);
//		}

//		public void dump(int index)
//		{
//			int n = m_nodes.size();
//			for(int i=0; i<n; i++)
//			{
//				for(int j=0; j<=index; j++)
//					System.out.print("    ");
//
//				if(m_nodes.get(i).getClass().toString().equals("class JTeamTreeTeam"))
//				{
//					JTeamTreeTeam team = (JTeamTreeTeam)m_nodes.get(i);
//					System.out.println(team.m_name);
//				}
//				else if(m_nodes.get(i).getClass().toString().equals("class JTeamTreeGroup"))
//				{
//					JTeamTreeGroup teams = (JTeamTreeGroup)m_nodes.get(i);
//					System.out.println(teams.m_name);
//					teams.dump(index+1);
//				}
//			}
//		}
	}

	private class JTeamTreeTeam extends JTeamTreeGroupNode
	{
		String m_name;
		String m_year;
		String m_filename;
		String m_helmet;

		public JTeamTreeTeam(String name, String year, String filename, String helmet)
		{
			m_name = name;
			m_year = year;
			m_filename = filename;
			m_helmet = helmet;
		}
	}

	private StackPane tree = new StackPane();
	private Scene _scene = new Scene(tree);
//	private Font font = new Font(16);

	public JTeamTree()
	{
		try
		{
			loadTeam();
		}
		catch(Exception e)
		{

		}
	}

	private String _rootDirectory = "";
	private String _selectedTeamFilename = "";
	public String selectedTeamFilename() { return _selectedTeamFilename; }

	public void loadTeam() throws Exception
	{
		TreeView<JTeamTreeGroupNode> treeView = new TreeView<>();
		TreeItem<JTeamTreeGroupNode> item = new TreeItem<>(new JTeamTreeGroup("Teams",null));
		TreeItem<JTeamTreeGroupNode> item2 = null;
		TreeItem<JTeamTreeGroupNode> item3 = null;

		treeView.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event)
			{
				TreeItem<JTeamTreeGroupNode> item = treeView.getSelectionModel().getSelectedItem();
				if(item != null)
				{
					JTeamTreeGroupNode node = item.getValue();
					if(node != null && node.getClass().toString().equals("class JTeamTree$JTeamTreeTeam"))
					{
						JTeamTreeTeam team = (JTeamTreeTeam)node;
						System.out.println("team selected: " + team.m_filename);
						_selectedTeamFilename = _rootDirectory + "\\Teams\\" + team.m_filename;
						close();
					}
				}
			}
		});

		item.setExpanded(true);

		treeView.setCellFactory((e) -> new TreeCell<JTeamTreeGroupNode>()
				{
					@Override
					protected void updateItem(JTeamTreeGroupNode node, boolean empty)
					{
						super.updateItem(node, empty);

						if(node != null)
						{
							if (node.getClass().toString().equals("class JTeamTree$JTeamTreeTeam"))
							{
								JTeamTreeTeam team = (JTeamTreeTeam) node;
								setText(team.m_name + ": " + team.m_year);

								if(team.m_helmet != null)
								{
									File file = new File(team.m_helmet);
									Image image = new Image(file.toURI().toString());
									ImageView iv = new ImageView(image);
									double height = image.getHeight();
									double width = image.getWidth();
									double scale = 64.0 / height;
									iv.setFitHeight(64.0);
									iv.setFitWidth(width * scale);
									setGraphic(iv);
								}
							}

							else if (node.getClass().toString().equals("class JTeamTree$JTeamTreeGroup"))
							{
								JTeamTreeGroup group = (JTeamTreeGroup) node;
								setText(group.m_name);
								setGraphic(null);
							}
						}
						else
						{
							setText("");
							setGraphic(null);
						}
					}
				}
		);

		// determine the executable path
		final String dir = System.getProperty("user.dir");

		int index = dir.lastIndexOf("\\");
		_rootDirectory = dir.substring(0,index);

		File f1 = new File(_rootDirectory + "\\Teams\\");
		if(f1.isDirectory())
		{
			String s[] = f1.list();
			for(int i = 0; i < s.length; i++)
			{
				if(s[i].endsWith(".csv"))
				{
					// open the grouping file and read the contents
					String filename = _rootDirectory + "\\Teams\\" + s[i];
					BufferedReader br = new BufferedReader(new FileReader(filename));
					try
					{
						String line = br.readLine();

						// parse the contents
						while(line != null)
						{
							String token = line.toString().substring(0, 1);

							if(token.equals("#"))
							{
								StringTokenizer items = new StringTokenizer(line, ",");
								int nItems = items.countTokens();
								if(nItems < 1)
									System.out.println("====Error: JTeamTreeGroup line error: " + line);
								else
								{
									items = new StringTokenizer(line, ",");

									if(nItems > 1)
										item3 = item2 = new TreeItem<>(new JTeamTreeGroup(items.nextToken(), items.nextToken()));
									else
										item3 = item2 = new TreeItem<>(new JTeamTreeGroup(items.nextToken(), null));
									item.getChildren().add(item2);
								}
							} else if(token.equals("&"))
							{
								StringTokenizer items = new StringTokenizer(line, ",");
								int nItems = items.countTokens();
								if(nItems < 1)
									System.out.println("====Error: JTeamTreeGroup line error: " + line);
								else
								{
									items = new StringTokenizer(line, ",");

									if(nItems > 1)
										item3 = new TreeItem<>(new JTeamTreeGroup(items.nextToken(), items.nextToken()));
									else
										item3 = new TreeItem<>(new JTeamTreeGroup(items.nextToken(), null));

									item2.getChildren().add(item3);
								}
							} else if(token.equals("%"))
							{
							} else
							{
								StringTokenizer items = new StringTokenizer(line, ",");

//								JTeamTreeTeam team = null;

								int nItems = items.countTokens();
								if(nItems < 3)
									System.out.println("====Error: JTeamTreeTeam line error: " + line);
								else
								{
									items = new StringTokenizer(line, ",");

									TreeItem<JTeamTreeGroupNode> item4 = null;
									if(nItems > 3)
										item4 = new TreeItem<>(new JTeamTreeTeam(items.nextToken(), items.nextToken(), items.nextToken(), dir + "\\..\\Helmets\\" + items.nextToken()));
									else
										item4 = new TreeItem<>(new JTeamTreeTeam(items.nextToken(), items.nextToken(), items.nextToken(), null));
									item3.getChildren().add(item4);
								}
							}

							line = br.readLine();
						}
					} catch(Exception e)
					{
						System.out.println("====Exception caught=====");
						br.close();
					} finally
					{
						br.close();
					}
				}
			}

			treeView.setRoot(item);
			tree.getChildren().add(treeView);
			tree.setMinWidth(300);
			setScene(_scene);
		}
	}
}
