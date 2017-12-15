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
	protected static JTextArea textArea; //클라이언트 및 서버 메시지 출력
	
	private final int portNum=3330; //포트번호
	private ServerSocket serverSocket;
	private ClientManager clientManager;
	private RoomManager roomManager;
	private Socket socket;//연결 소켓
	
	public static void main(String[] args) {
		Server frame = new Server();
		frame.setVisible(true);
	}

	public Server() {
		server = this;
		init(); //화면구성
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

		Start = new JButton("서버 실행");
		
		Myaction action = new Myaction();
		Start.addActionListener(action); // 내부클래스로 액션 리스너를 상속받은 클래스로
		Start.setBounds(0, 310, 264, 42);
		contentPane.add(Start);
		textArea.setEditable(false); // textArea를 사용자가 수정 못하게끔 막는다.
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
			serverSocket = new ServerSocket(portNum); // 서버가 포트 여는부분
			Start.setText("서버실행중");
			Start.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
			
			if(serverSocket!=null) // socket 이 정상적으로 열렸을때
			{
				Connection();
			}
			
		} catch (IOException e) {
			textArea.append("소켓이 이미 사용중입니다...\n");
		}
	}
	
	private void Connection() {
		Thread th = new Thread(new Runnable() { // 사용자 접속을 받을 스레드
			@Override
			public void run() {
				while (true) { // 사용자 접속을 계속해서 받기 위해 while문
					try {
						textArea.append("사용자 접속 대기중...\n");
						socket = serverSocket.accept(); // accept가 일어나기 전까지는 무한 대기중
						textArea.append("사용자 접속!!\n");
						
						//사용자 접속을 매니저에게 알림
						//사용자 목록에 추가, 스레드 실행을 위임
						clientManager.insertUser(socket);
						//usersUpdateFriendList();
						
						/*ClientManager client = new ClientManager(socket, server);
						serverClientList.add(client);// 해당 벡터에 사용자 객체를 추가
						client.start(); // 만든 객체의 스레드 실행*/		
						} catch (IOException e) {
						textArea.append("!!!! accept 에러 발생... !!!!\n");
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
	
	/*메시지 처리*/
	public void broad_cast(User user, String str, String roomName) {
		//RoomManager에게 브로드캐스팅 명령 -> Room이 자신에게 속한 사용자들에게 브로드캐스팅
		roomManager.broadcast(user, str, roomName);
	}
	
	//새 유저가 추가될 경우 기존의 유저들의 리스트를 업데이트
	public void usersUpdateFriendList(String newUser) {
		clientManager.usersUpdateFriendList(newUser);
	}
	//유저의 상태이미지나 상태메시지 변경이 있다면 모든 유저들의 화면을 갱신
	public void userInfoUpdate(String userName,String stateImg,String stateMsg) {
		clientManager.userInfoUpdate(userName,stateImg,stateMsg);
	}

	public void createSingleRoom(User user,String myName,String friendName) {
		ArrayList<User> users = new ArrayList<User>();
		User me = clientManager.searchByUserName(myName);
		User friend = clientManager.searchByUserName(friendName);
	
		String roomName=me.getNickname()+"와"+friend.getNickname()+"의 채팅방";
		if(friend==null) {
			//이건 해당 친구의 접속이 끊긴 상태라고 하면 될듯
		}
		//user.send_Message(User.SIGNAL_CRATE_ROOM_COMPLETE+"//"+roomName);
		else { //여긴 접속중!
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
			
			if(i==mutiRoomUserList.size()-1)//마지막 유저이름
				roomName += user.getNickname();
			else
				roomName += user.getNickname()+",";
		}
		ChattingRoom newRoom = roomManager.createRoom(users);
		newRoom.setRoomName(roomName);
		for(int i=0;i<users.size();i++) { //참여하는 모든 유저들에게 채팅방을 띄워줌
			users.get(i).send_Message(User.SIGNAL_CRATE_ROOM_COMPLETE+"//"+roomName);
		}
		
	}
	
	public ArrayList<User> getFriendList(){
		return clientManager.getUserList();
	}
	
	
}
