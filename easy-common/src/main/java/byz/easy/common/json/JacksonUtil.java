package byz.easy.common.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

public class JacksonUtil implements JsonUtil {

	public static JacksonUtil getDefault() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true); // 转换为格式化的json
//		mapper.setSerializationInclusion(Include.ALWAYS); // 序列化的时候序列对象的所有属性
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true); // 默认时间戳, 设置false 搭配 默认时间格式使用
//		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); // 默认时间格式
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // 如果是空对象的时候,不抛异常
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 如果json中有新增的字段并且是实体类类中不存在的，不报错
		JavaType mapJavaType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
		JavaType listJavaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, mapJavaType);
		return new JacksonUtil(mapper, mapJavaType, listJavaType);
	}

	protected ObjectMapper mapper;
	protected JavaType mapJavaType;
	protected JavaType listJavaType;

	public JacksonUtil(ObjectMapper mapper, JavaType mapJavaType, JavaType listJavaType) {
		this.mapper = Objects.requireNonNull(mapper, "new JacksonUtil(ObjectMapper, JavaType, JavaType); 参数mapper不能为null");
		this.mapJavaType = Objects.requireNonNull(mapJavaType, "new JacksonUtil(ObjectMapper, JavaType, JavaType); 参数mapJavaType不能为null");
		this.listJavaType = Objects.requireNonNull(listJavaType, "new JacksonUtil(ObjectMapper, JavaType, JavaType); 参数listJavaType不能为null");
	}

	@Override
	public String toString(Object obj) throws IOException {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new IOException("序列json对象出现错误", e);
		}
	}

	@Override
	public Map<String, Object> jsonToMap(String jsonStr) throws IOException {
		try {
			return mapper.readValue(jsonStr, mapJavaType);
		} catch (JsonProcessingException e) {
			throw new IOException("json转换Map<String, Object>错误", e);
		}
	}

	@Override
	public <T> T jsonToObject(String jsonStr, Class<T> c) throws IOException {
		try {
			return mapper.readValue(jsonStr, c);
		} catch (JsonProcessingException e) {
			throw new IOException("json转换T错误", e);
		}
	}

	@Override
	public List<Map<String, Object>> jsonToList(String jsonStr) throws IOException {
		try {
			return mapper.readValue(jsonStr, listJavaType);
		} catch (JsonProcessingException e) {
			throw new IOException("json转换List<Map<String, Object>>错误", e);
		}
	}

	@Override
	public <T> List<T> jsonToList(String jsonStr, Class<T> c) throws IOException {
		try {
			return mapper.readValue(jsonStr, mapper.getTypeFactory().constructCollectionType(List.class, c));
		} catch (JsonProcessingException e) {
			throw new IOException("json转换List<T>错误", e);
		}
	}

	@Override
	public String toString(File file) throws IOException {
		return mapper.readTree(file).toString();
	}

	@Override
	public Object load(File file) throws IOException {
		return mapper.readValue(file, Object.class);
	}

	@Override
	public <T> T load(File file, Class<T> c) throws IOException {
		return mapper.readValue(file, c);
	}

	@Override
	public void save(Object obj, File file) throws IOException {
		mapper.writeValue(new FileOutputStream(file), obj);
	}

}
