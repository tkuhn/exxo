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
