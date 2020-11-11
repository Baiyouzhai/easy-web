package byz.easy.common.file;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 资源(文件)过滤器
 */
public class SourceFolderFilter {

	protected File folder;
	protected FileFilter filter;

	public SourceFolderFilter(File folder, FileFilter filter) throws IOException {
		this.folder = FileUtil.checkFolder(Objects.requireNonNull(folder, "new SourceFolderFilter(File, FileFilter); 参数folder不能为null"));
		this.filter = Objects.requireNonNull(filter, "new SourceFolderFilter(File, FileFilter); 参数filter不能为null");
	}

	public File getFolder() {
		return folder;
	}
	
	public File[] getFiles() {
		return folder.listFiles(filter);
	}
	
	public List<File> getAllFiles() throws IOException {
		List<File> temp = new LinkedList<>();
		List<File> allFolders = FileUtil.getAllFolders(folder);
		for (File _folder : allFolders)
			temp.addAll(Arrays.asList(_folder.listFiles(filter)));
		return temp;
	}

}
