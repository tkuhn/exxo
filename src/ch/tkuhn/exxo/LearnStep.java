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

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Font;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Row;
import nextapp.echo.app.layout.RowLayoutData;
import ch.uzh.ifi.attempto.echocomp.HSpace;
import ch.uzh.ifi.attempto.echocomp.Label;
import ch.uzh.ifi.attempto.echocomp.VSpace;

public class LearnStep extends StatementStep {
	
	public LearnStep(String series, Map<String, String> arguments, Experiment experiment) {
		super(series, arguments, experiment);
		
		setDefaultTitle(getIntlText("title_learn_step"));
	}
	
	public Component getPage() {
		Column col = new Column();
		col.setInsets(new Insets(15, 20));
		
		RowLayoutData layout = new RowLayoutData();
		layout.setAlignment(new Alignment(Alignment.LEFT, Alignment.TOP));
		
		Component mainComp;
		Component imgComp;
		if ("top".equals(imgpos) || "bottom".equals(imgpos)) {
			mainComp = new Column();
			imgComp = new Row();
		} else {
			mainComp = new Row();
			imgComp = new Column();
		}
		imgComp.setLayoutData(layout);
		
		if ("off".equals(img)) {
			imgComp = null;
		} else {
			for (String s : img.split("\\|")) {
				imgComp.add(getResources().getImage(s));
				imgComp.add(new HSpace(20));
				imgComp.add(new VSpace(20));
			}
		}
		
		Column statCol = new Column();
		
		if (expl != null) {
			String explText = getResources().getResourceContent(expl + ".txt");
			statCol.add(ExpApp.getHTMLComponent(explText));
			statCol.add(new VSpace(20));
		}
		
		statCol.setLayoutData(layout);
		statCol.setInsets(new Insets(10));
		
		if (options == null) {
			statCol.add(new Label(getIntlText("heading_true_statements"), Font.ITALIC | Font.BOLD));
			statCol.add(new VSpace(10));
			for (Statement st : statements) {
				if (!st.hasTag("+")) continue;
				statCol.add(createStatementRow(st));
				statCol.add(new VSpace(5));
			}
	
			statCol.add(new VSpace(20));
			statCol.add(new Label(getIntlText("heading_false_statements"), Font.ITALIC | Font.BOLD));
			statCol.add(new VSpace(10));
			for (Statement st : statements) {
				if (!st.hasTag("-")) continue;
				statCol.add(createStatementRow(st));
				statCol.add(new VSpace(5));
			}
		} else {
			statCol.add(new Label(getIntlText("heading_examples"), Font.ITALIC | Font.BOLD));
			statCol.add(new VSpace(20));
			for (Statement st : statements) {
				statCol.add(createStatementRow(st));
				statCol.add(createAnswerRow(st));
				statCol.add(new VSpace(10));
			}
		}
		
		if (imgComp == null) {
			mainComp.add(statCol);
		} else if ("bottom".equals(imgpos) || "right".equals(imgpos)) {
			mainComp.add(statCol);
			mainComp.add(new HSpace(50));
			mainComp.add(new VSpace(50));
			mainComp.add(imgComp);
		} else {
			mainComp.add(imgComp);
			mainComp.add(new HSpace(50));
			mainComp.add(new VSpace(50));
			mainComp.add(statCol);
		}
		
		col.add(mainComp);
		return col;
	}
	
	private Row createAnswerRow(Statement statement) {
		Row r = new Row();
		r.add(new Label(getIntlText("heading_answer"), Font.ITALIC | Font.BOLD));
		for (String o : options) {
			if (statement.hasTag(o)) {
				r.add(new HSpace(10));
				r.add(new Label(getOptionText(o)));
			}
		}
		return r;
	}

}
