package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JTextArea;


public class User extends Thread{
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private Socket user_socket;
	Server server;
	
	//유저가 속해있는 룸들
	ArrayList<ChattingRoom> joinRoomList = new ArrayList<ChattingRoom>();
	ArrayList<User> friendsList = new ArrayList<User>();//아직 안씀
	ArrayList<String> userName = new ArrayList<String>();
	ArrayList<String> multiRoomUserList;
	
	Socket socket;
	String nickname,password,stateImage,stateMessage;
	JTextArea textArea = Server.textArea;
	
	public User(Socket socket,Server server) {
		nickname = "newUser";
		password="1111";
		stateImage = "happy.png";
		stateMessage = "상태 메세지";
		user_socket = socket;
		this.server = server;
		network();
	}
	
	public void EnterRoom(ChattingRoom room) {
		room.EnterRoom(this);//룸에 입장(위임)
		this.joinRoomList.add(room);//유저가 속한 방을 추가
	}
	
	public void addFreind(User user) { //친구추가
		friendsList.add(user);
	}
	
	public void network() {
		try {
			is = user_socket.getInputStream();
			dis = new DataInputStream(is);
			os = user_socket.getOutputStream();
			dos = new DataOutputStream(os);
			/*//Nickname = dis.readUTF(); // 사용자의 닉네임 받는부분
			byte[] b=new byte[128];
			dis.read(b);
			String Nickname = new String(b);
			Nickname = Nickname.trim();*/
			
			textArea.append("ID " + nickname + " 접속\n");
			textArea.setCaretPosition(textArea.getText().length());	
			
			//send_Message(nickname + "님 환영합니다."); // 연결된 사용자에게 정상접속을 알림
		} catch (Exception e) {
			textArea.append("스트림 셋팅 에러\n");
			textArea.setCaretPosition(textArea.getText().length());
		}
	}
	
	public static final String SIGNAL_CREATE_SINGLEROOM = "SIGNAL_CREATE_SINGLEROOM";
	public static final String SIGNAL_CREATE_MULTIROOM = "SIGNAL_CREATE_MULTIROOM";
	public static final String SIGNAL_CRATE_ROOM_COMPLETE = "SIGNAL_CRATE_ROOM_COMPLETE";
	public static final String SIGNAL_NOMAL_MSG = "SIGNAL_NOMAL_MSG";
	public static final String SIGNAL_USER_ID = "SIGNAL_USER_ID";
	public static final String SIGNAL_ONLINE_USER_LIST = "SIGNAL_ONLINE_USER_LIST";
	//public static final String SIGNAL_UPDATE_FRIENDS_LIST = "SIGNAL_UPDATE_FRIENDS_LIST";
	public static final String SIGNAL_NEW_USER_CONNECT = "SIGNAL_NEW_USER_CONNECT";
	public static final String SIGNAL_CHANGE_STATE = "SIGNAL_CHANGE_STATE_MSG";
	
