package command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import chatting.ChatFrame;
import chatting.LoginPanel;
import chatting.MainFrame;
import chatting.StartPanel;
import chatting.chatPanel;
import chatting.friendPanel;
import info.UserInfo;
import server.User;

public class CommandController {
	
	Socket socket = LoginPanel.socket;//������� ������ �޾ƿ�
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private HashMap<String, JTextPane> chattingRoomList = new HashMap<String, JTextPane>();//key = roomName,value = textPane
	private HashMap<String, MainFrame> mainFrameList = new HashMap<String, MainFrame>();//key = userName,value = StartPanel
	private JTextPane textPane;
	private String roomName;
	private List<JLabel> ChatRoom = new ArrayList<JLabel>();
	private List<JLabel> userLabel = new ArrayList<JLabel>();
	private ArrayList<UserInfo> onlineUserList = new ArrayList<UserInfo>();
	
	public static CommandController controller;
	private CommandController(){
		//Ŭ���̾�Ʈ�� �����κ��� �޽����� �����ϴ� ������ ������ ����
		//Singleton�����̶� �̺κ��� �ѹ��� �����=>�� ������ ������ �ϳ�
		RecieveMassage();
	}
	public static CommandController getController() {
		if(controller == null)
			controller = new CommandController();
		return controller;
	}
	public void append_Message(String roomName, String str) {//chatPanel�� TextArea�� ���� ����
		
		setTextPane(chattingRoomList.get(roomName)); //���̸��� key�� �ؽ�Ʈ�� ���� �ؽ�Ʈ���� �ؽ��ʿ��� ã�Ƽ� ����
		
		if(textPane==null) {
			//�ʴ�� ����̶� ä�ù濡 ������ ������ ȭ���� �ȶ�����
			System.out.println("Commandcontroller -> textPane==null");
			//ChatFrame a = new ChatFrame(friendName);
			
		}
		else {
			/*
			//append_Icon(icon1);
			int len = textPane.getDocument().getLength(); // same value as
			
			textPane.setCaretPosition(len); // place caret at the end (with no selection)
			textPane.replaceSelection(str); // there is no selection, so inserts at caret
			//System.out.println("len = "+len+"\nstr = "+str);
			*/
			
			String temp = textPane.getText()+"\n"+str;
			textPane.setText(temp);
			
			int len = textPane.getDocument().getLength(); // same value as
			textPane.setCaretPosition(len); // place caret at the end (with no selection)
			
		}
			
	}
	
	public void append_Icon(ImageIcon icon) {
		int len = textPane.getDocument().getLength();
		textPane.setCaretPosition(len); // place caret at the end (with no selection)
		textPane.insertIcon(icon);
	}
	
