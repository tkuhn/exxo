package ch.tkuhn.exxo;

import nextapp.echo.app.Border;
import nextapp.echo.app.Color;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.Insets;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.extras.app.DropDownMenu;
import nextapp.echo.extras.app.menu.DefaultOptionModel;
import nextapp.echo.extras.app.menu.ItemModel;
import nextapp.echo.extras.app.menu.MenuModel;
import ch.uzh.ifi.attempto.echocomp.Style;

public class AnswerDropDown extends DropDownMenu implements ActionListener {
	
	private static final long serialVersionUID = -7581163255446610220L;
	private StatementStep step;
	private String selection = StatementStep.NO_CHOICE;

	public AnswerDropDown(final StatementStep step, final String... options) {
		this.step = step;
		setHeight(new Extent(17));
		setBackground(Style.mediumBackground);
		setForeground(Style.darkForeground);
		setBorder(new Border(1, Color.BLACK, Border.STYLE_INSET));
		setDisabledBackground(Style.lightDisabled);
		setDisabledForeground(Style.darkDisabled);
		setInsets(new Insets(2, 0));
		setFont(new Font(Style.fontTypeface, Font.PLAIN, new Extent(13)));
		addActionListener(this);
		
		setModel(new MenuModel() {
			
			private static final long serialVersionUID = 8012812392179384784L;

			public String getId() {
				return "";
			}
			
			public String getText() {
				return "";
			}
			
			public int getItemCount() {
				return options.length;
			}
			
			public ItemModel getItem(final int i) {
				return new DefaultOptionModel(options[i], step.getOptionText(options[i]), null);
			}
			
			public ImageReference getIcon() {
				return null;
			}
		});
		
	}

	public void actionPerformed(ActionEvent e) {
		selection = e.getActionCommand();
		setSelectionText(step.getOptionText(selection));
	}
	
	public String getSelection() {
		return selection;
	}

}
