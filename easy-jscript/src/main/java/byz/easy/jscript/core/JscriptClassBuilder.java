package byz.easy.jscript.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import byz.easy.common.ClassBuilderFactory;
import byz.easy.common.ClassBuilderRegister;
import byz.easy.common.LambdaUtil;

/**
 * @author
 * @since 2019年12月20日
 */
public class JscriptClassBuilder extends ClassBuilderRegister {

	private static JscriptClassBuilder $default = getDefault();

	public static JscriptClassBuilder getDefault() {
		try {
			if (null == $default) {
				$default = new JscriptClassBuilder();
				$default.registJscript("js", SimpleJscript.class);
				$default.registJscript("var", SimpleJscriptVariable.class);
				$default.registJscript("init", SimpleJscriptInit.class);
				$default.registJscript("run", SimpleJscriptRun.class);
				$default.registJscript("function", SimpleJscriptFunction.class);
			}
			return $default;
		} catch (JscriptException e) {
			throw new JscriptRuntimeException("JscriptClassBuilder注册默认值错误", e);
		}
	}

	protected Map<String, Class<?>> typeNameMapping;

	public JscriptClassBuilder() {
		factoryMapping = new ConcurrentHashMap<>();
		typeNameMapping = new ConcurrentHashMap<>();
	}

	@Override
	public ClassBuilderFactory<?> unregist(String name) {
		typeNameMapping.remove(name);
		return super.unregist(name);
	}

	public void registJscript(String name, Class<? extends Jscript> c) throws JscriptException {
		super.regist(name, c, LambdaUtil.apply(array -> {
			try {
				Jscript jscript = c.newInstance();
				if (null != array)
					jscript.loadMap((Map<String, Object>) ((Object[]) array)[0]);
				return jscript;
			} catch (InstantiationException | IllegalAccessException | JscriptException e) {
				throw new JscriptException("Jscript需要无参构造方法: name->" + name, e);
			}
		}));
		typeNameMapping.put(name, c);
	}

	public <T extends Jscript> Jscript buildJscript(String name, Map<String, Object> map) throws JscriptException {
		try {
			return super.build(name, map);
		} catch (NullPointerException e) {
			throw new JscriptException("构建Jscript错误, " + name + "尚未注册", e);
		} catch (Exception e) {
			throw new JscriptException("Jscript构建错误", e);
		}
	}

	public String[] getTypeNames(Class<?> c) {
		return typeNameMapping.entrySet().stream().filter(entry -> {
			return entry.getValue() == c;
		}).map(Map.Entry::getKey).toArray(String[]::new);
	}

	public String getTypeName(Class<?> c) {
		String[] typeNames = getTypeNames(c);
		return typeNames.length == 0 ? null : typeNames[0];
	}

}