package byz.easy.jscript.core.itf;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author
 * @since 2020年9月1日
 */
public interface JscriptLoader {

	/**
	 * 设置排序
	 * 
	 * @param jscript
	 * @param order
	 */
	void setOrder(Jscript jscript, long order);

	/**
	 * 获得指定排序
	 * 
	 * @param jscript
	 * @return null为未记录排序内容
	 */
	Long getOrder(Jscript jscript);

	/**
	 * 映射
	 * @return
	 */
	Map<Jscript, Long> getOrderMapping();

	/**
	 * 排序好的脚本
	 * 
	 * @return
	 */
	default List<Jscript> getOrderList() {
		return getOrderMapping().entrySet().stream().sorted((entry1, entry2) -> {
			if (entry1.getValue() == entry2.getValue())
				return 0;
			return entry1.getValue() < entry2.getValue() ? -1 : 1;
		}).map(Map.Entry::getKey).collect(Collectors.toList());
	}

}
