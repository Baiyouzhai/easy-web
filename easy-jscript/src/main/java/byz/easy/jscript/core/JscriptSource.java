package byz.easy.jscript.core;

public class JscriptSource implements Comparable<JscriptSource> {

	protected String name; // 对应资源名称
	protected Jscript jscript; // 对应脚本内容
	protected long order; // 对应加载时间顺序

	public JscriptSource(String name, Jscript jscript) {
		this(name, jscript, System.currentTimeMillis());
	}

	public JscriptSource(String name, Jscript jscript, long order) {
		this.name = name;
		this.jscript = jscript;
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Jscript getJscript() {
		return jscript;
	}

	public void setJscript(Jscript jscript) {
		this.jscript = jscript;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JscriptSource other = (JscriptSource) obj;
		if (jscript == null) {
			if (other.jscript != null)
				return false;
		} else if (!jscript.equals(other.jscript))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (order != other.order)
			return false;
		return true;
	}

	@Override
	public int compareTo(JscriptSource obj) {
		if (null == obj)
			return -1;
		if (order == obj.order)
			return 0;
		return order < obj.order ? -1 : 1;
	}

}
