package scheduled;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class BackgroundTaskManager implements ServletContextListener {
	private ScheduledExecutorService pool;
	
	 
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		pool = Executors.newScheduledThreadPool(4);
		
		EventGenerator eg = new EventGenerator();
		
		pool.scheduleAtFixedRate(eg, 0, 3, TimeUnit.HOURS);
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		pool.shutdown();
	}

	
}
