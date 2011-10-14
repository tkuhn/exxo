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

import java.util.ArrayList;
import java.util.List;

public class ExpData {

	private final int id;
	private ExpApp app;
	private String name;
	private String stackTrace;
	private String path;
	private ArrayList<LogEntry> logEntries = new ArrayList<LogEntry>();
	private boolean active = true;

	public ExpData(int id, ExpApp app) {
		this.id = id;
		this.app = app;
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStackTrace() {
		if (app == null) {
			return stackTrace;
		} else {
			return app.getExpStackTrace();
		}
	}
	
	public String getPath() {
		if (app == null) {
			return path;
		} else {
			return app.getResources().getPath();
		}
	}
	
	public List<LogEntry> getLogEntries() {
		return new ArrayList<LogEntry>(logEntries);
	}
	
	public void addLogEntry(LogEntry l) {
		logEntries.add(l);
	}
	
	public void inactivate() {
		stackTrace = getStackTrace();
		path = getPath();
		active = false;
		app = null;
	}
	
	public boolean isActive() {
		return active;
	}

}
