package byz.easy.database;

/**
 * @author 
 * @since 2019年11月3日
 */
public abstract class AbstractPrimaryKey<T> extends AbstractColumn<T> {

	public AbstractPrimaryKey(String columnName, String comments, T defaultValue) {
		super(columnName, comments, defaultValue);
		this.isPrimaryKey = true;
		this.unique = true;
		this.notNull = true;
	}

}
