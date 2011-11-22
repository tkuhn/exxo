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
	
	@SuppressWarnings("rawtypes")
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
