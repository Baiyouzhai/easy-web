package byz.easy.jscript;

import byz.easy.common.VirtualFile;
import byz.easy.jscript.core.itf.Jscript;

/**
 * @author 
 * @since 2020年8月4日
 */
public class JscriptFile implements VirtualFile<Jscript, JscriptFolder> {

	protected JscriptFolder folder;
	protected String name;
	protected Jscript jscript;

	public JscriptFile(String name) {
		this(null, name, null);
	}

	public JscriptFile(String name, Jscript jscript) {
		this(null, name, jscript);
	}

	public JscriptFile(JscriptFolder folder, String name, Jscript jscript) {
		this.folder = folder;
		this.name = name;
		this.jscript = jscript;
	}

	@Override
	public JscriptFolder getFolder() {
		return folder;
	}

	public void setFolder(JscriptFolder folder) {
		this.folder = folder;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Jscript get() {
		return jscript;
	}

	public void set(Jscript jscript) {
		this.jscript = jscript;
	}

}
