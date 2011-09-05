package ch.tkuhn.exxo;

import java.util.Map;

import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import ch.uzh.ifi.attempto.echocomp.Label;


public class EndStep extends ExperimentStep {
	
	private boolean waitTextEnabled = false;
	
	public EndStep(Map<String, String> arguments, Experiment experiment) {
		super(arguments, experiment);
		setDefaultTitle(getIntlText("title_end_step"));
		setForwardButtonText(getIntlText("button_restart"));
		if (arguments.get("waittext") != null) {
			waitTextEnabled = arguments.get("waittext").equals("on");
		}
		
		log("! Finished !");
	}

	public Component getPage() {
		Column mailCol = new Column();
		mailCol.setInsets(new Insets(15, 20));
		mailCol.setCellSpacing(new Extent(10));
		mailCol.add(new Label(getIntlText("text_finished")));
		if (waitTextEnabled) {
			mailCol.add(new Label(getIntlText("text_finished_wait")));
		}
		return mailCol;
	}

}
