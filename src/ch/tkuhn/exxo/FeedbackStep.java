package ch.tkuhn.exxo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Row;
import nextapp.echo.app.button.ButtonGroup;
import ch.uzh.ifi.attempto.echocomp.MessageWindow;
import ch.uzh.ifi.attempto.echocomp.RadioButton;
import ch.uzh.ifi.attempto.echocomp.TextArea;
import ch.uzh.ifi.attempto.echocomp.TextField;
import ch.uzh.ifi.attempto.echocomp.VSpace;


public class FeedbackStep extends ExperimentStep {
	
	private String form;
	private Map<String, ButtonGroup> buttonGroupMap;
	private List<String> buttonGroupNames;
	private Map<String, TextArea> textAreaMap;
	private List<String> textAreaNames;
	private Map<String, TextField> textMap;
	private List<String> textNames;
	
	public FeedbackStep(String form, Map<String, String> arguments, Experiment experiment) {
		super(arguments, experiment);
		
		this.form = form;

		setDefaultTitle(getIntlText("title_feedback_step"));
		setForwardButtonText(getIntlText("button_finish"));
	}

	public Component getPage() {
		Column mainCol = new Column();
		mainCol.setInsets(new Insets(15, 20, 15, 40));
		mainCol.setCellSpacing(new Extent(3));
		buttonGroupMap = new HashMap<String, ButtonGroup>();
		buttonGroupNames = new ArrayList<String>();
		textAreaMap = new HashMap<String, TextArea>();
		textAreaNames = new ArrayList<String>();
		textMap = new HashMap<String, TextField>();
		textNames = new ArrayList<String>();

		String[] lines = getResources().getResourceContent(form + ".txt").split("\n");
		for (String line : lines) {
			if (line.matches("\\s*")) {
				mainCol.add(new VSpace(20));
			} else if (line.matches("\\[[A-Z]+ [a-zA-Z0-9\\*\\-_]+\\].*")) {
				String type = line.replaceFirst("\\[([A-Z]+) [a-zA-Z0-9\\*\\-_]+\\].*", "$1");
				String name = line.replaceFirst("\\[[A-Z]+ ([a-zA-Z0-9\\*\\-_]+)\\].*", "$1");
				String text = line.replaceFirst("\\[[A-Z]+ [a-zA-Z0-9\\*\\-_]+\\](.*)", "$1");
				if (type.equals("RADIO")) {
					ButtonGroup bg = buttonGroupMap.get(name);
					if (bg == null) {
						bg = new ButtonGroup();
						buttonGroupMap.put(name, bg);
						buttonGroupNames.add(name);
					}
					mainCol.add(createRadioButtonRow(text, bg));
				} else if (type.equals("TEXT")) {
					TextField tf = new TextField(500);
					tf.setText(text);
					textNames.add(name);
					textMap.put(name, tf);
					mainCol.add(tf);
				} else if (type.equals("TEXTAREA")) {
					TextArea ta = new TextArea(500, 80);
					ta.setText(text);
					textAreaNames.add(name);
					textAreaMap.put(name, ta);
					mainCol.add(ta);
				}
			} else {
				mainCol.add(ExpApp.getHTMLComponent(line));
			}
		}
		
		return mainCol;
	}
	
	public boolean proceed() {
		for (String n : buttonGroupNames) {
			if (n.endsWith("*") && getChoice(buttonGroupMap.get(n)) == null) {
				showMessageIncomplete();
				return false;
			}
		}
		for (String n : textNames) {
			if (n.endsWith("*") && textMap.get(n).getText().matches("\\s*")) {
				showMessageIncomplete();
				return false;
			}
		}
		for (String n : textAreaNames) {
			if (n.endsWith("*") && textAreaMap.get(n).getText().matches("\\s*")) {
				showMessageIncomplete();
				return false;
			}
		}
		return true;
	}
	
	private void showMessageIncomplete() {
		getApp().showWindow(new MessageWindow(
				"Error",
				"Please answer the questions.",
				"OK"
			));
	}
	
	public void finish() {
		for (String n : buttonGroupNames) {
			log(n + ": " + getChoice(buttonGroupMap.get(n)));
		}
		for (String n : textNames) {
			log(n + ": " + textMap.get(n).getText());
		}
		for (String n : textAreaNames) {
			log(n + ": " + textAreaMap.get(n).getText());
		}
	}
	
	private String getChoice(ButtonGroup group) {
		for (Object o : group.getButtons()) {
			RadioButton r = (RadioButton) o;
			if (r.isSelected()) return r.getText();
		}
		return null;
	}
	
	private Row createRadioButtonRow(String text, ButtonGroup group) {
		Row row = new Row();
		row.add(new RadioButton(text, group));
		return row;
	}

}
