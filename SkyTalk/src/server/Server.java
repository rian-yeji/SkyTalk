package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import info.UserInfo;

public class Server extends JFrame{
	private Server server;
	
	private JPanel contentPane;
	private JButton Start;
	protected static JTextArea textArea; //Ŭ���̾�Ʈ �� ���� �޽��� ���
	
	private final int portNum=3330; //��Ʈ��ȣ
	private ServerSocket serverSocket;
	private ClientManager clientManager;
	private RoomManager roomManager;
	private Socket socket;//���� ����
	
	public static void main(String[] args) {
		Server frame = new Server();
		frame.setVisible(true);
	}

	public Server() {
		server = this;
		init(); //ȭ�鱸��
		clientManager = new ClientManager(server);
		roomManager = new RoomManager(server);
	}
	
	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 280, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane js = new JScrollPane();				

		textArea = new JTextArea();
		textArea.setColumns(20);
		textArea.setRows(5);
		js.setBounds(0, 0, 264, 310);
		contentPane.add(js);
		js.setViewportView(textArea);

		Start = new JButton("���� ����");
		
		Myaction action = new Myaction();
		Start.addActionListener(action); // ����Ŭ������ �׼� �����ʸ� ��ӹ��� Ŭ������
		Start.setBounds(0, 310, 264, 42);
		contentPane.add(Start);
		textArea.setEditable(false); // textArea�� ����ڰ� ���� ���ϰԲ� ���´�.
	}

	class Myaction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == Start)
				server_start();
		}
	}
	
	private void server_start() {
		try {
			serverSocket = new ServerSocket(portNum); // ������ ��Ʈ ���ºκ�
			Start.setText("����������");
			Start.setEnabled(false); // ������ ���̻� �����Ű�� �� �ϰ� ���´�
			
			if(serverSocket!=null) // socket �� ���������� ��������
			{
				Connection();
			}
			
		} catch (IOException e) {
			textArea.append("������ �̹� ������Դϴ�...\n");
		}
	}
	
	private void Connection() {
		Thread th = new Thread(new Runnable() { // ����� ������ ���� ������
			@Override
			public void run() {
				while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
					try {
						textArea.append("����� ���� �����...\n");
						socket = serverSocket.accept(); // accept�� �Ͼ�� �������� ���� �����
						textArea.append("����� ����!!\n");
						
						//����� ������ �Ŵ������� �˸�
						//����� ��Ͽ� �߰�, ������ ������ ����
						clientManager.insertUser(socket);
						//usersUpdateFriendList();
						
						/*ClientManager client = new ClientManager(socket, server);
						serverClientList.add(client);// �ش� ���Ϳ� ����� ��ü�� �߰�
						client.start(); // ���� ��ü�� ������ ����*/		
						} catch (IOException e) {
						textArea.append("!!!! accept ���� �߻�... !!!!\n");
					} 
				}
			}
		});
		th.start();
	}

	public void exitUser(User user) {
		clientManager.exitUser(user);
		roomManager.exitUser(user);
	}
	
	/*�޽��� ó��*/
	public void broad_cast(User user, String str, String roomName) {
		//RoomManager���� ��ε�ĳ���� ��� -> Room�� �ڽſ��� ���� ����ڵ鿡�� ��ε�ĳ����
		roomManager.broadcast(user, str, roomName);
	}
	
	//�� ������ �߰��� ��� ������ �������� ����Ʈ�� ������Ʈ
	public void usersUpdateFriendList(String newUser) {
		clientManager.usersUpdateFriendList(newUser);
	}
	//������ �����̹����� ���¸޽��� ������ �ִٸ� ��� �������� ȭ���� ����
	public void userInfoUpdate(String userName,String stateImg,String stateMsg) {
		clientManager.userInfoUpdate(userName,stateImg,stateMsg);
	}

	public void createSingleRoom(User user,String myName,String friendName) {
		ArrayList<User> users = new ArrayList<User>();
		User me = clientManager.searchByUserName(myName);
		User friend = clientManager.searchByUserName(friendName);
	
		String roomName=me.getNickname()+"��"+friend.getNickname()+"�� ä�ù�";
		if(friend==null) {
			//�̰� �ش� ģ���� ������ ���� ���¶�� �ϸ� �ɵ�
		}
		//user.send_Message(User.SIGNAL_CRATE_ROOM_COMPLETE+"//"+roomName);
		else { //���� ������!
			users.add(me);
			users.add(friend);
			ChattingRoom newRoom = roomManager.createRoom(users);
			newRoom.setRoomName(roomName);
			user.send_Message(User.SIGNAL_CRATE_ROOM_COMPLETE+"//"+roomName);
			friend.send_Message(User.SIGNAL_CRATE_ROOM_COMPLETE+"//"+roomName);
			
		}
		
	}
	
	public void createMultiRoom(ArrayList<String> mutiRoomUserList) {
		ArrayList<User> users = new ArrayList<User>();
		String roomName="";
		for(int i=0;i<mutiRoomUserList.size();i++) {
			User user = clientManager.searchByUserName(mutiRoomUserList.get(i));
			users.add(user);
			
			if(i==mutiRoomUserList.size()-1)//������ �����̸�
				roomName += user.getNickname();
			else
				roomName += user.getNickname()+",";
		}
		ChattingRoom newRoom = roomManager.createRoom(users);
		newRoom.setRoomName(roomName);
		for(int i=0;i<users.size();i++) { //�����ϴ� ��� �����鿡�� ä�ù��� �����
			users.get(i).send_Message(User.SIGNAL_CRATE_ROOM_COMPLETE+"//"+roomName);
		}
		
	}
	
	public ArrayList<User> getFriendList(){
		return clientManager.getUserList();
	}
	
	
}
