package byz.easy.common.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface JsonStringUtil {

	String toString(Object obj) throws IOException;

	Map<String, Object> jsonToMap(String jsonStr) throws IOException;

	<T> T jsonToObject(String jsonStr, Class<T> c) throws IOException;

	List<Map<String, Object>> jsonToList(String jsonStr) throws IOException;

	<T> List<T> jsonToList(String jsonStr, Class<T> c) throws IOException;

}
