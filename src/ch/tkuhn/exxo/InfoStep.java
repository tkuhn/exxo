// This file is part of Exxo.
// Copyright 2011, Tobias Kuhn.
// 
// Exxo is free software: you can redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation, either version 3 of
// the License, or (at your option) any later version.
// 
// Exxo is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
// the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
// General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with Exxo. If
// not, see http://www.gnu.org/licenses/.

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
			Component imgComp;
			if ("top".equals(imgpos) || "bottom".equals(imgpos)) {
				comp = new Column();
				imgComp = new Row();
			} else {
				comp = new Row();
				imgComp = new Column();
			}
			imgComp.setLayoutData(layout);
			
			Column contentCol = new Column();
			contentCol.setLayoutData(layout);
			contentCol.add(htmlComp);
			
			for (String s : img.split("\\|")) {
				imgComp.add(getResources().getImage(s));
				imgComp.add(new HSpace(20));
				imgComp.add(new VSpace(20));
			}

			if ("top".equals(imgpos) || "left".equals(imgpos)) {
				comp.add(imgComp);
				comp.add(new HSpace(50));
				comp.add(new VSpace(50));
				comp.add(contentCol);
			} else {
				comp.add(contentCol);
				comp.add(new HSpace(50));
				comp.add(new VSpace(50));
				comp.add(imgComp);
			}
			
			col.add(comp);
		}
		return col;
	}

}
