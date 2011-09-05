package ch.tkuhn.exxo;

import java.util.List;
import java.util.Map;

import nextapp.echo.app.Border;
import nextapp.echo.app.Color;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.Insets;
import nextapp.echo.app.ListBox;
import nextapp.echo.app.Row;
import ch.uzh.ifi.attempto.echocomp.Label;
import ch.uzh.ifi.attempto.echocomp.Style;


public class ChoiceStep extends ExperimentStep {
	
	private List<String> choices;
	private ListBox listBox;
	
	public ChoiceStep(List<String> choices, Map<String, String> arguments, Experiment experiment) {
		super(arguments, experiment);
		this.choices = choices;
		
		setDefaultTitle(getIntlText("title_choice_step"));
		setForwardButtonText(getIntlText("button_run"));
	}

	public Component getPage() {
		Column mainCol = new Column();
		mainCol.setInsets(new Insets(15, 20));
		mainCol.setCellSpacing(new Extent(15));
		mainCol.add(new Label(getIntlText("text_choice")));
		
		Row selectRow = new Row();
		listBox = new ListBox(choices.toArray());
		listBox.setSelectedIndex(0);
		listBox.setBackground(Style.lightBackground);
		listBox.setForeground(Color.BLACK);
		listBox.setBorder(new Border(1, Color.BLACK, Border.STYLE_INSET));
		listBox.setFont(new Font(Style.fontTypeface, Font.PLAIN, new Extent(13)));
		listBox.setHeight(new Extent(400));
		listBox.setWidth(new Extent(300));
		selectRow.add(listBox);
		mainCol.add(selectRow);
		
		return mainCol;
	}
	
	public void finish() {
		String c = listBox.getSelectedValue().toString();
		log("Choice: " + c);
		getExperiment().run(c);
	}

}
