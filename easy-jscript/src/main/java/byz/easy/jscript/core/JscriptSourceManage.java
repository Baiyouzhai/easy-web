package byz.easy.jscript.core;

import java.util.List;

/**
 * JscriptSource资源管理器
 * 
 * @author
 * @since 2020年9月1日
 */
public interface JscriptSourceManage {

	public static String JscriptOrderName = "order";
	public static String JscriptTypeName = "type";

	public static JscriptClassBuilder builder = JscriptClassBuilder.getDefault();

	/**
	 * 加载资源
	 * 
	 * @return
	 * @throws JscriptException
	 */
	List<JscriptSource> load() throws JscriptException;

	/**
	 * 获取排序好的脚本
	 * 
	 * @return
	 */
	List<Jscript> getOrderList();

	/**
	 * 保存/添加资源
	 * 
	 * @param jscriptSource
	 */
	void save(JscriptSource jscriptSource);

	/**
	 * 删除资源
	 * 
	 * @param jscriptSource
	 */
	void delete(JscriptSource jscriptSource);

	/**
	 * 更新资源
	 * 
	 * @param old
	 * @param _new
	 */
	default void update(JscriptSource old, JscriptSource _new) {
		delete(old);
		save(_new);
	}

	/**
	 * 预留, 获取原始资源对象以方便后续操作
	 * 
	 * @return
	 * @throws JscriptException
	 */
	List<JscriptSource> getJscriptSources() throws JscriptException;

}
