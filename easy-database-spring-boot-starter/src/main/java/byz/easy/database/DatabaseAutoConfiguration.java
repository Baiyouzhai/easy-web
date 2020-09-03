package byz.easy.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author
 * @since 2019年12月27日
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DatabaseProperties.class)
public class DatabaseAutoConfiguration {

	@Autowired(required = false)
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DatabaseProperties databaseProperties;

	@Bean
	public DatabaseManage getDatabaseManage() {
		return new DatabaseManage(jdbcTemplate, databaseProperties);
	}

}