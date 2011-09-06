package ch.tkuhn.exxo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Experiment {

	private int count = -1;
	
	private ExpApp app;
	private String name;
	
	private List<String> stepDefs = new ArrayList<String>();
	
	private Experiment subExperiment = null;
	
	public Experiment(String name, ExpApp app) {
		this.app = app;
		this.name = name;
		
		String[] lines = getResources().getResourceContent(name + ".exp").split("\n");
		for (String line : lines) {
			line = line.replaceFirst("^\\s*", "").replaceFirst("\\s*$", "");
			if (!line.equals("")) {
				stepDefs.add(line);
			}
		}
	}
	
	public String getExpStackTrace() {
		if (subExperiment == null) {
			return name;
		} else {
			return name + "/" + subExperiment.getExpStackTrace();
		}
	}
	
	public boolean hasNextStep() {
		return count < stepDefs.size()-1;
	}
	
	public void run(String subExperimentName) {
		this.subExperiment = new Experiment(subExperimentName, app);
	}
	
	public void log(String text) {
		app.log(text);
	}

	public ExpApp getApp() {
		return app;
	}
	
	public Resources getResources() {
		return app.getResources();
	}

	public ExperimentStep getNextStep() {
		if (subExperiment != null) {
			if (subExperiment.hasNextStep()) {
				return subExperiment.getNextStep();
			} else {
				subExperiment = null;
			}
		}
		
		count++;
		if (count >= stepDefs.size()) {
			count = 0;
		}
		
		ExperimentStep step = null;
		String[] tokens = extractTokens(stepDefs.get(count));
		String c = tokens[0];
		log("> " + stepDefs.get(count));
		
		Map<String, String> arguments = extractArguments(tokens);
		
		if (c.equals("identification")) {
			step = new IdentificationStep(arguments, this);
		} else if (c.equals("test")) {
			step = new TestStep(tokens[1], arguments, this);
		} else if (c.equals("learn")) {
			step = new LearnStep(tokens[1], arguments, this);
		} else if (c.equals("feedback")) {
			step = new FeedbackStep(tokens[1], arguments, this);
		} else if (c.equals("end")) {
			step = new EndStep(arguments, this);
		} else if (c.equals("empty")) {
			step = new EmptyStep(arguments, this);
		} else if (c.equals("wait")) {
			step = new WaitStep(arguments, this);
		} else if (c.equals("info")) {
			step = new InfoStep(tokens[1], arguments, this);
		} else if (c.equals("set")) {
			for (String argName : arguments.keySet()) {
				app.setVariableValue(argName, arguments.get(argName));
				if (argName.equals("locale")) {
					String[] localeInfo = arguments.get("locale").split("_");
					if (localeInfo.length == 2) {
						app.setLocale(new Locale(localeInfo[0], localeInfo[1]));
					}
				}
			}
		} else if (c.equals("run")) {
			run(tokens[1]);
			return subExperiment.getNextStep();
		} else if (c.equals("choice")) {
			List<String> t = new ArrayList<String>(Arrays.asList(tokens));
			t.remove(0);
			step = new ChoiceStep(t, arguments, this);
		}
		if (step == null) {
			return getNextStep();
		} else {
			return step;
		}
	}
	
	private String[] extractTokens(String text) {
		return text.split("\\s+");
	}
	
	private Map<String, String> extractArguments(String[] tokens) {
		Map<String, String> arguments = new HashMap<String, String>();
		for (int i = 1 ; i < tokens.length ; i++) {
			String[] argParts = tokens[i].split("=");
			String argName = argParts[0];
			String argValue;
			if (tokens[i].endsWith("=")) {
				argValue = "";
			} else if (argParts.length == 2) {
				argValue = argParts[1];
			} else {
				continue;
			}
			if (argValue.startsWith("$")) {
				argValue = app.getVariableValue(argValue.substring(1));
			}
			arguments.put(argName, argValue);
		}
		return arguments;
	}
	
}
