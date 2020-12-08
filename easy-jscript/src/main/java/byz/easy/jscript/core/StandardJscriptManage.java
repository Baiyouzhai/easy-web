package byz.easy.jscript.core;

public class StandardJscriptManage implements JscriptManage {

	protected JscriptSourceManage sourceManage;
	protected JscriptEngineWapper engineWapper;

	public StandardJscriptManage(JscriptSourceManage sourceManage, JscriptEngineWapper engineWapper) {
		this.sourceManage = sourceManage;
		this.engineWapper = engineWapper;
	}

	@Override
	public JscriptSourceManage getJscriptSourceManage() {
		return sourceManage;
	}

	@Override
	public JscriptEngine getJscriptEngine() {
		return engineWapper.getJscriptEngine();
	}

}
