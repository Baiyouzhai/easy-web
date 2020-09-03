package byz.easy.database;

import java.util.List;

/**
 * @author 
 * @since 2019年11月3日
 */
public abstract class AbstractDatabase {

	protected List<AbstractTable> tables;

	public List<AbstractTable> getTables() {
		return tables;
	}

	public void setTables(List<AbstractTable> tables) {
		this.tables = tables;
	}

}
