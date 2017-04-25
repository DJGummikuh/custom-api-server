package de.renida.nightbot.queue.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class QueueManagementTest {

	@Test
	public void testAddMember() {
		QueueManagement manager = new QueueManagement();
		manager.activate("test");
		int pos = manager.addToQueue("test", "Test Player", "Test Ingame");
		assertEquals(1, pos);
		assertEquals(1, manager.size("test"));
	}

	@Test
	public void testPopMember() {
		QueueManagement manager = new QueueManagement();
		manager.activate("test");
		int pos = manager.addToQueue("test", "Test Player", "Test Ingame");
		assertEquals(1, pos);
		assertEquals(1, manager.size("test"));
		QueueMember member = manager.next("test");
		assertEquals("Test Player", member.getPlayer());
		assertEquals("Test Ingame", member.getIngameName());
		assertEquals(0, manager.size("test"));
		try {
			manager.next("test");
			assertFalse(true);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetNonExistingQueue() {
		QueueManagement manager = new QueueManagement();
		List<QueueMember> result = manager.getEntireQueue("non-existing");
		assertNotNull(result);
		assertEquals(0, manager.size("non-existing"));
	}

	@Test
	public void testMoveFIRST() {
		QueueManagement manager = getTestdata();
		int target = manager.moveQueueMember("test", "Test 3", "FIRST");
		QueueMember first = manager.getEntireQueue("test").get(0);
		assertEquals(1, target);
		assertEquals("Test 3", first.getPlayer());
		target = manager.moveQueueMember("test", "Test 2", "fiRSt");
		first = manager.getEntireQueue("test").get(0);
		assertEquals(1, target);
		assertEquals("Test 2", first.getPlayer());
	}

	@Test
	public void testMoveLAST() {
		QueueManagement manager = getTestdata();
		int target = manager.moveQueueMember("test", "Test 1", "LAST");
		QueueMember last = manager.getEntireQueue("test").get(2);
		assertEquals(3, target);
		assertEquals("Test 1", last.getPlayer());
		target = manager.moveQueueMember("test", "Test 2", "laSt");
		last = manager.getEntireQueue("test").get(2);
		assertEquals(3, target);
		assertEquals("Test 2", last.getPlayer());
	}

	@Test
	public void testMoveAbsolute() {
		QueueManagement manager = getTestdata();
		int target = manager.moveQueueMember("test", "Test 1", "3");
		QueueMember last = manager.getEntireQueue("test").get(2);
		assertEquals(3, target);
		assertEquals("Test 1", last.getPlayer());
		target = manager.moveQueueMember("test", "Test 3", "1");
		last = manager.getEntireQueue("test").get(0);
		assertEquals(1, target);
		assertEquals("Test 3", last.getPlayer());
		target = manager.moveQueueMember("test", "Test 3", "10");
		last = manager.getEntireQueue("test").get(2);
		assertEquals(3, target);
		assertEquals("Test 3", last.getPlayer());
	}

	@Test
	public void testMoveRelative() {
		QueueManagement manager = getTestdata();
		int target = manager.moveQueueMember("test", "Test 1", "+4");
		assertEquals(1, target);
		target = manager.moveQueueMember("test", "Test 1", "-1");
		assertEquals(2, target);
		target = manager.moveQueueMember("test", "Test 2", "-10");
		assertEquals(3, target);
	}

	@Test
	public void testResetQueue() {
		QueueManagement manager = getTestdata();
		manager.resetQueue("test");
		assertEquals(0, manager.size("test"));
		manager.resetQueue("non-existing");
		assertEquals(0, manager.size("non-existing"));
	}

	@Test
	public void testRemovePlayer() {
		QueueManagement manager = getTestdata();
		manager.removeFromQueue("test", "Test 2");
		assertEquals(2, manager.size("test"));
	}

	private QueueManagement getTestdata() {
		QueueManagement result = new QueueManagement();
		result.activate("test");
		result.addToQueue("test", "Test 1", "Ingame 1");
		result.addToQueue("test", "Test 2", "Ingame 2");
		result.addToQueue("test", "Test 3", "Ingame 3");
		result.deactivate("test");
		return result;
	}
}
