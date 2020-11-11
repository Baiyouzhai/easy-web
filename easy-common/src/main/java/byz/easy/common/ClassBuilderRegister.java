package byz.easy.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ClassBuilderRegister {

	protected Map<String, ClassBuilderFactory<?>> factoryMapping;

	public ClassBuilderRegister() {
		factoryMapping = new ConcurrentHashMap<>();
	}

	/**
	 * @see ClassConstructorBuilder#ClassConstructorBuilder(Class)
	 * @param name
	 * @param c
	 * @return
	 */
	@Deprecated
	public ClassBuilderFactory<?> regist(String name, Class<?> c) {
		return regist(name, new ClassConstructorBuilder<>(c));
	}

	public ClassBuilderFactory<?> regist(String name, Class<?> c, Class<?>[] parameterTypes)
			throws NoSuchMethodException, SecurityException {
		return regist(name, new ClassConstructorBuilder<>(c, parameterTypes));
	}

	public ClassBuilderFactory<?> regist(String name, Class<?> c, Function<Object, ?> callBack) {
		return regist(name, new ClassFunctionBuilder<>(callBack));
	}

	public ClassBuilderFactory<?> regist(String name, ClassBuilderFactory<?> factory) {
		return factoryMapping.put(name, factory);
	}

	public ClassBuilderFactory<?> unregist(String name) {
		return factoryMapping.remove(name);
	}

	public ClassBuilderFactory<?> getFactory(String name) {
		return factoryMapping.get(name);
	}

	/**
	 * 通过已注册的构建方式构建实体
	 * 
	 * @see ClassFunctionBuilder#build(Object...)
	 * @see ClassConstructorBuilder#build(Object...)
	 * @param <T>
	 * @param name
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public <T> T build(String name, Object... obj) throws Exception {
		ClassBuilderFactory<?> factory = factoryMapping.get(name);
		if (null == factory)
			throw new NullPointerException(name + "没有可执行的Factory");
		return (T) factory.build(obj);
	}

}
