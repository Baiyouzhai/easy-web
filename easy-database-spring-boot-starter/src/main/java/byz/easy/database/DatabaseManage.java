package byz.easy.database;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

import byz.easy.database.AbstractDatabase;
import byz.easy.database.AbstractTable;

/**
 * @author
 * @since 2019年11月3日
 */
public class DatabaseManage {

	protected JdbcTemplate jdbcTemplate;

	protected AbstractDatabase database;

	protected Map<String, AbstractDatabase> databases;

	protected Map<String, JdbcTemplate> jdbcTemplates;

	public DatabaseManage(JdbcTemplate jdbcTemplate, DatabaseProperties databaseProperties) {
		System.out.println("多数据源初始化……");
		long time = System.currentTimeMillis();
		this.jdbcTemplate = jdbcTemplate;
		databases = new ConcurrentHashMap<String, AbstractDatabase>();
		jdbcTemplates = new ConcurrentHashMap<String, JdbcTemplate>();
		List<DruidDataSource> dataBases = databaseProperties.getMoreDataBase();
		for (DruidDataSource dataBase : dataBases)
			jdbcTemplates.put(dataBase.getName(), new JdbcTemplate(dataBase));
		System.out.println("多数据源初始化完成，用時" + (System.currentTimeMillis() - time) / 1000.0 + "s");
	}

	public JdbcTemplate getConnect() {
		return jdbcTemplate;
	}

	public JdbcTemplate getConnect(String name) {
		return jdbcTemplates.get(name);
	}

	public AbstractDatabase getDataBase() {
		return database;
	}

	public JdbcTemplate getDataBase(String dbName) {
		return jdbcTemplates.get(dbName);
	}

}