package ch.tkuhn.exxo;

import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.webcontainer.WebContainerServlet;


public class MonitorServlet extends WebContainerServlet {

	private static final long serialVersionUID = 887859780010206591L;

	public ApplicationInstance newApplicationInstance() {
		return new MonitorApp();
	}

}
