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
	
	//������ �����ִ� ���
	ArrayList<ChattingRoom> joinRoomList = new ArrayList<ChattingRoom>();
	ArrayList<User> friendsList = new ArrayList<User>();//���� �Ⱦ�
	ArrayList<String> userName = new ArrayList<String>();
	ArrayList<String> multiRoomUserList;
	
	Socket socket;
	String nickname,password,stateImage,stateMessage;
	JTextArea textArea = Server.textArea;
	
	public User(Socket socket,Server server) {
		nickname = "newUser";
		password="1111";
		stateImage = "happy.png";
		stateMessage = "���� �޼���";
		user_socket = socket;
		this.server = server;
		network();
	}
	
	public void EnterRoom(ChattingRoom room) {
		room.EnterRoom(this);//�뿡 ����(����)
		this.joinRoomList.add(room);//������ ���� ���� �߰�
	}
	
	public void addFreind(User user) { //ģ���߰�
		friendsList.add(user);
	}
	
	public void network() {
		try {
			is = user_socket.getInputStream();
			dis = new DataInputStream(is);
			os = user_socket.getOutputStream();
			dos = new DataOutputStream(os);
			/*//Nickname = dis.readUTF(); // ������� �г��� �޴ºκ�
			byte[] b=new byte[128];
			dis.read(b);
			String Nickname = new String(b);
			Nickname = Nickname.trim();*/
			
			textArea.append("ID " + nickname + " ����\n");
			textArea.setCaretPosition(textArea.getText().length());	
			
			//send_Message(nickname + "�� ȯ���մϴ�."); // ����� ����ڿ��� ���������� �˸�
		} catch (Exception e) {
			textArea.append("��Ʈ�� ���� ����\n");
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
	
	/*�޽��� ó�� �κ�*/
	public void InMessage(String str) {// ����� �޼��� ó��
		String[] array = str.split("//");
		if(array[0].equals(SIGNAL_CREATE_SINGLEROOM)) { //freindPanel�κ��� ����� ������û
			//SIGNAL_CREATE_SINGLEROOM//myName//friendName
			String myName = array[1];
			String friendName = array[2];
			String roomName = friendName+"�԰��� ä�ù�"; //�⺻ ���̸�

			textArea.append(myName+"����"+friendName+"�԰��� ä�ù� ������ ��û�߽��ϴ�.\n");
			textArea.setCaretPosition(textArea.getText().length());
			
			server.createSingleRoom(this,myName, friendName);
		}
		else if(array[0].equals(SIGNAL_CREATE_MULTIROOM)) {//�����
			multiRoomUserList = new ArrayList<String>();
			String msg="";
			for(int i=1;i<array.length;i++) {
				multiRoomUserList.add(array[i]);
				if(i==array.length)
					msg+=array[i];
				else
					msg+=array[i]+",";
			}
			textArea.append(msg+" ä�ù� ������ ��û�߽��ϴ�.\n");
			textArea.setCaretPosition(textArea.getText().length());
			server.createMultiRoom(multiRoomUserList);
		}
		else if(array[0].equals(SIGNAL_USER_ID)) {
			//���Ͽ��� ���� �������̵� �޴ºκ�
			textArea.append("�������̵� ���� : "+array[1]+"\n");
			textArea.setCaretPosition(textArea.getText().length());
			setNickname(array[1]);
			//�� ������ �߰��� ��� ������ �������� ����Ʈ�� ������Ʈ
			server.usersUpdateFriendList(array[1]);
		}
		else if(array[0].equals(SIGNAL_NOMAL_MSG)){//�Ϲ� ä�� �޽���
			//textArea.append("����ڷκ��� ���� �޼��� : " + str+"\n");
			/* str => SIGNAL_NOMAL_MSG//ä�ù��̸�//[�����̸�] �޽���~~~ */
			String roomName = array[1];
			str = array[2];
			textArea.append(roomName+" : "+str + "\n");
			textArea.setCaretPosition(textArea.getText().length());
			
			//�������� ��ε�ĳ����(���� ��ü�� �Բ� �Ѱ���)
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
				if(i==friendsList.size()-1) {//������ �迭
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
			textArea.append(array[1]+"�� ���� ���� "+array[2]+" / "+array[3]+"\n");
			textArea.setCaretPosition(textArea.getText().length());
			this.stateImage = array[2];
			this.stateMessage = array[3];
			server.userInfoUpdate(array[1],array[2],array[3]);
		}
		else {
			System.out.println("�������� �ʴ� �޽��� ����");
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
			textArea.append("�޽��� �۽� ���� �߻�\n");	
			textArea.setCaretPosition(textArea.getText().length());
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				//����ڿ��� �޴� �޼���(chatPanel���� ���� �޽���)
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
					user_socket.close();//�ڿ��ݳ�
					//������ ���� ����� �Ŵ������� �˸�
					//���� ����Ʈ���� ����,������ ����Ʈ�� ����
					server.exitUser(this);
					break;
				
				} catch (Exception ee) {
				
				}// catch�� ��
			}// �ٱ� catch����
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
