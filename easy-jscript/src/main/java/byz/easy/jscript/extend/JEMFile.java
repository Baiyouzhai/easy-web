package byz.easy.jscript.extend;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import byz.easy.jscript.core.itf.Jscript;

/**
 * 用于管理本地文件与引擎的统一管理对象
 * 
 * @author
 * @since 2020年8月13日
 */
public interface JEMFile {

	/**
	 * 状态标识是否需要重置
	 * 
	 * @return
	 */
	public int getStatus();

	/**
	 * 设置基本文件夹目录
	 * 
	 * @param folder 必须是文件夹
	 * @return
	 * @throws IOException
	 */
	public JEMFile setBaseFolder(File folder) throws IOException;

	/**
	 * 获取基本文件夹目录
	 * 
	 * @return
	 */
	public File getBaseFolder();

	/**
	 * 扫描 baseFolder 下文件
	 * 
	 * @return
	 * @throws IOException
	 */
	public JEMFile scan() throws IOException;

	/**
	 * 扫描指定文件夹
	 * 
	 * @return
	 * @throws IOException
	 */
	public JEMFile scan(File folder) throws IOException;

	/**
	 * 引用路径与脚本映射
	 * 引用路径 = 基本路径 + 扫描包路径
	 * 
	 * @return
	 */
	public Map<String, Jscript> getMapping();

	/**
	 * 脚本与本地文件的映射
	 * 
	 * @return
	 */
	public Map<Jscript, File> getFileMapping();

}
