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
	
	Socket socket = LoginPanel.socket;//사용자의 소켓을 받아옴
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
		//클라이언트가 서버로부터 메시지를 수신하는 스레드 생성후 실행
		//Singleton패턴이라 이부분이 한번만 실행됨=>한 유저당 스레드 하나
		RecieveMassage();
	}
	public static CommandController getController() {
		if(controller == null)
			controller = new CommandController();
		return controller;
	}
	public void append_Message(String roomName, String str) {//chatPanel의 TextArea에 글자 붙임
		
		setTextPane(chattingRoomList.get(roomName)); //룸이름을 key로 텍스트를 붙일 텍스트팬을 해쉬맵에서 찾아서 지정
		
		if(textPane==null) {
			//초대된 사람이라 채팅방에 입장은 했지만 화면이 안떴을때
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
	
	public void RecieveMassage() { // 스레드를 돌려서 서버로부터 메세지를 수신
		try { // 스트림 설정
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		} catch (IOException e) {
			//textArea.append("스트림 설정 에러!!\n");
			System.out.println("스트림 설정 에러!!\n");
		}
		Thread th = new Thread(new Runnable() { //서버에서 메시지 받는 스레드
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
						System.out.println("CommandControll -> 서버로부터의 메시지 : "+msg);
						String[] array = msg.split("//");
						
						//채팅방 생성 완료시->채팅방 이름을 전달받아 채팅방의 모든 인원 채팅방 띄우기(강제)
						if(array[0].equals(User.SIGNAL_CRATE_ROOM_COMPLETE)) {
							/*String chattingRoomName = array[1];//채팅방이름
							setRoomName(chattingRoomName);*/
							roomName = array[1];
							if(chattingRoomList.get(roomName)==null) { //처음 생성되는 방
								System.out.println("CommandControll -> roomName "+roomName);
								ChatFrame a = new ChatFrame(roomName); //처음 방이 생성된 경우 채팅방창을 띄움
								chattingRoomList.put(roomName, a.getChatPanel().getTextPaneChat());	//채팅방들의 TextPane을 해쉬맵으로 저장
								
								///////////////////////////////////////////////////////////////////////////////////
								JLabel room = new JLabel(roomName);
								ChatRoom.add(room); //처음 방이 생성되면 chatting목록에 추가함
								for(JLabel j:ChatRoom) {
									System.out.println("채팅방 리스트 추가 -> " + j.getText().toString());
								}
							}
							else {
								//이미 있는 방이라고 알림창 띄우기
								
							}
							
						}
						else if(array[0].equals(User.SIGNAL_NOMAL_MSG)) {
							//String message = User.SIGNAL_NOMAL_MSG+"//"+roomName+"//"+str;
							roomName= array[1];
							String str = array[2];
							append_Message(roomName,str + "\n");
						}
						else if(array[0].equals(User.SIGNAL_ONLINE_USER_LIST)) {
							//msg =  Signal//유저이름!!상태이미지!!상태메시지//유저이름!!상태이미지!!상태메시지....
							userLabel.clear();
							onlineUserList.clear(); //접속중인 유저 리스트 초기화
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
							//새로운 유저가 추가 array[1]:갱신 대상 유저 이름 array[2]:새 유저이름 
							//기존 유저들의 frendList에 새로운 유저를 추가(갱신X)
							System.out.println("CommandController->SIGNAL_NEW_USER_CONNECT userName="+array[2]);
							//friendPanel의 dataSetting을 호출해야함
							mainFrameList.get(array[1]).getStartPanel().friendPanel.update(array[2]);
						}
						else if(array[0].equals(User.SIGNAL_EXIST_USER_CONNECT)) {
							/*SIGNAL_EXIST_USER_CONNECT+"//"+existingRooms.get(i).roomName+"//"+existingRooms.get(i).chat*/
							JLabel room = new JLabel(array[1]);
							ChatRoom.add(room);
							
							JTextPane temp = new JTextPane();
							temp.setText(array[2]);
							chattingRoomList.put(array[1], temp);	//채팅방들의 TextPane을 해쉬맵으로 저장
							
							
							//=========================================================================
						}
						else if(array[0].equals(User.SIGNAL_CHANGE_STATE)) {
							//유저의 상태이미지나 상태메시지 변경이 있다면 모든 유저들의 화면을 갱신
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
							System.out.println("CommandController-> 지원하지 않는 메시지 형식입니다.");
						}
		
						
					} catch (IOException e) {
						//textArea.append("메세지 수신 에러!!\n");
						append_Message(roomName,"메세지 수신 에러!!\n");
						// 서버와 소켓 통신에 문제가 생겼을 경우 소켓을 닫는다
						try {
							os.close();
							is.close();
							dos.close();
							dis.close();
							socket.close();
							break; // 에러 발생하면 while문 종료
						} catch (IOException e1) {
						}
					}
				} // while문 끝
			}// run메소드 끝
		});
		th.start();
	}

	public void send_Message(String str) { // 서버로 메세지를 보내는 메소드
		try {
			byte[] bb;
			bb = str.getBytes();
			dos.write(bb); //.writeUTF(str);
		} catch (IOException e) {
			//textArea.append("메세지 송신 에러!!\n");
			append_Message(roomName,"메세지 송신 에러!!\n");//여기의 roomName은 의미가 없음(추후에 더 보완!!)
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
