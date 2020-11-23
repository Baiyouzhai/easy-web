package byz.easy.jscript.core;

public class LocalJscriptSourceFile extends JscriptSource {

	public LocalJscriptSourceFile(String name, Jscript jscript) {
		super(name, jscript, System.currentTimeMillis());
	}

	public LocalJscriptSourceFile(String name, Jscript jscript, long order) {
		super(name, jscript, order);
	}

}
