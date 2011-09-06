package ch.tkuhn.exxo;

import java.util.ArrayList;
import java.util.List;

public class Statement {
	
	private List<String> tags = new ArrayList<String>();
	private String signature = "";
	private String text;
	
	public Statement(String s) {
		int i = s.indexOf(":");
		if (i > -1) {
			signature = s.substring(0,i).replaceAll("\\s+", " ");
			signature = signature.replaceAll("^\\s*", "").replaceAll("\\s*$", "");
			for (String t : signature.split(" ")) {
				tags.add(t);
			}
			text = s.substring(i+1);
		} else {
			text = s;
		}
		text = text.replaceAll("^\\s*", "").replaceAll("\\s*$", "");
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}
	
	public String getSignature() {
		return signature;
	}
	
	public String getText() {
		return text;
	}

}
