package ch.tkuhn.exxo;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Border;
import nextapp.echo.app.Button;
import nextapp.echo.app.Column;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.Insets;
import nextapp.echo.app.ResourceImageReference;
import nextapp.echo.app.Row;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.layout.RowLayoutData;
import ch.uzh.ifi.attempto.echocomp.Style;
import echopoint.ContainerEx;
import echopoint.able.Positionable;

public class AnswerMenu extends Column implements ActionListener {

	private static final long serialVersionUID = 3646511994109953476L;

	private Button button = new Button();
	private Button toggle = new Button();
	private ContainerEx popup;
	private Button overlayLockButton;
	
	private Column menuColumn = new Column();
	private RowLayoutData layout;
	
	private StatementStep step;
	private String selection = StatementStep.NO_CHOICE;
	
	private static ResourceImageReference icon =
		new ResourceImageReference("ch/uzh/ifi/attempto/echocomp/style/down.png");
	
	public AnswerMenu(final StatementStep step, final String... options) {
		this.step = step;
		
		Row mainRow = new Row();
		add(mainRow);
		
		layout = new RowLayoutData();
		layout.setAlignment(new Alignment(Alignment.LEFT, Alignment.TOP));
		
		button.setWidth(new Extent(76));
		button.setHeight(new Extent(20));
		button.setBackground(Style.lightBackground);
		button.setBorder(new Border(1, Style.darkBackground, Border.STYLE_OUTSET));
		button.setLayoutData(layout);
		button.setFont(new Font(Style.fontTypeface, Font.PLAIN, new Extent(13)));
		button.setAlignment(new Alignment(Alignment.LEFT, Alignment.CENTER));
		button.setInsets(new Insets(2, 0));
		button.addActionListener(this);
		mainRow.add(button);
		
		toggle.setWidth(new Extent(20));
		toggle.setHeight(new Extent(20));
		toggle.setBackground(Style.lightBackground);
		toggle.setBorder(new Border(1, Style.darkBackground, Border.STYLE_OUTSET));
		toggle.setLayoutData(layout);
		toggle.setIcon(icon);
		toggle.setRolloverEnabled(true);
		toggle.setRolloverBackground(Style.mediumBackground);
		toggle.setPressedBackground(Style.mediumBackground);
		toggle.addActionListener(this);
		mainRow.add(toggle);
		
		for (String s : options) {
			addMenuEntry(step.getOptionText(s), s);
		}
	}
	
	public void addMenuEntry(String text, String ac) {
		Button menuEntry = new Button(text);
		menuEntry.setActionCommand(ac);
		menuEntry.setHeight(new Extent(16));
		menuEntry.setWidth(new Extent(98));
		menuEntry.setBackground(Style.mediumBackground);
		menuEntry.setForeground(Style.darkForeground);
		menuEntry.setRolloverEnabled(true);
		menuEntry.setRolloverForeground(Style.lightForeground);
		menuEntry.setRolloverBackground(Style.darkBackground);
		menuEntry.setInsets(new Insets(2, 0));
		menuEntry.setAlignment(new Alignment(Alignment.LEFT, Alignment.CENTER));
		menuEntry.setTextAlignment(new Alignment(Alignment.LEFT, Alignment.CENTER));
		menuEntry.setFont(new Font(Style.fontTypeface, Font.PLAIN, new Extent(13)));
		menuEntry.addActionListener(this);
		menuColumn.add(menuEntry);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		
		if (src == toggle || src == button) {
			setExpanded(true);
		} else if (src == overlayLockButton) {
			setExpanded(false);
		} else {
			setExpanded(false);
			selection = e.getActionCommand();
			button.setText(step.getOptionText(selection));
		}
	}
	
	public void setExpanded(boolean expanded) {
		if (expanded) {
			if (isExpanded()) return;
			if (menuColumn.getComponentCount() == 0) return;
			if (popup == null) {
				popup = new ContainerEx();
				popup.setPosition(Positionable.ABSOLUTE);
				popup.add(menuColumn);
				popup.setBorder(new Border(1, Style.darkBackground, Border.STYLE_OUTSET));
				popup.setZIndex(Integer.MAX_VALUE);
				popup.setLayoutData(layout);
				add(popup);
				
				ContainerEx overlayLockComponent = new ContainerEx();
				overlayLockComponent.setPosition(Positionable.FIXED);
				overlayLockComponent.setTop(new Extent(0));
				overlayLockComponent.setLeft(new Extent(0));
				overlayLockButton = new Button();
				overlayLockButton.setWidth(new Extent(10000));
				overlayLockButton.setHeight(new Extent(10000));
				overlayLockButton.addActionListener(this);
				overlayLockComponent.add(overlayLockButton);
				add(overlayLockComponent);
			} else {
				popup.setVisible(true);
				overlayLockButton.setVisible(true);
			}
		} else {
			if (!isExpanded()) return;
			popup.setVisible(false);
			overlayLockButton.setVisible(false);
		}
	}
	
	public boolean isExpanded() {
		return popup != null && popup.isVisible();
	}
	
	public String getSelection() {
		return selection;
	}

}
