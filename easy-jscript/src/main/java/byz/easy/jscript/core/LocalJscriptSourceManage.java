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

/**
 * 本地文件资源加载管理器, 负责读取本地(.json)文件转换成JscriptSource, 
 * 
 */
public class LocalJscriptSourceManage implements JscriptSourceManage {

	protected SourceFolderFilter sourceFilter;
	protected boolean recursion;
	protected Map<Jscript, File> fileMapping; // 脚本与文件的映射
	protected Map<Jscript, Map<String, Object>> mapMapping; // 脚本与转换后的map映射(File读取后的map)
	protected Map<Jscript, JscriptSource> sourceMapping; // 脚本与原始JscriptSource(包装)映射

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
	public List<JscriptSource> load() throws JscriptException {
		List<JscriptSource> sources = new ArrayList<>();
		try {
			File[] files = recursion ? (File[]) sourceFilter.getAllFiles().toArray() : sourceFilter.getFiles();
			Map<String, Object> map = null;
			Jscript jscript = null;
			JscriptSource source = null;
			for (File file : files) {
				map = JscriptFileUtil.read(file);
				if (map.containsKey(JscriptTypeName)) {
					jscript = builder.buildJscript((String) map.get(JscriptTypeName), map);
					fileMapping.put(jscript, file);
					mapMapping.put(jscript, map);
					source = new JscriptSource(file.getName(), jscript, map.containsKey(JscriptOrderName) ? (long) map.get(JscriptOrderName) : file.lastModified());
					sources.add(source);
					sourceMapping.put(jscript, source);
				}
			}
		} catch (IOException e) {
			throw new JscriptException("本地资源读取错误", e);
		} catch (Exception e) {
			throw new JscriptException("JscriptClassBuilder构建对象错误", e);
		}
		return sources;
	}

	@Override
	public List<Jscript> getOrderList() {
		return sourceMapping.values().stream().sorted().map(JscriptSource::getJscript).collect(Collectors.toList());
	}

	@Override
	public void save(JscriptSource jscriptSource) {
		JscriptSource source = sourceMapping.get(jscriptSource.getJscript());
		File file = null;
		Map<String, Object> map = null;
		if (null != source) { // 在内存中的脚本
			file = fileMapping.get();
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
	public void delete(JscriptSource jscriptSource) {
		originalMapSources.remove(jscript);
		File file = sourceMapping.remove(jscript);
		if (null != file)
			file.delete();
	}

	@Override
	public void update(JscriptSource old, JscriptSource _new) {
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
