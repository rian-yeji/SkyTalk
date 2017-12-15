package chatting;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import command.CommandController;
import server.User;

public class chatPanel extends JPanel {
	
	private String nickname = LoginPanel.userID;
	
	private ChatFrame cf;
	private JLabel profile;
	private JTextPane textPaneChat;
	private TextField txtWrite;
	private JButton sendBtn;
	private JScrollPane chatScroll;

	private String roomName;
	private CommandController controller = CommandController.getController();
	
	public chatPanel(ChatFrame cf,String roomName) {
		this.roomName = roomName;
		this.cf = cf;
		System.out.println("chatPanel roomName = "+roomName);
		setLayout(null);
		setSize(600,850);
		setBackground(new Color(168,218,255));
		
		profile = new JLabel(roomName);
		profile.setBounds(15, 20, 565, 40);
		profile.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		profile.setOpaque(true);
		profile.setForeground(Color.white);
		profile.setBackground(new Color(0,0,0,122));
		cf.add(profile);
		
		textPaneChat = new JTextPane();
		textPaneChat.setEditable(false);
		textPaneChat.setBounds(15, 60, 565, 670);
		textPaneChat.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		//textPaneChat.setHorizontalAlignment(textPaneChat.RIGHT_ALIGNMENT);
		//cf.add(textPaneChat);
		
		chatScroll = new JScrollPane(textPaneChat);
		chatScroll.setBounds(15, 60, 565, 670);
		cf.add(chatScroll);
	
		txtWrite = new TextField();

		txtWrite.setBounds(15, 740, 490, 50);
		txtWrite.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
	    cf.add(txtWrite);

		sendBtn = new JButton("send");

		sendBtn.setBounds(510, 740, 70, 50);
		cf.add(sendBtn);
		
		//controller.setTextPane(textPaneChat);//controller가 서버에서 받아온 메시지를 표시할 TextPane을 지정해줌.
		start(); //액션이벤트 지정 메소드
		
		//RecieveMassage(); //서버로부터 메시지를 수신하는 스레드
	}
	
	public void start() { // 액션이벤트 지정 메소드
		Myaction action = new Myaction(this.textPaneChat);
		sendBtn.addActionListener(action); // 내부클래스로 액션 리스너를 상속받은 클래스로
		txtWrite.addActionListener(action);
	}

	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		public Myaction(JTextPane textPaneChat) {

		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
			if (e.getSource() == sendBtn || e.getSource() == txtWrite) 
			{
				String msg = null;
				msg = String.format("%s//%s//[%s] %s\n", User.SIGNAL_NOMAL_MSG, roomName, nickname, txtWrite.getText());
				//채팅을 친 채팅방 이름, 유저네임, 메시지를 서버로 전달
				controller.send_Message(msg);
				//send_Message(msg);
				txtWrite.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtWrite.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다	
			}

		}

	}
	
	public JTextPane getTextPaneChat() {
		return this.textPaneChat;
	}
	
	public void setTextPaneChat(JTextPane textPaneChat){
		this.textPaneChat = textPaneChat;
		
	}

}
