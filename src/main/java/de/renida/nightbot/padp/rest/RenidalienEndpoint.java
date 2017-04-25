package de.renida.nightbot.padp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.renida.nightbot.persistence.points.RenidalienService;

@Component
@Path("/points/{streamer}/")
public class RenidalienEndpoint {

	@Autowired
	RenidalienService service;

	@GET
	@Path("{user}")
	@Produces("text/plain;charset=utf-8")
	public String getCurrentBalance(@PathParam("streamer") String streamer, @PathParam("user") String user) {
		return String.valueOf(service.getCurrentBalance(streamer, user));
	}

	@GET
	@Path("{user}/add")
	@Produces("text/plain;charset=utf-8")
	public String addBalance(@PathParam("streamer") String streamer, @PathParam("user") String user, String amount) {
		return String.valueOf(service.addBalance(streamer, user, Long.valueOf(amount)));
	}
}
