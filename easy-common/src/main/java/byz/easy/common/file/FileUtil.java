package byz.easy.common.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

public class FileUtil {

	public static final String fileSeparator = File.separator;
	public static final String sysFileSeparator = Matcher.quoteReplacement(fileSeparator);
	public static final String folderNameRegx = "[^\\/:\\*\\?\\\\\"<>\\|]*";

	public static FileFilter folderFilter = new FolderFilter();

	public static File[] getFolders(File folder) throws IOException {
		return folder.listFiles(folderFilter);
	}

	public static void recursionFolders(List<File> holder, File folder) throws IOException {
		File[] child = getFolders(folder);
		for (File temp : child) {
			holder.add(temp);
			recursionFolders(holder, temp);
		}
	}

	public static List<File> getAllFolders(File folder) throws IOException {
		List<File> temp = new LinkedList<>();
		temp.add(checkFolder(folder));
		recursionFolders(temp, folder);
		return temp;
	}

	public static File checkFolder(File folder) throws IOException {
		if (!folder.exists())
			throw new FileNotFoundException("文件夹不存在 -> " + folder.getAbsolutePath());
		if (!folder.isDirectory())
			throw new IOException("这不是一个文件夹 -> " + folder.getAbsolutePath());
		return folder;
	}

	public static boolean checkFolderName(String name) throws IOException {
		if (StringUtils.isEmpty(name))
			throw new IOException("文件夹名为空：" + name);
		if (name.length() != name.trim().length())
			throw new IOException("文件夹名前后包含空格：" + name);
//		if (name.length() > 213)
//			throw new IOException("文件夹名过长：" + name);
		if (!name.matches(folderNameRegx))
			throw new IOException("文件夹名包含非法字符：" + name);
		return true;
	}

	public static File checkFile(File file) throws IOException {
		if (!file.exists())
			throw new FileNotFoundException("文件不存在 -> " + file.getAbsolutePath());
		if (file.isDirectory())
			throw new IOException("这不是一个文件 -> " + file.getAbsolutePath());
		return file;
	}

	public static String getSystemFormatPath(String path) {
		return path.replaceAll("\\/", sysFileSeparator).replaceAll("\\\\", sysFileSeparator);
	}

	public static SourceFolderFilter sourceFolderFilter(File folder, FileFilter filter) throws IOException {
		return new SourceFolderFilter(folder, filter);
	}

	public static FileSuffixFilter fileSuffixFilter(String suffix) {
		return new FileSuffixFilter(suffix);
	}

}
