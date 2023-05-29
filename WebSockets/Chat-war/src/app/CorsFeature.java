package app;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;



@Provider public class CorsFeature implements ContainerResponseFilter{
 
	@Override public void filter(ContainerRequestContext reqContext,
		ContainerResponseContext resContext) throws IOException {
		resContext.getHeaders().add("Access-Control-Allow-Origin", "*");
		resContext.getHeaders().add("Access-Control-Allow-Headers",
				"origin, content-type, accept, authorization");
		resContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
		resContext.getHeaders().add("Access-Control-Allow-Methods",
				"GET, POST, PUT, DELETE, OPTIONS, HEAD");
		resContext.getHeaders().add("Access-Control-Max-Age", "1209600"); 
		}
}
/*
 * @Provider public class CorsFeature implements Feature {
 * 
 * @Override public boolean configure(FeatureContext context) { CorsFilter
 * corsFilter = new CorsFilter(); corsFilter.getAllowedOrigins().add("*");
 * context.register(corsFilter); return true; } }
 */
