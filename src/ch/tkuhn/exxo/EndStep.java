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
