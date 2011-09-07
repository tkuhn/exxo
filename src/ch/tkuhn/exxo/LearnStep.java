package ch.tkuhn.exxo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Row;
import nextapp.echo.app.layout.RowLayoutData;
import ch.uzh.ifi.attempto.echocomp.Label;
import ch.uzh.ifi.attempto.echocomp.VSpace;

public class LearnStep extends ExperimentStep {
	
	private String img;
	private List<Statement> statements;
	private String expl;
	
	public LearnStep(String series, Map<String, String> arguments, Experiment experiment) {
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
		statCol.setCellSpacing(new Extent(5));
		statCol.add(new Label(getIntlText("heading_true_statements"), Font.ITALIC | Font.BOLD));
		statCol.add(new VSpace(5));
		for (Statement st : statements) {
			if (!st.hasTag("+")) continue;
			Row r = new Row();
			r.add(Resources.createStatementComponent(st));
			statCol.add(r);
		}

		statCol.add(new VSpace(20));
		statCol.add(new Label(getIntlText("heading_false_statements"), Font.ITALIC | Font.BOLD));
		statCol.add(new VSpace(5));
		for (Statement st : statements) {
			if (!st.hasTag("-")) continue;
			Row r = new Row();
			r.add(Resources.createStatementComponent(st));
			statCol.add(r);
		}
		mainRow.add(statCol);
		
		mailCol.add(mainRow);
		return mailCol;
	}
	
	public boolean hasProceedConfirmation() {
		return true;
	}

}
