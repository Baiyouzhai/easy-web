package byz.easy.common.file;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * 文件后缀过滤器
 */
public class FileSuffixFilter implements FileFilter {

	protected String suffix;
	protected boolean ignoreCase;

	/**
	 * 
	 * @param suffix 文件后缀 .exe 或 exe
	 */
	public FileSuffixFilter(String suffix) {
		this(suffix, true);
	}

	/**
	 * 
	 * @param suffix 文件后缀 .exe 或 exe
	 * @param ignoreCase 是否忽略大小写
	 */
	public FileSuffixFilter(String suffix, boolean ignoreCase) {
		if (null == suffix || "".equals(suffix))
			this.suffix = "";
		else
			this.suffix = '.' == suffix.charAt(0) ? suffix : '.' + suffix;
		this.ignoreCase = ignoreCase;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	@Override
	public boolean accept(File file) {
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (-1 == index) {
			return "".equals(suffix);
		} else {
			return ignoreCase ? suffix.equalsIgnoreCase(name.substring(index)) : suffix.equals(name.substring(index));
		}
	}
	
	public File[] getFiles(File folder) throws IOException {
		return FileUtil.checkFolder(folder).listFiles(this);
	}

}