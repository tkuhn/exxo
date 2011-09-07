package ch.tkuhn.exxo;

import java.util.Map;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
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
		
		statCol.setLayoutData(layout);
		statCol.setInsets(new Insets(20, 10, 20, 20));
		
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
		
		mainRow.add(statCol);
		
		mailCol.add(mainRow);
		return mailCol;
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
