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
