package de.renida.nightbot.padp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.renida.nightbot.queue.impl.QueueManagement;
import de.renida.nightbot.queue.impl.QueueMember;

@Component
@Path("/queue/{streamer}/{queue}/")
public class QueueEndpoint {

	@Autowired
	private QueueManagement manager;

	@GET
	@Produces("text/html;charset=utf-8")
	public String getCompleteQueue(@PathParam("queue") String queueName, @PathParam("streamer") String streamer,
			@QueryParam("secret") String secret) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"<table id=\"queuetable\"><tr><th>Warteschlangenplatz</th><th>Zuschauername</th><th>Ingame Name</th></tr>");
		int i = 1;
		for (QueueMember s : manager.getEntireQueue(queueName)) {
			sb.append("<tr><td>" + i++ + "</td><td>" + s.getPlayer() + "</td><td>" + s.getIngameName() + "</td></tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}

	@GET
	@Path("next")
	@Produces("text/plain;charset=utf-8")
	public String getNextInLine(@PathParam("queue") String queueName, @PathParam("streamer") String streamer,
			@QueryParam("secret") String secret) {
		if (QuoteEndpoint.isAuthenticated(streamer, secret)) {
			try {
				QueueMember result = manager.next(queueName);
				return result.getPlayer() + (result.getIngameName() != null || "".equals(result.getIngameName())
						? " (Ingame Name: " + result.getIngameName() + ")" : " (Kein Ingame Name angegeben)");
			} catch (IllegalArgumentException e) {
				return "Es ist niemand mehr in der Warteliste '" + queueName + "' eingetragen! :-(";
			}
		} else {
			return "Ungültige Benutzerinformationen!";
		}
	}

	@GET
	@Path("position")
	@Produces("text/plain;charset=utf-8")
	public String getPlayerPosition(@PathParam("queue") String queue, @QueryParam("playername") String playerName) {
		int position = manager.position(queue, playerName);
		if (position == -1) {
			return "Du bist nicht in der Warteliste, " + playerName + ".";
		} else {
			return "Du bist aktuell an Position " + position + " in der Warteliste '" + queue + "' " + playerName + ".";
		}
	}

	@GET
	@Path("add")
	@Produces("text/plain;charset=utf-8")
	public String addPlayerToQueue(@QueryParam("admin") String issuedByAdmin, @QueryParam("following") String following, //
			@QueryParam("provider") String provider, //
			@PathParam("queue") String queueName, //
			@QueryParam("player") String playerName, //
			@PathParam("streamer") String streamer, //
			@QueryParam("ingame") String ingameName, //
			@QueryParam("secret") String secret) {
		System.out.println("Provider: " + provider);
		if (!"twitch".equalsIgnoreCase(provider)) {
			return "Dieser Befehl funktioniert nur aus dem Twitch Chat heraus!";
		}
		if (!"true".equals(following)) {
			return "Entschuldige bitte, " + playerName + " aber nur Follower dürfen sich in die Warteliste eintragen!";
		}
		if (QuoteEndpoint.isAuthenticated(streamer, secret)) {
			try {
				if ("true".equals(issuedByAdmin)) {
					ingameName = ingameName.substring(ingameName.indexOf(' ') + 1);
				}
				int position = manager.addToQueue(queueName, playerName, ingameName);
				if ("true".equals(issuedByAdmin)) {
					return "'" + playerName + "' wurde erfolgreich in die Warteliste '" + queueName + "' an Position "
							+ position + " aufgenommen.";
				} else {
					return "Du wurdest erfolgreich in die Warteliste '" + queueName + "' an Position " + position
							+ " aufgenommen, " + playerName + "!";
				}
			} catch (IllegalArgumentException e) {
				if ("true".equals(issuedByAdmin)) {
					return "'" + playerName + "' ist bereits Mitglied der warteliste '" + queueName + "'.";
				} else {
					return "Du bist bereits in der Warteliste '" + queueName + "' enthalten!";
				}
			} catch (IllegalStateException e) {
				return "Entschuldige bitte, die Warteliste '" + queueName + "' ist derzeit nicht geöffnet!";
			}
		} else {
			return "Ungültige Benutzerinformationen!";
		}
	}

	@GET
	@Path("delete")
	@Produces("text/plain;charset=utf-8")
	public String removeFromQueue(@QueryParam("admin") String issuedByAdmin, @QueryParam("provider") String provider,
			@PathParam("queue") String queueName, @QueryParam("player") String playerName,
			@PathParam("streamer") String streamer, @QueryParam("secret") String secret) {
		if (!"twitch".equals(provider)) {
			return "Dieser Befehl funktioniert nur aus dem Twitch Chat heraus!";
		}
		if (QuoteEndpoint.isAuthenticated(streamer, secret)) {
			try {
				manager.removeFromQueue(queueName, playerName);
				if ("true".equals(issuedByAdmin)) {
					return "'" + playerName + "' wurde erfolgreich aus der Warteliste '" + queueName + "' ausgetragen!";
				} else {
					return "Du wurdest erfolgreich aus der Warteliste '" + queueName + "' ausgetragen!";
				}
			} catch (IllegalArgumentException e) {
				if ("true".equals(issuedByAdmin)) {
					return "'" + playerName + "' ist kein Mitglied der Warteliste '" + queueName + "'.";
				} else {
					return "Du warst überhaupt nicht in der Warteliste '" + queueName + "' eingetragen!";
				}
			}
		} else {
			return "Ungültige Benutzerinformationen!";
		}
	}

	@GET
	@Path("move")
	@Produces("text/plain;charset=utf-8")
	public String movePlayer(@PathParam("queue") String queueName, @QueryParam("player") String player,
			@QueryParam("offset") String offset, @PathParam("streamer") String streamer,
			@QueryParam("secret") String secret) {
		if (QuoteEndpoint.isAuthenticated(streamer, secret)) {
			int position = manager.moveQueueMember(queueName, player, offset);
			return "'" + player + "' wurde erfolgreich in der Warteliste '" + queueName + "' an die Position "
					+ position + " verschoben!";
		} else {
			return "Ungültige Benutzerinformationen!";
		}
	}

	@GET
	@Path("clear")
	@Produces("text/plain;charset=utf-8")
	public String clearList(@PathParam("queue") String queueName, @PathParam("streamer") String streamer,
			@QueryParam("secret") String secret) {
		if (QuoteEndpoint.isAuthenticated(streamer, secret)) {
			manager.resetQueue(queueName);
			return "Warteschlange '" + queueName + "' wurde zurückgesetzt.";
		} else {
			return "Ungültige Benutzerinformationen!";
		}
	}

	@GET
	@Path("size")
	@Produces("text/plain;charset=utf-8")
	public String getQueueSize(@PathParam("queue") String queueName, @PathParam("streamer") String streamer,
			@QueryParam("secret") String secret) {
		if (QuoteEndpoint.isAuthenticated(streamer, secret)) {
			return String.valueOf(manager.size(queueName));
		} else {
			return "Ungültige Benutzerinformationen!";
		}
	}

	@GET
	@Path("status")
	@Produces("text/plain;charset=utf-8")
	public String queueStatus(@PathParam("queue") String queueName, @PathParam("streamer") String streamer,
			@QueryParam("secret") String secret, @QueryParam("status") String status) {
		if (QuoteEndpoint.isAuthenticated(streamer, secret)) {
			if (status == null) {
				return manager.isQueueOpen(queueName) ? "Die Warteliste '" + queueName + "' ist derzeit geöffnet!"
						: "Die Warteliste '" + queueName + "' ist derzeit geschlossen!";
			} else {
				switch (status.toLowerCase()) {
				case "active":
					manager.activate(queueName);
					return "Die Warteliste '" + queueName + "' nimmt nun neue Einträge an!";
				case "inactive":
					manager.deactivate(queueName);
					return "Die Warteliste '" + queueName + "' ist nun für Neueintragungen geschlossen!";
				default:
					return "Ungültiger Status " + status + " gegeben. Gültige Werte sind: 'active' und 'inactive'!";
				}
			}
		} else {
			return "Ungültige Benutzerinformationen!";
		}
	}
}
