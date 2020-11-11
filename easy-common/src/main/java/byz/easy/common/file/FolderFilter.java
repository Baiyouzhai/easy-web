package byz.easy.common.file;

import java.io.File;
import java.io.FileFilter;

/**
 * 文件夹过滤器
 */
public class FolderFilter implements FileFilter {

	@Override
	public boolean accept(File file) {
		return file.isDirectory();
	}

}
