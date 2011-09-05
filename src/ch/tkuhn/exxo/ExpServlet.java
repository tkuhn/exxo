package ch.tkuhn.exxo;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.webcontainer.WebContainerServlet;


public class ExpServlet extends WebContainerServlet {

	private static final long serialVersionUID = 887859780010206591L;

	public ApplicationInstance newApplicationInstance() {
		return new ExpApp(getInitParameters());
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> getInitParameters() {
		Map<String, String> initParameters = new HashMap<String, String>();
		Enumeration paramEnum = getInitParameterNames();
		while (paramEnum.hasMoreElements()) {
			String n = paramEnum.nextElement().toString();
			initParameters.put(n, getInitParameter(n));
		}
		Enumeration contextParamEnum = getServletContext().getInitParameterNames();
		while (contextParamEnum.hasMoreElements()) {
			String n = contextParamEnum.nextElement().toString();
			initParameters.put("context:" + n, getServletContext().getInitParameter(n));
		}
		return initParameters;
	}

}
