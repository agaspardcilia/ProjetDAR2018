package filters;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.reflections.Reflections;

import annotations.AuthenticationRequiried;
import database.exceptions.CannotConnectToDatabaseException;
import database.exceptions.QueryFailedException;
import services.ServicesTools;
import services.auth.Authentication;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
	private Set<String> trackedPaths;
    /**
     * Default constructor. 
     */
    public AuthenticationFilter() {
    	trackedPaths = new HashSet<>();
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		trackedPaths.clear();
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		boolean authRequired = false;
		
		for (String s : trackedPaths) {
			if (req.getRequestURI().equals("/ProjetDAR" + s)) {
				authRequired = true;
				break;
			}
		}
		
		
		if (authRequired) {
			JSONObject answer = null;
			String key = request.getParameter(ServicesTools.KEY_ARG);
			
			if (key == null) {
				answer = ServicesTools.createMissingArgumentError();
			} else {
				try {
					if (!Authentication.validateAndRefreshKey(key)) {
						answer = ServicesTools.createInvalidKeyError();
					}
				} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException e) {
					answer = ServicesTools.createDatabaseError(e);
				}
			}
			
			if (answer != null) {
				resp.getOutputStream().println(answer.toString());
				return;
			}
			
		}
		
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		Reflections reflections = new Reflections("services");
		
		Set<Class<?>> classes = reflections.getTypesAnnotatedWith(AuthenticationRequiried.class);
		
		classes.forEach(c -> {
			Arrays.stream(c.getAnnotationsByType(WebServlet.class)).forEach(ws -> {
				Arrays.stream(ws.value()).forEach(p -> {
					trackedPaths.add(p);
				});
			});
			
		});
		
	}

}







