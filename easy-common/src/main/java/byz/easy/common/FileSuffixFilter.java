package byz.easy.common;

import java.io.File;
import java.io.FileFilter;

/**
 * @author 
 * @since 2020年8月4日
 */
public class FileSuffixFilter implements FileFilter {

	protected String suffix;

	/**
	 * 
	 * @param suffix 文件后缀 .exe 或 exe
	 */
	public FileSuffixFilter(String suffix) {
		if (null == suffix) {
			this.suffix = "";
		} else {
			this.suffix = '.' == suffix.charAt(0) ? suffix : '.' + suffix;
		}
	}

	@Override
	public boolean accept(File file) {
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (-1 == index) {
			return "".equals(suffix);
		} else {
			return this.suffix.equalsIgnoreCase(name.substring(index));
		}
	}

}
