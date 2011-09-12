package ch.tkuhn.exxo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import nextapp.echo.app.Row;

public abstract class StatementStep extends ExperimentStep {
	
	public final static String NO_CHOICE = "#";
	public final static String DONT_KNOW = "?";
	
	protected String img;
	protected String imgpos;
	protected List<Statement> statements;
	protected String expl;
	protected List<String> options;
	
	public StatementStep(String series, Map<String, String> arguments, Experiment experiment) {
		super(arguments, experiment);
		
		img = arguments.get("img");
		if (img == null) {
			img = series.split(":")[0];
		}
		imgpos = arguments.get("imgpos");
		statements = getResources().getStatements(series);
		
		String shuffle = arguments.get("shuffle");
		if (shuffle == null || !shuffle.equals("off")) {
			Collections.shuffle(statements);
		}
		
		expl = arguments.get("expl");
		
		String o = arguments.get("options");
		if (o != null) {
			options = Arrays.asList(o.replaceAll("_"," ").split("\\|"));
		}
	}
	
	public boolean hasProceedConfirmation() {
		return true;
	}
	
	protected Row createStatementRow(Statement statement) {
		Row r = new Row();
		r.add(Resources.createStatementComponent(statement));
		return r;
	}
	
	protected String getOptionText(String option) {
		if (option.equals("+")) {
			return getIntlText("classify_true");
		} else if (option.equals("-")) {
			return getIntlText("classify_false");
		} else if (option.equals("?")) {
			return getIntlText("no_classification");
		} else {
			return option;
		}
	}

}
