package de.scrum_master.galileo.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.scrum_master.util.SimpleLogger;

/**
 * Currently this class only fixes one specific file: <i>ruby_on_rails_2/index.htm</i>.
 * If different types of pre-Tidy fixing should ever be needed, refactor this class into
 * a base class with specialised subclasses.
 */
public class PreJTidyFilter extends BasicFilter
{
	protected BufferedReader input;
	protected PrintStream output;
	protected String line;

	protected static final String FILE_EXTENSION = ".pretidy";

	private static final Pattern REGEX_TITLE      = Pattern.compile("(<title>.+)(Ruby on Rails 2 .+Entwickler.+)");
	private static final Pattern REGEX_MAIN_TABLE = Pattern.compile("<table .*bgcolor=.#eeeeee.*");

	public PreJTidyFilter(InputStream in, OutputStream out, File origFile) {
		super(in, out, origFile);
		input = new BufferedReader(new InputStreamReader(in));
		output = new PrintStream(out);
	}

	@Override
	protected void filter() throws Exception {
		SimpleLogger.indent();
		Matcher matcher;
		while ((line = input.readLine()) != null) {
			if ((matcher = REGEX_TITLE.matcher(line)).matches()) {
				SimpleLogger.debug("Found title tag, adding dummy text as a workaround for later being cut off too much");
				line = matcher.group(1) + "dummy - " + matcher.group(2);
			}
			if (REGEX_MAIN_TABLE.matcher(line).matches()) {
				SimpleLogger.debug("Found main content table, inserting missing </table> tag before it");
				output.println("</table>");
			}
			output.println(line);
		}
		SimpleLogger.dedent();
	}

	@Override
	protected String getDebugLogMessage() {
		return "Fixing HTML so as to enable JTidy to parse it";
	}
}
