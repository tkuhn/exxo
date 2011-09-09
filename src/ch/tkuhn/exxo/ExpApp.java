package ch.tkuhn.exxo;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.Color;
import nextapp.echo.app.Component;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Row;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.Window;
import nextapp.echo.app.WindowPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import ch.uzh.ifi.attempto.echocomp.GeneralButton;
import ch.uzh.ifi.attempto.echocomp.HSpace;
import ch.uzh.ifi.attempto.echocomp.Label;
import ch.uzh.ifi.attempto.echocomp.MessageWindow;
import ch.uzh.ifi.attempto.echocomp.SolidLabel;
import ch.uzh.ifi.attempto.echocomp.Style;
import de.exxcellent.echolot.app.KeystrokeListener;
import echopoint.DirectHtml;


public class ExpApp extends ApplicationInstance implements ActionListener {

	private static final long serialVersionUID = -5486047923341168913L;
	
	private static HashMap<Integer, ExpApp> apps = new HashMap<Integer, ExpApp>();
	private static ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
	private static int idCount = -1;
	private static String dir = "exp";
	
	static {
		Resources.registerLanguageFormatter(new MLLFormatter());
		Resources.registerLanguageFormatter(new SOULFormatter());
		Resources.registerLanguageFormatter(new PrologFormatter());
	}
	
	
	private Experiment experiment;
	private ExperimentStep step;
	private ResourceBundle intlTexts;
	private Map<String, String> parameters;
	private Map<String, String> variables = new HashMap<String, String>();
	private Resources resources;
	
	private ContentPane contentPane;
	private SplitPane splitPane1, splitPane2;
	private Row timeRow, buttonRow;
	private Countdown countdown;
	private GeneralButton nextButton;
	private Label titleLabel;
	private MessageWindow timeExceededWindow;
	private MessageWindow proceedConfirmationWindow;
	
	private final int id;
	private String name;
	
	private ArrayList<LogEntry> logEntries = new ArrayList<LogEntry>();
	
	private KeystrokeListener keystrokeListener;
	
	public ExpApp(Map<String, String> parameters) {
		if (idCount == -1) {
			// first application
			idCount = 0;
			String d = parameters.get("context:datadir");
			if (d != null && d.length() > 0) dir = d;
			if (dir.endsWith("/")) dir = dir.substring(0, dir.length()-1);
			File dataDir = new File(dir);
			if (dataDir.exists()) {
				for (File file : dataDir.listFiles()) {
					try {
						int id = new Integer(file.getName());
						if (id > idCount) idCount = id;
					} catch (NumberFormatException ex) {}
				}
			} else {
				dataDir.mkdir();
			}
		}
		
		idCount++;
		id = idCount;
		apps.put(id, this);
		
		notifyActionListeners("new");
		
		setLocale(new Locale("en", "US"));
		
		String script = parameters.get("script");
		if (script == null) script = "run";
		
		this.parameters = parameters;
		this.resources = new Resources(getParameterValue("resources"));
		this.experiment = new Experiment(script, this);
		this.step = experiment.getNextStep();
	}
	
	public static ExpApp getApp(int id) {
		return apps.get(id);
	}
	
	public static Collection<ExpApp> getApps() {
		return apps.values();
	}
	
	public static void addActionListener(ActionListener a) {
		actionListeners.add(a);
	}
	
	private void notifyActionListeners(String text) {
		for (ActionListener a : actionListeners) {
			a.actionPerformed(new ActionEvent(this, text));
		}
	}
	
	public int getID() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getExpStackTrace() {
		if (experiment == null) {
			return "";
		}
		return "/" + experiment.getExpStackTrace();
	}
	
	public List<LogEntry> getLogEntries() {
		return new ArrayList<LogEntry>(logEntries);
	}

	public Window init() {
		setStyleSheet(Style.styleSheet);
		Window window = new Window();
		window.setTitle(getIntlText("window_title"));
		
		splitPane1 = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(40));
		splitPane1.setSeparatorColor(Color.BLACK);
		splitPane1.setSeparatorHeight(new Extent(1));
		
		splitPane2 = new SplitPane(SplitPane.ORIENTATION_VERTICAL_TOP_BOTTOM, new Extent(40));
		splitPane2.setSeparatorColor(Color.BLACK);
		splitPane2.setSeparatorHeight(new Extent(1));
		
		SplitPane topSplitPane = new SplitPane(SplitPane.ORIENTATION_HORIZONTAL_RIGHT_LEFT, new Extent(0));
		topSplitPane.setBackground(new Color(230, 230, 230));
		topSplitPane.add(new Label());
		
		Row titleRow = new Row();
		titleRow.setBackground(new Color(230, 230, 230));
		titleRow.setAlignment(new Alignment(Alignment.LEFT, Alignment.CENTER));
		titleRow.setInsets(new Insets(15, 10, 0, 10));
		titleLabel = new SolidLabel(step.getTitle(), Font.BOLD, 15);
		titleRow.add(titleLabel);
		topSplitPane.add(titleRow);
		
		splitPane2.add(topSplitPane);
		splitPane2.add(new Label());
		
		SplitPane bottomSplitPane = new SplitPane(SplitPane.ORIENTATION_HORIZONTAL_RIGHT_LEFT, new Extent(180));
		bottomSplitPane.setBackground(new Color(230, 230, 230));
		
