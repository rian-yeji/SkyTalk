package chatting;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import command.CommandController;
import server.User;

public class SettingPanel extends JPanel {

	private JLabel Myprofile;
	private JLabel MyImg;
	final String defaultImg = "happy.png";
	private JLabel line;
	private JLabel profileMessage;
	
	private String myStateMessage;
	private String stateImage = "happy.png";

	final String stateList[] = { "happy.png", "crying.png", "angry.png", "in-love.png", "laughing.png", "secret.png" };
	private List<ImageIcon> stateImg = new ArrayList<ImageIcon>();
	private List<JLabel> stateLabel = new ArrayList<JLabel>();
	private CommandController controller = CommandController.getController();

	public SettingPanel(String user_id/* ,String myStateMessage */) {
		setLayout(null);
		setSize(600, 850);
		setBackground(new Color(168, 218, 255));

		MyImg = new JLabel(new ImageIcon(defaultImg));
		MyImg.setBounds(235, 80, 130, 130);
		add(MyImg);

		Myprofile = new JLabel(user_id);
		Myprofile.setBounds(0, 240, 600, 50);
		Myprofile.setHorizontalAlignment(JLabel.CENTER);
		Myprofile.setFont(new Font("Tmon몬소리 Black", Font.PLAIN, 20));
		Myprofile.setOpaque(false);
		add(Myprofile);

		line = new JLabel();
		line.setBounds(235, 290, 130, 3);
		line.setOpaque(true);
		line.setBackground(Color.WHITE);
		add(line);

		profileMessage = new JLabel("안녕");/* == myStateMessage */
		profileMessage.setBounds(200, 320, 200, 40);
		profileMessage.setHorizontalAlignment(JLabel.CENTER);
		profileMessage.setFont(new Font("Tmon몬소리 Black", Font.PLAIN, 20));
		profileMessage.setOpaque(true);
		profileMessage.setBackground(Color.WHITE);
		add(profileMessage);

		profileMessage.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
					myStateMessage = JOptionPane.showInputDialog("상태메세지를 입력하세요");
					profileMessage.setText(myStateMessage);
					repaint();
					
					controller.send_Message(User.SIGNAL_CHANGE_STATE+"//"+user_id+"//"+stateImage+"//"+myStateMessage);
					////////////////////////////////////////
					//       상태메세지 바꾸고 서버로 전송                       //
					////////////////////////////////////////
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

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		setStateImg();

	}
	
	public void setStateImg() {
		for(int j=0; j<6;j++)
			stateImg.add(new ImageIcon(stateList[j]));
		
		
		for(ImageIcon state : stateImg) {
			stateLabel.add(new JLabel(state));
			
		}
		System.out.println(stateLabel.size());
		
		int i=0;
		for(JLabel state : stateLabel) {
			state.setToolTipText(stateList[i]);
			if(i<3) {
				state.setBounds(75+(160*i),430,130,130);
			}
			
			else {
				state.setBounds(75+(160*(i-3)),630,130,130);
				
			}
			state.setOpaque(false);
			add(state);
			i++;
			
			state.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					System.out.println(state.getToolTipText());
					//stateImage == 이미지 파일 이름!!!!!!!
					stateImage = state.getToolTipText();
					MyImg.setIcon(new ImageIcon(stateImage));
					repaint();
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
