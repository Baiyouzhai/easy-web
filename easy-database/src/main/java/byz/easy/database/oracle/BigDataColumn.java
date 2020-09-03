package byz.easy.database.oracle;

import byz.easy.database.AbstractBigDataColumn;

/**
 * @author 
 * @since 2019年11月3日
 */
public class BigDataColumn<T> extends AbstractBigDataColumn<T> {

	public BigDataColumn(String columnName, String comments, T defaultValue) {
		super(columnName, comments, defaultValue);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setLength(Long length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCreateColumnSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUpdateCommentsSQL() {
		// TODO Auto-generated method stub
		return null;
	}

}
