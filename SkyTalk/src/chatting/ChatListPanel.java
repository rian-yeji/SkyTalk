package chatting;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import command.CommandController;
import server.User;

public class ChatListPanel extends JPanel {
	private JLabel Myprofile;
	private JLabel friendImg;
	private JLabel MyImg;
	private TextField searchFriend;

	private List<JLabel> chattingRoomList = new ArrayList<JLabel>();
	private List<JLabel> chatImg = new ArrayList<JLabel>();
	String chat = "chat.png";

	private CommandController controller = CommandController.getController();

	private MainFrame f;

	public ChatListPanel(MainFrame f) {
		this.f = f;
		setLayout(null);
		setSize(600, 850);
		setBackground(new Color(168, 218, 255));
		
		setchatList();
	}

	public void setchatList() {
		int i = 0;

		chattingRoomList = controller.getChatLabel();
		for (JLabel room : chattingRoomList)
			System.out.println("*********" + room.getText().toString());

		for (JLabel room : chattingRoomList) {
			chatImg.add(new JLabel(new ImageIcon(chat)));
			chatImg.get(i).setBounds(10, 15 + (i * 81), 80, 80);
			chatImg.get(i).setOpaque(true);
			chatImg.get(i).setBackground(Color.white);
			add(chatImg.get(i));
			
			room.setFont(new Font("Tmon몬소리 Black", Font.PLAIN, 20));
			room.setOpaque(true);
			room.setBackground(Color.WHITE);
			room.setBounds(90, 15 + (i * 81), 495, 80);
			add(room);
			i++;

			room.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseReleased(MouseEvent e) { // 친구목록을 누르면 해당 친구와의
					if(e.getClickCount()==2) {
						String roomName = room.getText();
						System.out.println("ChatListPanel-> roomName = "+roomName);
						
						ChatFrame chattingroom = new ChatFrame(roomName);
						JTextPane temp = controller.getChattingRoomList().get(roomName);
						chattingroom.getChatPanel().getTextPaneChat().setText(temp.getText());
						controller.getChattingRoomList().remove(temp);
						controller.getChattingRoomList().put(roomName, chattingroom.getChatPanel().getTextPaneChat());
						
						chattingroom.repaint();
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
}
