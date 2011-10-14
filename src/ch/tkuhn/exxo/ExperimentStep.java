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

import nextapp.echo.app.Component;


public abstract class ExperimentStep {
	
	private String defaultTitle = "";
	private String title;
	private boolean forwardButtonVisible = true;
	private String forwardButtonText;
	private int timeLimit = 0;
	private Experiment experiment;
	private boolean proceedConfirmationEnabled = false;
	
	public ExperimentStep(Map<String, String> arguments, Experiment experiment) {
		this.experiment = experiment;
		this.forwardButtonText = getIntlText("button_next");
		if (arguments.get("time") != null) {
			timeLimit = new Integer(arguments.get("time"));
		}
		if (arguments.get("fwbutton") != null) {
			forwardButtonVisible = arguments.get("fwbutton").equals("on");
		}
		if (arguments.get("prconfirm") != null) {
			proceedConfirmationEnabled = arguments.get("prconfirm").equals("on");
		}
		title = arguments.get("title");
		if (title != null) {
			title = title.replaceAll("_", " ");
		}
	}
	
	public abstract Component getPage();
	
	public boolean proceed() {
		return true;
	}
	
	public void finish() {
	}
	
	public boolean hasProceedConfirmation() {
		return proceedConfirmationEnabled;
	}
	
	public boolean isForwardButtonVisible() {
		return forwardButtonVisible;
	}
	
	public void setForwardButtonVisible(boolean visible) {
		this.forwardButtonVisible = visible;
	}
	
	public String getForwardButtonText() {
		return forwardButtonText;
	}
	
	public void setForwardButtonText(String forwardButton) {
		this.forwardButtonText = forwardButton;
	}
	
	public String getTitle() {
		if (title == null) return defaultTitle;
		return title;
	}
	
	public void setDefaultTitle(String title) {
		this.defaultTitle = title;
	}
	
	public int getTimeLimit() {
		return timeLimit;
	}
	
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	public Experiment getExperiment() {
		return experiment;
	}
	
	public ExpApp getApp() {
		return experiment.getApp();
	}
	
	public Resources getResources() {
		return experiment.getResources();
	}
	
	protected String getIntlText(String key) {
		return experiment.getApp().getIntlText(key);
	}
	
	protected void log(String text) {
		experiment.log(text);
	}

}
