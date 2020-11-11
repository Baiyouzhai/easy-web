package byz.easy.common.json;

import java.io.File;
import java.io.IOException;

public interface JsonFileUtil {

	String toString(File file) throws IOException;

	Object load(File file) throws IOException;

	<T> T load(File file, Class<T> c) throws IOException;

	void save(Object obj, File file) throws IOException;

}
