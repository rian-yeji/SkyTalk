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
		textArea.append(userList.size() +" : ���� ���Ϳ� ����� ����� ��\n");
		textArea.append("����� ���ӿϷ�\n");
		user.start();//���� ���� ������ ������ ����
	}
	
	public void exitUser(User user) {
		userList.remove(user); // ��������(exit��) ���� ��ü�� ���Ϳ��� �����
		textArea.append(userList.size() +" : ���� ���Ϳ� ����� ����� ��\n");
		textArea.append("����� ���� ������ �ڿ� �ݳ�\n");
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	public void usersUpdateFriendList(String newUser) {
		
/*		for(int i=0;i<userList.size();i++) {
			User user = userList.get(i);
			user.send_Message(User.SIGNAL_UPDATE_FRIENDS_LIST+"//"+user.getNickname());
		}*/
		textArea.append("���ο� ����" + newUser + "�� �����ϰ� ������Ʈ\n");
		textArea.setCaretPosition(textArea.getText().length());
		for (int i = 0; i < userList.size(); i++) {
			User user = userList.get(i);
			if (!user.getNickname().equals(newUser)) { // �������� ����
				user.send_Message(User.SIGNAL_NEW_USER_CONNECT +"//"+user.getNickname()+ "//" + newUser);
				textArea.append(user.getNickname() + "���� ģ����� ������Ʈ\n");
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

	//������ �����̹����� ���¸޽��� ������ �ִٸ� ��� �������� ȭ���� ����
	public void userInfoUpdate(String userName,String stateImg,String stateMsg) {
		//�ش� ������ ������ �ٲٰ�
		User chageUser = searchByUserName(userName);
		chageUser.setStateImage(stateImg);
		chageUser.setStateMessage(stateMsg);
		System.out.println("check");
		textArea.append(searchByUserName(userName).getNickname() + "���� ���� ������Ʈ\n");
		textArea.append(searchByUserName(userName).getStateImage()+"/"+searchByUserName(userName).getStateMessage()+ "\n");
		textArea.setCaretPosition(textArea.getText().length());
		//��� ������ ȭ�� ���� ���
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
