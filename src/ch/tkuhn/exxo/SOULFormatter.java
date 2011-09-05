package ch.tkuhn.exxo;

public class SOULFormatter implements LanguageFormatter {

	public String getLanguage() {
		return "SOUL";
	}

	public String getFormattedText(String text) {
		String k = " style=\"color:#006699; font-weight:bold\"";
		String v = " style=\"color:#FF1493\"";
		text = text.replaceAll(" if ", " <span" + k + ">if</span> ");
		text = text.replaceAll(" not\\( ", " <span" + k + ">not(</span> ");
		text = text.replaceAll(" or\\( ", " <span" + k + ">or(</span> ");
		text = text.replaceAll(", ", "<span" + k + ">,</span> ");
		text = text.replaceAll(" \\)", " <span" + k + ">)</span>");
		text = text.replaceAll("(\\?[a-zA-Z0-9]+)", "<span" + v + ">$1</span>");
		return text;
	}

}
