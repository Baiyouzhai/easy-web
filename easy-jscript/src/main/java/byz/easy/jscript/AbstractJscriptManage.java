package byz.easy.jscript;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import byz.easy.common.JavaUtil;
import byz.easy.common.LambdaExceptionUtil;
import byz.easy.jscript.core.JscriptException;
import byz.easy.jscript.core.JscriptRuntimeException;
import byz.easy.jscript.core.SimpleJscript;
import byz.easy.jscript.core.SimpleJscriptFunction;
import byz.easy.jscript.core.SimpleJscriptInit;
import byz.easy.jscript.core.itf.Jscript;
import byz.easy.jscript.core.itf.JscriptEngine;
import byz.easy.jscript.core.itf.JscriptEngineManage;
import byz.easy.jscript.core.itf.JscriptFunction;
import byz.easy.jscript.core.itf.JscriptInit;
import byz.easy.jscript.core.itf.JscriptRun;

/**
 * 
 * @author
 * @since 2020年7月23日
 */
public abstract class AbstractJscriptManage implements JscriptEngineManage {

	static {
		orderTypeMapping.put(SimpleJscript.class, 100);
		orderTypeMapping.put(SimpleJscriptInit.class, 200);
		orderTypeMapping.put(SimpleJscriptFunction.class, 300);
	}

	public static void loadJscriptFolders(JscriptFolder folder, boolean recursion) throws IOException {
		JscriptUtil.loadJscriptFolders(folder);
		if (recursion) {
			List<JscriptFolder> folders = folder.getFolders();
			for (JscriptFolder temp : folders)
				loadJscriptFolders(temp, recursion);
		}
	}

	public static boolean checkPackagePath(String[] _package) throws ScriptException {
		for (String temp : _package) {
			try {
				JavaUtil.checkFolderName(temp);
			} catch (IOException e) {
				throw new JscriptException("文件夹名错误 _package -> " + e.getMessage(), e);
			}
		}
		return true;
	}

	public static String getPackagePath(String[] _package, String join) {
		return Arrays.stream(_package).collect(Collectors.joining(join));
	}

	public static Object[] toArgs(String[] argNames, Map<String, Object> map) {
		Object[] args = new Object[argNames.length];
		for (int i = 0; i < argNames.length; i++)
			args[i] = map.get(argNames[i]);
		return args;
	}

	public static Object[] toArgs(Jscript jscript, Map<String, Object> map) {
		if (jscript instanceof JscriptFunction)
			return toArgs(((JscriptFunction) jscript).getArgNames(), map);
		else
			return toArgs(new String[0], map);
	}

	public static Object[] toRunnerArgs(String[] _package, String name, String version, String[] argNames, Object... args) {
		Object[] params = new Object[5];
		params[0] = Arrays.stream(_package).collect(Collectors.joining(JavaUtil.fileSeparator));
		params[1] = name;
		params[2] = version;
		params[3] = Arrays.stream(argNames).collect(Collectors.joining(","));
		params[4] = args;
		return params;
	}

	protected JscriptEngine engine;
	protected JscriptFolder folder;
	protected Map<String, Jscript> jscriptMapping;
	protected Map<String, JscriptFunction> jscriptFunctionMapping;
	protected Map<Jscript, JscriptFile> fileMapping; // 记录实际文件信息与虚拟文件
	protected List<JscriptOrder> orders;
	protected Map<Jscript, JscriptOrder> orderMapping; // 记录实际文件中排序与虚拟文件中排序
	protected Map<Jscript, JscriptVersion> versionMapping; // 记录实际文件中版本与虚拟文件中版本

	public AbstractJscriptManage(String engineName, String savePath) throws ScriptException {
		this(JscriptUtil.buildJscriptEngine(engineName), savePath);
	}

	public AbstractJscriptManage(JscriptEngine engine, String savePath) throws ScriptException {
		try {
			this.engine = engine;
			folder = JscriptUtil.buildJscriptFolder(savePath);
		} catch (IOException e) {
			throw new JscriptException("JscriptFolder 构建错误 -> " + e.getMessage(), e);
		}
		this.engine = engine;
		jscriptMapping = new HashMap<>();
		jscriptFunctionMapping = new HashMap<>();
		fileMapping = new HashMap<>();
		orders = new ArrayList<>();
		orderMapping = new HashMap<>();
		versionMapping = new HashMap<>();
	}

