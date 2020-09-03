package byz.easy.database.oracle;

import java.util.Map.Entry;

import byz.easy.database.AbstractTextColumn;

/**
 * @author 
 * @since 2019年11月3日
 */
public class TextColumn extends AbstractTextColumn {

	public TextColumn(String columnName, String comments, CharSequence defaultValue) {
		super(columnName, comments, defaultValue);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setLength(Long length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCreateColumnSQL() {
		return null;
	}

	@Override
	public String getUpdateCommentsSQL() {
		return null;
	}

}
