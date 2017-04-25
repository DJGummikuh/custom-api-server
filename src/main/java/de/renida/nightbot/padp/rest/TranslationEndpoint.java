package de.renida.nightbot.padp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

/**
 * This class houses a couple of endpoints that can be used to translate
 * non-cooperative english external apis to german.
 * 
 * @author jfrank
 *
 */
@Component
@Path("/translate")
public class TranslationEndpoint {

	/**
	 * Translates the NightBot-formatted LoL Rank message and only returns the
	 * rank name.
	 * 
	 * @param rank
	 *            the entire message.
	 * @return only the rank.
	 */
	@GET
	@Path("/rank")
	@Produces("text/plain;charset=utf-8")
	public String getRankMessage(@QueryParam("rank") String rank) {
		return rank.split(":")[1].trim();
	}

	/**
	 * Translates the english uptime message to a german time.
	 * 
	 * @param uptime
	 *            the entire uptime string.
	 * @return the german uptime.
	 */
	@GET
	@Path("uptime")
	@Produces("text/plain;charset=utf-8")
	public String getUptimeMessage(@QueryParam("uptime") String uptime) {
		uptime = uptime.replace("Renida89 isn't currently streaming", "einiger Zeit (da ich offline bin :D) nicht");
		uptime = uptime.replace("Renida89 has been streaming for ", "");
		uptime = uptime.replace("hours", "Stunden");
		uptime = uptime.replace("hour", "Stunde");
		uptime = uptime.replace("minutes", "Minuten");
		uptime = uptime.replace("minute", "Minute");
		uptime = uptime.replace("seconds", "Sekunden");
		uptime = uptime.replace("second", "Sekunde");
		uptime = uptime.replace(",", " und");
		return uptime;
	}
}