	@Override
	public List<JscriptInit> getInits() throws JscriptRuntimeException {
		try {
			List<JscriptInit> inits = new ArrayList<>();
			Class<?> c = AbstractJscriptManage.class;
			String codeBlock = JscriptUtil.read(c.getResourceAsStream("JavaImport.js"));
			inits.add(new SimpleJscriptInit(codeBlock));
			codeBlock = JscriptUtil.read(c.getResourceAsStream("FunctionContainer.js"));
			inits.add(new SimpleJscriptInit(codeBlock));
			codeBlock = JscriptUtil.read(c.getResourceAsStream("Runner.js"));
			inits.add(new SimpleJscriptInit(codeBlock));
			return inits;
		} catch (IOException e) {
			throw new JscriptRuntimeException("初始化失败，内部文件读取错误", e);
		}
	}

	@Override
	public AbstractJscriptManage create() throws ScriptException {
		try {
			List<JscriptInit> inits = getInits();
			for (JscriptInit init : inits)
				engine.put(init);
		} catch (ScriptException e) {
			throw new JscriptException("初始化失败 -> " + e.getMessage(), e);
		} catch (JscriptRuntimeException e) {
			throw new JscriptException("本地文件读取错误", e);
		}

		Map<Jscript, JscriptFile> fileMapping = new HashMap<>();
		Map<Jscript, JscriptVersion> versionMapping = new HashMap<>();
		try {
			JscriptUtil.loadJscriptFolder(folder, file -> {
				return new JscriptFolder(file.getName());
			}, LambdaExceptionUtil.applyFunction(file -> {
				Map<String, Object> map = JscriptUtil.read(file);
				Jscript jscript = JscriptUtil.buildJscript(map);
				JscriptFile jscriptFile = new JscriptFile(file.getName(), jscript);
				fileMapping.put(jscript, jscriptFile);
				JscriptOrder order = null;
				if (map.containsKey("orderType") && map.containsKey("order")) {
					order = new JscriptOrder((Integer) map.get("orderType"), (Long) map.get("order"), jscript);
				} else {
					order = new JscriptOrder(orderTypeMapping.get(jscript.getClass()), file.lastModified(), jscript);
				}
				orders.add(order);
				orderMapping.put(jscript, order);
				if (map.containsKey("version") && map.containsKey("versionType") && map.containsKey("versionStatus")) {
					versionMapping.put(jscript, new JscriptVersion((String) map.get("version"), (Integer) map.get("versionType"), (Integer) map.get("versionStatus")));
				} else {
					versionMapping.put(jscript, new JscriptVersion("default", 0, 0));
				}
				return jscriptFile;
			}), true);
		} catch (IOException e) {
			throw new JscriptException("初始化失败，内部文件读取错误 -> " + e.getMessage(), e);
		}
		folder.setParent(null);

		orders.sort(Comparator.comparing(obj -> obj, (obj1, obj2) -> {
			if (null == obj1)
				return 1;
			if (null == obj2)
				return -1;
			if (obj1.getType() == obj2.getType()) {
				if (obj1.getOrder() == obj2.getOrder())
					return 0;
				else
					return obj1.getOrder() < obj2.getOrder() ? -1 : 1;
			}
			return obj1.getType() < obj2.getType() ? -1 : 1;
		}));

		Jscript jscript = null;
		for (JscriptOrder order : orders) {
			jscript = order.get();
			put(jscript, fileMapping.get(jscript), versionMapping.get(jscript));
		}

		return this;
	}

	@Override
	public JscriptEngine getEngine() {
		return engine;
	}

	@Override
	public Map<String, Jscript> getMapping() {
		return jscriptMapping;
	}

	public JscriptFolder getFolder() {
		return folder;
	}

	@Override
	public Jscript get(String[] _package, String name) throws ScriptException {
		checkPackagePath(_package);
		String path = getPackagePath(_package, JavaUtil.fileSeparator) + "#" + name;
		JscriptFunction function = jscriptFunctionMapping.get(path);
		if (null != function) {
			return function;
		} else {
			return jscriptMapping.get(path);
		}
	}

