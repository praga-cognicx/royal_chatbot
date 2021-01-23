
package com.royal.app.configuration;


import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.royal.app.constants.AppicationConstants;
import com.royal.app.dao.impl.RateSheetDAOImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
public class DataSourceConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);

  @Autowired
  private Environment environment;


  @Bean(destroyMethod = "close", name = AppicationConstants.FIRST_DATA_SOURCE_BEAN_NAME)
  public DataSource firstDataSource() {
    HikariConfig dataSourceConfig = new HikariConfig();
    dataSourceConfig.setDriverClassName(environment.getRequiredProperty("db1.datasource.driver-class-name"));
    dataSourceConfig.setJdbcUrl(environment.getRequiredProperty("db1.datasource.url"));
    dataSourceConfig.setUsername(environment.getRequiredProperty("db1.datasource.username"));
    dataSourceConfig.setPassword(environment.getRequiredProperty("db1.datasource.password"));

    return new HikariDataSource(dataSourceConfig);
  }


  public JpaVendorAdapter firstJpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  private Properties commonJpaProperties() {
    Properties properties = new Properties();
    properties.put(AppicationConstants.HIBERNATE_DIALECT, environment.getRequiredProperty("db1.jpa.properties.hibernate.dialect"));
    properties.put(AppicationConstants.HIBERNATE_SHOW_SQL, environment.getRequiredProperty("db1.jpa.show-sql"));
    properties.put(AppicationConstants.HIBERNATE_FORMAT_SQL, environment.getRequiredProperty(AppicationConstants.HIBERNATE_FORMAT_SQL));
    properties.put("hibernate.ddl-auto", environment.getRequiredProperty("db1.jpa.hibernate.ddl-auto"));
        
    return properties;
  }

  @Primary
  @Bean(name = AppicationConstants.FIRST_ENTITY_MANAGER)
  public LocalContainerEntityManagerFactoryBean firstEntityManagerFactory() {
    try {
      LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
      localContainerEntityManagerFactoryBean.setDataSource(firstDataSource());
      localContainerEntityManagerFactoryBean.setPackagesToScan(AppicationConstants.FIRST_MODAL_PACKAGE);
      localContainerEntityManagerFactoryBean.setPersistenceUnitName(AppicationConstants.FIRST_PERSISTENCE_UNIT_NAME);
      localContainerEntityManagerFactoryBean.setJpaVendorAdapter(firstJpaVendorAdapter());
      localContainerEntityManagerFactoryBean.setJpaProperties(commonJpaProperties());

      return localContainerEntityManagerFactoryBean;

    } catch (Exception e) {
      logger.error("DSConfiguration.LocalContainerEntityManagerFactoryBean(): " + e.getMessage());
    }
    return new LocalContainerEntityManagerFactoryBean();
  }
  
  @Primary
  @Bean(name = AppicationConstants.FIRST_TRANSACTION_MANAGER)
  public PlatformTransactionManager firstTransactionManager(javax.persistence.EntityManagerFactory firstEntityManager) {
    try {
      return new JpaTransactionManager(firstEntityManager);
    } catch (Exception e) {
      logger.error("DSConfiguration.firstTransactionManager(): " + e.getMessage());
    }
    return new JpaTransactionManager();
  }

  @Bean(name = AppicationConstants.FIRST_JDBC_TEMPLATE)
  public JdbcTemplate firstJdbcTemplate(DataSource firstDataSource) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(firstDataSource);
    jdbcTemplate.setResultsMapCaseInsensitive(true);
    return jdbcTemplate;
  }
  
  @Bean(name="transactionManager")
  public HibernateTransactionManager transactionManager(SessionFactory s) {
     HibernateTransactionManager txManager = new HibernateTransactionManager();
     txManager.setSessionFactory(s);
     return txManager;
  }
  
  @Bean(name="sessionFactory")
  public LocalSessionFactoryBean sessionFactory() {
      LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
      sessionFactory.setDataSource(firstDataSource());
      sessionFactory.setPackagesToScan(new String[] {AppicationConstants.FIRST_MODAL_PACKAGE});
      sessionFactory.setHibernateProperties(commonJpaProperties());
      return sessionFactory;
   }
}

