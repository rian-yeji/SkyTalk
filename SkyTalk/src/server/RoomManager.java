package server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTextArea;

public class RoomManager {

	private static ArrayList<ChattingRoom> roomList;
	private static HashMap<String,ArrayList<ChattingRoom>> backupRoomList;
	Server server;
	private static JTextArea textArea = Server.textArea;
	
	
	public RoomManager(Server server) {
		roomList = new ArrayList<ChattingRoom>();
		backupRoomList = new HashMap<String,ArrayList<ChattingRoom>>();
		this.server = server;
	}
	
	public void broadcast(User user,String str, String roomName) {
		//유저가 속한 룸을 찾아서 브로드캐스팅 명령
		ChattingRoom room = serchByRoomName(roomName);//룸이름으로 브로드캐스팅할 룸을 찾음
		room.broadcast(str);
	}
	
	public ChattingRoom serchByRoomName(String roomName) {
		//채팅방 이름으로 방찾기
		for(int i=0;i<roomList.size();i++) {
			if(roomList.get(i).getRoomName().equals(roomName))
				return roomList.get(i);
		}
		return null;//없으면 null
	}
/*	public ChattingRoom createRoom() {//빈방 생성
		ChattingRoom room = new ChattingRoom();
		roomList.add(room);
		printInServerTextArea(1);
		return room;
	}
	
	public ChattingRoom createRoom(User user) {//유저가 방을 생성
		ChattingRoom room = new ChattingRoom(user);
		roomList.add(room);
		printInServerTextArea(1);
		return room;
	}*/
	
	public ChattingRoom createRoom(ArrayList<User> users) {//유저가 방을 생성
		ChattingRoom room = new ChattingRoom(users);
		roomList.add(room);
		textArea.append("Room Create! Recent Number of Room : " + roomList.size() + "\n");
		textArea.append("Number of Person in the Room : " + room.getUserCount() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
		return room;
	}
	
	public static void removeRoom(ChattingRoom room) {
		roomList.remove(room);
		textArea.append("Room Delete! Recent Number of Room : " + roomList.size() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	public int roomCount() {
		return roomList.size();
	}
	
	public void exitUser(User user) {
		ArrayList<ChattingRoom> backup = new ArrayList<ChattingRoom>();
		for(int i=0;i<roomList.size();i++) {
			if(roomList.get(i).isUserJoin(user)==true) {
				roomList.get(i).ExitRoom(user);
				backup.add(roomList.get(i));
			}
		}
		backupRoomList.put(user.getNickname(), backup);
		System.out.println("Backup Room : "+user.getNickname()+"-> roomList "+backupRoomList.get(user.getNickname()).size());
	}

	public ArrayList<ChattingRoom> getJoinRooms(String userName) {
		return backupRoomList.get(userName);
	}
	
}
