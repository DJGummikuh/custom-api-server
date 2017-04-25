package de.renida.nightbot.persistence.quotes;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * High-level service to work on quotes.
 * 
 * @author jfrank
 *
 */
@Service
public class QuoteService {

	@Autowired
	QuoteRepository repository;
	Random r = new Random(System.currentTimeMillis());

	/**
	 * Returns a random quote from the database if at least one quote exists.
	 * 
	 * @return a random quote if at least one quote exists, null otherwise.
	 */
	public Quote getRandomQuote() {
		System.out.println("Getting random Quote");
		List<Quote> quotes = repository.findAll();
		if (quotes.size() > 0) {
			return quotes.get(r.nextInt((int) quotes.size()));
		} else
			System.out.println("Quote Repository empty");
		return null;
	}

	/**
	 * Adds a quote.
	 * 
	 * @param quote
	 *            The quote to add.
	 */
	public void addQuote(Quote quote) {
		List<Quote> quotes = repository.findAll();
		int lastId = 0;
		if (quotes.size() > 0) {
			lastId = quotes.get(quotes.size() - 1).getId();
		}
		lastId++;
		quote.setId(lastId);
		repository.save(quote);
	}
}
