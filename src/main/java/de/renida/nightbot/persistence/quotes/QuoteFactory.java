package de.renida.nightbot.persistence.quotes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public final class QuoteFactory {

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.GERMANY);

	public Quote createQuote(String quoteString, String game, String issuer, String streamer) {
		Quote quote = new Quote();
		quote.setGame(game);
		quote.setQuote(quoteString);
		quote.setNumberOfRequests(0L);
		quote.setStreamer(streamer);
		quote.setIssuer(issuer);
		synchronized (sdf) {
			quote.setDate(sdf.format(new Date(System.currentTimeMillis())));
		}
		System.out.println("Generated quote: " + quote);
		return quote;
	}
}
