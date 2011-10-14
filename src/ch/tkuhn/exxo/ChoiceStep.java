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

import java.util.List;
import java.util.Map;

import nextapp.echo.app.Border;
import nextapp.echo.app.Color;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.Insets;
import nextapp.echo.app.ListBox;
import nextapp.echo.app.Row;
import ch.uzh.ifi.attempto.echocomp.Label;
import ch.uzh.ifi.attempto.echocomp.Style;


public class ChoiceStep extends ExperimentStep {
	
	private List<String> choices;
	private ListBox listBox;
	
	public ChoiceStep(List<String> choices, Map<String, String> arguments, Experiment experiment) {
		super(arguments, experiment);
		this.choices = choices;
		
		setDefaultTitle(getIntlText("title_choice_step"));
		setForwardButtonText(getIntlText("button_run"));
	}

	public Component getPage() {
		Column mainCol = new Column();
		mainCol.setInsets(new Insets(15, 20));
		mainCol.setCellSpacing(new Extent(15));
		mainCol.add(new Label(getIntlText("text_choice")));
		
		Row selectRow = new Row();
		listBox = new ListBox(choices.toArray());
		listBox.setSelectedIndex(0);
		listBox.setBackground(Style.lightBackground);
		listBox.setForeground(Color.BLACK);
		listBox.setBorder(new Border(1, Color.BLACK, Border.STYLE_INSET));
		listBox.setFont(new Font(Style.fontTypeface, Font.PLAIN, new Extent(13)));
		listBox.setHeight(new Extent(400));
		listBox.setWidth(new Extent(300));
		selectRow.add(listBox);
		mainCol.add(selectRow);
		
		return mainCol;
	}
	
	public void finish() {
		String c = listBox.getSelectedValue().toString();
		log("Choice: " + c);
		getExperiment().run(c);
	}

}
