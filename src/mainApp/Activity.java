package mainApp;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
public class Activity {

	private String action;
	private String time;
	
	public Activity(String action)
	{
		this.action = action;
		this.time = getCurrentTimeStamp();
	}
	
	@Override
	public String toString()
	{
		return(getTime() + "\t" + getAction() + "\n");
	}

	private String getAction() {
		return this.action;
	}

	private String getTime() {
		return this.time;
	}
	public String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}
	
}
