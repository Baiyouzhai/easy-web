package byz.easy.jscript;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import byz.easy.common.JavaUtil;
import byz.easy.common.VirtualFolder;
import byz.easy.jscript.core.JscriptException;

/**
 * @author
 * @since 2020年7月20日
 */
public class JscriptFolder implements VirtualFolder<JscriptFile> {

	protected String name;
	protected JscriptFolder parent;
	protected List<JscriptFolder> folders;
	protected List<JscriptFile> files;

	public JscriptFolder(String name) {
		this(name, null);
	}

	public JscriptFolder(String name, JscriptFolder parent) {
		this.name = name;
		this.folders = new ArrayList<>();
		this.files = new ArrayList<>();
		this.parent = parent;
	}

	@Override
	public JscriptFolder getRoot() {
		return null ==  this.getParent() ? this : getParent().getRoot();
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public JscriptFolder getParent() {
		return parent;
	}

	public void setParent(JscriptFolder parent) {
		this.parent = parent;
	}

	@Override
	public void loadFolders() throws IOException {
		JscriptSerializableUtil.loadJscriptFolders(this);
	}

	@Override
	public List<JscriptFolder> getFolders() {
		return folders;
	}

	public void setFolders(List<JscriptFolder> folders) {
		this.folders = folders;
	}

	@Override
	public JscriptFolder getFolder(String name) {
		String[] names = name.split(JavaUtil.sysFileSeparator);
		if (names.length > 1)
			return getFolder(names);
		List<JscriptFolder> folders = getFolders();
		for (JscriptFolder folder : folders)
			if (folder.getName().equalsIgnoreCase(name))
				return folder;
		return null;
	}

	@Override
	public JscriptFolder getFolder(String... names) {
		JscriptFolder temp = this;
		for (String name : names) {
			temp = temp.getFolder(name);
			if (null == temp)
				return null;
		}
		return temp;
	}

	@Override
	public void loadFiles() throws IOException {
		try {
			JscriptSerializableUtil.loadJscriptFiles(this);
		} catch (JscriptException e) {
			throw new IOException("文件序列化失败 -> " + e.getMessage(), e);
		}
	}

	@Override
	public List<JscriptFile> getFiles() {
		return files;
	}

	public void setFiles(List<JscriptFile> files) {
		this.files = files;
	}

	@Override
	public JscriptFile getFile(String name) {
		for (JscriptFile jscriptFile : files)
			if (jscriptFile.getName().equalsIgnoreCase(name))
				return jscriptFile;
		return null;
	}

	@Override
	public JscriptFolder addFolders(String... names) {
		JscriptFolder folder = this, temp = null;
		for (String name : names) {
			temp = folder.getFolder(name);
			if (null == temp) {
				temp = new JscriptFolder(name, folder);
				folder.getFolders().add(temp);
			}
			folder = temp;
		}
		return folder;
	}

}
