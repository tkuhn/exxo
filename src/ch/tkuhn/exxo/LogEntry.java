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

import java.util.Calendar;

public class LogEntry {
	
	private long timeStamp;
	private String text;
	
	public LogEntry(long timeStamp, String text) {
		this.timeStamp = timeStamp;
		this.text = text;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public String getTimeWithMillis() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeStamp);
		String hour = makeString(c.get(Calendar.HOUR_OF_DAY), 2);
		String min = makeString(c.get(Calendar.MINUTE), 2);
		String sec = makeString(c.get(Calendar.SECOND), 2);
		String millis = makeString(c.get(Calendar.MILLISECOND), 3);
		return hour + ":" + min + ":" + sec + "." + millis;
	}
	
	public String getTime() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeStamp);
		String hour = makeString(c.get(Calendar.HOUR_OF_DAY), 2);
		String min = makeString(c.get(Calendar.MINUTE), 2);
		String sec = makeString(c.get(Calendar.SECOND), 2);
		return hour + ":" + min + ":" + sec;
	}
	
	public String getDate() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeStamp);
		String year = c.get(Calendar.YEAR) + "";
		String month = makeString(c.get(Calendar.MONTH)+1, 2);
		String day = makeString(c.get(Calendar.DAY_OF_MONTH), 2);
		return year + "-" + month + "-" + day;
	}
	
	public String getText() {
		return text;
	}
	
	private static String makeString(int value, int size) {
		String s = value + "";
		while (s.length() < size) {
			s = "0" + s;
		}
		return s;
	}
	
	public String toString() {
		return timeStamp + " (" + getDate() + " " + getTimeWithMillis() + ") " + text + "\n";
	}

}
