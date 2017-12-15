package chatting;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class LoginFrame extends JFrame{
	
	public LoginFrame() {

		setTitle("SKY TALK- Login");
		setSize(600, 600);
		setResizable(false);
		
		/*setUndecorated(true);
		setBackground(new Color(0,0,0,122));*/

		setLayout(null);
		
		Dimension frameSize = this.getSize(); // ������ ������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // ����� ������

		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2); // ȭ�� ���

		Container c = getContentPane();
		
		c.add(new LoginPanel(this));
		
		setVisible(true);
	}

}
