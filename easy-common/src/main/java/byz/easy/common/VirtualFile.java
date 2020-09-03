package byz.easy.common;

/**
 * @author 
 * @since 2020年8月4日
 */
public interface VirtualFile<T, F extends VirtualFolder<?>> {

	/**
	 * 所在文件夹
	 * @return
	 */
	F getFolder();

	/**
	 * 文件名
	 * @return
	 */
	String getName();

	/**
	 * 虚拟内容
	 * @return
	 */
	T get();

}
