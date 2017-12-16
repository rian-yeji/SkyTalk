package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTextArea;

import server.Server;

public class ClientManager {
	Server server;

	protected static ArrayList<User> userList;
	protected static HashMap<String,User> backupUserList;
	
/*	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;*/
	
	private static JTextArea textArea = Server.textArea;

	public ClientManager(Server server) {
		userList = new ArrayList<User>();
		backupUserList = new HashMap<String,User>();
		this.server = server;
	}
	
	public User searchByUserNameInOnline(String userName) {
		for(int i=0;i<userList.size();i++) {
			if(userList.get(i).getNickname().equals(userName))
				return userList.get(i);
		}
		return null;
	}
	
	public User searchByUserNameInExist(String userName) {
		return backupUserList.get(userName);
	}
	
	public void insertUser(Socket socket) {
		User user = new User(socket,server);
		userList.add(user);
		textArea.append(userList.size() +" : ���� ���Ϳ� ����� ����� ��\n");
		textArea.append("����� ���ӿϷ�\n");
		user.start();//���� ���� ������ ������ ����
	}
	
	public void exitUser(User user) {
		userList.remove(user); // ��������(exit��) ���� ��ü�� ���Ϳ��� �����
		backupUserList.put(user.getNickname(),user);
		textArea.append(userList.size() +" : ���� ���Ϳ� ����� ����� ��\n");
		textArea.append("����� ���� ������ �ڿ� �ݳ�\n");
		textArea.setCaretPosition(textArea.getText().length());
		System.out.println("Backup UserInfo : "+user.getNickname()+"/"+backupUserList.get(user.getNickname()).getStateImage()+"/"+backupUserList.get(user.getNickname()).getStateMessage());
	}
	
	public void usersUpdateFriendList(String newUser) {
		textArea.append("���ο� ����" + newUser + "�� �����ϰ� ������Ʈ\n");
		textArea.setCaretPosition(textArea.getText().length());
		for (int i = 0; i < userList.size(); i++) {
			User user = userList.get(i);
			if (!user.getNickname().equals(newUser)) { // �������� ����
				user.send_Message(User.SIGNAL_NEW_USER_CONNECT +"//"+user.getNickname()+ "//" + newUser);
				textArea.append(user.getNickname() + "���� ģ����� ������Ʈ\n");
				textArea.setCaretPosition(textArea.getText().length());
			}
		}
	}
	
/*	public void usersUpdateFriendList(String newUser, String stateImg, String stateMsg) {
		textArea.append("���� ���� �ٽ� ���� : " + newUser + "\n");
		textArea.setCaretPosition(textArea.getText().length());
		
		for (int i = 0; i < userList.size(); i++) {
			User user = userList.get(i);
			if (!user.getNickname().equals(newUser)) { // �������� ����
				user.send_Message(User.SIGNAL_EXIST_USER_CONNECT +"//"+user.getNickname()+ "//" + newUser+"//"+stateImg+"//"+stateMsg);
				textArea.append(user.getNickname() + "���� ģ����� ������Ʈ\n");
				textArea.setCaretPosition(textArea.getText().length());
			}
		}
	}*/

	//������ �����̹����� ���¸޽��� ������ �ִٸ� ��� �������� ȭ���� ����
	public void userInfoUpdate(String userName,String stateImg,String stateMsg) {
		//�ش� ������ ������ �ٲٰ�
		User chageUser = searchByUserNameInOnline(userName);
		chageUser.setStateImage(stateImg);
		chageUser.setStateMessage(stateMsg);
		textArea.append(searchByUserNameInOnline(userName).getNickname() + "���� ���� ������Ʈ\n");
		textArea.append(searchByUserNameInOnline(userName).getStateImage()+"/"+searchByUserNameInOnline(userName).getStateMessage()+ "\n");
		textArea.setCaretPosition(textArea.getText().length());
		//��� ������ ȭ�� ���� ���
		for (int i = 0; i < userList.size(); i++) {
			User user = userList.get(i);
			user.send_Message(User.SIGNAL_CHANGE_STATE+"//"+user.getNickname()+"//"+userName+"//"+stateImg+"//"+stateMsg);
		}
	}
	
	public static ArrayList<User> getUserList() {
		return userList;
	}

	public static void setUserList(ArrayList<User> userList) {
		ClientManager.userList = userList;
	}

	

	
	
	
}
