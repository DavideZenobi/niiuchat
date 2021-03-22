package io.dz.niiuchat.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class DataSourceConfiguration extends HikariConfig {

  @Bean
  public DataSource dataSource() {
    return new HikariDataSource(this);
  }

  @Bean
  public DSLContext dslContext(DataSource dataSource) {
    return DSL.using(dataSource, SQLDialect.MYSQL);
  }

}
