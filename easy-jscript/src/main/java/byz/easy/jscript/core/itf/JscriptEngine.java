package byz.easy.jscript.core.itf;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptException;

/**
 * Jscript 运行所需要的载体
 * 
 * @author
 * @since 2019年12月19日
 */
public interface JscriptEngine {

	Map<String, Class<?>> typeMapping = new HashMap<>();

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
	 * @throws ScriptException
	 */
	@Deprecated
	Object execute(String script) throws ScriptException;

	/**
	 * 流中执行代码，将直接注入到引擎当中，使用时需要注意引擎当中的内容被覆盖
	 * 
	 * @param reader
	 * @return
	 * @throws ScriptException
	 */
	@Deprecated
	Object execute(Reader reader) throws ScriptException;

	/**
	 * 设置变量数据
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	Object put(String name, Object value);

	Object put(Jscript jscript) throws ScriptException;

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
	 * @throws ScriptException
	 * @throws NoSuchMethodException
	 */
	Object run(String name, Object... args) throws ScriptException, NoSuchMethodException;

	/**
	 * 获取临时引擎，用于运行测试内容
	 * 
	 * @return
	 * @throws ScriptException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	JscriptEngine getTempEngine() throws ScriptException;

}