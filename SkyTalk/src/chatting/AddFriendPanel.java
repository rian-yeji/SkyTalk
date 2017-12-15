package chatting;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import command.CommandController;
import server.User;

public class AddFriendPanel extends JPanel {

	private AddRoomFrame f;
	private JButton addBtn = new JButton("犬牢");
	private CommandController controller = CommandController.getController();
	private List<JLabel> friendList = new ArrayList<JLabel>();

	private List<String> AddFriendName = new ArrayList<String>();
	
	//private JPanel listPanel;
	
	private JScrollPane listPanel;

	private String myId;
	public AddFriendPanel(AddRoomFrame f,String userId) {
		this.f = f;
		this.myId = userId;
		
		setLayout(null);
		setSize(400, 600);
		setBackground(new Color(168, 218, 255));
		
		JLabel list = new JLabel("立加吝牢 模备 格废");
		list.setHorizontalAlignment(JLabel.CENTER);
		list.setBounds(27, 15, 340, 50);
		list.setBackground(new Color(255,225,231));
		list.setFont(new Font("Tmon阁家府 Black", Font.PLAIN, 17));
		//list.setBackground(Color.WHITE);
		list.setOpaque(true);
		f.add(list);

		addList();

		addBtn.setBounds(150, 502, 100, 50);
		addBtn.setBackground(new Color(255,225,231));
		addBtn.setFont(new Font("Tmon阁家府 Black", Font.PLAIN, 17));
		f.add(addBtn);

		addBtn.addActionListener(new ActionListener() {
			int j=1;

			@Override
			public void actionPerformed(ActionEvent e) {
				//急琶等 模备 府胶飘
				String signal = User.SIGNAL_CREATE_MULTIROOM+"//"+myId;
				
				System.out.println("~模备格废~");
				for(String name:AddFriendName) {
					signal += "//"+name;
					System.out.println(j + name);
					j++;
				}
				controller.send_Message(signal);
				
				f.dispose();

			}
		});
	}

	public void addList() {
		friendList.clear();
		/*listPanel = new JScrollPane();
		listPanel.setBounds(27, 75, 345, 400);
		listPanel.setOpaque(false);
		*/
		controller.send_Message(User.SIGNAL_ONLINE_USER_LIST);
		try {
			Thread.sleep(300);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		friendList = controller.getUserLabel();

		for (int i = 0,j=0; i < friendList.size(); i++,j++) {
			if(!friendList.get(i).getText().equals(myId)) { //夯牢篮 力寇窍绊 模备免仿
				
				friendList.get(i).setBounds(27, 75 + (j * 51), 340, 50);
				friendList.get(i).setFont(new Font("Tmon阁家府 Black", Font.PLAIN, 17));
				friendList.get(i).setBackground(Color.WHITE);
				friendList.get(i).setOpaque(true);
				f.add(friendList.get(i));
				System.out.println("**" + friendList.get(i).getText());

				JLabel a = friendList.get(i);

				friendList.get(i).addMouseListener(new MouseListener() {
					boolean addFriend = false;
					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseReleased(MouseEvent e) {
						//boolean b = false;
						if (addFriend == false) {
							addFriend = true;
							AddFriendName.add(a.getText());
							System.out.println("模备 急琶 ? : " + addFriend);
							a.setBackground(Color.LIGHT_GRAY);
						}

						else if (addFriend == true) {
							addFriend = false;
							AddFriendName.remove(a.getText());
							System.out.println("模备 急琶 ? : " + addFriend);
							a.setBackground(Color.WHITE);
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
			else{
				--j;
			}
		}
		
	}

}
