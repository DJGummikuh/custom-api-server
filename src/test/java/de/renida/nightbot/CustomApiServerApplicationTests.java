package de.renida.nightbot;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.renida.nightbot.persistence.points.RenidalienService;
import de.renida.nightbot.persistence.points.UserBalance;
import de.renida.nightbot.persistence.quotes.QuoteFactory;
import de.renida.nightbot.persistence.quotes.QuoteService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomApiServerApplicationTests {

	@Test
	public void contextLoads() {
	}
	
	@Autowired
	RenidalienService service;
	
	@Autowired
	QuoteService quotes;
	
	@Autowired
	QuoteFactory factory;
	@Test
	public void testBalance() {
		quotes.addQuote(factory.createQuote("adsas", "asdasdasda", "asdasdas", "adsasdasd"));
		service.addBalance("Test", "Ich", 200);
		List<UserBalance> userbalances = service.getAllBalanceForStreamer("Test");
		System.out.println(userbalances.get(0).getStreamer());
		
	}

}
