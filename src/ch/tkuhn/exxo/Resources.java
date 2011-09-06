package ch.tkuhn.exxo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nextapp.echo.app.Component;
import nextapp.echo.app.ResourceImageReference;
import ch.uzh.ifi.attempto.echocomp.Label;
import ch.uzh.ifi.attempto.echocomp.SolidLabel;


public class Resources {
	
	private static List<LanguageFormatter> languageFormatters =	new ArrayList<LanguageFormatter>();
	
	private String path;
	
	public Resources(String path) {
		if (path == null || path.length() == 0) {
			this.path = "ch/tkuhn/exxo/test/";
		} else {
			if (!path.endsWith("/")) path += "/";
			this.path = path;
		}
	}
	
	public Component getImage(String imgName) {
		return new Label(new ResourceImageReference(path + imgName + ".png"));
	}
	
	public List<Statement> getStatements(String series) {
		String file;
		String[] tags;
		int i = series.indexOf(":");
		if (i > -1) {
			file = series.substring(0, i);
			tags = series.substring(i+1).split("\\|");
		} else {
			file = series;
			tags = new String[] {};
		}
		
		List<Statement> statements = new ArrayList<Statement>();
		String currentLine = null;
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream in = cl.getResourceAsStream(path + file + ".txt");
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			in.close();
			String[] lines = new String(bytes, "UTF-8").split("\n");
			for (String line : lines) {
				currentLine = line;
				line = line.replaceFirst("^\\s*", "").replaceFirst("\\s*$", "");
				if (line.equals("") || line.startsWith("#")) {
					continue;
				}
				Statement statement = new Statement(line);
				boolean addStatement = true;
				for (String t : tags) {
					if (!statement.hasTag(t)) {
						addStatement = false;
						break;
					}
				}
				if (addStatement) {
					statements.add(statement);
				}
			}
		} catch (Exception ex) {
			if (currentLine == null) {
				ex.printStackTrace();
			} else {
				System.err.println("Invalid line: " + currentLine);
			}
		}
		return statements;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getResourceContent(String filename) {
		String c = null;
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream in = cl.getResourceAsStream(path + filename);
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			in.close();
			c = new String(bytes, "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return c;
	}
	
	public static void registerLanguageFormatter(LanguageFormatter lf) {
		languageFormatters.add(lf);
	}
	
	public static Component createStatementComponent(Statement statement) {
		for (LanguageFormatter lf : languageFormatters) {
			if (statement.hasTag(lf.getLanguage())) {
				String t =
					"<span><nobr>" +
					lf.getFormattedText(statement.getText()) +
					"</nobr></span>";
				return ExpApp.getHTMLComponent(t);
			}
		}
		return new SolidLabel(statement.getText());
	}
	
}
