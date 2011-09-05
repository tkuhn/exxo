package ch.tkuhn.exxo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nextapp.echo.app.Component;
import nextapp.echo.app.ResourceImageReference;
import ch.uzh.ifi.attempto.echocomp.Label;
import ch.uzh.ifi.attempto.echocomp.SolidLabel;


public class Resources {
	
	private static Map<String, LanguageFormatter> languageFormatters =
		new HashMap<String, LanguageFormatter>();
	
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
	
	public List<Statement> getStatements(String seriesID, String language) {
		String[] seriesIDparts = seriesID.split("/");
		String graphID = seriesIDparts[0];
		String variant = null;
		if (seriesIDparts.length > 1) {
			variant = seriesIDparts[1];
		}
		
		List<Statement> statements = new ArrayList<Statement>();
		String currentLine = null;
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream in = cl.getResourceAsStream(path + graphID + ".txt");
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			in.close();
			String[] lines = new String(bytes, "UTF-8").split("\n");
			for (String line : lines) {
				currentLine = line;
				line = line.replaceFirst("^\\s*", "").replaceFirst("\\s*$", "");
				if (line.equals("")) {
					continue;
				}
				String[] parts = line.split("\\s*:\\s*", 2);
				String[] categories = parts[0].split("\\s+");
				String v = categories[0].replaceFirst("^[0-9]+(.*)[\\+\\-]$", "$1");
				String l = categories[1];
				String text = parts[1];
				if (language.equals(l) && (variant == null || variant.equals(v)) ) {
					statements.add(new Statement(categories[0], l, text));
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
		languageFormatters.put(lf.getLanguage(), lf);
	}
	
	public static Component createStatementComponent(Statement statement) {
		LanguageFormatter lf = languageFormatters.get(statement.getLanguage());
		if (lf != null) {
			String t =
				"<span><nobr>" +
				lf.getFormattedText(statement.getText()) +
				"</nobr></span>";
			return ExpApp.getHTMLComponent(t);
		}
		return new SolidLabel(statement.getText());
	}
	
}
