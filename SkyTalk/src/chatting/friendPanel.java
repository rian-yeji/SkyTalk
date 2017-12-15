package chatting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import command.CommandController;
import info.UserInfo;
import server.User;

public class friendPanel extends JPanel {

	private MainFrame f;
	int i;

	public ArrayList<UserInfo> userList;
	public ArrayList<UserInfo> friendList;
	//private Vector<String> friendVector = new Vector<String>();
	public List<JLabel> friendLabel = new ArrayList<JLabel>();
	public JLabel Myprofile;
	public List<JLabel> friendImg = new ArrayList<JLabel>();
	public JLabel MyImg;
	public TextField searchFriend;
	public JScrollPane friendScroll;
	public JButton createRoomBtn;
	
	public JButton testBtn;
	
	public String myStateMessage;
	
	public List<JLabel> stateLabel = new ArrayList<JLabel>();
	public JLabel myStateLabel;

	final String defaultImg = "src/friendListImg/happy.png";
	private String myStateImage = "happy.png";
	public CommandController controller = CommandController.getController();
	
	String user_id;

	public friendPanel(MainFrame f, String user_id) {

		this.user_id = user_id;
		getAutoscrolls();
		this.f = f;

		setLayout(null);
		setSize(600, 850);
		setBackground(new Color(168, 218, 255));
		
		//유저 정보 받아와서 라벨생성,붙이기
		dataSetting();
		
		createRoomBtn = new JButton("채팅방 만들기");
		createRoomBtn.setBounds(0, 1, 600, 40);
		createRoomBtn.setBackground(new Color(255,225,231));
		add(createRoomBtn);
		
		createRoomBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AddRoomFrame rf = new AddRoomFrame(user_id);
				
			}
		});

	}
	
	//새 유저가 접속해서 화면갱신
	public void update(String userName) {
		UserInfo newUser = new UserInfo(userName);
		friendList.add(newUser);
		setFriendList(friendList);
		repaint();
		System.out.println("friendPanel-> update 새 유저"+newUser.getNickname());
		System.out.println("friendPanel-> update 친구 수"+friendList.size());
		
	}
	
	//유저가 상태를 변경해서 화면갱신
	public void updateState(String userName,String stateImag,String stateMsg) {
		for(int i=0;i<userList.size();i++) {
			UserInfo user = userList.get(i);
			if(user.getNickname().equals(userName)) {
				userList.remove(user);
				UserInfo reUser = new UserInfo(userName);
				reUser.setStateImg(stateImag);
				reUser.setStateMsg(stateMsg);
				userList.add(reUser);
				System.out.println("friendPanel->updateState userName="+reUser.getNickname());
				System.out.println("updateState result="+reUser.getStateImg()+","+reUser.getStateMsg());
			}
		}
		
		removeAll();//혹시..?이거 안하면 라벨이쌓여서 안보이는듯!
		//모두 지웠으니 단체방 만들기 버튼 다시 붙이기
		createRoomBtn = new JButton("채팅방 만들기");
		createRoomBtn.setBounds(0, 1, 600, 40);
		createRoomBtn.setBackground(new Color(255,225,231));
		add(createRoomBtn);
		
		createRoomBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AddRoomFrame rf = new AddRoomFrame(user_id);
				
			}
		});
		seperate();
		setMyField();
		setFriendList(friendList);
		repaint();
	}
	
	public void dataSetting() {
		controller.send_Message(User.SIGNAL_ONLINE_USER_LIST);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		seperate();
		setMyField();
		setFriendList(friendList);
		repaint();
	}

	public void seperate() { //유저목록중 본인과 친구를 구분
		friendList = new ArrayList<UserInfo>();// 친구목록 초기화

		userList = controller.getOnlineUserList();
		for (int i = 0; i < userList.size(); i++) {
			UserInfo user = userList.get(i);
			if (user.getNickname().equals(user_id)) { // 본인
				myStateImage = "src/friendListImg/" + user.getStateImg();
				myStateMessage = user.getStateMsg();
			} else { //본인이 아님=>친구
				friendList.add(user);
			}
		}
	}
	public void setMyField() {
		Myprofile = new JLabel(user_id);
		Myprofile.setBounds(90, 55, 295, 80);
		Myprofile.setFont(new Font("Tmon몬소리 Black", Font.PLAIN, 20));
		Myprofile.setOpaque(true);
		Myprofile.setBackground(Color.WHITE);
		add(Myprofile);
		
		MyImg = new JLabel(new ImageIcon(myStateImage));
		MyImg.setBounds(10, 55, 80, 80);
		MyImg.setOpaque(true);
		MyImg.setBackground(Color.WHITE);
		add(MyImg);
		
		myStateLabel = new JLabel(myStateMessage);
		myStateLabel.setBounds(385, 55, 200, 80);
		myStateLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		myStateLabel.setBackground(Color.white);
		myStateLabel.setOpaque(true);
		add(myStateLabel);
	}
	
	public void setFriendList(ArrayList<UserInfo> friendList) {
		friendLabel.clear();
		friendImg.clear();
		stateLabel.clear();
	
		for (i = 0; i < friendList.size(); i++) {
			UserInfo user = friendList.get(i);
			System.out.println("friendList="+user.getNickname()+"/"+user.getStateImg()+"/"+user.getStateMsg());
			
			friendLabel.add(new JLabel(user.getNickname()));
			friendImg.add(new JLabel(new ImageIcon("src/friendListImg/"+user.getStateImg())));
			stateLabel.add(new JLabel(user.getStateMsg()));
			
			System.out.println("/////"+user.getStateImg());
			
			friendImg.get(i).setBounds(10, 145 + (i * 81), 80, 80);
			friendImg.get(i).setBackground(Color.white);
			friendImg.get(i).setOpaque(true);
			
			stateLabel.get(i).setBounds(385, 145 + (i * 81), 200, 80);
			stateLabel.get(i).setFont(new Font("맑은 고딕", Font.PLAIN, 20));
			stateLabel.get(i).setBackground(Color.white);
			stateLabel.get(i).setOpaque(true);

			friendLabel.get(i).setBounds(90, 145 + (i * 81), 295, 80);
			friendLabel.get(i).setFont(new Font("Tmon몬소리 Black", Font.PLAIN, 20));
			friendLabel.get(i).setOpaque(true);
			friendLabel.get(i).setBackground(Color.WHITE);
			
			add(friendLabel.get(i));
			add(friendImg.get(i));
			add(stateLabel.get(i));

			friendLabel.get(i).addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseReleased(MouseEvent e) { // 친구목록을 누르면 해당 친구와의 갠톡방을 개설
					// TODO Auto-generated method stub
					if(e.getClickCount()==2) {
						String friendName = ((JLabel) e.getSource()).getText();

						// 서버에게 채팅방 생성 요청
						controller.send_Message(User.SIGNAL_CREATE_SINGLEROOM + "//" + user_id + "//" + friendName);

					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

			});
		}
	}

	public void createChatPanel(String friendName) {
		ChatFrame a = new ChatFrame(friendName);
		System.out.println("friendPanel -> " + friendName);
	}

}
