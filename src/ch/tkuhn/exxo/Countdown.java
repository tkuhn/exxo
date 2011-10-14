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

import java.util.HashMap;

import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.TaskQueueHandle;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import ch.uzh.ifi.attempto.echocomp.SolidLabel;


public class Countdown extends SolidLabel {
	
	private static final long serialVersionUID = 2019314495573465518L;
	
	private static final HashMap<ApplicationInstance, TaskQueueHandle> taskQueues = new HashMap<ApplicationInstance, TaskQueueHandle>();

	private ApplicationInstance app;
	private int secs;
	private final ActionListener actionListener;
	private boolean started = false;
	private boolean aborted = false;
	

	public Countdown(int secs, ActionListener actionListener) {
		this.secs = secs;
		this.actionListener = actionListener;
		this.app = ApplicationInstance.getActive();
		
		update();
	}
	
	public void start() {
		if (started || aborted) return;
		started = true;
		
		final Thread thread = new Thread() {
			
			public synchronized void run() {
				long millis = System.currentTimeMillis();
				long millisNow = millis;
				
				while (true) {
					
					app.enqueueTask(
						getTaskQueue(app),
						new Runnable() {
							public synchronized void run() {
								update();
							}
						}
					);
					
					while (millisNow < millis + 1000) {
						try {
							sleep(200);
						} catch (InterruptedException ex) {}
						millisNow = System.currentTimeMillis();
					}
					
					if (aborted) break;
					
					long secDiff = (millisNow - millis) / 1000;
					secs -= secDiff;
					millis = millis + (secDiff * 1000);
				
				}
			}
		};

		app.enqueueTask(
			getTaskQueue(app),
			new Runnable() {
				public synchronized void run() {
					thread.start();
				}
			}
		);
	}
	
	public void abort() {
		aborted = true;
	}
	
	public int getSeconds() {
		return secs;
	}
	
	private static TaskQueueHandle getTaskQueue(ApplicationInstance app) {
		TaskQueueHandle taskQueue = taskQueues.get(app);
		if (taskQueue == null) {
			taskQueue = app.createTaskQueue();
			taskQueues.put(app, taskQueue);
		}
		return taskQueue;
	}
	
	private void update() {
		if (secs < 0) secs = 0;
		
		int m = secs / 60;
		int s = secs % 60;
		String text = m + ":";
		if (s == 0) {
			text += "00";
		} else if (s < 10) {
			text += "0" + s;
		} else {
			text += s + "";
		}
		setText(text);
		
		if (secs == 0 && !aborted) {
			aborted = true;
			if (actionListener != null) {
				actionListener.actionPerformed(new ActionEvent(this, "Countdown finished"));
			}
		}
	}

}
