package server;

import java.util.ArrayList;

public class ChattingRoom {
	private ArrayList<User> userList;
	private User roomOwner; //방장
	String roomName; //방이름
	String chat="\n";//채팅내용
	
	public ChattingRoom() {
		userList = new ArrayList<User>();
	}
	
	public ChattingRoom(User user) { //유저가 방을 만들 때
		userList = new ArrayList<User>();
		userList.add(user);
		this.roomOwner = user;
		roomName = "NewChatting";
	}
	
	public ChattingRoom(ArrayList<User> users) { //여러명의 유저가 방을 만들 때
		this.userList = users;
		this.roomOwner = users.get(0); //첫번째 유저를 방장으로
		roomName = "NewChatting";
	}
	
	public void EnterRoom(User newUser) {
		userList.add(newUser);
	}
	
	public boolean isUserJoin(User user) { //찾으려는 유저가 이 방에 참여중이라면 true,아니면 false
		for(int i=0;i<userList.size();i++)
			if(userList.get(i).equals(user))
				return true;
		
		return false;
	}
	public void ExitRoom(User user) {
		userList.remove(user);
		if(user==roomOwner) {//방장이 나갈경우 다시 첫번째 사람으로 방장지정
			roomOwner = userList.get(0);
			System.out.println("ChattingRoom-> Owner User Exit, Owner Change");
		}
		
		if(userList.size()<1) {//모든 인원이 다 방을 나갔다면 방을 제거
			RoomManager.removeRoom(this);
			System.out.println("ChattingRoom-> All User Exit, Room Delete");
			return;
		}
	}
	
	//방안의 모든 유저들에게 브로드캐스팅
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
