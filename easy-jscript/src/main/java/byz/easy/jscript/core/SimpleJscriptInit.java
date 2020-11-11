package byz.easy.jscript.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @since 2020年7月24日
 */
public class SimpleJscriptInit implements JscriptInit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String codeBlock;

	public SimpleJscriptInit() {
	}

	public SimpleJscriptInit(String codeBlock) {
		setCodeBlock(codeBlock);
	}

	@Override
	public String getCodeBlock() {
		return codeBlock;
	}

	@Override
	public void setCodeBlock(String codeBlock) {
		this.codeBlock = null == codeBlock ? "" : codeBlock;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codeBlock", codeBlock);
		return map;
	}

	@Override
	public void loadMap(Map<String, Object> map) throws JscriptException {
		Object codeBlock = map.get("codeBlock");
		if (!(codeBlock instanceof String))
			throw new JscriptException("codeBlock应为String类型");
		setCodeBlock((String) codeBlock);
	}

}
