package byz.easy.jscript;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import byz.easy.common.JavaUtil;
import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.Utils;
import byz.easy.jscript.core.itf.Jscript;

/**
 * @author
 * @since 2019年12月20日
 */
public class JscriptUtil extends Utils {

	public static JscriptFolder buildJscriptFolder(String path) throws IOException {
		return buildJscriptFolder(new File(JavaUtil.getSystemFormatPath(path)));
	}

	public static JscriptFolder buildJscriptFolder(File folder) throws IOException {
		String[] path = checkFolder(folder).getPath().split(JavaUtil.sysFileSeparator);
		JscriptFolder temp = new JscriptFolder(path[0]);
		for (int i = 1; i < path.length; i++)
			temp = new JscriptFolder(path[i], temp);
		return temp;
	}

	public static void loadJscriptFolder(JscriptFolder folder, Function<File, JscriptFolder> folderCallBack, Function<File, JscriptFile> fileCallBack, boolean recursion) throws IOException, JscriptException {
		loadJscriptFolder(folder, folderCallBack, fileCallBack);
		if (recursion) {
			List<JscriptFolder> folders = folder.getFolders();
			for (JscriptFolder temp : folders)
				loadJscriptFolder(temp, folderCallBack, fileCallBack, recursion);
		}
	}

	public static void loadJscriptFolder(JscriptFolder folder, Function<File, JscriptFolder> folderCallBack, boolean recursion) throws IOException, JscriptException {
		loadJscriptFolder(folder, folderCallBack);
		if (recursion) {
			List<JscriptFolder> folders = folder.getFolders();
			for (JscriptFolder temp : folders)
				loadJscriptFolder(temp, folderCallBack, recursion);
		}
	}

	public static void loadJscriptFolder(JscriptFolder folder, boolean recursion) throws IOException, JscriptException {
		loadJscriptFolder(folder);
		if (recursion) {
			List<JscriptFolder> folders = folder.getFolders();
			for (JscriptFolder temp : folders)
				loadJscriptFolder(temp);
		}
	}

	public static void loadJscriptFolder(JscriptFolder folder, Function<File, JscriptFolder> folderCallBack, Function<File, JscriptFile> fileCallBack) throws IOException, JscriptException {
		loadJscriptFolders(folder, folderCallBack);
		loadJscriptFiles(folder, fileCallBack);
	}

	public static void loadJscriptFolder(JscriptFolder folder, Function<File, JscriptFolder> folderCallBack) throws IOException, JscriptException {
		loadJscriptFolders(folder, folderCallBack);
		loadJscriptFiles(folder);
	}

	public static void loadJscriptFolder(JscriptFolder folder) throws IOException, JscriptException {
		loadJscriptFolders(folder);
		loadJscriptFiles(folder);
	}

	public static List<JscriptFolder> loadJscriptFolders(JscriptFolder folder, Function<File, JscriptFolder> callBack) throws IOException {
		List<JscriptFolder> old = folder.getFolders();
		folder.setFolders(getJscriptFolders(folder, callBack));
		return old;
	}

	public static List<JscriptFolder> loadJscriptFolders(JscriptFolder folder) throws IOException {
		List<JscriptFolder> old = folder.getFolders();
		folder.setFolders(getJscriptFolders(folder));
		return old;
	}

	public static List<JscriptFile> loadJscriptFiles(JscriptFolder folder, Function<File, JscriptFile> callBack) throws IOException, JscriptException {
		List<JscriptFile> old = folder.getFiles();
		folder.setFiles(getJscriptFiles(folder, callBack));
		return old;
	}

	public static List<JscriptFile> loadJscriptFiles(JscriptFolder folder) throws JscriptException, IOException {
		List<JscriptFile> old = folder.getFiles();
		folder.setFiles(getJscriptFiles(folder));
		return old;
	}

	public static List<JscriptFolder> getJscriptFolders(JscriptFolder folder, Function<File, JscriptFolder> callBack) throws IOException {
		File[] folders = getFolders(folder);
		List<JscriptFolder> jscriptFolders = new ArrayList<>();
		JscriptFolder jscriptFolder = null;
		for (File file : folders) {
			jscriptFolder = callBack.apply(file);
			jscriptFolder.setParent(folder);
			jscriptFolders.add(jscriptFolder);
		}
		return jscriptFolders;
	}

	public static List<JscriptFolder> getJscriptFolders(JscriptFolder folder) throws IOException {
		File[] folders = getFolders(folder);
		List<JscriptFolder> jscriptFolders = new ArrayList<>();
		for (File file : folders)
			jscriptFolders.add(new JscriptFolder(file.getName(), folder));
		return jscriptFolders;
	}

	public static List<JscriptFile> getJscriptFiles(JscriptFolder folder, Function<File, JscriptFile> callBack) throws IOException {
		File[] files = getJsonFiles(folder);
		List<JscriptFile> jscriptFiles = new ArrayList<>();
		JscriptFile jscriptFile = null;
		for (File file : files) {
			jscriptFile = callBack.apply(file);
			jscriptFile.setFolder(folder);
			jscriptFiles.add(jscriptFile);
		}
		return jscriptFiles;
	}

	public static List<JscriptFile> getJscriptFiles(JscriptFolder folder) throws IOException, JscriptException {
		File[] files = getJsonFiles(folder);
		List<JscriptFile> jscriptFiles = new ArrayList<>();
		for (File file : files)
			jscriptFiles.add(new JscriptFile(folder, file.getName(), buildJscript(read(file))));
		return jscriptFiles;
	}

	public static List<Jscript> getJscripts(JscriptFolder folder) throws IOException, JscriptException {
		return getJscripts(new File(folder.getPath()));
	}

	public static File[] getFolders(JscriptFolder folder) throws IOException {
		return getFolders(new File(folder.getPath()));
	}

	public static File[] getJsonFiles(JscriptFolder folder) throws IOException {
		return getJsonFiles(new File(folder.getPath()));
	}

}