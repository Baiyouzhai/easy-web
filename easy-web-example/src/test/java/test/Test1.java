package test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test1 {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Test
	public void test() {
		TransactionTemplate transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(jdbcTemplate.getDataSource()));
		transactionTemplate.executeWithoutResult(status -> {
			System.out.println("更新前");
			List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select * from dept");
			for (Map<String, Object> map : queryForList)
				System.out.println(map.toString());
			Object savepoint = status.createSavepoint();
			System.out.println("更新");
			jdbcTemplate.update("update dept set tel=? where did=?", "14", 1);
			System.out.println("更新后");
			queryForList = jdbcTemplate.queryForList("select * from dept");
			for (Map<String, Object> map : queryForList)
				System.out.println(map.toString());
			System.out.println("回滚后");
			status.rollbackToSavepoint(savepoint);
			queryForList = jdbcTemplate.queryForList("select * from dept");
			for (Map<String, Object> map : queryForList)
				System.out.println(map.toString());
		});
	}

}