package byz.easy.jscript.core;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class JscriptLogger {

	protected static Logger logger;

	public static Logger getLogger() {
		if (null == logger) {
			logger = Logger.getLogger("jscript");
			try {
				FileHandler fileHandler = new FileHandler("jscript.log", 5000, 1, true);
				fileHandler.setLevel(Level.ALL);
				fileHandler.setEncoding("utf-8");
				fileHandler.setFormatter(new SimpleFormatter());
				logger.addHandler(fileHandler);
			} catch (SecurityException | IOException e) {
				throw new JscriptRuntimeException("读取日志文件错误", e);
			}
		}
		return logger;
	}

	public static void openInfoOut() {
		getLogger().setLevel(Level.ALL);
	}

	public static void closeInfoOut() {
		getLogger().setLevel(Level.OFF);
	}

}
