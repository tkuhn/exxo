package ch.tkuhn.exxo;

import java.util.HashMap;
import java.util.Map;

import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.Button;
import nextapp.echo.app.Color;
import nextapp.echo.app.Column;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.Insets;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.TaskQueueHandle;
import nextapp.echo.app.Window;
import nextapp.echo.app.WindowPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.event.WindowPaneEvent;
import nextapp.echo.app.event.WindowPaneListener;
import ch.uzh.ifi.attempto.echocomp.SolidLabel;
import ch.uzh.ifi.attempto.echocomp.Style;

public class MonitorApp extends ApplicationInstance implements ActionListener {
	
	private static final long serialVersionUID = 6243682574901357330L;
	
	private Map<Integer, LogWindow> windows = new HashMap<Integer, LogWindow>();
	private Map<Integer, ListEntry> listEntries = new HashMap<Integer, ListEntry>();
	
	private SplitPane splitPane;
	private Column list;
	private ContentPane contentPane;
	private TaskQueueHandle taskQueue;
	private int windowPos = 0;
	
	public Window init() {
		setStyleSheet(Style.styleSheet);
		taskQueue = createTaskQueue();
		
		Window window = new Window();
		window.setTitle("Experiment Monitor");
		splitPane = new SplitPane(SplitPane.ORIENTATION_HORIZONTAL_LEFT_RIGHT, new Extent(200));
		splitPane.setSeparatorWidth(new Extent(5));
		splitPane.setSeparatorColor(Color.DARKGRAY);
		splitPane.setSeparatorRolloverColor(Style.mediumBackground);
		splitPane.setResizable(true);
		
		list = new Column();
		ContentPane listPane = new ContentPane();
		listPane.add(list);
		listPane.setBackground(new Color(240,240,240));
		splitPane.add(listPane);
		
		contentPane = new ContentPane();
		splitPane.add(contentPane);
		
		ContentPane cp2 = new ContentPane();
		cp2.add(splitPane);
		window.setContent(cp2);
		
		for (ExpData data : ExpApp.getAllData()) {
			list.add(new ListEntry(data.getID()), 0);
		}
		
		ExpApp.addActionListener(this);
		
		return window;
	}
	
	public void actionPerformed(final ActionEvent e) {
		final Object src = e.getSource();
		final String c = e.getActionCommand();
		final int id = ((ExpApp) src).getID();
		if (c.equals("new")) {
			enqueueTask(taskQueue, new Runnable() {
				public void run() {
					ListEntry l = new ListEntry(id);
					list.add(l, 0);
					l.actionPerformed(null);
				}
			});
		} else {
			enqueueTask(taskQueue, new Runnable() {
				public void run() {
					LogWindow w = windows.get(id);
					if (w != null) {
						w.refresh();
					}
					ListEntry l = listEntries.get(id);
					if (l != null) {
						l.refresh();
					}
				}
			});
		}
	}
	
	private static String makeTitleString(ExpData data) {
		String name = data.getName();
		if (name == null) name = "";
		return "[" + data.getID() + "] " + name + " " + data.getStackTrace() +
				" (" + data.getPath() + ")";
	}
	
	private class LogWindow extends WindowPane implements WindowPaneListener {
		
		private static final long serialVersionUID = 1040853956642152475L;
		
		private int id;
		
		public LogWindow(ListEntry listEntry) {
			this.id = listEntry.getID();
			windows.put(id, this);
			setClosable(true);
			addWindowPaneListener(this);
			setWidth(new Extent(400));
			setHeight(new Extent(150));
			setPositionX(new Extent(20));
			setPositionY(new Extent(20));
			setStyleName("Default");
			refresh();
			listEntry.refresh();
		}
		
		public synchronized void refresh() {
			ExpData data = ExpApp.getData(id);
			if (data.isActive()) {
				setTitleBackground(Style.windowTitleBackground);
			} else {
				setTitleBackground(Color.DARKGRAY);
			}
			
			setTitle(makeTitleString(data));
			removeAll();
			Column c = new Column();
			for (LogEntry logEntry : data.getLogEntries()) {
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

		public void windowPaneClosing(WindowPaneEvent e) {
			windows.remove(id);
		}
		
	}
	
	private class ListEntry extends Button implements ActionListener {
		
		private static final long serialVersionUID = 7339306585448630158L;
		
		private int id;
		
		public ListEntry(int id) {
			this.id = id;
			listEntries.put(id, this);
			addActionListener(this);
			
			setHeight(new Extent(20));
			setFont(new Font(Style.fontTypeface, Font.PLAIN, new Extent(12)));
			setForeground(Color.BLACK);
			
			setRolloverEnabled(true);
			setRolloverForeground(Style.lightForeground);
			setRolloverBackground(Style.darkBackground);
			setInsets(new Insets(2, 0));
			
			setLineWrap(false);
			
			refresh();
		}
		
		public void refresh() {
			ExpData data = ExpApp.getData(id);
			
			setText(makeTitleString(data));
			if (data.isActive()) {
				setBackground(Style.lightBackground);
			} else {
				setBackground(Style.lightDisabled);
			}
		}
		
		public int getID() {
			return id;
		}

		public void actionPerformed(ActionEvent e) {
			enqueueTask(taskQueue, new Runnable() {
				public void run() {
					for (LogWindow w : windows.values()) {
						w.setZIndex(0);
					}
					LogWindow w = windows.get(id);
					if (w == null) {
						w = new LogWindow(ListEntry.this);
						contentPane.add(w);
						w.setPositionX(new Extent(20 + windowPos*30));
						w.setPositionY(new Extent(20 + windowPos*15));
						windowPos = (windowPos+1) % 12;
					} else {
						contentPane.remove(w);
						w.setVisible(true);
						w.setZIndex(10);
						contentPane.add(w);
					}
				}
			});
		}
		
	}

}
