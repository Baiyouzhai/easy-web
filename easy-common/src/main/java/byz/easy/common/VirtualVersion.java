package byz.easy.common;

/**
 * 
 * @author 
 * @since 2020年6月29日
 */
public interface VirtualVersion {

	/**
	 * 版本名
	 * @return
	 */
	String getVersion();

	/**
	 * 状态
	 * @return
	 */
	int getVersionStatus();

	/**
	 * 类型
	 * @return
	 */
	int getVersionType();

}
