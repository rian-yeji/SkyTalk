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
		profile.setFont(new Font("���� ���", Font.PLAIN, 20));
		profile.setOpaque(true);
		profile.setForeground(Color.white);
		profile.setBackground(new Color(0,0,0,122));
		cf.add(profile);
		
		textPaneChat = new JTextPane();
		textPaneChat.setEditable(false);
		textPaneChat.setBounds(15, 60, 565, 670);
		textPaneChat.setFont(new Font("���� ���", Font.PLAIN, 20));
		//textPaneChat.setHorizontalAlignment(textPaneChat.RIGHT_ALIGNMENT);
		//cf.add(textPaneChat);
		
		chatScroll = new JScrollPane(textPaneChat);
		chatScroll.setBounds(15, 60, 565, 670);
		cf.add(chatScroll);
	
		txtWrite = new TextField();

		txtWrite.setBounds(15, 740, 490, 50);
		txtWrite.setFont(new Font("���� ���", Font.PLAIN, 20));
	    cf.add(txtWrite);

		sendBtn = new JButton("send");

		sendBtn.setBounds(510, 740, 70, 50);
		cf.add(sendBtn);
		
		//controller.setTextPane(textPaneChat);//controller�� �������� �޾ƿ� �޽����� ǥ���� TextPane�� ��������.
		start(); //�׼��̺�Ʈ ���� �޼ҵ�
		
		//RecieveMassage(); //�����κ��� �޽����� �����ϴ� ������
	}
	
	public void start() { // �׼��̺�Ʈ ���� �޼ҵ�
		Myaction action = new Myaction(this.textPaneChat);
		sendBtn.addActionListener(action); // ����Ŭ������ �׼� �����ʸ� ��ӹ��� Ŭ������
		txtWrite.addActionListener(action);
	}

	class Myaction implements ActionListener // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
	{
		public Myaction(JTextPane textPaneChat) {

		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// �׼� �̺�Ʈ�� sendBtn�϶� �Ǵ� textField ���� Enter key ġ��
			if (e.getSource() == sendBtn || e.getSource() == txtWrite) 
			{
				String msg = null;
				msg = String.format("%s//%s//[%s] %s\n", User.SIGNAL_NOMAL_MSG, roomName, nickname, txtWrite.getText());
				//ä���� ģ ä�ù� �̸�, ��������, �޽����� ������ ����
				controller.send_Message(msg);
				//send_Message(msg);
				txtWrite.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
				txtWrite.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��	
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
