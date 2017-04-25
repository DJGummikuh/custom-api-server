package de.renida.nightbot.persistence.quotes;

import javax.persistence.EntityManagerFactory;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "quotesEntityManager", transactionManagerRef = "quotesTransactionManager", basePackageClasses = Quote.class)
public class QuoteConfig {

	@Autowired(required = false)
	private PersistenceUnitManager persistenceUnitManager;

	@Bean
	@ConfigurationProperties("app.quotes.spring.jpa")
	public JpaProperties quotesJpaProperties() {
		return new JpaProperties();
	}

	@Bean
	@ConfigurationProperties(prefix = "app.quotes.spring.datasource")
	public DataSource quotesDataSource() {
		return (DataSource) DataSourceBuilder.create().type(DataSource.class).build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean quotesEntityManager(JpaProperties quotesJpaProperties) {
		EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(quotesJpaProperties);
		return builder.dataSource(quotesDataSource())
				.packages(Quote.class)
				.persistenceUnit("quotesDs")
				.build();
	}

	@Bean
	public JpaTransactionManager quotesTransactionManager(EntityManagerFactory quotesEntityManager) {
		return new JpaTransactionManager(quotesEntityManager);
	}

	private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties customerJpaProperties) {
		JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(customerJpaProperties);
		return new EntityManagerFactoryBuilder(jpaVendorAdapter, customerJpaProperties.getProperties(),
				this.persistenceUnitManager);
	}

	private JpaVendorAdapter createJpaVendorAdapter(JpaProperties jpaProperties) {
		AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(jpaProperties.isShowSql());
		adapter.setDatabase(jpaProperties.getDatabase());
		adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
		adapter.setGenerateDdl(jpaProperties.isGenerateDdl());

		return adapter;
	}

}
