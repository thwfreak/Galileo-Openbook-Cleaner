package de.scrum_master.util;

import java.io.PrintStream;

// TODO: This class could be replaced by java.util.logging.Logger,
// but it is even simpler, so we keep it for the moment.

public class SimpleLogger
{
	public static boolean ECHO    = true;
	public static boolean VERBOSE = false;
	public static boolean DEBUG   = false;
	public static boolean ERROR   = true;
	public static boolean TIME    = true;

	private static InheritableThreadLocal<Integer> indentLevel =
		new InheritableThreadLocal<Integer>() {
			@Override protected Integer initialValue() { return 0; }
		};

	private static InheritableThreadLocal<String> indentText =
		new InheritableThreadLocal<String>() {
			@Override protected String initialValue() { return ""; }
		};

	public static void echo   (String message) { log(System.out, ECHO,    message); }
	public static void verbose(String message) { log(System.out, VERBOSE, message); }
	public static void debug  (String message) { log(System.out, DEBUG,   message); }
	public static void error  (String message) { log(System.err, ERROR,   message); }

	public static void time(String header, long milliSeconds) {
		log(System.out, TIME, header + ": " + milliSeconds / 1000.0 + " s");
	}

	public static void indent() {
		indentLevel.set(indentLevel.get() + 1);
		indentText.set(indentText.get() + "  ");
	}

	public static void dedent() {
		if (!indentLevel.get().equals(0)) {
			indentLevel.set(indentLevel.get() - 1);
			indentText.set(indentText.get().substring(2));
		}
	}

	private static void log(PrintStream channel, boolean isActive, String message) {
		if (!isActive)
			return;
		channel.println(indentText.get() + message);
	}
}
