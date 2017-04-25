package de.renida.nightbot.persistence.points;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RenidalienService {

	@Autowired
	RenidalienRepository repository;

	public UserBalance getCurrentBalance(String streamer, String user) {
		return repository.findByStreamerAndUserName(streamer, user);
	}

	public List<UserBalance> getAllBalanceForStreamer(String streamer) {
		return repository.findByStreamer(streamer);
	}

	public long multiplyBalance(String streamer, String user, double multiplier) {
		UserBalance balance = repository.findByStreamerAndUserName(streamer, user);
		if (balance != null) {
			balance.setBalance((long) (balance.getBalance() * multiplier));
			return balance.getBalance();
		}
		return 0;
	}

	public long addBalance(String streamer, String user, long amountToAdd) {
		UserBalance balance = repository.findByStreamerAndUserName(streamer, user);
		if (balance == null) {
			balance = new UserBalance(streamer, user, amountToAdd);
		} else {
			balance.setBalance(balance.getBalance() + amountToAdd);
		}
		repository.save(balance);
		return balance.getBalance();
	}
}
