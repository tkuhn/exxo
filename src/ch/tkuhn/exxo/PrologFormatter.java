package ch.tkuhn.exxo;

public class PrologFormatter implements LanguageFormatter {

	public String getLanguage() {
		return "PL";
	}

	public String getFormattedText(String text) {
		String k = " style=\"color:#000000; font-weight:bold\"";
		String v = " style=\"color:#8888FF; font-weight:bold\"";
		text = text.replaceAll(":-", "<span" + k + ">:-</span>");
		text = text.replaceAll("\\\\\\+", "<span" + k + ">\\\\+</span>");
		text = text.replaceAll("\\( ", "<span" + k + ">(</span> ");
		text = text.replaceAll(" \\)", " <span" + k + ">)</span>");
		text = text.replaceAll(", ", "<span" + k + ">,</span> ");
		text = text.replaceAll(" ; ", " <span" + k + ">;</span> ");
		text = text.replaceAll("\\.", "<span" + k + ">.</span>");
		text = text.replaceAll("X", "<span" + v + ">X</span>");
		text = text.replaceAll("Y", "<span" + v + ">Y</span>");
		return text;
	}

}
