package com.iexpress.spring.config.db;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.Getter;
import lombok.Setter;
import org.flywaydb.core.Flyway;

//@EnableJpaRepositories(basePackages = "com.iexpress.spring.api.*", entityManagerFactoryRef = "demoEntityManagerFactory", 
//	transactionManagerRef = "demoTransactionManager")

@Configuration
@EnableTransactionManagement
@Getter
@Setter
@EnableJpaRepositories(basePackages = "com.iexpress.*")
public class DatabaseConfig {

	@Autowired
	Environment env;

	@Bean(name = "dataSource")
	@Primary
	public DataSource dataSource() {
		DataSourceBuilder<?> builder = DataSourceBuilder.create();
		builder.driverClassName(env.getProperty("driverClassName"));
		builder.url(env.getProperty("spring.datasource.url"));
		builder.username(env.getProperty("spring.datasource.username"));
		builder.password(env.getProperty("spring.datasource.password"));
		
		System.out.println("URL ******************* "+env.getProperty("spring.datasource.url") );
		
		return builder.build();
	}

	@Primary
	@Bean(name = "entityManagerFactory")
	@DependsOn("flyway")
	public LocalContainerEntityManagerFactoryBean demoEntityManagerFactory(
			@Autowired @Qualifier("dataSource") DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean fac = new LocalContainerEntityManagerFactoryBean();
		fac.setPersistenceUnitName("iexpress");
		fac.setDataSource(dataSource);
		fac.setPackagesToScan("com.iexpress.spring.*");
		fac.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		fac.setJpaProperties(additionalProperties());
		return fac;
	}

	@Bean(initMethod = "migrate")
	public Flyway flyway() throws PropertyVetoException {
		Flyway flyway = new Flyway();
		flyway.setBaselineOnMigrate(true);
		flyway.setLocations("db_migration_script");
		flyway.setDataSource(dataSource());
		return flyway;
	}

	@Primary
	@Bean(name = "transactionManager")
	public JpaTransactionManager transactionManager(
			@Autowired @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager mgr = new JpaTransactionManager();
		mgr.setEntityManagerFactory(entityManagerFactory);
		return mgr;
	}

	Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		return properties;
	}
}
