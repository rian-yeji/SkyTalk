package chatting;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import command.CommandController;


public class MainFrame extends JFrame {

	public StartPanel startPanel;
	public CommandController controller = CommandController.getController();
	
	public MainFrame(String userID/*, String myStateMessage*/) {
		setTitle("SKY TALK");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 900);
		setResizable(false);
		
		/*setUndecorated(true);
		setBackground(new Color(0,0,0,122));*/
		setLayout(null);
		
		startPanel = new StartPanel(this,userID/*,myStateMessage*/);
		
		Dimension frameSize = this.getSize(); // ������ ������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // ����� ������

		this.setLocation((screenSize.width - frameSize.width), (screenSize.height - screenSize.height)); // ȭ�� ���� ���

		Container c = getContentPane();
		c.add(startPanel);
		
		setVisible(true);
		
		controller.saveMainFrame(userID,this);

	}

	public StartPanel getStartPanel() {
		return startPanel;
	}

	public void setStartPanel(StartPanel startPanel) {
		this.startPanel = startPanel;
	}
	
	
}
