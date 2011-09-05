package ch.tkuhn.exxo;

import java.util.Map;

import nextapp.echo.app.Column;
import nextapp.echo.app.Component;


public class EmptyStep extends ExperimentStep {
	
	public EmptyStep(Map<String, String> arguments, Experiment experiment) {
		super(arguments, experiment);
	}

	public Component getPage() {
		return new Column();
	}

}
