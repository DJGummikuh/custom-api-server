package de.renida.nightbot.persistence.points;

import java.io.Serializable;

public class StreamerViewerID implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8910342484676335129L;
	private String streamer;
	private String userName;

	public StreamerViewerID() {
		// TODO Auto-generated constructor stub
	}

	public StreamerViewerID(String streamer, String userName) {
		this.streamer = streamer;
		this.userName = userName;

	}

	@Override
	public int hashCode() {
		return streamer.hashCode() + userName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof StreamerViewerID)) {
			return false;
		}
		StreamerViewerID other = (StreamerViewerID) obj;
		return streamer.equalsIgnoreCase(other.streamer) && userName.equalsIgnoreCase(other.userName);
	}
}
