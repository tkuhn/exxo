package ch.tkuhn.exxo;

import java.util.HashMap;

import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.Color;
import nextapp.echo.app.Column;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.Extent;
import nextapp.echo.app.TaskQueueHandle;
import nextapp.echo.app.Window;
import nextapp.echo.app.WindowPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import ch.uzh.ifi.attempto.echocomp.SolidLabel;
import ch.uzh.ifi.attempto.echocomp.Style;

public class MonitorApp extends ApplicationInstance implements ActionListener {
	
	private static final long serialVersionUID = 6243682574901357330L;
	
	private HashMap<Integer, LogWindow> windows = new HashMap<Integer, LogWindow>();
	
	private ContentPane contentPane;
	private TaskQueueHandle taskQueue;
	
	public Window init() {
		setStyleSheet(Style.styleSheet);
		taskQueue = createTaskQueue();
		Window window = new Window();
		window.setTitle("Experiment Monitor");
		contentPane = new ContentPane();
		window.setContent(contentPane);
		
		for (ExpApp app : ExpApp.getApps()) {
			contentPane.add(new LogWindow(app));
		}
		
		ExpApp.addActionListener(this);
		
		return window;
	}
	
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals("new")) {
			enqueueTask(taskQueue, new Runnable() {
				public void run() {
					contentPane.add(new LogWindow((ExpApp) e.getSource()));
				}
			});
		} else if (e.getActionCommand().equals("dispose")) {
			enqueueTask(taskQueue, new Runnable() {
				public void run() {
					LogWindow w = windows.remove(((ExpApp) e.getSource()).getID());
					contentPane.remove(w);
				}
			});
		} else if (e.getActionCommand().equals("log")) {
			enqueueTask(taskQueue, new Runnable() {
				public void run() {
					windows.get(((ExpApp) e.getSource()).getID()).refresh();
				}
			});
		}
	}
	
	private class LogWindow extends WindowPane {
		
		private static final long serialVersionUID = 1040853956642152475L;
		
		private ExpApp app;
		
		public LogWindow(ExpApp app) {
			this.app = app;
			windows.put(app.getID(), this);
			setClosable(false);
			setTitleBackground(Style.windowTitleBackground);
			setWidth(new Extent(400));
			setHeight(new Extent(150));
			setPositionX(new Extent(20));
			setPositionY(new Extent(20));
			setStyleName("Default");
			refresh();
		}
		
		public synchronized void refresh() {
			String name = app.getName();
			if (name == null) name = "";
			setTitle("[" + app.getID() + "] " + name + " " + app.getExpStackTrace() +
					" (" + app.getResources().getPath() + ")");
			removeAll();
			Column c = new Column();
			for (LogEntry logEntry : app.getLogEntries()) {
				Color color = Color.BLACK;
				String text = logEntry.getText();
				if (text.startsWith(">")) {
					color = Color.LIGHTGRAY;
				} else if (text.startsWith("!")) {
					color = Color.RED;
				} else if (text.startsWith("$")) {
					color = Color.BLUE;
				}
				String[] lines = text.split("\\n");
				SolidLabel l = new SolidLabel(logEntry.getTime() + " " + lines[0]);
				l.setForeground(color);
				c.add(l, 0);
				for (int i = 1 ; i < lines.length ; i++) {
					l = new SolidLabel("   " + lines[i]);
					l.setForeground(color);
					c.add(l, i);
				}
			}
			add(c);
		}
		
	}

}
