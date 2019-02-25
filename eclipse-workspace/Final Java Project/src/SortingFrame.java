import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
public class SortingFrame extends JFrame
{


	//Main method
	public static void main(String[] args) {
		
		//Running the frame on an event dispatch thread
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				//This makes the interface visible
				new SortingFrame().setVisible(true);
			}
		});

	}
	
	//Constructor for the JFrame
	public SortingFrame()
	{
		//setting the title of the frame
		setTitle("Sorting Animation");
		//sets size of the frame
		setPreferredSize(new Dimension (1500,700));
		//and the action to execute on close
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// JFrame cannot be resized
		setResizable(false);;
		//We load the components inside the frame
		loadComponents();
		//and then we pack everything, so that it gets properly displayed
		pack();
	}
	
	//Method used to load the components inside the jframe
	private void loadComponents() {
		//Initializing the JSplitPane
		JSplitPane mainPanel = new JSplitPane();
		//Setting the resizeWeight to 0.5, so that both left and right part gets the same size
		mainPanel.setResizeWeight(0.5);
		//setting setenabled to false makes the JSplitPane can't be resized
		mainPanel.setEnabled(false);
		
		//Initializing two SortPanel objects
		SortingPanel leftPanel = new SortingPanel();
		SortingPanel rightPanel = new SortingPanel();
		
		//Adding  left and right SortPanels of the JSpitPane
		mainPanel.setLeftComponent(leftPanel);
		mainPanel.setRightComponent(rightPanel);
	
		//adding the JSplitPane to the JFrame
		add(mainPanel);
	}

}
