package me.dylanmullen.weightchallenge.util.mysql.callbacks;

import java.sql.ResultSet;

public abstract class SQLCallback implements Callback
{

	protected long timeExecuted;
	protected ResultSet result;
	protected boolean completed;
	
	public void setTimeExecuted(long timeExecuted)
	{
		this.timeExecuted = timeExecuted;
	}
	
	public void setResult(ResultSet result)
	{
		this.result = result;
	}
	
	public ResultSet getResult()
	{
		return result;
	}
	
	public boolean isCompleted()
	{
		return completed;
	}
}
