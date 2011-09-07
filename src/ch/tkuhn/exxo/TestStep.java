package ch.tkuhn.exxo;

import java.util.ArrayList;
import java.util.Collections;
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

public class TestStep extends ExperimentStep {
	
	private final static String NO_CHOICE = "-";
	private final static String FALSE = "F";
	private final static String TRUE = "T";
	private final static String DONT_KNOW = "D";
	
	private String img;
	private List<Statement> statements;
	private List<ButtonGroup> buttonGroups = new ArrayList<ButtonGroup>();
	private String expl;
	private boolean force;
	
	public TestStep(String series, Map<String, String> arguments, Experiment experiment) {
		super(arguments, experiment);
		
		img = arguments.get("img");
		if (img == null) {
			img = series.split(":")[0];
		}
		statements = getResources().getStatements(series);
		
		String shuffle = arguments.get("shuffle");
		if (shuffle == null || !shuffle.equals("off")) {
			Collections.shuffle(statements);
		}
		
		expl = arguments.get("expl");
		
		force = "on".equals(arguments.get("force"));
		
		setDefaultTitle(getIntlText("title_test_step"));
	}
	
	public Component getPage() {
		Column mailCol = new Column();
		mailCol.setInsets(new Insets(15, 20));
		
		RowLayoutData layout = new RowLayoutData();
		layout.setAlignment(new Alignment(Alignment.LEFT, Alignment.TOP));
		
		Row mainRow = new Row();
		mainRow.setCellSpacing(new Extent(20));
		
		if (!"off".equals(img)) {
			Component image = getResources().getImage(img);
			image.setLayoutData(layout);
			mainRow.add(image);
		}
		
		Column statCol = new Column();
		
		if (expl != null) {
			String explText = getResources().getResourceContent(expl + ".txt");
			statCol.add(ExpApp.getHTMLComponent(explText));
			statCol.add(new VSpace(20));
		}
		
		statCol.setInsets(new Insets(20, 10, 20, 20));
		statCol.setLayoutData(layout);
		
		statCol.add(new Label(getIntlText("heading_classification_task"), Font.ITALIC | Font.BOLD));
		statCol.add(new VSpace(20));
		
		Grid statementsGrid;
		if (force) {
			statementsGrid = new Grid(3);
		} else {
			statementsGrid = new Grid(4);
		}
		statementsGrid.setInsets(new Insets(0, 5, 0, 0));
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
			statementsGrid.add(createRadioButton("classify_true", group));
			statementsGrid.add(createRadioButton("classify_false", group));
			if (!force) {
				statementsGrid.add(createRadioButton("no_classification", group));
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
		statCol.add(statementsGrid);
		mainRow.add(statCol);
		
		mailCol.add(mainRow);
		
		return mailCol;
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
			if (choice == TRUE && st.hasTag("+")) {
				corr++;
			} else if (choice == FALSE && st.hasTag("-")) {
				corr++;
			} else if (choice == DONT_KNOW) {
				dontKnow++;
			} else if (choice == DONT_KNOW || choice == NO_CHOICE) {
				noChoice++;
			} else {
				incorr++;
			}
		}
		log("Choice:\n" + s);
		int scoreC = new Integer(getApp().getVariableValue("scorec"));
		int scoreD = new Integer(getApp().getVariableValue("scored"));
		int score = corr * scoreC + (dontKnow + noChoice) * scoreD;
		String d = "";
		if (!force) d = ", d=" + dontKnow;
		log("$ Score: " + score + " (c=" + corr + ", i=" + incorr + d + ", n=" + noChoice + ")");
	}
	
	public boolean hasProceedConfirmation() {
		return true;
	}
	
	private String getChoice(int index) {
		for (Object o : buttonGroups.get(index).getButtons()) {
			RadioButton r = (RadioButton) o;
			if (r.isSelected() && r.getActionCommand().equals("classify_true")) return TRUE;
			if (r.isSelected() && r.getActionCommand().equals("classify_false")) return FALSE;
			if (r.isSelected() && r.getActionCommand().equals("no_classification")) return DONT_KNOW;
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
