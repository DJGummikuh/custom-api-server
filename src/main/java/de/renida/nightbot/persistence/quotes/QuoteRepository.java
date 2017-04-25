package de.renida.nightbot.persistence.quotes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Empty Interface of the quote repository. Auto-generated at runtime by spring
 * boot.
 * 
 * @author jfrank
 *
 */
@Repository
public interface QuoteRepository extends JpaRepository<Quote, Integer> {

}
