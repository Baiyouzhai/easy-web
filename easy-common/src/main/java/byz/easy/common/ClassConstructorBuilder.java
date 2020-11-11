package byz.easy.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ClassConstructorBuilder<T> implements ClassBuilderFactory<T> {

	protected Class<T> c;
	protected Constructor<T> constructor;

	/**
	 * 此构造方法创建 ClassConstructorBuilder 在执行 {@link #build(Object...)} 会去查找对应的构造方法来调用, 存在较大时间损耗
	 * 推荐使用 {@link #ClassConstructorBuilder(Class, Class[])}
	 * @param c
	 */
	@Deprecated
	public ClassConstructorBuilder(Class<T> c) {
		this.c = c;
	}

	/**
	 * 设定调用的构造方法, 参数类型错误会抛出异常, 如果使用空构造 -> parameterTypes = null
	 * @param c
	 * @param parameterTypes
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public ClassConstructorBuilder(Class<T> c, Class<?>[] parameterTypes) throws NoSuchMethodException, SecurityException {
		this.c = c;
		constructor = null == parameterTypes ? c.getDeclaredConstructor() : c.getDeclaredConstructor(parameterTypes);
		constructor.setAccessible(true);
	}

	/**
	 * @see #ClassConstructorBuilder(Class)
	 */
	@Override
	public T build(Object... args) throws Exception {
		try {
			if (null != constructor)
				return constructor.newInstance(args);
			Class<?>[] parameterTypes = JavaUtil.getDataTypes(args);
			Constructor<?> _constructor = JavaUtil.getConstructor(c, parameterTypes);
			if (null == _constructor)
				new Exception(c.getName() + "没有此构造" + Arrays.toString(parameterTypes));
			_constructor.setAccessible(true);
			return (T) _constructor.newInstance(args);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new Exception(c.getName() + "构造时错误", e);
		}
	}

}