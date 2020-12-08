package test;

import java.io.File;
import java.util.List;

import byz.easy.common.file.FileSuffixFilter;
import byz.easy.jscript.core.Jscript;
import byz.easy.jscript.core.JscriptFileUtil;
import byz.easy.jscript.core.LocalJscriptSourceManage;
import byz.easy.jscript.core.StandardJscriptFunction;
import byz.easy.jscript.core.StandardJscriptInit;

public class Test2 {

	public static void main(String[] args) {
		String uri = "F:/GitHub/easy-web/easy-web-example/jscript";
		File folder = new File(uri);
		try {
			LocalJscriptSourceManage fileLoader = new LocalJscriptSourceManage(folder, true, new FileSuffixFilter(".json"));
			fileLoader.load();
			List<Jscript> list = fileLoader.getOrderList();
			for (Jscript jscript : list) {
				System.out.println(jscript.toMap().toString());
			}
			Jscript fun = new StandardJscriptFunction("aaaa", "temp = 11; return temp;");
			fileLoader.save(fun);
			Thread.sleep(8000);
			fileLoader.delete(fun);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
