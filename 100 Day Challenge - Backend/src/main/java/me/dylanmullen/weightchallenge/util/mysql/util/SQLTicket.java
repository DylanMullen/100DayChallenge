package me.dylanmullen.weightchallenge.util.mysql.util;

import me.dylanmullen.weightchallenge.util.mysql.callbacks.SQLCallback;

public class SQLTicket
{

	private SQLQuery query;
	private SQLCallback callback;

	public SQLTicket(SQLQuery query, SQLCallback callback)
	{
		this.query = query;
		this.callback = callback;
	}

	public SQLQuery getQuery()
	{
		return query;
	}

	public SQLCallback getCallback()
	{
		return callback;
	}

}
