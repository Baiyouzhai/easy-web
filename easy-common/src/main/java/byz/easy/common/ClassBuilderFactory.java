package byz.easy.common;

@FunctionalInterface
public interface ClassBuilderFactory<T> {

	/**
	 * 参数构建
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public T build(Object... args) throws Exception;

}
