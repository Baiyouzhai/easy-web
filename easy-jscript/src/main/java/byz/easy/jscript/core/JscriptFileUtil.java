package byz.easy.jscript.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import byz.easy.common.json.JacksonUtil;
import byz.easy.common.json.JsonUtil;

/**
 * @author
 * @since 2019年12月20日
 */
public class JscriptFileUtil {

	public static JsonUtil jsonUtil = getJsonUtil();

	public static JsonUtil getJsonUtil() {
		if (null == jsonUtil)
			jsonUtil = JacksonUtil.getDefault();
		return jsonUtil;
	}

	public static void save(Map<String, Object> map, File target, ObjectMapper mapper) throws JscriptException {
		if (!target.getParentFile().exists())
			target.getParentFile().mkdirs();
		try {
			mapper.writeValue(new FileOutputStream(target), map);
		} catch (IOException e) {
			throw new JscriptException("保存至本地文件错误 -> {\n\tpath: " + target.getAbsolutePath() + ",\n\tmap: " + map.toString() + "\n}", e);
		}
	}

	public static void save(Map<String, Object> map, File target) throws JscriptException {
		try {
			jsonUtil.save(map, target);
		} catch (IOException e) {
			throw new JscriptException("map序列成本地文件错误: {\n\tfile:" + target.getAbsolutePath() + ",\n\tmap: " + map.toString() + "\n}", e)  ;
		}
	}

	public static void save(Jscript jscript, File target) throws JscriptException {
		save(jscript.toMap(), target);
	}

	public static String read(InputStream is, String charset) throws IOException {
		return read(new InputStreamReader(is, charset));
	}

	public static String read(Reader reader) throws IOException {
		BufferedReader _reader = new BufferedReader(reader);
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = _reader.readLine()) != null) {
			buffer.append(line + "\n");
		}
		_reader.close();
		return buffer.toString();
	}

	public static Map<String, Object> read(File jsonFile) throws JscriptException {
		try {
			Object object = jsonUtil.load(jsonFile);
			if (object instanceof Map)
				return (Map<String, Object>) object;
			throw new JscriptException("文件内容不符合解析规则: file -> " + jsonFile.getAbsolutePath());
		} catch (IOException e) {
			throw new JscriptException("本地json文件读取错误 -> " + jsonFile.getAbsolutePath(), e);
		}
	}

}