package byz.easy.jscript.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @since 2020年7月24日
 */
@Deprecated
public class SimpleJscript implements Jscript {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String runBody;

	public SimpleJscript() {
	}

	public SimpleJscript(String runBody) {
		setRunBody(runBody);
	}

	@Override
	public String getRunBody() {
		return runBody;
	}

	public void setRunBody(String runBody) {
		this.runBody = null == runBody ? "" : runBody;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("runBody", getRunBody());
		return map;
	}

	@Override
	public void loadMap(Map<String, Object> map) throws JscriptException {
		Object runBody = map.get("runBody");
		if (!(runBody instanceof String))
			throw new JscriptException("runBody应为String类型");
		setRunBody((String) runBody);
	}

}
