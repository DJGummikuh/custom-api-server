package de.renida.nightbot.queue.impl;

public class QueueMember {
	private String player;
	private String ingameName;

	/**
	 * Default Constructor. For (potential) Bean Deserialization Uses.
	 */
	public QueueMember() {
	}

	/**
	 * Constructor taking only the player (leaving ingame name null). Since
	 * equals is overridden, this constructor can be used to create stub objects
	 * to be used in List.contains() and .remove() calls.
	 * 
	 * @param player
	 *            The name of the player.
	 */
	public QueueMember(String player) {
		this.player = player;
	}

	/**
	 * Full preinit Constructor.
	 * 
	 * @param player
	 *            the name of the player.
	 * @param ingameName
	 *            the ingame name of the player.
	 */
	public QueueMember(String player, String ingameName) {
		this.player = player;
		this.ingameName = ingameName;
	}

	/**
	 * Getter for the player name.
	 * 
	 * @return the player name.
	 */
	public String getPlayer() {
		return player;
	}

	/**
	 * Setter for the player name.
	 * 
	 * @param player
	 *            the player name.
	 */
	public void setPlayer(String player) {
		this.player = player;
	}

	/**
	 * Getter for the ingame name.
	 * 
	 * @return the ingame name.
	 */
	public String getIngameName() {
		return ingameName;
	}

	/**
	 * Setter for the ingame name.
	 * 
	 * @param ingameName
	 *            the ingame name.
	 */
	public void setIngameName(String ingameName) {
		this.ingameName = ingameName;
	}

	/**
	 * Equals method, checking only the player name and not the ingame name.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof QueueMember))
			return false;
		QueueMember other = (QueueMember) obj;

		return player != null ? player.equalsIgnoreCase(other.player) : other.player == null;
	}

	/**
	 * Hashcode override. Contains the answer for the world the universe and
	 * everything.
	 */
	@Override
	public int hashCode() {
		return 42;
	}
}