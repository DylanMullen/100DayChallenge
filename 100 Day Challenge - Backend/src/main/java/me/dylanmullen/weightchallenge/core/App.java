package me.dylanmullen.weightchallenge.core;

import lombok.Data;
import lombok.Getter;

@Data
public class App implements Runnable
{

	@Getter
	private Thread thread;
	private boolean running;

	public App()
	{
	}

	public synchronized boolean start()
	{
		if (getThread() != null || isRunning())
			return false;
		this.thread = new Thread(this);
		thread.start();
		return true;
	}

	public void run()
	{
		
	}
	
	
}
