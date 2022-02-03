package me.dylanmullen.weightchallenge.util.mysql.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dylan
 * @date 27 Sep 2021
 * @project Drinkers Bot
 * @file beer.drinkers.drinkersbot.modules.mysql.util.SQLTable.java
 */
public class SQLTable
{

	private String tableName;
	private String primaryKey;

	private Map<String, String> columns;

	public SQLTable(String tableName)
	{
		this.tableName = tableName;
		this.columns = new HashMap<>();
	}

	// TODO: Change to Enum
	public SQLTable addColumn(String column, String type, int length)
	{
		return addColumn(column, type, length, false);
	}

	public SQLTable addColumn(String column, String type, int length, boolean primary)
	{
		if (columns.containsKey(column))
			return this;
		columns.put(column, column + ";" + type + ";" + length);
		if (primary)
			primaryKey = column;
		return this;
	}

	public SQLTicket toTicket()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS " + tableName + " (");

		for (String column : columns.values())
		{
			String[] data = column.split(";");
			String name = "`" + data[0] + "`";
			String type = data[1];
			String length = "";
			if (!data[2].equals("-1"))
				length = "(" + data[2] + ")";

			sb.append(name + " " + type + length + ",");
		}

		sb.append("PRIMARY KEY (`" + primaryKey + "`))");
		return new SQLTicket(new SQLQuery(sb.toString()), null);
	}

}
