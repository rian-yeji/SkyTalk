package chatting;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import command.CommandController;

public class StartPanel extends JPanel {
	
	/*GUI¿ë º¯¼ö*/
	public friendPanel friendPanel;
	public chatPanel chatPanel;
	public ButtonPanel buttonPanel;
	public ChatListPanel chatlistPanel;
	public SettingPanel settingPanel;
	
	public MainFrame f;
	CommandController controller = CommandController.getController();
	
	public StartPanel(MainFrame f, String user_id /*,String myStateMessage*/) {

		this.f = f;
		
		chatlistPanel = new ChatListPanel(f);
		chatlistPanel.setBounds(0, 50, 600, 850);
		buttonPanel = new ButtonPanel(f);
		friendPanel = new friendPanel(f, user_id);
		friendPanel.setBounds(0, 50, 600, 850);
		settingPanel = new SettingPanel(user_id/*,String myStateMessage*/);
		settingPanel.setBounds(0, 50, 600, 850);
		
		/*chatlistPanel = new ChatListPanel(f);
		chatlistPanel.setBounds(0, 50, 600, 850);*/
		//controller.saveStartPanel(user_id,this);

		f.add(friendPanel);

		buttonPanel.getChatBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				f.remove(friendPanel);
				f.remove(settingPanel);
				chatlistPanel.setchatList();
				f.add(chatlistPanel);
				f.revalidate();
				f.repaint();
			}
		});

		buttonPanel.getFriendBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				f.remove(chatlistPanel);
				f.remove(settingPanel);
				friendPanel.dataSetting();
				f.add(friendPanel);
				f.revalidate();
				f.repaint();

			}
		});

		buttonPanel.getSetBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				f.remove(friendPanel);
				f.remove(chatlistPanel);
				f.add(settingPanel);
				f.revalidate();
				f.repaint();
			}
		});

	}
	
	public ChatListPanel getChatlistPanel() {
		return chatlistPanel;
	}

	public void setChatlistPanel(ChatListPanel chatlistPanel) {
		this.chatlistPanel = chatlistPanel;
	}

	public friendPanel getFriendPanel() {
		return friendPanel;
	}

	public void setFriendPanel(friendPanel friendPanel) {
		this.friendPanel = friendPanel;
	}

	

}