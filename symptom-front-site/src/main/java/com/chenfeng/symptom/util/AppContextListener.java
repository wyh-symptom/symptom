package com.chenfeng.symptom.util;



import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * @author Administrator
 *
 */
public class AppContextListener implements ServletContextListener {

	private ServletContext context = null;
	Log log = LogFactory.getLog(AppContextListener.class);

	public void contextDestroyed(ServletContextEvent arg0) {
		this.context = null;
	}

	public void contextInitialized(ServletContextEvent event) {
		// 加载应用资源
		context = event.getServletContext();
		AppStart.init(context);
	}

}
