package byz.easy.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author
 * @since 2020年7月17日
 */
public class JavaUtil {

	public static final String fileSeparator = File.separator;
	public static final String sysFileSeparator = Matcher.quoteReplacement(fileSeparator);
	public static final String variableNameRegx = "^[a-zA-Z\\$_][a-zA-Z0-9\\$_]*$";
	public static final String packageNameRegx = "^[a-zA-Z\\$_\\u4e00-\\u9fa5][a-zA-Z0-9\\$_\\u4e00-\\u9fa5]*$";
	public static final String folderNameRegx = "[^\\/:\\*\\?\\\\\"<>\\|]*";

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

	public static boolean checkFolderName(String name) throws IOException {
		if (StringUtils.isEmpty(name))
			throw new IOException("文件夹名为空：" + name);
		if (name.length() != name.trim().length())
			throw new IOException("文件夹名前后包含空格：" + name);
		if (name.length() > 213)
			throw new IOException("文件夹名过长：" + name);
		if (!name.matches(folderNameRegx))
			throw new IOException("文件夹名包含非法字符：" + name);
		return true;
	}

	public static String getSystemFormatPath(String path) {
		return path.replaceAll("\\/", sysFileSeparator).replaceAll("\\\\", sysFileSeparator);
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