package byz.easy.jscript.core;

import java.text.NumberFormat;
import java.util.Arrays;

public class JscriptSource implements Comparable<JscriptSource> {

	protected String name;
	protected Jscript jscript;
	protected long order;

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
	public int compareTo(JscriptSource obj) {
		if (null == obj)
			return -1;
		if (order == obj.order)
			return 0;
		return order < obj.order ? -1 : 1;
	}

	public static void main(String[] args) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		double count = 0;
		double temp = 300;
		String[] array = new String[5];
		for (int i = 0; i < 7;) {
			for (int j = 1; j <= 5; j++) {
				temp = (temp + Math.pow((8 * i), 1.7)) * (1 + 0.05 * (i * j + j));
				count += temp;
				array[j - 1] = nf.format(temp);
			}
			System.out.println(Arrays.toString(array));
		}
		System.out.println(Math.round(count));
	}

}
