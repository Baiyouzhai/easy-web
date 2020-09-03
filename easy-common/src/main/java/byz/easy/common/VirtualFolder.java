package byz.easy.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @author
 * @since 2020年6月29日
 */
public interface VirtualFolder<T> {

	default VirtualFolder<T> getRoot() {
		return null ==  this.getParent() ? this : getParent().getRoot();
	}

	String getName();

	VirtualFolder<T> getParent();

	void loadFolders() throws FileNotFoundException, IOException;

	List<? extends VirtualFolder<T>> getFolders();

	default VirtualFolder<T> getFolder(String name) {
		String[] names = name.split(JavaUtil.sysFileSeparator);
		if (names.length > 1)
			return getFolder(names);
		List<? extends VirtualFolder<T>> folders = getFolders();
		for (VirtualFolder<T> virtualFolder : folders)
			if (virtualFolder.getName().equalsIgnoreCase(name))
				return virtualFolder;
		return null;
	}

	default VirtualFolder<T> getFolder(String... names) {
		VirtualFolder<T> temp = this;
		for (String name : names) {
			temp = temp.getFolder(name);
			if (null == temp)
				return null;
		}
		return temp;
	}

	void loadFiles() throws FileNotFoundException, IOException;

	List<T> getFiles();

	T getFile(String name);

	default String getPath() {
		return getPath(JavaUtil.fileSeparator);
	}

	default String getPath(String join) {
		if (null != getParent())
			return getParent().getPath(join) + join + getName();
		return getName();
	}

	VirtualFolder<T> addFolders(String... names);

}
