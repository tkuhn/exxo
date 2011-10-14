// This file is part of Exxo.
// Copyright 2011, Tobias Kuhn.
// 
// Exxo is free software: you can redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation, either version 3 of
// the License, or (at your option) any later version.
// 
// Exxo is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
// the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
// General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with Exxo. If
// not, see http://www.gnu.org/licenses/.

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
