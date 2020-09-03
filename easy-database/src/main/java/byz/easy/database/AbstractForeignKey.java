package byz.easy.database;

/**
 * @author 
 * @since 2019年11月3日
 */
public abstract class AbstractForeignKey<T> extends AbstractColumn<T> {

	protected AbstractTable table;
	protected AbstractColumn<T> column;

	public AbstractForeignKey(String columnName, String comments, T defaultValue) {
		super(columnName, comments, defaultValue);
		this.isForeignKey = true;
	}

	public AbstractForeignKey(String columnName, String comments, T defaultValue, AbstractTable table, AbstractColumn<T> column) {
		this(columnName, comments, defaultValue);
		this.table = table;
		this.column = column;
	}

}
