package de.renida.nightbot.padp.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.renida.nightbot.persistence.quotes.Quote;
import de.renida.nightbot.persistence.quotes.QuoteFactory;
import de.renida.nightbot.persistence.quotes.QuoteRepository;
import de.renida.nightbot.persistence.quotes.QuoteService;

/**
 * The REST endpoint for the quote management.
 * 
 * @author jfrank
 *
 */
@Component
@Path("/quote")
public class QuoteEndpoint {

	/** The secret required for authentication. */
	private static final String SECRET = "asdfasdf";

	/** Reference to the quote repository. */
	@Autowired
	QuoteRepository repository;

	/** Reference to the quote service. */
	@Autowired
	QuoteService service;

	/** Reference to the quote factory. */
	@Autowired
	QuoteFactory factory;

	/**
	 * Adds a quote to the system.
	 * 
	 * @param quoteString
	 *            the quote to add.
	 * @param streamer
	 *            the streamer to add it for.
	 * @param game
	 *            the game to add it for.
	 * @param issuer
	 *            who issued this quote.
	 * @param secret
	 *            the secret for authentication.
	 * @return Feedback.
	 */
	@GET
	@Path("/add")
	@Produces("text/plain;charset=utf-8")
	public String addQuote(//
			@QueryParam("quote") String quoteString, //
			@QueryParam("streamer") String streamer, //
			@QueryParam("game") String game, //
			@QueryParam("issuer") String issuer, //
			@QueryParam("secret") String secret) {
		if (isAuthenticated(streamer, secret)) {
			Quote quote = factory.createQuote(quoteString, game, issuer, streamer);
			service.addQuote(quote);
			return serializeQuote(quote) + " wurde erfolgreich hinzugefügt!";
		} else {
			return "Ungültige Benutzerinformationen!";
		}
	}

	/**
	 * Helper method that authenticates the call.
	 * 
	 * @param streamer
	 *            the streamer for which this call is.
	 * @param secret
	 *            the secret.
	 * @return
	 */
	public static boolean isAuthenticated(String streamer, String secret) {
		return SECRET.equals(secret) && "renida".equalsIgnoreCase(streamer);
	}

	/**
	 * Deletes a given Quote.
	 * 
	 * @param id
	 *            the ID of the quote to delete.
	 * @param secret
	 *            the secret that authenticates writing operations.
	 * @return A message telling the user if the deletion was successful.
	 */
	@GET
	@Path("{id}/delete/")
	@Produces("text/plain;charset=utf-8")
	public String deleteQuote(@PathParam("id") String id, @QueryParam("secret") String secret) {
		try {
			int idNum = Integer.valueOf(id);
			Quote quote = repository.findOne(idNum);
			if (quote != null && "renida".equalsIgnoreCase(quote.getStreamer()) && SECRET.equals(secret)) {
				repository.delete(idNum);
				return "Zitat " + idNum + " wurde erfolgreich gelöscht!";
			} else {
				return "Beim Löschen des Zitates ist ein Fehler aufgetreten!";
			}
		} catch (NumberFormatException e) {
			return "Bitte eine gültige Zitat-ID angeben.";
		}
	}

	/**
	 * Modifies an existing quote, replacing ONLY the String of that quote!
	 * 
	 * @param id
	 *            the id to modify
	 * @param secret
	 *            the secret for authorization.
	 * @param quoteString
	 *            the replacement string for the given quote.
	 * @return Feedback.
	 */
	@GET
	@Path("{id}/modify")
	@Produces("text/plain;charset=utf-8")
	public String modifyQuote(@PathParam("id") String id, @QueryParam("secret") String secret,
			@QueryParam("quote") String quoteString) {
		try {
			int idNum = Integer.valueOf(id);
			Quote quote = repository.findOne(idNum);
			if (quote != null && isAuthenticated(quote.getStreamer(), secret)) {
				quoteString = quoteString.substring(quoteString.indexOf(' ') + 1);
				quote.setQuote(quoteString);
				repository.save(quote);
			}
			return serializeQuote(quote) + " wurde erfolgreich aktualisiert.";
		} catch (NumberFormatException e) {
			return "Bitte eine gültige Zitat-ID angeben.";
		}
	}

	/**
	 * Returns a random quote.
	 * 
	 * @return a random quote.
	 */
	@GET
	@Produces("text/plain;charset=utf-8")
	public String getRandom() {
		Quote quote = service.getRandomQuote();
		return serializeQuote(quote);
	}

	private String serializeQuote(Quote quote) {
		if (quote != null) {
			SimpleDateFormat longsdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.GERMANY);
			SimpleDateFormat shortsdf = new SimpleDateFormat("dd.MM.yyyy");
			try {
				return "Zitat #" + quote.getId() + ": " + quote.getQuote() + " ("
						+ shortsdf.format(longsdf.parse(quote.getDate())) + " " + quote.getGame() + ")";
			} catch (ParseException e) {
				return "Fehler beim Datumskonvertieren " + e.getMessage();
			}
		} else {
			return "Das angeforderte Zitat konnte leider nicht gefunden werden!";
		}
	}

	/**
	 * Returns a quote identified by its ID. If the ID is not provided, a random
	 * quote is returned instead.
	 * 
	 * @param id
	 *            the ID to find.
	 * @return the required code or a default text if the quote is not found.
	 */
	@GET
	@Path("{id}")
	@Produces("text/plain;charset=utf-8")
	public String getQuoteById(@PathParam("id") String id) {
		try {
			System.out.println("ID: '" + id + "'");
			if ("".equals(id) || id == null || "null".equals(id)) {
				return getRandom();
			}
			int idNum = Integer.valueOf(id);
			return serializeQuote(repository.findOne(idNum));
		} catch (NumberFormatException e) {
			return "Das Suchen nach Zitaten ist leider noch nicht implementiert.";
		}
	}
}
