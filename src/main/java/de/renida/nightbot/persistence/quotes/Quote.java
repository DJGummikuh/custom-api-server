package de.renida.nightbot.persistence.quotes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Quote")
public class Quote {

	@Id
	Integer id;

	@Column(name = "quote")
	String quote;

	@Column(name = "game")
	String game;

	@Column(name = "date")
	String date;

	@Column(name = "streamer")
	String streamer;

	@Column(name = "issuer")
	String issuer;

	@Column(name = "numberOfRequests")
	Long numberOfRequests;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getNumberOfRequests() {
		return numberOfRequests;
	}

	public void setNumberOfRequests(Long numberOfRequests) {
		this.numberOfRequests = numberOfRequests;
	}

	public String getStreamer() {
		return streamer;
	}

	public void setStreamer(String streamer) {
		this.streamer = streamer;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	@Override
	public String toString() {
		return "Quote [id=" + id + ", quote=" + quote + ", game=" + game + ", date=" + date + ", streamer=" + streamer
				+ ", issuer=" + issuer + ", numberOfRequests=" + numberOfRequests + "]";
	}

}
