package server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTextArea;

public class RoomManager {

	private static ArrayList<ChattingRoom> roomList;
	Server server;
	private static JTextArea textArea = Server.textArea;
	public HashMap<String,ChattingRoom[]> tempRoom = new HashMap<String,ChattingRoom[]>();
	
	
	public RoomManager(Server server) {
		roomList = new ArrayList<ChattingRoom>();
		this.server = server;
	}
	
	public void broadcast(User user,String str, String roomName) {
		//������ ���� ���� ã�Ƽ� ��ε�ĳ���� ���
		ChattingRoom room = serchByRoomName(roomName);//���̸����� ��ε�ĳ������ ���� ã��
		room.broadcast(str);
	}
	
	public ChattingRoom serchByRoomName(String roomName) {
		//ä�ù� �̸����� ��ã��
		for(int i=0;i<roomList.size();i++) {
			if(roomList.get(i).getRoomName().equals(roomName))
				return roomList.get(i);
		}
		return null;//������ null
	}
/*	public ChattingRoom createRoom() {//��� ����
		ChattingRoom room = new ChattingRoom();
		roomList.add(room);
		printInServerTextArea(1);
		return room;
	}
	
	public ChattingRoom createRoom(User user) {//������ ���� ����
		ChattingRoom room = new ChattingRoom(user);
		roomList.add(room);
		printInServerTextArea(1);
		return room;
	}*/
	
	public ChattingRoom createRoom(ArrayList<User> users) {//������ ���� ����
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
		ArrayList<ChattingRoom> temp = new ArrayList<ChattingRoom>();
		for(int i=0;i<roomList.size();i++) {
			if(roomList.get(i).isUserJoin(user)==true) {
				roomList.get(i).ExitRoom(user);
				temp.add(roomList.get(i));
			}
		}
	}
	
}
