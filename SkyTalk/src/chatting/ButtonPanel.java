package chatting;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel{
	
	private MainFrame f;
	
	private JButton friendBtn = new JButton("Friend");
	private JButton chatBtn = new JButton("Chat");
	private JButton setBtn = new JButton("Set");
	
	public ButtonPanel(MainFrame f) {
		this.f = f;
		
		setLayout(null);
		setBounds(0, 0, 600, 50);
		friendBtn.setBounds(0, 0, 200, 50);
		friendBtn.setForeground(Color.BLACK);
		friendBtn.setBackground(Color.WHITE);
		friendBtn.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 20));
		
		chatBtn.setBounds(200, 0, 200, 50);
		chatBtn.setForeground(Color.BLACK);
		chatBtn.setBackground(Color.WHITE);
		chatBtn.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 20));
		
		setBtn.setBounds(400, 0, 200, 50);
		setBtn.setForeground(Color.BLACK);
		setBtn.setBackground(Color.WHITE);
		setBtn.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 20));

		f.add(friendBtn);
		f.add(chatBtn);
		f.add(setBtn);
	}
	
	public JButton getFriendBtn() {
		return friendBtn;
	}
	
	public JButton getChatBtn() {
		return chatBtn;
	}
	
	public JButton getSetBtn() {
		return setBtn;
	}
	
	

}
