package ch.tkuhn.exxo;

import java.util.Map;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Row;
import nextapp.echo.app.layout.RowLayoutData;
import ch.uzh.ifi.attempto.echocomp.HSpace;
import ch.uzh.ifi.attempto.echocomp.VSpace;

public class InfoStep extends ExperimentStep {
	
	private String content;
	private String img;
	private String imgpos;
	
	public InfoStep(String content, Map<String, String> arguments, Experiment experiment) {
		super(arguments, experiment);
		this.content = content;
		
		img = arguments.get("img");
		imgpos = arguments.get("imgpos");
		
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
			
			Component comp;
			if ("top".equals(imgpos) || "bottom".equals(imgpos)) {
				comp = new Column();
			} else {
				comp = new Row();
			}
			
			Column contentCol = new Column();
			contentCol.setLayoutData(layout);
			contentCol.add(htmlComp);
			
			Component image = getResources().getImage(img);
			image.setLayoutData(layout);

			if ("top".equals(imgpos) || "left".equals(imgpos)) {
				comp.add(image);
				comp.add(new HSpace(50));
				comp.add(new VSpace(50));
				comp.add(contentCol);
			} else {
				comp.add(contentCol);
				comp.add(new HSpace(50));
				comp.add(new VSpace(50));
				comp.add(image);
			}
			
			col.add(comp);
		}
		return col;
	}

}
