package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JTextArea;

import server.Server;

public class ClientManager {
	Server server;

	protected static ArrayList<User> userList;
	
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private static JTextArea textArea = Server.textArea;

	public ClientManager(Server server) {
		userList = new ArrayList<User>();
		this.server = server;
	}
	
	public User searchByUserName(String userName) {
		for(int i=0;i<userList.size();i++) {
			if(userList.get(i).getNickname().equals(userName))
				return userList.get(i);
		}
		return null;
	}
	
	public void insertUser(Socket socket) {
		User user = new User(socket,server);
		userList.add(user);
		textArea.append(userList.size() +" : 현재 벡터에 담겨진 사용자 수\n");
		textArea.append("사용자 접속완료\n");
		user.start();//새로 들어온 유저의 스레드 실행
	}
	
	public void exitUser(User user) {
		userList.remove(user); // 에러가난(exit한) 현재 객체를 벡터에서 지운다
		textArea.append(userList.size() +" : 현재 벡터에 담겨진 사용자 수\n");
		textArea.append("사용자 접속 끊어짐 자원 반납\n");
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	public void usersUpdateFriendList(String newUser) {
		
/*		for(int i=0;i<userList.size();i++) {
			User user = userList.get(i);
			user.send_Message(User.SIGNAL_UPDATE_FRIENDS_LIST+"//"+user.getNickname());
		}*/
		textArea.append("새로운 유저" + newUser + "를 제외하고 업데이트\n");
		textArea.setCaretPosition(textArea.getText().length());
		for (int i = 0; i < userList.size(); i++) {
			User user = userList.get(i);
			if (!user.getNickname().equals(newUser)) { // 새유저는 제외
				user.send_Message(User.SIGNAL_NEW_USER_CONNECT +"//"+user.getNickname()+ "//" + newUser);
				textArea.append(user.getNickname() + "님의 친구목록 업데이트\n");
				textArea.setCaretPosition(textArea.getText().length());
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	//유저의 상태이미지나 상태메시지 변경이 있다면 모든 유저들의 화면을 갱신
	public void userInfoUpdate(String userName,String stateImg,String stateMsg) {
		//해당 유저의 정보를 바꾸고
		User chageUser = searchByUserName(userName);
		chageUser.setStateImage(stateImg);
		chageUser.setStateMessage(stateMsg);
		System.out.println("check");
		textArea.append(searchByUserName(userName).getNickname() + "님의 정보 업데이트\n");
		textArea.append(searchByUserName(userName).getStateImage()+"/"+searchByUserName(userName).getStateMessage()+ "\n");
		textArea.setCaretPosition(textArea.getText().length());
		//모든 유저들 화면 갱신 명령
		for (int i = 0; i < userList.size(); i++) {
			User user = userList.get(i);
			user.send_Message(User.SIGNAL_CHANGE_STATE+"//"+user.getNickname()+"//"+userName+"//"+stateImg+"//"+stateMsg);
			/*try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}
	public static ArrayList<User> getUserList() {
		return userList;
	}

	public static void setUserList(ArrayList<User> userList) {
		ClientManager.userList = userList;
	}

	

	
	
	
}
