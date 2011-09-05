package ch.tkuhn.exxo;

public class MLLFormatter implements LanguageFormatter {

	public String getLanguage() {
		return "MLL";
	}

	public String getFormattedText(String text) {
		text = text.replaceAll(
				"([A-Z][a-z]+[A-Z][a-zA-Z]+)",
				"<span style=\"font-weight:bold\">$1</span>"
			);
		text = text.replaceAll(
				"(not|and|or|inverse)",
				"<span style=\"color:#00B2B2; font-weight:bold\">$1</span>"
			);
		text = text.replaceAll(
				"(some|only|min|max|exactly)",
				"<span style=\"color:#B200B2; font-weight:bold\">$1</span>"
			);
		return text;
	}

}
