package ch.tkuhn.exxo;

import java.util.Map;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Row;
import nextapp.echo.app.layout.RowLayoutData;
import ch.uzh.ifi.attempto.echocomp.HSpace;


public class InfoStep extends ExperimentStep {
	
	private String content;
	private String img;
	
	public InfoStep(String content, Map<String, String> arguments, Experiment experiment) {
		super(arguments, experiment);
		this.content = content;
		this.img = arguments.get("img");
		
		setDefaultTitle(getIntlText("title_info_step"));
	}
	
	public Component getPage() {
		Column col = new Column();
		col.setInsets(new Insets(15, 20, 35, 20));
		String explText = getResources().getResourceContent(content + ".txt");
		Component htmlComp = ExpApp.getHTMLComponent(explText);
		if (img == null) {
			col.add(htmlComp);
		} else {
			RowLayoutData layout = new RowLayoutData();
			layout.setAlignment(new Alignment(Alignment.LEFT, Alignment.TOP));
			
			Row row = new Row();
			
			Column contentCol = new Column();
			contentCol.setLayoutData(layout);
			contentCol.add(htmlComp);
			row.add(contentCol);
			
			row.add(new HSpace(50));
			
			Component image = getResources().getImage(img);
			image.setLayoutData(layout);
			row.add(image);
			
			col.add(row);
		}
		return col;
	}

}
