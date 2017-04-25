package de.renida.nightbot.padp.rest;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Class for the REST Interface. Defines the classes to be loaded
 * as Endpoints
 * 
 * @author jfrank
 *
 */
@Configuration
public class JerseyConfig extends ResourceConfig {

	/**
	 * Default Constructor, initializing Endpoints.
	 */
	public JerseyConfig() {
		register(QuoteEndpoint.class);
		register(TranslationEndpoint.class);
		register(QueueEndpoint.class);
	}
}
