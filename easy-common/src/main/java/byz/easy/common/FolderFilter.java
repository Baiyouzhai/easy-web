package byz.easy.common;

import java.io.File;
import java.io.FileFilter;

/**
 * @author 
 * @since 2020年8月4日
 */
public class FolderFilter implements FileFilter {

	@Override
	public boolean accept(File file) {
		return file.isDirectory();
	}

}
