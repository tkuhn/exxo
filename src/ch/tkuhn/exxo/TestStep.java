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
import java.util.Map;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Row;
import nextapp.echo.app.button.ButtonGroup;
import nextapp.echo.app.layout.GridLayoutData;
import nextapp.echo.app.layout.RowLayoutData;
import ch.uzh.ifi.attempto.echocomp.HSpace;
import ch.uzh.ifi.attempto.echocomp.Label;
import ch.uzh.ifi.attempto.echocomp.MessageWindow;
import ch.uzh.ifi.attempto.echocomp.RadioButton;
import ch.uzh.ifi.attempto.echocomp.VSpace;

public class TestStep extends StatementStep {
	
	private List<ButtonGroup> buttonGroups = new ArrayList<ButtonGroup>();
	private List<AnswerMenu> answerMenus = new ArrayList<AnswerMenu>();
	private boolean force;
	
	public TestStep(String series, Map<String, String> arguments, Experiment experiment) {
		super(series, arguments, experiment);
		
		force = "on".equals(arguments.get("force"));
		
		setDefaultTitle(getIntlText("title_test_step"));
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
		
		statCol.setInsets(new Insets(10));
		statCol.setLayoutData(layout);
		
		Grid statementsGrid;
		
		if (options == null) {
			statCol.add(new Label(getIntlText("heading_classification_task"), Font.ITALIC | Font.BOLD));
			statCol.add(new VSpace(20));
			
			if (force) {
				statementsGrid = new Grid(3);
			} else {
				statementsGrid = new Grid(4);
			}
			statementsGrid.setColumnWidth(0, new Extent(35));
			statementsGrid.setColumnWidth(1, new Extent(35));
			if (!force) {
				statementsGrid.setColumnWidth(2, new Extent(35));
			}
			GridLayoutData centerLayout = new GridLayoutData();
			centerLayout.setAlignment(new Alignment(Alignment.CENTER, Alignment.CENTER));
			Label l1 = new Label(getIntlText("classify_true"), Font.ITALIC, 10);
			l1.setLayoutData(centerLayout);
			statementsGrid.add(l1);
			Label l2 = new Label(getIntlText("classify_false"), Font.ITALIC, 10);
			l2.setLayoutData(centerLayout);
			statementsGrid.add(l2);
			if (!force) {
				Label l3 = new Label(getIntlText("no_classification"), Font.ITALIC, 10);
				l3.setLayoutData(centerLayout);
				statementsGrid.add(l3);
			}
			statementsGrid.add(new Label(""));
			for (Statement st : statements) {
				ButtonGroup group = new ButtonGroup();
				buttonGroups.add(group);
				statementsGrid.add(createRadioButton("+", group));
				statementsGrid.add(createRadioButton("-", group));
				if (!force) {
					statementsGrid.add(createRadioButton("?", group));
				}
				Row r = new Row();
				r.setInsets(new Insets(10, 0, 0, 5));
				r.add(Resources.createStatementComponent(st));
				statementsGrid.add(r);
			}
			statementsGrid.add(new HSpace(35));
			statementsGrid.add(new HSpace(35));
			if (!force) {
				statementsGrid.add(new HSpace(35));
			}
			statementsGrid.add(new HSpace(300));
		} else {
			statCol.add(new Label(getIntlText("heading_questions"), Font.ITALIC | Font.BOLD));
			statCol.add(new VSpace(20));
			
			statementsGrid = new Grid(3);
			statementsGrid.add(new Label(getIntlText("heading_answer"), Font.ITALIC, 10));
			statementsGrid.add(new VSpace(30));
			statementsGrid.add(new Label(getIntlText("heading_question"), Font.ITALIC, 10));
			
			for (Statement st : statements) {
				AnswerMenu m = new AnswerMenu(this, options.toArray(new String[] {}));
				answerMenus.add(m);
				statementsGrid.add(m);
				statementsGrid.add(new VSpace(30));
				statementsGrid.add(createStatementRow(st));
			}
			statementsGrid.add(new HSpace(100));
			statementsGrid.add(new HSpace(20));
			statementsGrid.add(new HSpace(300));
		}

		statementsGrid.setInsets(new Insets(0, 5, 0, 0));
		statCol.add(statementsGrid);
		
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
	
	public boolean proceed() {
		for (int i = 0 ; i < statements.size() ; i++) {
			if (getChoice(i) == NO_CHOICE) {
				getApp().showWindow(new MessageWindow(
						getIntlText("error_message_title"),
						getIntlText("unclassified_statements_error"),
						getIntlText("ok_option")
					));
				return false;
			}
		}
		return true;
	}
	
	public void finish() {
		String s = "";
		int corr = 0;
		int incorr = 0;
		int dontKnow = 0;
		int noChoice = 0;
		for (int i = 0 ; i < statements.size() ; i++) {
			Statement st = statements.get(i);
			String choice = getChoice(i);
			s += "(" + choice + ") " + st.getSignature() + " '" + st.getText() + "'\n";
			if (st.hasTag(choice)) {
				corr++;
			} else if (choice.equals(DONT_KNOW)) {
				dontKnow++;
			} else if (choice.equals(NO_CHOICE)) {
				noChoice++;
			} else {
				incorr++;
			}
		}
		log("Choice:\n" + s);
		int scoreC = 0;
		try {
			scoreC = new Integer(getApp().getVariableValue("scorec"));
		} catch (NumberFormatException ex) {}
		int scoreD = 0;
		try {
			scoreD = new Integer(getApp().getVariableValue("scored"));
		} catch (NumberFormatException ex) {}
		if (scoreC != 0 || scoreD != 0) {
			int score = corr * scoreC + (dontKnow + noChoice) * scoreD;
			String d = "";
			if (!force) d = ", d=" + dontKnow;
			log("$ Score: " + score + " (c=" + corr + ", i=" + incorr + d + ", n=" + noChoice + ")");
		}
	}
	
	private String getChoice(int index) {
		if (options == null) {
			for (Object o : buttonGroups.get(index).getButtons()) {
				RadioButton r = (RadioButton) o;
				if (r.isSelected()) return r.getActionCommand();
			}
		} else {
			return answerMenus.get(index).getSelection();
		}
		return NO_CHOICE;
	}
	
	private RadioButton createRadioButton(String name, ButtonGroup group) {
		RadioButton r = new RadioButton(group);
		r.setActionCommand(name);
		GridLayoutData centerLayout = new GridLayoutData();
		centerLayout.setAlignment(new Alignment(Alignment.CENTER, Alignment.TOP));
		r.setLayoutData(centerLayout);
		return r;
	}

}
