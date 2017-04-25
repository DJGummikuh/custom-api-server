package de.renida.nightbot.persistence.points;

import javax.persistence.EntityManagerFactory;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "pointsEntityManager", transactionManagerRef = "pointsTransactionManager", basePackageClasses = UserBalance.class)
public class PointsConfig {

	@Autowired(required = false)
	private PersistenceUnitManager persistenceUnitManager;

	@Bean
	@ConfigurationProperties("app.points.spring.jpa")
	public JpaProperties pointsJpaProperties() {
		return new JpaProperties();
	}

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "app.points.spring.datasource")
	public DataSource pointsDataSource() {
		return (DataSource) DataSourceBuilder.create().type(DataSource.class).build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean pointsEntityManager(JpaProperties pointsJpaProperties) {
		EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(pointsJpaProperties);
		return builder.dataSource(pointsDataSource()).packages(UserBalance.class).persistenceUnit("pointsDs").build();
	}

	@Bean
	@Primary
	public JpaTransactionManager pointsTransactionManager(EntityManagerFactory pointsEntityManager) {
		return new JpaTransactionManager(pointsEntityManager);
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
