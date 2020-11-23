package test;

import java.io.IOException;

import javax.script.ScriptException;
import javax.sql.DataSource;

import byz.easy.jscript.core.JscriptEngine;
import byz.easy.jscript.nashorn.NashornEngine;
import byz.easy.jscript.springboot.JscriptManage;
import byz.easy.jscript.springboot.JscriptProperties;

import org.springframework.beans.factory.annotation.Autowired;
//import org.byz.easy.web.EnableEasyWebPlug;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

/**
 * @author
 * @since 2020年3月16日
 */
@SpringBootApplication(exclude = { DruidDataSourceAutoConfigure.class, DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
//@SpringBootApplication
@Configuration
public class TestApplication {
	
	@Autowired(required=false)
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private JscriptProperties properties;
	
	@Autowired
    private TransactionTemplate transactionTemplate(DataSource dataSource) {
        return  new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }
	
//	@Bean
	public JscriptEngine getJscriptEngine() throws ScriptException {
		System.out.println("JscriptEngine重写测试");
		return new NashornEngine();
	}
	
//	@Bean
	public JscriptManage getJscriptManage(JscriptEngine engine) throws IOException, ScriptException {
		System.out.println("JscriptManage重写测试");
		JscriptManage manage = new JscriptManage(engine, properties).create();
		manage.getEngine().put("jdbcTemplate", jdbcTemplate);
		return manage;
	}

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

}