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
		
		Dimension frameSize = this.getSize(); // 프레임 사이즈
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 사이즈

		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2); // 화면 가운데

		Container c = getContentPane();
		
		c.add(new LoginPanel(this));
		
		setVisible(true);
	}

}
