package de.renida.nightbot.queue.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * Domain class for the queue management.
 * 
 * @author jfrank
 *
 */
@Service
public class QueueManagement {

	/** The queues currently managed by us. */
	private Map<String, List<QueueMember>> queues = new ConcurrentHashMap<>();

	/** The current state of all queues. Non existing entries mean "inactive" */
	private Map<String, Boolean> queueStates = new ConcurrentHashMap<>();

	/**
	 * Activates the given queue (allows new entries to be added).
	 * 
	 * @param queue
	 *            the name of the queue.
	 */
	public synchronized void activate(String queue) {
		queueStates.put(queue, true);
	}

	/**
	 * Deactiavates the given queue (new entries are denied).
	 * 
	 * @param queue
	 *            the name of the queue.
	 */
	public synchronized void deactivate(String queue) {
		queueStates.put(queue, false);
	}

	/**
	 * Adds the given player to the given queue. Each player can only be added
	 * once.
	 * 
	 * @param queue
	 *            the queue to add the player to.
	 * @param player
	 *            the name of the player.
	 * @param ingameName
	 *            the ingame name of the player.
	 * @throws IllegalStateException
	 *             If the chosen queue currently is set to inactive.
	 * @throws IllegalArgumentException
	 *             If the given player is already member of that queue.
	 * @return the current position in the queue.
	 */
	public synchronized int addToQueue(String queue, String player, String ingameName) {
		if (!isQueueOpen(queue)) {
			throw new IllegalStateException("Queue " + queue + " is not currently open for new joins!");
		}
		List<QueueMember> members = null;
		if (!queues.containsKey(queue)) {
			members = new LinkedList<QueueMember>();
			queues.put(queue, members);
		} else {
			members = queues.get(queue);
		}
		if (members.contains(new QueueMember(player))) {
			throw new IllegalArgumentException("Player already in list!");
		} else {
			QueueMember member = new QueueMember(player, ingameName);
			members.add(member);
			return members.indexOf(member) + 1;
		}
	}

	/**
	 * Removes the given player from the designated queue. If a player is not
	 * member of the given queue, nothing happens.
	 * 
	 * @param queue
	 *            the queue to delete from. Must not nececessarily exist.
	 * @param player
	 *            the player to delete.
	 */
	public synchronized void removeFromQueue(String queue, String player) {
		List<QueueMember> members = queues.get(queue);
		if (members != null) {
			members.remove(new QueueMember(player));
		}
	}

	/**
	 * Allows to move a specific player within a queue. The Syntax for the
	 * offset is as follows: <br>
	 * <int> - moves the player to that absolute position within the queue,
	 * pushing the exiting member one place behind.<br>
	 * +/-<int> - moves the player relative to his current location. Plus means
	 * further to the front of the queue, Minus means further to the end.<br>
	 * FIRST - moves the player to the first position in the list.<br>
	 * LAST - moves the player to the last position in the list.<br>
	 * If the offset would place the player outside the Queue, he will be placed
	 * at the current head in that direction (absolutely in front or absolutely
	 * in back).
	 * 
	 * @param queue
	 *            the queue to modify.
	 * @param player
	 *            the name of the player to modify.
	 * @param offset
	 *            where to put the player
	 * @return the new Position in the queue.
	 * @throws NoSuchElementException
	 * @throws IllegalArgumentException
	 *             if the offset could not be parsed.
	 */
	public synchronized int moveQueueMember(String queue, String player, String offset) throws NoSuchElementException {
		if (offset == null || "".equals(offset)) {
			// invalid offset given.
			throw new IllegalArgumentException("Offset must be defined!");
		}
		List<QueueMember> members = queues.get(queue);
		if (members == null) {
			// queue not found.
			throw new NoSuchElementException("Queue not found");
		}
		int index = members.indexOf(new QueueMember(player));
		if (index == -1) {
			// player not found.
			throw new NoSuchElementException("Player not found");
		}

		QueueMember member = members.get(index);
		int targetValue = -1;
		if ("FIRST".equalsIgnoreCase(offset)) {
			targetValue = 0;
		} else if ("LAST".equalsIgnoreCase(offset)) {
			targetValue = members.size() - 1;
		} else if (offset.charAt(0) == '+') {
			try {
				int newValue = index - Integer.valueOf(offset.substring(1));
				targetValue = newValue < 0 ? 0 : newValue;
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Offset invalid", e);
			}
		} else if (offset.charAt(0) == '-') {
			try {
				int newValue = index + Integer.valueOf(offset.substring(1));
				targetValue = newValue > members.size() - 1 ? members.size() - 1 : newValue;
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Offset invalid", e);
			}
		} else {
			// either a raw number or nonsense.
			try {
				// accounting for zero- and one-based difference with -1
				int value = Integer.valueOf(offset) - 1;
				if (value < 0) {
					value = 0;
				} else if (value > members.size() - 1) {
					value = members.size() - 1;
				}
				targetValue = value;
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Offset invalid", e);
			}

		}
		members.remove(member);
		members.add(targetValue, member);
		return targetValue + 1;
	}

	/**
	 * Returns the next player in the queue.
	 * 
	 * @param queueName
	 *            the queueName to pop from
	 * @return the next player.
	 * @throws NoSuchElementException
	 *             If no more Players remain in this queue.
	 */
	public synchronized QueueMember next(String queueName) throws NoSuchElementException {
		List<QueueMember> members = queues.get(queueName);
		if (members != null) {
			if (members.size() > 0) {
				QueueMember value = members.get(0);
				members.remove(0);
				return value;
			} else {
				throw new IllegalArgumentException("Queue empty!");
			}
		} else {
			throw new IllegalArgumentException("Queue empty!");
		}
	}

	/**
	 * Returns the current Size of the given queue.
	 * 
	 * @param queueName
	 *            the queue name.
	 * @return the number of members in that queue. Returns 0 if the queue does
	 *         not exist.
	 */
	public synchronized int size(String queueName) {
		List<QueueMember> members = queues.get(queueName);
		if (members != null) {
			return members.size();
		} else {
			return 0;
		}
	}
	
	public synchronized int position(String queueName, String member) {
		List<QueueMember> members = queues.get(queueName);
		if (members != null) {
			return members.indexOf(new QueueMember(member));
		} else {
			return -1;
		}
	}

	/**
	 * Empties the given queue.
	 * 
	 * @param queueName
	 *            the queue name.
	 */
	public synchronized void resetQueue(String queueName) {
		List<QueueMember> members = queues.get(queueName);
		if (members != null) {
			members.clear();
		}
	}

	/**
	 * Returns the entire queue for the given name.
	 * 
	 * @param queueName
	 *            the queue name.
	 * @return the entire queue. Returns an empty list if the queue does not
	 *         exist.
	 */
	public synchronized List<QueueMember> getEntireQueue(String queueName) {
		List<QueueMember> members = queues.get(queueName);
		if (members != null) {
			return members;
		} else {
			return new LinkedList<QueueMember>();
		}
	}

	/**
	 * Returns whether or not a queue currently accepts new members.
	 * 
	 * @param queueName
	 *            the queue name.
	 * @return whether or not the queue is open for new admissions or not.
	 */
	public synchronized boolean isQueueOpen(String queueName) {
		return queueStates.get(queueName) != null && queueStates.get(queueName);
	}
}
