package byz.easy.jscript.core;

import java.util.Set;
import java.util.logging.Logger;

/**
 * Jscript 运行所需要的载体
 * 
 * @author
 * @since 2019年12月19日
 */
public interface JscriptEngine {

	Logger logger = JscriptLogger.getLogger();

	/**
	 * 引擎名称
	 * 
	 * @return
	 */
	String getJscriptEngineName();

	/**
	 * 获取引擎核心
	 * 
	 * @return
	 */
	Object getScriptEngine();

	/**
	 * 代码将直接注入到引擎当中，使用时需要注意引擎当中的内容被覆盖
	 * 
	 * @param script
	 * @return
	 * @throws JscriptException
	 */
	@Deprecated
	Object execute(String script) throws JscriptException;

	/**
	 * 设置变量数据
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	Object put(String name, Object value);

	/**
	 * 
	 * @param jscript
	 * @return
	 * @throws JscriptException
	 */
	Object put(Jscript jscript) throws JscriptException;

	/**
	 * 所有变量名
	 * 
	 * @return
	 */
	Set<String> getNames();

	/**
	 * 获取变量
	 * 
	 * @param name
	 * @return
	 */
	Object get(String name);

	/**
	 * 移除变量
	 * 
	 * @param name
	 * @return
	 */
	Object remove(String name);

	/**
	 * 运行函数
	 * 
	 * @param name
	 * @param args
	 * @return
	 * @throws JscriptException
	 */
	Object run(String name, Object... args) throws JscriptException;
	
	default JscriptEngine getTempEngine() throws JscriptException {
		return getTempEngine(true);
	}

	/**
	 * 获取临时引擎，用于运行测试内容
	 * 
	 * @param hasInitData 是否包含初始化时的数据
	 * @return
	 * @throws JscriptException
	 */
	JscriptEngine getTempEngine(boolean hasInitData) throws JscriptException;

}