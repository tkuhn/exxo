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


public class EndStep extends ExperimentStep {
	
	private boolean waitTextEnabled = false;
	private String expl;
	
	public EndStep(Map<String, String> arguments, Experiment experiment) {
		super(arguments, experiment);
		setDefaultTitle(getIntlText("title_end_step"));
		setForwardButtonText(getIntlText("button_restart"));
		if (arguments.get("waittext") != null) {
			waitTextEnabled = arguments.get("waittext").equals("on");
		}
		expl = arguments.get("expl");
		
		log("! Finished !");
	}

	public Component getPage() {
		Column mainCol = new Column();
		mainCol.setInsets(new Insets(15, 20, 35, 20));
		mainCol.setCellSpacing(new Extent(10));
		if (expl != null) {
			String explText = getResources().getResourceContent(expl + ".txt");
			mainCol.add(ExpApp.getHTMLComponent(explText));
		} else {
			mainCol.add(new Label(getIntlText("text_finished")));
		}
		if (waitTextEnabled) {
			mainCol.add(new Label(getIntlText("text_finished_wait")));
		}
		return mainCol;
	}

}
