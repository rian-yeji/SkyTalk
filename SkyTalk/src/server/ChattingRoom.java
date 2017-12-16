package server;

import java.util.ArrayList;

public class ChattingRoom {
	private ArrayList<User> userList;
	private User roomOwner; //����
	String roomName; //���̸�
	String chat="\n";//ä�ó���
	
	public ChattingRoom() {
		userList = new ArrayList<User>();
	}
	
	public ChattingRoom(User user) { //������ ���� ���� ��
		userList = new ArrayList<User>();
		userList.add(user);
		this.roomOwner = user;
		roomName = "NewChatting";
	}
	
	public ChattingRoom(ArrayList<User> users) { //�������� ������ ���� ���� ��
		this.userList = users;
		this.roomOwner = users.get(0); //ù��° ������ ��������
		roomName = "NewChatting";
	}
	
	public void EnterRoom(User newUser) {
		userList.add(newUser);
	}
	
	public boolean isUserJoin(User user) { //ã������ ������ �� �濡 �������̶�� true,�ƴϸ� false
		for(int i=0;i<userList.size();i++)
			if(userList.get(i).equals(user))
				return true;
		
		return false;
	}
	public void ExitRoom(User user) {
		userList.remove(user);
		if(user==roomOwner) {//������ ������� �ٽ� ù��° ������� ��������
			roomOwner = userList.get(0);
			System.out.println("ChattingRoom-> Owner User Exit, Owner Change");
		}
		
		if(userList.size()<1) {//��� �ο��� �� ���� �����ٸ� ���� ����
			RoomManager.removeRoom(this);
			System.out.println("ChattingRoom-> All User Exit, Room Delete");
			return;
		}
	}
	
	//����� ��� �����鿡�� ��ε�ĳ����
	public void broadcast(String str) {
		String message = User.SIGNAL_NOMAL_MSG+"//"+roomName+"//"+str;
		chat += str+"\n"+"\n";
		for (int i = 0; i < userList.size(); i++) {
			User user = userList.get(i);
			user.send_Message(message);
		}
	}

	public int getUserCount() {
		return userList.size();
	}
	public ArrayList<User> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}

	public User getRoomOwner() {
		return roomOwner;
	}

	public void setRoomOwner(User roomOwner) {
		this.roomOwner = roomOwner;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	
}
