package me.dylanmullen.weightchallenge.util.mysql.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.dylanmullen.weightchallenge.util.config.Config;
import me.dylanmullen.weightchallenge.util.mysql.SQLFactory;

public class SQLDatabase
{

	private String databaseName;
	private String address;
	private String username;
	private String password;
	private int port;

	private String loginTable;
	private String userInfoTable;

	private Connection connection;
	private boolean connected;

	public SQLDatabase(Config config)
	{
		this.databaseName = (String) config.getValue("security.mysql", "database");
		this.address = (String) config.getValue("security.mysql.host", "address");
		this.port = ((Long) config.getValue("security.mysql.host", "port")).intValue();
		this.username = (String) config.getValue("security.mysql", "username");
		this.password = (String) config.getValue("security.mysql", "password");
	}

	public boolean connect()
	{
		if (connected)
			return false;
		System.out.println("Connecting to MySQL Database...");
		long last = System.currentTimeMillis();
		try
		{
			this.connection = DriverManager.getConnection(
					"jdbc:mysql://" + this.address + ":" + this.port + "/" + this.databaseName, username, password);
			connected = true;
			System.out.println("MySQL Connected. Time taken: " + (System.currentTimeMillis() - last) + "ms");
			return connected;
		} catch (SQLException e)
		{
			System.err.println("Error connecting to MySQL database: " + e.getMessage());
			System.err.println("Time Taken: " + (System.currentTimeMillis() - last) + "ms");
			return false;
		}
	}

	public void createTables()
	{
		SQLFactory.sendTicket(new SQLTable("servers").addColumn("server-id", "varchar", 255, true).toTicket());
	}

	public Connection getConnection()
	{
		return connection;
	}

	public void close()
	{
		try
		{
			this.connection.close();
		} catch (SQLException e)
		{
			System.err.println("Error disconnecting to MySQL database: " + e.getMessage());
		}
	}

	public boolean isConnected()
	{
		return connected;
	}

	public String getLoginTableName()
	{
		return loginTable;
	}

	public String getUserInfoTableName()
	{
		return userInfoTable;
	}

}
