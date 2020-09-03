package byz.easy.jscript.extend;

import java.util.Map;

import byz.easy.common.VirtualOrder;
import byz.easy.common.VirtualVersion;
import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.itf.JscriptInit;

/**
 * @author 
 * @since 2020年8月12日
 */
public interface JscriptInitExtend extends JscriptInit, VirtualVersion, VirtualOrder {

	@Override
	public default int getVersionStatus() {
		return 0;
	}

	/**
	 * 分类次序 init 100
	 * 
	 * @return
	 */
	@Override
	public default int getOrderType() {
		return 100;
	}

	/**
	 * 执行顺序
	 * 
	 * @return
	 */
	public long getOrder();

	public void setOrder(long order);

	/**
	 * 次序
	 * 
	 * @return
	 */
	@Override
	public default long getOrderNumber() {
		return getOrder();
	}

	@Override
	public default Map<String, Object> toMap() {
		Map<String, Object> map = JscriptInit.super.toMap();
		map.put("order", getOrder());
		return map;
	}

	@Override
	public default void loadMap(Map<String, Object> map) throws JscriptException {
		JscriptInit.super.loadMap(map);

		Object order = map.get("order");
		if (!(order instanceof Long))
			throw new JscriptException("order应为long类型");
		setOrder((Long) order);
	}

}
