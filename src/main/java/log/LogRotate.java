package log;

import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public abstract class LogRotate {

    private Timer timer;
	private String name;
    protected LogSinkFile logger;

    public LogRotate(String name, LogSinkFile logger, Properties props) {
        this.name = name;
    	this.logger = logger;    	
    }

    protected void createTimer(long intervall, boolean once) {

    	System.out.println(intervall);
         timer = new Timer(!once);
         TimerTask timertask = new TimerTask() {
        	 public void run() {
        		 rotate();
        	 }                
         };

         if (once) {
        	 timer.schedule(timertask, intervall);
         } else {
        	 timer.schedule(timertask, new Date(), intervall);
         }

    }

    protected void rotate() {
        logger.rotateNow();
    }
    
    public String getName() {
		return name;
	}
    
}
