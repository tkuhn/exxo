package ch.tkuhn.exxo;

import java.util.Map;

import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Row;
import ch.uzh.ifi.attempto.echocomp.HSpace;
import ch.uzh.ifi.attempto.echocomp.Label;
import ch.uzh.ifi.attempto.echocomp.MessageWindow;
import ch.uzh.ifi.attempto.echocomp.TextField;


public class IdentificationStep extends ExperimentStep {
	
	private TextField nameField;
	private String expl;
	
	public IdentificationStep(Map<String, String> arguments, Experiment experiment) {
		super(arguments, experiment);
		
		setDefaultTitle(getIntlText("title_identification_step"));
		setForwardButtonText(getIntlText("button_start"));
		expl = arguments.get("expl");
	}

	public Component getPage() {
		Column mainCol = new Column();
		mainCol.setInsets(new Insets(15, 20));
		mainCol.setCellSpacing(new Extent(15));
		
		if (expl != null) {
			String explText = getResources().getResourceContent(expl + ".txt");
			mainCol.add(ExpApp.getHTMLComponent(explText));
		} else {
			mainCol.add(new Label(getIntlText("text_identification")));
		}
		
		Row loginRow = new Row();
		loginRow.setInsets(new Insets(0, 2));
		loginRow.add(new Label(getIntlText("name_label")));
		loginRow.add(new HSpace(10));
		nameField = new TextField();
		nameField.setWidth(new Extent(200));
		loginRow.add(nameField);
		mainCol.add(loginRow);
		
		return mainCol;
	}
	
	public boolean proceed() {
		String name = nameField.getText();
		if (name == null || name.equals("")) {
			getApp().showWindow(new MessageWindow(
					getIntlText("error_message_title"),
					getIntlText("no_name_error"),
					getIntlText("ok_option")
				));
			return false;
		} else {
			return true;
		}
	}
	
	public void finish() {
		String name = nameField.getText();
		getApp().setName(name);
		log("Name: " + name);
	}

}
