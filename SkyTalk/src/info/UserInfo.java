package info;

import java.util.ArrayList;

public class UserInfo {

	private String nickname;
	private String password;
	private String stateImg;
	private String stateMsg;
	private boolean isConnect;//¡¢º”¡ﬂ
	
	
	public UserInfo() {
		nickname="sample";
		password="1111";
		isConnect = true;
		stateImg = "happy.png";
		stateMsg ="userInfo stateMsg";
	}
	
	public UserInfo(String nickname) {
		this.nickname = nickname;
		stateImg = "happy.png";
		stateMsg = "userInfo stateMsg";
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public boolean isConnect() {
		return isConnect;
	}

	public void setConnect(boolean isConnect) {
		this.isConnect = isConnect;
	}

	public String getStateMsg() {
		return stateMsg;
	}

	public void setStateMsg(String stateMsg) {
		this.stateMsg = stateMsg;
	}

	public String getStateImg() {
		return stateImg;
	}

	public void setStateImg(String stateImg) {
		this.stateImg = stateImg;
	}
	
	
	
	
	
}
