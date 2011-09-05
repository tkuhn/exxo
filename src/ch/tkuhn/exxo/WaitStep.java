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
