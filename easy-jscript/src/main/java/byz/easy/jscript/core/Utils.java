package byz.easy.jscript.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import byz.easy.common.FileSuffixFilter;
import byz.easy.common.FolderFilter;
import byz.easy.jscript.core.itf.Jscript;
import byz.easy.jscript.core.itf.JscriptEngine;

/**
 * @author
 * @since 2019年12月20日
 */
public class Utils {

	public static ObjectMapper mapper = new ObjectMapper();
	public static JavaType mapperJavaType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
	public static FileSuffixFilter fileFilter = new FileSuffixFilter(".json");
	public static FolderFilter folderFilter = new FolderFilter();

	static { // 注册
		try {
			Class.forName("org.byz.easy.jscript.core.SimpleJscript");
			Class.forName("org.byz.easy.jscript.core.SimpleJscriptInit");
			Class.forName("org.byz.easy.jscript.core.SimpleJscriptFunction");
			Class.forName("org.byz.easy.jscript.core.nashorn.NashornEngine");
			Class.forName("org.byz.easy.jscript.core.v8.V8Engine");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Jscript buildJscript(Map<String, Object> map, String typeName) throws JscriptException {
		Object name = map.get(typeName);
		if (null == name)
			throw new JscriptException("未找到 name 所对应的 Jscript 初始化映射 -> {name:" + name + "}");
		Jscript jscript = null;
		try {
			Class<?> c = Jscript.typeMapping.get(name);
			jscript = (Jscript) c.newInstance();
			jscript.loadMap(map);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new JscriptException(name + "引擎需要无参构造方法", e);
		}
		return jscript;
	}

	public static JscriptEngine buildJscriptEngine(String name) throws JscriptException {
		Set<Entry<String, Class<?>>> entrySet = JscriptEngine.typeMapping.entrySet();
		for (Entry<String, Class<?>> entry : entrySet)
			if (entry.getKey().equalsIgnoreCase(name))
				try {
					return (JscriptEngine) entry.getValue().newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new JscriptException(entry.getValue().getName() + "引擎需要无参构造方法", e);
				}
		throw new JscriptException("未找到 name 所对应的 JscriptEngine 初始化映射 -> {name:" + name + "}");
	}

	public static Jscript buildJscript(Map<String, Object> map) throws JscriptException {
		return buildJscript(map, "type");
	}

	public static List<Jscript> getJscripts(File folder) throws IOException, JscriptException {
		List<Map<String, Object>> jsons = getJsonMaps(new File(folder.getPath()));
		List<Jscript> jscripts = new ArrayList<>();
		for (Map<String, Object> json : jsons)
			jscripts.add(buildJscript(json));
		return jscripts;
	}

	public static File[] getFolders(File folder) throws IOException {
		return checkFolder(folder).listFiles(folderFilter);
	}

	public static List<Map<String, Object>> getJsonMaps(File folder) throws IOException, JscriptException {
		File[] files = getJsonFiles(folder);
		List<Map<String, Object>> jsons = new ArrayList<>();
		for (File file : files)
			jsons.add(read(file));
		return jsons;
	}

	public static File[] getJsonFiles(File folder) throws IOException {
		File[] files = checkFolder(folder).listFiles(fileFilter);
		return files;
	}

	public static File checkFolder(File folder) throws IOException {
		if (!folder.exists())
			throw new FileNotFoundException("文件夹不存在 -> " + folder.getAbsolutePath());
		if (!folder.isDirectory())
			throw new IOException("这不是一个文件夹 -> " + folder.getAbsolutePath());
		return folder;
	}

	public static File checkFile(File file) throws IOException {
		if (!file.exists())
			throw new FileNotFoundException("文件不存在 -> " + file.getAbsolutePath());
		if (file.isDirectory())
			throw new IOException("这不是一个文件 -> " + file.getAbsolutePath());
		if (!fileFilter.accept(file))
			throw new IOException("这不是一个json文件 -> " + file.getAbsolutePath());
		return file;
	}

	public static void save(Jscript function, File jsonFile, ObjectMapper mapper) throws JscriptException {
		if (!jsonFile.getParentFile().exists())
			jsonFile.getParentFile().mkdirs();
		try {
			mapper.writeValue(new FileOutputStream(jsonFile), function.toMap());
		} catch (IOException e) {
			throw new JscriptException("保存至本地文件错误 -> {path:" + jsonFile.getAbsolutePath() + ", map:" + function.toMap().toString() + "}", e);
		}
	}

	public static void save(Jscript function, File jsonFile) throws JscriptException {
		save(function, jsonFile, mapper);
	}

	public static String read(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line + "\n");
		}
		in.close();
		return buffer.toString();
	}

	public static Map<String, Object> read(File jsonFile, ObjectMapper mapper) throws JscriptException {
		try {
			checkFile(jsonFile);
			return mapper.readValue(new FileInputStream(jsonFile), mapperJavaType);
		} catch (IOException e) {
			throw new JscriptException("本地json文件读取错误 -> " + jsonFile.getAbsolutePath(), e);
		}
	}

	public static Map<String, Object> read(File jsonFile) throws JscriptException {
		return read(jsonFile, mapper);
	}

}