package byz.easy.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author
 * @since 2020年7月17日
 */
public class JavaUtil {

	public static final String variableNameRegx = "^[a-zA-Z\\$_][a-zA-Z0-9\\$_]*$";
	public static final String packageNameRegx = "^[a-zA-Z\\$_\\u4e00-\\u9fa5][a-zA-Z0-9\\$_\\u4e00-\\u9fa5]*$";

	public static boolean checkPackageName(String name) throws IOException {
		if (StringUtils.isEmpty(name))
			throw new IOException("包名为空：" + name);
		if (-1 != name.indexOf(" "))
			throw new IOException("包名含空格：" + name);
		if (!name.matches(packageNameRegx))
			throw new IOException("包名含非法字符：" + name);
		return true;
	}

	public static boolean checkVariableName(String name) throws Exception {
		if (StringUtils.isEmpty(name))
			throw new Exception("命名不能为空");
		if (!Pattern.matches(variableNameRegx, name))
			throw new Exception("无效的命名：" + name);
		return true;
	}

	public static List<Integer> getCode(int num) {
		char[] charArray = new StringBuilder(Integer.toBinaryString(num)).reverse().toString().toCharArray();
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < charArray.length; i++)
			if (charArray[i] == 49)
				result.add(1 << i);
		return result;
	}

	public static long arrayTotalHash(Object[] objs) {
		long hash = 0;
		for (Object object : objs)
			hash += object.hashCode();
		return hash;
	}

	public static List<String> findPairBracket(char[] array, int begin, int deep, char leftBrack, char rightBrack) {
		StringBuilder builder = new StringBuilder(deep > 0 ? leftBrack + "" : "");
		List<String> list = new ArrayList<>();
		for (int i = begin; i < array.length; i++) {
			if (array[i] == leftBrack) {
				++deep;
				if (deep > 1) {
					List<String> find = findPairBracket(array, i + 1, deep - 1, leftBrack, rightBrack);
					for (String child : find)
						if (!list.contains(child))
							list.add(child);
				}
			} else if (array[i] == rightBrack) {
				--deep;
				if (deep == 0) {
					builder.append(array[i]);
					list.add(builder.toString());
					builder = new StringBuilder();
				}
			}
			if (deep > 0) {
				builder.append(array[i]);
			}
		}
		return list;
	}

	public static Class<?>[] getDataTypes(Object... data) {
		return Arrays.stream(data).map(obj -> {
			return obj.getClass();
		}).toArray(Class<?>[]::new);
	}

	/**
	 * 构造参数优先匹配</br>
	 * 通过权重计算获取最高匹配, 权重 = 8*typeA + 4*typeB + 2*typeC + 1*typeD, 存在typeE出局</br>
	 * typeA - 构造参数类型==参数类型 -> int==int, Integer==Integer</br>
	 * typeB - 构造参数类型==参数类型父类 -> A==? extend A</br>
	 * typeC - 构造参数类型==参数类型转型 -> int==Integer</br>
	 * typeD - 构造参数类型转型==参数类型 -> Integer==int</br>
	 * typeE - 构造参数类型!=参数类型</br>
	 * @param c
	 * @param parameterTypes
	 * @return
	 */
	public static Constructor<?> getConstructor(Class<?>c, final Class<?>... parameterTypes) {
		Constructor<?> constructor = null;
		try {
			constructor = c.getDeclaredConstructor(parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			Constructor<?>[] constructors = c.getDeclaredConstructors();
			Map<Class<?>[], Constructor<?>> mapping = new HashMap<>();
			Class<?>[] _parameterTypes = null;
			for (Constructor<?> _constructor : constructors) {
				_parameterTypes = _constructor.getParameterTypes();
				if (_parameterTypes.length == parameterTypes.length)
					mapping.put(_parameterTypes, _constructor);
			}
			if (!mapping.isEmpty()) {
				Class<?>[][] array = new Class<?>[parameterTypes.length * 8][];
				mapping.keySet().forEach(types -> {
					int typeA = 0, typeB = 0, typeC = 0, typeD = 0, typeE = 0;
					for (int i =0; i < types.length; i++) {
						if (types[i] == parameterTypes[i]) { // 构造参数类型与参数类型一致
							++typeA;
						} else if (types[i] == parameterTypes[i].getSuperclass()) { //构造参数类型是参数类型的父类
							++typeB;
						} else if (types[i] == boolean.class && parameterTypes[i] == Boolean.class) { // 构造参数类型是参数类型的基本类
							++typeC;
						} else if (types[i] == byte.class && parameterTypes[i] == Byte.class) {
							++typeC;
						} else if (types[i] == short.class && parameterTypes[i] == Short.class) {
							++typeC;
						} else if (types[i] == int.class && parameterTypes[i] == Integer.class) {
							++typeC;
						} else if (types[i] == long.class && parameterTypes[i] == Long.class) {
							++typeC;
						} else if (types[i] == float.class && parameterTypes[i] == Float.class) {
							++typeC;
						} else if (types[i] == double.class && parameterTypes[i] == Double.class) {
							++typeC;
						} else if (types[i] == Boolean.class && parameterTypes[i] == boolean.class) { // 构造参数类型是参数类型的封装类
							++typeD;
						} else if (types[i] == Byte.class && parameterTypes[i] == byte.class) {
							++typeD;
						} else if (types[i] == Short.class && parameterTypes[i] == short.class) {
							++typeD;
						} else if (types[i] == Integer.class && parameterTypes[i] == int.class) {
							++typeD;
						} else if (types[i] == Long.class && parameterTypes[i] == long.class) {
							++typeD;
						} else if (types[i] == Float.class && parameterTypes[i] == float.class) {
							++typeD;
						} else if (types[i] == Double.class && parameterTypes[i] == double.class) {
							++typeD;
						} else { // 类型不一致
							++typeE;
						}
					}
					if (0 == typeE)
						array[8*typeA + 4*typeB + 2*typeC + 1*typeD] = types;
				});
				for (int i = array.length - 1; i > 0; --i) {
					if (null != array[i]) {
						constructor = mapping.get(array[i]);
						break;
					}
				}
			}
		}
		if (null == constructor)
			return null;
		constructor.setAccessible(true);
		return constructor;
	}

	/**
	 * 序列化检查
	 * 
	 * @param obj
	 * @return 没有实现序列化的属性
	 * @throws IllegalAccessException
	 */
	public static List<Field> serializeCheck(Object obj) throws IllegalAccessException {
		Field[] fields = obj.getClass().getDeclaredFields();
		List<Field> result = new ArrayList<Field>();
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.get(obj) instanceof Serializable) {
				// 什么都没有
			} else {
				result.add(field);
			}
		}
		return result;
	}

	/**
	 * 序列化准备，返回指定属性值，并赋null
	 * 
	 * @param obj
	 * @param fields 需要赋null的属性
	 * @return 键: {@link Field#getName()}, 值: {@link Field#get(Object)}
	 * @throws IllegalAccessException
	 */
	public static Map<String, Object> serializePrepare(Object obj, List<Field> fields) throws IllegalAccessException {
		Map<String, Object> result = new HashMap<String, Object>();
		for (Field field : fields) {
			result.put(field.getName(), field.get(obj));
			field.set(obj, null);
		}
		return result;
	}

	/**
	 * 序列化准备，套用{@link #serializePrepare(Object, List)} 和
	 * {@link #serializeCheck(Object)}</br>
	 * 对象未实现 {@link Serializable} 的属性将返回，并赋null
	 * 
	 * @param obj
	 * @return 键: {@link String}, 值: {@link Object}
	 * @throws IllegalAccessException
	 */
	public static Map<String, Object> serializePrepare(Object obj) throws IllegalAccessException {
		return serializePrepare(obj, serializeCheck(obj));
	}

	/**
	 * 使用 I/O 流克隆，推荐先使用 {@link #serializePrepare(Object)} 将属性处理好
	 * 
	 * @param <T>
	 * @param obj 对象以及对象属性都要实现 {@link Serializable} 接口，否则序列化异常
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T ioClone(T obj) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(obj);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (T) ois.readObject();
	}

}