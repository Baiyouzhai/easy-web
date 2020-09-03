package byz.easy.common;

/**
 * 
 * @author
 * @since 2020年7月27日
 */
public interface VirtualOrder extends Comparable<VirtualOrder> {

	/**
	 * 分类次序
	 * 
	 * @return
	 */
	int getOrderType();

	/**
	 * 次序
	 * 
	 * @return
	 */
	long getOrderNumber();

	default int compareTo(VirtualOrder obj) {
		if (null == obj)
			return -1;
		if (getOrderType() == obj.getOrderType()) {
			if (getOrderNumber() == obj.getOrderNumber())
				return 0;
			else
				return getOrderNumber() < obj.getOrderNumber() ? -1 : 1;
		}
		return getOrderType() < obj.getOrderType() ? -1 : 1;
	}

}