	@Override
	public Jscript get(String[] _package, String name, String version, String[] argNames) throws ScriptException {
		checkPackagePath(_package);
		String path = getPackagePath(_package, JavaUtil.fileSeparator) + "#" + name + "@" + version;
		JscriptFunction function = jscriptFunctionMapping.get(path + "@" + Arrays.toString(argNames));
		return null == function ? jscriptMapping.get(path) : function;
	}

	@Override
	public Object put(String[] _package, Map<String, Object> json, String version) throws ScriptException {
		return put(_package, JscriptUtil.buildJscript(json), version);
	}

	@Override
	public Object put(String[] _package, Jscript jscript, String version) throws ScriptException {
		checkPackagePath(_package);
		JscriptFolder folder = this.folder.addFolders(_package);
		JscriptVersion jscriptVersion = new JscriptVersion(version, 1, 1);
		JscriptFile jscriptFile = null;
		long time = System.currentTimeMillis();
		if (jscript instanceof JscriptFunction) {
			jscriptFile = new JscriptFile(folder, "function@" + version + "@" + time + ".json", jscript);
		} else if (jscript instanceof JscriptRun) {
			jscriptFile = new JscriptFile(folder, "init@" + version + "@" + time + ".json", jscript);
		} else {
			jscriptFile = new JscriptFile(folder, "jscript@" + version + "@" + time + ".json", jscript);
		}
		return put(jscript, jscriptFile, jscriptVersion);
	}

	public Object put(Jscript jscript, JscriptFile jscriptFile, JscriptVersion version) throws ScriptException {
		fileMapping.put(jscript, jscriptFile);
		versionMapping.put(jscript, version);
		int status = version.getStatus();
		switch (status) {
			case 1 :
				String path = jscriptFile.getFolder().getPath() + "#" + jscriptFile.getName();
				jscriptMapping.put(path + "@" + version.getName(), jscript);
				if (0 == version.getType())
					jscriptMapping.put(path, jscript);
				if (jscript instanceof JscriptFunction) {
					JscriptFunction temp = (JscriptFunction) jscript;
					if (0 == version.getType())
						jscriptFunctionMapping.put(path, temp);
					path = path + version.getName() + "@" + Arrays.toString(temp.getArgNames());
					JscriptFunction old = jscriptFunctionMapping.put(path, temp);
					engine.execute("FunctionContainer.put('" + path + "','" + Arrays.stream(temp.getArgNames()).collect(Collectors.joining(",")) + "', '" + jscript.getCodeBlock() + "')");
					return old;
				}
				return engine.execute(jscript.getRunBody());
			default :
				return null;
		}
	}

	public Object put(Jscript jscript, JscriptFile jscriptFile, JscriptVersion version, JscriptOrder order) throws ScriptException {
		orderMapping.put(jscript, order);
		return put(jscript, jscriptFile, version);
	}

	@Override
	public Object run(String[] _package, String name, Object... args) throws NoSuchMethodException, ScriptException {
		checkPackagePath(_package);
		String path = getPackagePath(_package, JavaUtil.fileSeparator) + "#" + name;
		JscriptFunction function = jscriptFunctionMapping.get(path);
		if (null != function) {
			Object[] asArgs = toRunnerArgs(_package, name, versionMapping.get(function).getName(), function.getArgNames(), null == args ? new Object[0] : args);
			return engine.run("Runner", asArgs);
		} else {
			Jscript jscript = jscriptMapping.get(path);
			if (null != jscript)
				return engine.execute(jscript.getRunBody());
			throw new NoSuchMethodException("没有可运行的脚本 -> " + path);
		}
	}

	@Override
	public Object run(String[] _package, String name, String version, String[] argNames, Object... args) throws NoSuchMethodException, ScriptException {
		checkPackagePath(_package);
		String path = getPackagePath(_package, JavaUtil.fileSeparator) + "#" + name + "@" + version;
		JscriptFunction function = jscriptFunctionMapping.get(path + "@" + Arrays.toString(argNames));
		if (null != function) {
			Object[] asArgs = toRunnerArgs(_package, name, version, function.getArgNames(), null == args ? new Object[0] : args);
			return engine.run("Runner", asArgs);
		} else {
			Jscript jscript = jscriptMapping.get(path);
			if (null != jscript) {
				return engine.execute(jscript.getRunBody());
			}
			throw new NoSuchMethodException("没有可运行的脚本 -> " + path);
		}
	}

}
