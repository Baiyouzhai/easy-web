package byz.easy.html;

import byz.easy.common.shell.CommandRunner;

public class Test {

	public static void main(String[] args) {
		CommandRunner runner = null;
		try {
			runner = new CommandRunner(System.out, System.err, "gbk");
			runner.openConsole();
			runner.start();
			runner.run("F:");
//			Thread.sleep(5 * 1000);
			runner.run("cd F:/GitHub/easy-web/easy-vue-edit-monaco");
//			Thread.sleep(5 * 1000);
			runner.run("yarn run serve");
			Thread.sleep(30 * 1000);
			runner.closeConsole();
			Thread.sleep(10 * 1000);
			System.out.println("准备停止");
			int time = 10;
			for (int i = time; i > 0; i--) {
				Thread.sleep(1000);
				System.out.println(i);
				runner.openConsole();
			}
			runner.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
