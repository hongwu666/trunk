import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.lodogame.ldsg.api.Config;

public class StartWeb {

	private static final Logger logger = Logger.getLogger(StartWeb.class);

	public static void main(String[] args) throws Exception {

		int port = Config.ins().getPort();

		logger.info("server start.port[" + port + "]");

		Server server = buildServer(port, "/");
		server.start();

	}

	public static Server buildServer(int port, String contextPath) {

		Server server = new Server(port);
		WebAppContext webContext = new WebAppContext();
		webContext.addServlet(org.springframework.web.servlet.DispatcherServlet.class, "*.do").setInitParameter("contextConfigLocation", "classpath:config/web/spring-mvc.xml");

		webContext.setResourceBase("web");
		server.setHandler(webContext);

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

		webContext.setClassLoader(applicationContext.getClassLoader());

		XmlWebApplicationContext xmlWebAppContext = new XmlWebApplicationContext();
		xmlWebAppContext.setParent(applicationContext);
		xmlWebAppContext.setConfigLocation("");
		xmlWebAppContext.setServletContext(webContext.getServletContext());
		xmlWebAppContext.refresh();

		webContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, xmlWebAppContext);

		return server;

	}
}
