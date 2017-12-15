package chatting;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JTextPane;

public class ChatFrame extends JFrame{
	
	private JTextPane textArea;
	private chatPanel chatPanel;
	
	public ChatFrame(String roomName) {
		setTitle("SKY TALK- chatting");
		setSize(600, 850);
		setResizable(false);
		
		/*setUndecorated(true);
		setBackground(new Color(0,0,0,122));*/

		setLayout(null);
		
		Dimension frameSize = this.getSize(); // ������ ������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // ����� ������

		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2); // ȭ�� �������̤�����

		Container c = getContentPane();
		
		this.chatPanel = new chatPanel(this,roomName);
		c.add(chatPanel);
		
		setVisible(true);
	}

	public chatPanel getChatPanel() {
		return chatPanel;
	}

	public void setChatPanel(chatPanel chatPanel) {
		this.chatPanel = chatPanel;
	}
	
	
	
	
}
