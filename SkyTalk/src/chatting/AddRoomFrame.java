package chatting;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class AddRoomFrame extends JFrame{
	
	private AddFriendPanel friendPanel;
	
	public AddRoomFrame(String userId) {
		
		//this.friendPanel = friendPanel;
	
		setTitle("SKY TALK- addFriend");
		setSize(400, 600);
		setResizable(false);
		
		/*setUndecorated(true);
		setBackground(new Color(0,0,0,122));*/

		setLayout(null);
		
		//friendPanel = new AddFriendPanel(this);
		
		Dimension frameSize = this.getSize(); // 프레임 사이즈
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 사이즈
		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2); // 화면 ㄱㅏㅇㅜㄴㄷㅔ

		Container c = getContentPane();
		
		c.add(new AddFriendPanel(this,userId));
		
		setVisible(true);
	}

}
