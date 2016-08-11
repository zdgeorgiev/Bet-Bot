package com.bet.manager.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class HealthChecksSpringContextListener extends HealthCheckServlet.ContextListener implements ServletContextListener {

	private WebApplicationContext context;

	public HealthChecksSpringContextListener(WebApplicationContext context) {
		this.context = context;
	}

	public HealthChecksSpringContextListener() {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		this.context = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
		event.getServletContext().setAttribute(HealthCheckServlet.HEALTH_CHECK_REGISTRY,
				context.getBean(HealthCheckRegistry.class));
		event.getServletContext().setAttribute(MetricsServlet.METRICS_REGISTRY,
				context.getBean(MetricRegistry.class));
	}

	@Override
	protected HealthCheckRegistry getHealthCheckRegistry() {
		return context.getBean(HealthCheckRegistry.class);
	}
}
