package me.dylanmullen.weightchallenge.util.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import me.dylanmullen.weightchallenge.util.config.Config;
import me.dylanmullen.weightchallenge.util.mysql.callbacks.SQLCallback;
import me.dylanmullen.weightchallenge.util.mysql.util.SQLDatabase;
import me.dylanmullen.weightchallenge.util.mysql.util.SQLQuery;
import me.dylanmullen.weightchallenge.util.mysql.util.SQLQuery.ExecutionType;
import me.dylanmullen.weightchallenge.util.mysql.util.SQLTicket;

public class MySQLController implements Runnable
{

	private ArrayList<SQLTicket> queue;

	private Thread thread;
	private boolean running;
	private boolean connected;

	private Config config;
	private SQLDatabase database;

	public MySQLController(Config properties)
	{
		this.queue = new ArrayList<SQLTicket>();
		this.config = properties;
		new SQLFactory(this);
	}

	public synchronized void start()
	{
		if (running)
			return;
		this.running = true;
		thread = new Thread(this);
		thread.start();

		// WAITING FOR A RESPONSE ON CONNECT
		synchronized (thread)
		{
			try
			{
				System.out.println("waiting");
				wait();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run()
	{
		database = new SQLDatabase(config);
		connected = database.connect();

		synchronized (this)
		{
			notify();
		}

		if (!connected)
		{
			System.out.println("Failed to connect!");
			return;
		}

		database.createTables();

		while (connected)
		{
			handleRequests();
		}
		System.out.println("[MySQL Thread] Stopped");
	}

	private void handleRequests()
	{
		synchronized (queue)
		{
			for (int i = 0; i < queue.size(); i++)
			{
				SQLTicket ticket = queue.get(i);
				SQLQuery query = ticket.getQuery();

				if (!query.createStatement(database.getConnection()))
					continue;

				if (query.getExecutionMethod() == null)
					continue;

				if (query.getExecutionMethod().equals(ExecutionType.QUERY))
					sendSQLQueryRequest(query.getStatement(), ticket.getCallback());
				else
					sendSQLDataManipRequest(query.getStatement(), ticket.getCallback());

				queue.remove(i);
			}
		}
	}

	private void sendSQLQueryRequest(PreparedStatement statement, SQLCallback callback)
	{
		try
		{
			callback.setTimeExecuted(System.currentTimeMillis());
			ResultSet rs = statement.executeQuery();
			callback.setResult(rs);
			callback.callback();
		} catch (SQLException e)
		{
			System.err.println("Failed to execute query: " + e.getMessage());
		}
	}

	private void sendSQLDataManipRequest(PreparedStatement statement, SQLCallback callback)
	{
		try
		{
			if (callback != null)
			{
				callback.setTimeExecuted(System.currentTimeMillis());
				callback.callback();
			}
			statement.execute();
		} catch (SQLException e)
		{
			System.err.println("Failed to execute query: " + e.getMessage());
		}
	}

	public void submitTicket(SQLTicket ticket)
	{
		synchronized (queue)
		{
			if (queue.contains(ticket))
				return;
			queue.add(ticket);
		}
	}

	public synchronized void dispose()
	{
		if (!running)
			return;
			running = false;
			connected = false;
	}

	public boolean isConnected()
	{
		return connected;
	}

	public Thread getThread()
	{
		return thread;
	}

	public SQLDatabase getDatabase()
	{
		return database;
	}
}
