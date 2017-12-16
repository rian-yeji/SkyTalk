package chatting;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
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
import javax.swing.JTextField;
import javax.swing.JTextPane;

import chatting.chatPanel.Myaction;
import command.CommandController;
import server.User;

public class LoginPanel extends JPanel {
	/* 네트워크용 변수 */
	private final String ip = "127.0.0.1";
	private final int port = 3330;
	public static Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	public static String userID;
	private CommandController controller;

	private JTextField tf_ID; // ID를 입력받을곳
	private JTextField tf_PW; // PW입력
	private LoginFrame lf;
	private String id;
	private StartPanel startPanel;
	final String logoImg = "skyLogo.png";
	private JButton btnNewButton;

	private JLabel Logo;

	public LoginPanel(LoginFrame loginFrame) {

		this.lf = loginFrame;
		setBackground(new Color(168, 218, 255));
		setLayout(null);
		setSize(600, 900);

		Logo = new JLabel(new ImageIcon(logoImg));
		Logo.setBounds(0, 10, 600, 400);
		Logo.setOpaque(false);
		lf.add(Logo);

		/*
		 * JPanel login = new JPanel(); login.setBounds(150, 450, 300, 300);
		 */

		JLabel idLabel = new JLabel("ID : ");
		idLabel.setBounds(125, 350, 35, 30);
		idLabel.setFont(new Font("Adobe Garamond Pro", Font.PLAIN, 17));
		lf.add(idLabel);

		JLabel pwLabel = new JLabel("PW : ");
		pwLabel.setBounds(125, 390, 40, 30);
		pwLabel.setFont(new Font("Adobe Garamond Pro", Font.PLAIN, 17));
		lf.add(pwLabel);

		tf_ID = new JTextField();
		tf_ID.setBounds(170, 350, 200, 30);
		lf.add(tf_ID);

		tf_PW = new JTextField();
		tf_PW.setBounds(170, 390, 200, 30);
		lf.add(tf_PW);
		tf_PW.setColumns(10);
		
		btnNewButton = new JButton("로그인");
		btnNewButton.setBounds(380, 350, 100, 70);
		lf.add(btnNewButton);

		start();
	}
	
	public void start() { // 액션이벤트 지정 메소드
		Myaction action = new Myaction();
		btnNewButton.addActionListener(action); // 내부클래스로 액션 리스너를 상속받은 클래스로
		tf_PW.addActionListener(action);
		tf_ID.addActionListener(action);
	}

	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		public Myaction() {

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
			if (e.getSource() == btnNewButton || e.getSource() == tf_ID || e.getSource() == tf_PW) {
				String user_id = tf_ID.getText().trim();
				userID = user_id;

				if (user_id != null) { // 유저아이디 빈칸이면 안됨
					network();// 서버와 연결
					MainFrame f = new MainFrame(userID);
					lf.dispose();
				}
			}

		}

	}

	/* 네트워크 소켓 연결 */

	public void network() {
		try {
			socket = new Socket(ip, port);
			if (socket != null) // socket이 null값이 아닐때 즉! 연결되었을때
			{
				controller = CommandController.getController();// 맨처음
																// getController하는
																// 부분->여기서 생성됨
				Connection(); // 연결 메소드를 호출
			}
		} catch (UnknownHostException e) {

		} catch (IOException e) {
			// textArea.append("소켓 접속 에러!!\n");
			System.out.println("Client 소켓 접속 에러!!\n");
		}
	}

	public void Connection() { // 실직 적인 메소드 연결부분
		try { // 스트림 설정
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		} catch (IOException e) {
			// textArea.append("스트림 설정 에러!!\n");
			System.out.println("스트림 설정 에러!!\n");
		}
		if (controller == null) {
			System.out.println("controller == null");
		} else {
			controller.send_Message(User.SIGNAL_USER_ID + "//" + userID); // 정상적으로 연결되면 나의 아이디를 전송
			//이후에 바로 SIGNAL_ONLINE_USER_LIST를 보내기때문에 서버가 처리할 시간을 주어야 함.
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public StartPanel getStartPanel() {
		return startPanel;
	}

	public void setStartPanel(StartPanel startPanel) {
		this.startPanel = startPanel;
	}
}
