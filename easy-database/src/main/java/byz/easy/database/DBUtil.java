package byz.easy.database;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author 
 * @since 2020年6月1日
 */
public class DBUtil {

	protected static DBUtil _this;

	public static DBUtil build(DataSource dataSource) {
		return new DBUtil(dataSource);
	}

	protected JdbcTemplate jdbcTemplate;

	public DBUtil(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Map<String, Object> query(String sql, Object... args) {
		return jdbcTemplate.queryForMap(sql, args);
	}

	public <T> T query(String sql, Class<T> c, Object... args) {
		return jdbcTemplate.queryForObject(sql, c, args);
	}

	public List<Map<String, Object>> queryList(String sql, Object... args) {
		return jdbcTemplate.queryForList(sql, args);
	}

	public <T> List<T> queryList(String sql, Class<T> c,  Object... args) {
		return jdbcTemplate.queryForList(sql, c, args);
	}
	
}
