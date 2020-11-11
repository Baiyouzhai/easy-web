package byz.easy.jscript.core;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import byz.easy.common.LambdaUtil;
import byz.easy.common.file.FileSuffixFilter;
import byz.easy.common.file.FileUtil;
import byz.easy.common.file.SourceFolderFilter;

public class LocalJscriptSourceManage implements JscriptSourceManage {

	public static String orderName = "order";
	public static String typeName = "type";

	protected SourceFolderFilter sourceFilter;
	protected boolean recursion;
	protected Map<Jscript, File> fileMapping;
	protected Map<Jscript, Map<String, Object>> mapMapping;
	protected Map<Jscript, JscriptSource> sourceMapping;

	public LocalJscriptSourceManage() throws JscriptException {
		this(new File(""), false, new FileSuffixFilter(".json"));
	}

	public LocalJscriptSourceManage(File folder) throws JscriptException {
		this(folder, false, new FileSuffixFilter(".json"));
	}

	public LocalJscriptSourceManage(File folder, boolean recursion, FileFilter fileFilter) throws JscriptException {
		try {
			this.recursion = recursion;
			this.sourceFilter = new SourceFolderFilter(folder, fileFilter);
			this.fileMapping = new HashMap<>();
			this.mapMapping = new HashMap<>();
			this.sourceMapping = new HashMap<>();
		} catch (IOException e) {
			throw new JscriptException("资源过滤器初始错误", e);
		}
	}

	@Override
	public void load() throws JscriptException {
		try {
			File[] files = recursion ? (File[]) sourceFilter.getAllFiles().toArray() : sourceFilter.getFiles();
			Map<String, Object> map = null;
			Jscript jscript = null;
			for (File file : files) {
				map = JscriptFileUtil.read(file);
				if (map.containsKey(typeName)) {
					jscript = builder.buildJscript((String) map.get(typeName), map);
					fileMapping.put(jscript, file);
					mapMapping.put(jscript, map);
					sourceMapping.put(jscript, new JscriptSource(file.getName(), jscript, map.containsKey(orderName) ? (long) map.get(orderName) : file.lastModified()));
				}
			}
		} catch (IOException e) {
			throw new JscriptException("本地资源读取错误", e);
		} catch (Exception e) {
			throw new JscriptException("JscriptClassBuilder构建对象错误", e);
		}
	}

	@Override
	public List<Jscript> getOrderList() {
		return sourceMapping.values().stream().sorted().map(JscriptSource::getJscript).collect(Collectors.toList());
	}

	@Override
	public void save(Jscript jscript) {
		JscriptSource source = sourceMapping.get(jscript);
		File file = null;
		Map<String, Object> map = null;
		if (null != source) { // 在内存中的脚本
			file = fileMapping.get(jscript);
			map = mapMapping.get(jscript);
		} else { // 新脚本
			String type = builder.getTypeName(jscript.getClass());
			if (null == type) {
				throw new JscriptRuntimeException("未注册的Jscript类型 Class: " + jscript.getClass());
			}
			long time = System.currentTimeMillis();
			map = jscript.toMap();
			File folder = sourceFilter.getFolder();
			map.put("order", time);
			map.put("type", type);
			if (jscript instanceof JscriptFunction) // 文件名设置
				file = new File(folder.getAbsolutePath() + "/" + ((JscriptFunction) jscript).getName() + ".json");
			else
				file = new File(folder.getAbsolutePath() + "/" + time + ".json");
		}
		try {
			JscriptFileUtil.save(map, file);
		} catch (JscriptException e) {
			throw new JscriptRuntimeException("本地文件保存失败", e);
		}
	}

	@Override
	public void delete(Jscript jscript) {
		originalMapSources.remove(jscript);
		File file = sourceMapping.remove(jscript);
		if (null != file)
			file.delete();
	}

	@Override
	public void update(Jscript old, Jscript _new) {
//		JscriptSourceManage.super.update(old, _new);
		String type = builder.getTypeName(_new.getClass());
		File file = sourceMapping.get(old);
		Map<String, Object> map = _new.toMap();
		if (null != file) {
			map.put("order", originalMapSources.get(old).get("order"));
		} else {
			long time = System.currentTimeMillis();
			if (_new instanceof JscriptFunction)
				file = new File(folder.getAbsolutePath() + "/" + ((JscriptFunction) _new).getName() + ".json");
			else
				file = new File(folder.getAbsolutePath() + "/" + time + ".json");
		}
		map.put("type", type);
		try {
			JscriptFileUtil.save(map, file);
		} catch (JscriptException e) {
			throw new JscriptRuntimeException("本地文件保存失败", e);
		}
	}

	@Override
	public List<JscriptSource> getJscriptSources() throws JscriptException {
		return sources;
	}

}
