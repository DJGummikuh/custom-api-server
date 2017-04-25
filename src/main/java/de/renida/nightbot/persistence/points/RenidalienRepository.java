package de.renida.nightbot.persistence.points;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface RenidalienRepository
		extends CrudRepository<UserBalance, StreamerViewerID>, QueryByExampleExecutor<UserBalance> {

	List<UserBalance> findByStreamer(String streamer);
	UserBalance findByStreamerAndUserName(String streamer, String userName);
}