	public void RecieveMassage() { // �����带 ������ �����κ��� �޼����� ����
		try { // ��Ʈ�� ����
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		} catch (IOException e) {
			//textArea.append("��Ʈ�� ���� ����!!\n");
			System.out.println("��Ʈ�� ���� ����!!\n");
		}
		Thread th = new Thread(new Runnable() { //�������� �޽��� �޴� ������
			@SuppressWarnings("null")
			@Override
			public void run() {
				while (true) {
					try {
						//byte[] b = new byte[128];
						byte[] b = new byte[1024];
						dis.read(b);
						String msg = new String(b);
						msg = msg.trim();
						System.out.println("CommandControll -> �����κ����� �޽��� : "+msg);
						String[] array = msg.split("//");
						
						//ä�ù� ���� �Ϸ��->ä�ù� �̸��� ���޹޾� ä�ù��� ��� �ο� ä�ù� ����(����)
						if(array[0].equals(User.SIGNAL_CRATE_ROOM_COMPLETE)) {
							/*String chattingRoomName = array[1];//ä�ù��̸�
							setRoomName(chattingRoomName);*/
							roomName = array[1];
							if(chattingRoomList.get(roomName)==null) { //ó�� �����Ǵ� ��
								System.out.println("CommandControll -> roomName "+roomName);
								ChatFrame a = new ChatFrame(roomName); //ó�� ���� ������ ��� ä�ù�â�� ���
								chattingRoomList.put(roomName, a.getChatPanel().getTextPaneChat());	//ä�ù���� TextPane�� �ؽ������� ����
								
								///////////////////////////////////////////////////////////////////////////////////
								JLabel room = new JLabel(roomName);
								ChatRoom.add(room); //ó�� ���� �����Ǹ� chatting��Ͽ� �߰���
								for(JLabel j:ChatRoom) {
									System.out.println("ä�ù� ����Ʈ �߰� -> " + j.getText().toString());
								}
							}
							else {
								//�̹� �ִ� ���̶�� �˸�â ����
								
							}
							
						}
						else if(array[0].equals(User.SIGNAL_NOMAL_MSG)) {
							//String message = User.SIGNAL_NOMAL_MSG+"//"+roomName+"//"+str;
							roomName= array[1];
							String str = array[2];
							append_Message(roomName,str + "\n");
						}
						else if(array[0].equals(User.SIGNAL_ONLINE_USER_LIST)) {
							//msg =  Signal//�����̸�!!�����̹���!!���¸޽���//�����̸�!!�����̹���!!���¸޽���....
							userLabel.clear();
							onlineUserList.clear(); //�������� ���� ����Ʈ �ʱ�ȭ
							for(int i=1; i<array.length;i++) {
								String[] userInformation = array[i].split("!!");
								
								userLabel.add(new JLabel(userInformation[0]));
								//-------------------------------------------
								UserInfo user = new UserInfo();
								user.setNickname(userInformation[0]);
								user.setStateImg(userInformation[1]);
								user.setStateMsg(userInformation[2]);
								System.out.println("CommandController->"+userInformation[0]+userInformation[1]+userInformation[2]);
								onlineUserList.add(user);
							}
						}
						else if(array[0].equals(User.SIGNAL_NEW_USER_CONNECT)) {
							//���ο� ������ �߰� array[1]:���� ��� ���� �̸� array[2]:�� �����̸� 
							//���� �������� frendList�� ���ο� ������ �߰�(����X)
							System.out.println("CommandController->SIGNAL_NEW_USER_CONNECT userName="+array[2]);
							//friendPanel�� dataSetting�� ȣ���ؾ���
							mainFrameList.get(array[1]).getStartPanel().friendPanel.update(array[2]);
						}
						else if(array[0].equals(User.SIGNAL_EXIST_USER_CONNECT)) {
							/*SIGNAL_EXIST_USER_CONNECT+"//"+existingRooms.get(i).roomName+"//"+existingRooms.get(i).chat*/
							JLabel room = new JLabel(array[1]);
							ChatRoom.add(room);
							
							JTextPane temp = new JTextPane();
							temp.setText(array[2]);
							chattingRoomList.put(array[1], temp);	//ä�ù���� TextPane�� �ؽ������� ����
							
							
							//=========================================================================
						}
						else if(array[0].equals(User.SIGNAL_CHANGE_STATE)) {
							//������ �����̹����� ���¸޽��� ������ �ִٸ� ��� �������� ȭ���� ����
							/*User.SIGNAL_CHANGE_STATE+"//"+user.getNickname()+"//"+userName+"//"+stateImg+"//"+stateMsg*/
							UserInfo user = searchByUserName(array[2]);
							onlineUserList.remove(user);
							UserInfo reUser = new UserInfo(array[2]);
							reUser.setStateImg(array[3]);
							reUser.setStateMsg(array[4]);
							onlineUserList.add(reUser);
							System.out.println("CommandController->SIGNAL_CHANGE_STATE "+searchByUserName(array[2]).getNickname()+searchByUserName(array[2]).getStateImg()+searchByUserName(array[2]).getStateMsg());
							mainFrameList.get(array[1]).getStartPanel().friendPanel.updateState(array[2],array[3],array[4]);
							
						}
						else {
							System.out.println("CommandController-> �������� �ʴ� �޽��� �����Դϴ�.");
						}
		
						
					} catch (IOException e) {
						//textArea.append("�޼��� ���� ����!!\n");
						append_Message(roomName,"�޼��� ���� ����!!\n");
						// ������ ���� ��ſ� ������ ������ ��� ������ �ݴ´�
						try {
							os.close();
							is.close();
							dos.close();
							dis.close();
							socket.close();
							break; // ���� �߻��ϸ� while�� ����
						} catch (IOException e1) {
						}
					}
				} // while�� ��
			}// run�޼ҵ� ��
		});
		th.start();
	}

	public void send_Message(String str) { // ������ �޼����� ������ �޼ҵ�
		try {
			byte[] bb;
			bb = str.getBytes();
			dos.write(bb); //.writeUTF(str);
		} catch (IOException e) {
			//textArea.append("�޼��� �۽� ����!!\n");
			append_Message(roomName,"�޼��� �۽� ����!!\n");//������ roomName�� �ǹ̰� ����(���Ŀ� �� ����!!)
		}
	}

	public void saveMainFrame(String userId,MainFrame mainFrame) {
		mainFrameList.put(userId, mainFrame);
	}
	
	public UserInfo searchByUserName(String userName) {
		for(int i=0;i<onlineUserList.size();i++) {
			if(onlineUserList.get(i).getNickname().equals(userName))
				return onlineUserList.get(i);
		}
		return null;
	}
	/*Getter Setter*/
	public String getRoomName() {
		return roomName;
	}
	public JTextPane getTextPane() {
		System.out.println(textPane.getText().toString());
		return textPane;
	}
	public void setTextPane(JTextPane textPane) {
		this.textPane = textPane;
	}
	
	public List<JLabel> getChatLabel() {
		return ChatRoom; 
	}
	public List<JLabel> getUserLabel() {
		return userLabel;
	}
	public void setUserLabel(List<JLabel> userLabel) {
		this.userLabel = userLabel;
	}
	public HashMap<String, JTextPane> getChattingRoomList() {
		return chattingRoomList;
	}
	public void setChattingRoomList(HashMap<String, JTextPane> chattingRoomList) {
		this.chattingRoomList = chattingRoomList;
	}
	public HashMap<String, MainFrame> getMainFrameList() {
		return mainFrameList;
	}
	public void setMainFrameList(HashMap<String, MainFrame> mainFrameList) {
		this.mainFrameList = mainFrameList;
	}
	public ArrayList<UserInfo> getOnlineUserList() {
		return onlineUserList;
	}
	public void setOnlineUserList(ArrayList<UserInfo> onlineUserList) {
		this.onlineUserList = onlineUserList;
	}
	
	

}
