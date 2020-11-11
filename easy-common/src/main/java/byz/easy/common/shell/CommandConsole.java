package byz.easy.common.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class CommandConsole extends Thread {

	private final PrintWriter writer;
	private final BufferedReader reader;
	private boolean close = false;
	private boolean output = false;

	public CommandConsole(InputStream in, OutputStream out, String charsetName) throws UnsupportedEncodingException {
		reader = new BufferedReader(new InputStreamReader(in, charsetName));
		writer = new PrintWriter(out);
	}

	public void openOut() {
		output = true;
	}

	public void closeOut() {
		output = false;
	}

	public void close() {
		close = true;
	}

	@Override
	public void run() {
		try {
			String cmd = null;
			while (!close && null != (cmd = reader.readLine())) { // 完成一次空读
				if (output) {
					writer.println(cmd);
					writer.flush();
				}
			}
//			reader.close();
//			writer.close();
			System.out.println("关闭" + getName());
		} catch (IOException e) {
			throw new RuntimeException("处理命令出现错误：" + e.getMessage());
		}
	}
}