	/*메시지 처리 부분*/
	public void InMessage(String str) {// 사용자 메세지 처리
		String[] array = str.split("//");
		if(array[0].equals(SIGNAL_CREATE_SINGLEROOM)) { //freindPanel로부터 갠톡방 생성요청
			//SIGNAL_CREATE_SINGLEROOM//myName//friendName
			String myName = array[1];
			String friendName = array[2];
			String roomName = friendName+"님과의 채팅방"; //기본 룸이름

			textArea.append(myName+"님이"+friendName+"님과의 채팅방 개설을 요청했습니다.\n");
			textArea.setCaretPosition(textArea.getText().length());
			
			server.createSingleRoom(this,myName, friendName);
		}
		else if(array[0].equals(SIGNAL_CREATE_MULTIROOM)) {//단톡방
			multiRoomUserList = new ArrayList<String>();
			String msg="";
			for(int i=1;i<array.length;i++) {
				multiRoomUserList.add(array[i]);
				if(i==array.length)
					msg+=array[i];
				else
					msg+=array[i]+",";
			}
			textArea.append(msg+" 채팅방 개설을 요청했습니다.\n");
			textArea.setCaretPosition(textArea.getText().length());
			server.createMultiRoom(multiRoomUserList);
		}
		else if(array[0].equals(SIGNAL_USER_ID)) {
			//소켓연결 직후 유저아이디 받는부분
			textArea.append("유저아이디 수신 : "+array[1]+"\n");
			textArea.setCaretPosition(textArea.getText().length());
			setNickname(array[1]);
			//새 유저가 추가될 경우 기존의 유저들의 리스트를 업데이트
			server.usersUpdateFriendList(array[1]);
		}
		else if(array[0].equals(SIGNAL_NOMAL_MSG)){//일반 채팅 메시지
			//textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
			/* str => SIGNAL_NOMAL_MSG//채팅방이름//[유저이름] 메시지~~~ */
			String roomName = array[1];
			str = array[2];
			textArea.append(roomName+" : "+str + "\n");
			textArea.setCaretPosition(textArea.getText().length());
			
			//서버에서 브로드캐스팅(유저 객체도 함께 넘겨줌)
			server.broad_cast(this,str,roomName); 
		}
		else if(array[0].equals(SIGNAL_ONLINE_USER_LIST)) {
			/*friendsList = server.getFriendList();
			String msg = array[0];
			
			for(User user : friendsList) {
				msg += "//" + user.getNickname();
			}
			textArea.append("userList : "+msg+ "\n");
			textArea.setCaretPosition(textArea.getText().length());
			send_Message(msg);*/
			friendsList = server.getFriendList();
			String msg = array[0]+"//";
			
			for(int i=0;i<friendsList.size();i++) {
				User user = friendsList.get(i);
				if(i==friendsList.size()-1) {//마지막 배열
					msg+=user.getNickname()+"!!"+user.getStateImage()+"!!"+user.getStateMessage();
				}
				else {
					msg+=user.getNickname()+"!!"+user.getStateImage()+"!!"+user.getStateMessage()+"//";
				}
			}
			send_Message(msg);
			textArea.append(msg+"\n");
			textArea.setCaretPosition(textArea.getText().length());
		}
		else if(array[0].equals(SIGNAL_CHANGE_STATE)) {
			//User.SIGNAL_CHANGE_STATE_MSG+"//"+user_id+"//"+stateImage+"//"+myStateMessage
			textArea.append(array[1]+"님 상태 변경 "+array[2]+" / "+array[3]+"\n");
			textArea.setCaretPosition(textArea.getText().length());
			this.stateImage = array[2];
			this.stateMessage = array[3];
			server.userInfoUpdate(array[1],array[2],array[3]);
		}
		else {
			System.out.println("지원하지 않는 메시지 유형");
		}
	}
	
	public void send_Message(String str) {
		try {
			//dos.writeUTF(str);
			byte[] bb;		
			bb = str.getBytes();
			dos.write(bb); //.writeUTF(str);
		} 
		catch (IOException e) {
			textArea.append("메시지 송신 에러 발생\n");	
			textArea.setCaretPosition(textArea.getText().length());
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				//사용자에게 받는 메세지(chatPanel에서 오는 메시지)
				//byte[] b = new byte[128];
				byte[] b = new byte[1024];
				dis.read(b);
				String msg = new String(b);
				msg = msg.trim();
				//String msg = dis.readUTF();
				InMessage(msg);
			} 
			catch (IOException e) 
			{
				try {
					dos.close();
					dis.close();
					user_socket.close();//자원반납
					//유저가 나간 사실을 매니저에게 알림
					//유저 리스트에서 삭제,서버에 프린트를 위임
					server.exitUser(this);
					break;
				
				} catch (Exception ee) {
				
				}// catch문 끝
			}// 바깥 catch문끝
		}
	}
	
	/*Getter Setter*/
	public ArrayList<ChattingRoom> getJoinRoomList() {
		return joinRoomList;
	}

	public void setJoinRoomList(ArrayList<ChattingRoom> joinRoomList) {
		this.joinRoomList = joinRoomList;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStateImage() {
		return stateImage;
	}

	public void setStateImage(String stateImage) {
		this.stateImage = stateImage;
	}

	public String getStateMessage() {
		return stateMessage;
	}

	public void setStateMessage(String stateMessage) {
		this.stateMessage = stateMessage;
	}
	
	
	
}
