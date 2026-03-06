package br.com.arenco.arenco_cronjobs.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EntityScan(basePackages = "br.com.arenco.arenco_cronjobs.oracle.entities")
@EnableJpaRepositories(
    basePackages = "br.com.arenco.arenco_cronjobs.oracle.repositories",
    entityManagerFactoryRef = "oracleEntityManagerFactory",
    transactionManagerRef = "oracleTransactionManager")
public class AppConfig {

  @Value("${spring.datasource.oracle.url}")
  private String url;

  @Value("${spring.datasource.oracle.username}")
  private String username;

  @Value("${spring.datasource.oracle.password}")
  private String password;

  @Value("${spring.datasource.oracle.driver-class-name}")
  private String driverClassName;

  @Bean(name = "oracleDataSource")
  public DataSource oracleDataSource() {
    final HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDriverClassName(driverClassName);
    return dataSource;
  }

  @Bean(name = "oracleEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(
      @Qualifier("oracleDataSource") final DataSource dataSource) {

    final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("br.com.arenco.arenco_cronjobs.oracle.entities");
    em.setPersistenceUnitName("oraclePU");
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    em.setEntityManagerFactoryInterface(EntityManagerFactory.class);

    final Properties jpaProperties = new Properties();
    jpaProperties.put("hibernate.hbm2ddl.auto", "none");
    em.setJpaProperties(jpaProperties);

    return em;
  }

  @Bean(name = "oracleTransactionManager")
  public JpaTransactionManager oracleTransactionManager(
      @Qualifier("oracleEntityManagerFactory")
          final LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory) {
    assert oracleEntityManagerFactory.getObject() != null;
    return new JpaTransactionManager(oracleEntityManagerFactory.getObject());
  }
}