		buttonRow = new Row();
		buttonRow.setBackground(new Color(230, 230, 230));
		buttonRow.setAlignment(new Alignment(Alignment.RIGHT, Alignment.CENTER));
		buttonRow.setInsets(new Insets(0, 10, 15, 10));
		keystrokeListener = new KeystrokeListener();
		keystrokeListener.setKeyCode("ctrl+alt+f");
		keystrokeListener.addActionListener(this);
		buttonRow.add(keystrokeListener);
		nextButton = new GeneralButton("", 110, this);
		buttonRow.add(nextButton);
		bottomSplitPane.add(buttonRow);
		
		timeRow = new Row();
		timeRow.setBackground(new Color(230, 230, 230));
		timeRow.setAlignment(new Alignment(Alignment.LEFT, Alignment.CENTER));
		timeRow.setInsets(new Insets(15, 10, 0, 10));
		bottomSplitPane.add(timeRow);
		
		splitPane1.add(bottomSplitPane);
		splitPane1.add(splitPane2);
		contentPane = new ContentPane();
		contentPane.add(splitPane1);
		window.setContent(contentPane);
		
		update();
		
		return window;
	}
	
	public void dispose() {
		apps.remove(id);
		notifyActionListeners("dispose");
		super.dispose();
	}
	
	private void update() {
		splitPane2.remove(1);
		splitPane2.add(step.getPage());
		splitPane2.setBackground(Color.WHITE);

		titleLabel.setText(step.getTitle());
		
		timeRow.removeAll();
		countdown = null;
		if (step.getTimeLimit() > 0 && step.isForwardButtonVisible()) {
			timeRow.add(new SolidLabel(getIntlText("timer_label"), Font.ITALIC));
			timeRow.add(new HSpace(10));
			countdown = new Countdown(step.getTimeLimit(), this);
			countdown.setFont(new Font(Style.fontTypeface, Font.BOLD, new Extent(15)));
			timeRow.add(countdown);
			countdown.start();
		}
		
		buttonRow.remove(nextButton);
		if (step.isForwardButtonVisible()) {
			nextButton.setText(step.getForwardButtonText());
			buttonRow.add(nextButton);
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		String c = e.getActionCommand();
		if (s == nextButton) {
			boolean proceedSuccess = step.proceed();
			if (proceedSuccess) {
				if (step.hasProceedConfirmation()) {
					String warningText = getIntlText("proceed_warning");
					if (countdown != null && countdown.getSeconds() > 120) {
						int mins = (countdown.getSeconds()-1) / 60;
						String t = getIntlText("proceed_warning_time");
						t = t.replaceFirst("<time>", mins + "");
						warningText += " (" + t + ")";
					}
					proceedConfirmationWindow = new MessageWindow(
						getIntlText("warning_message_title"),
						warningText,
						null,
						this,
						getIntlText("proceed_option"), getIntlText("cancel_option")
					);
					proceedConfirmationWindow.setClosable(false);
					showWindow(proceedConfirmationWindow);
				} else {
					step.finish();
					abortCountdown();
					step = experiment.getNextStep();
					update();
				}
			}
		} else if (s == countdown) {
			experiment.log("! Time limit exceeded !");
			splitPane2.remove(1);
			splitPane2.add(new Label(""));
			splitPane2.setBackground(Color.DARKGRAY);
			timeExceededWindow = new MessageWindow(
				getIntlText("time_exceeded_title"),
				getIntlText("time_exceeded_text"),
				null,
				this,
				getIntlText("ok_option")
			);
			timeExceededWindow.setClosable(false);
			showWindow(timeExceededWindow);
		} else if (s == keystrokeListener) {
			log("forced forward");
			abortCountdown();
			step = experiment.getNextStep();
			update();
		} else if (s == timeExceededWindow) {
			timeExceededWindow = null;
			step.finish();
			abortCountdown();
			step = experiment.getNextStep();
			update();
		} else if (s == proceedConfirmationWindow) {
			if (c.equals(getIntlText("proceed_option"))) {
				proceedConfirmationWindow = null;
				step.finish();
				abortCountdown();
				step = experiment.getNextStep();
				update();
			}
		}
	}
	
	public void showWindow(WindowPane window) {
		for (Component c : contentPane.getComponents()) {
			if (c instanceof WindowPane) {
				c.setVisible(false);
				contentPane.remove(c);
			}
		}
		contentPane.add(window);
	}
	
	public void setLocale(Locale locale) {
		super.setLocale(locale);
		intlTexts = ResourceBundle.getBundle("text", locale);
	}
	
	public String getIntlText(String key) {
		return intlTexts.getString(key);
	}
	
	public void log(String text) {
		LogEntry logEntry = new LogEntry(System.currentTimeMillis(), text);
		logEntries.add(logEntry);
		notifyActionListeners("log");
		
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(dir + "/" + getID(), true));
			out.writeBytes(logEntry.toString());
			out.flush();
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void abortCountdown() {
		if (countdown != null) {
			countdown.abort();
		}
	}
	
	public String getVariableValue(String name) {
		return variables.get(name);
	}
	
	public void setVariableValue(String name, String value) {
		if ("".equals(value)) value = null;
		variables.put(name, value);
	}
	
	public String getParameterValue(String name) {
		return parameters.get(name);
	}
	
	public Resources getResources() {
		return resources;
	}
	
	public static Component getHTMLComponent(String text) {
		return new DirectHtml(
				"<span style=\"font-family: Verdana,Arial,Helvetica,Sans-Serif; font-size: 13px;\">" +
				text +
				"</span>"
			);
	}

}
