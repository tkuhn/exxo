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

import java.util.Map;

import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import ch.uzh.ifi.attempto.echocomp.Label;


public class WaitStep extends ExperimentStep {
	
	public WaitStep(Map<String, String> arguments, Experiment experiment) {
		super(arguments, experiment);
		setDefaultTitle(getIntlText("title_wait_step"));
		setForwardButtonVisible(false);
		
		log("! Waiting... !");
	}

	public Component getPage() {
		Column mailCol = new Column();
		mailCol.setInsets(new Insets(15, 20));
		mailCol.setCellSpacing(new Extent(10));
		mailCol.add(new Label(getIntlText("text_wait")));
		return mailCol;
	}

}
