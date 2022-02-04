package me.dylanmullen.weightchallenge.modules.notification.runnables;

import lombok.Data;

@Data
public class NotificationRunnable implements Runnable
{

	private long startTime;
	private long endTime;
	private boolean running;
	
	public NotificationRunnable(long startTime, long endTime)
	{
		setStartTime(startTime);
		setEndTime(endTime);
	}
	
	@Override
	public void run()
	{
		while(isRunning())
		{
			
		}
	}

}
