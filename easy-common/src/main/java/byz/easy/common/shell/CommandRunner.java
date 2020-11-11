package byz.easy.common.shell;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class CommandRunner {

	private Process process;
	private PrintWriter cmdIn;
	private CommandConsole outConsole;
	private CommandConsole errConsole;

	public CommandRunner() throws IOException {
		this(System.out, System.err, "utf-8");
	}

	public CommandRunner(OutputStream out, OutputStream err, String charsetName) throws IOException {
		String systemName = System.getProperty("os.name").toLowerCase();
		if (systemName.contains("windows")) {
			process = Runtime.getRuntime().exec("cmd");
		} else {
			process = Runtime.getRuntime().exec("cd ~");
		}
		if (null != out) {
			outConsole = new CommandConsole(process.getInputStream(), out, charsetName);
			outConsole.setName("outConsole");
		}
		if (null != err) {
			errConsole = new CommandConsole(process.getErrorStream(), err, charsetName);
			errConsole.setName("errConsole");
		}
		cmdIn = new PrintWriter(process.getOutputStream());
	}

	public void start() {
		if (null != outConsole) {
			outConsole.start();
		}
		if (null != errConsole) {
			errConsole.start();
		}
	}

	public void openConsole() {
		if (null != outConsole) {
			outConsole.openOut();
		}
		if (null != errConsole) {
			errConsole.openOut();
		}
	}

	public void closeConsole() {
		System.out.println("停止输出");
		if (null != outConsole) {
			outConsole.closeOut();
		}
		if (null != errConsole) {
			errConsole.closeOut();
		}
	}

	public void run(String... commands) {
		for (String command : commands) {
			cmdIn.println(command);
			cmdIn.flush();
		}
	}

	public void stop(String... commands) {
		for (String command : commands) {
			cmdIn.println(command);
			cmdIn.flush();
		}
		cmdIn.close();
		if (null != errConsole) {
			errConsole.close();
		}
		if (null != outConsole) {
			outConsole.close();
		}
		process.destroy();
	}

}