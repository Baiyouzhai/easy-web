package test;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import byz.easy.jscript.core.Jscript;
import byz.easy.jscript.core.JscriptFileUtil;
import byz.easy.jscript.core.JscriptLogger;
import byz.easy.jscript.core.LocalJscriptSourceManage;
import byz.easy.jscript.core.StandardJscriptFunction;
import byz.easy.jscript.core.StandardJscriptInit;

public class Test3 extends Thread {

	private static Logger logger = JscriptLogger.getLogger();

	public boolean close = false;
	public int target = 0;

	@Override
	public void run() {
		int count = 0;
		while(!close) {
			try {
				Thread.sleep(1000);
				++count;
				if (count == target) {
					throw new RuntimeException("达到最大执行");
				}
				logger.log(Level.INFO, "正常");
			} catch (InterruptedException | RuntimeException e) {
				logger.logp(Level.WARNING, "test.Test3", "run", "线程错误", e);
			}
		}
	}

	public static void main(String[] args) {
		try {
			Test3 t1 = new Test3();
			t1.target = 3;
			Test3 t2 = new Test3();
			t1.target = 7;
			t1.start();
			Thread.sleep(500);
			t2.start();
			Thread.sleep(10000);
			t1.close = true;
			t2.close = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
