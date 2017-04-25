package de.renida.nightbot.persistence.points;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "UserBalance")
@IdClass(StreamerViewerID.class)
public class UserBalance implements Serializable {

	@Id
	private String streamer;

	@Id
	private String userName;

	private long balance;

	public UserBalance() {

	}

	public UserBalance(String streamer) {
		this.streamer = streamer;
	}

	public UserBalance(String streamer, String userName) {
		this.streamer = streamer;
		this.userName = userName;
	}

	public UserBalance(String streamer, String userName, long balance) {
		this.streamer = streamer;
		this.userName = userName;
		this.balance = balance;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStreamer() {
		return streamer;
	}

	public void setStreamer(String streamer) {
		this.streamer = streamer;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

}
