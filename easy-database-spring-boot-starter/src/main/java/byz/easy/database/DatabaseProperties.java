package byz.easy.database;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author
 * @since 2019年12月27日
 */
@ConfigurationProperties(prefix = "database")
public class DatabaseProperties {

	private List<DruidDataSource> moreDataBase = new ArrayList<DruidDataSource>();

	public DatabaseProperties() {
	}

	public DatabaseProperties(List<DruidDataSource> moreDataBase) {
		this.moreDataBase = moreDataBase;
	}

	public List<DruidDataSource> getMoreDataBase() {
		return moreDataBase;
	}

	public void setMoreDataBase(List<DruidDataSource> moreDataBase) {
		this.moreDataBase = moreDataBase;
	}

}