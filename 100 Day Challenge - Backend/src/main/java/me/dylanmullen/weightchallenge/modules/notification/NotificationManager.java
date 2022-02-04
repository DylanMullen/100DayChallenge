package me.dylanmullen.weightchallenge.modules.notification;

import java.sql.SQLException;

import lombok.Data;
import me.dylanmullen.weightchallenge.util.mysql.SQLFactory;
import me.dylanmullen.weightchallenge.util.mysql.callbacks.SQLCallback;
import me.dylanmullen.weightchallenge.util.mysql.util.SQLTicket;

@Data
public class NotificationManager
{

	private long startTime;
	private long endTime;

	public NotificationManager()
	{
		load();
	}

	private void load()
	{
		SQLTicket ticket = SQLFactory.selectData("challenge", "*", new String[] { "completed" },
				new String[] { "false" }, new SQLCallback()
				{

					@Override
					public boolean callback()
					{
						if (!hasEntries())
							return false;

						try
						{
							setStartTime(getResult().getLong("start_time"));
							setEndTime(getResult().getLong("end_time"));
						} catch (SQLException e)
						{
						}
						return true;
					}
				});
		SQLFactory.sendTicket(ticket);
	}

	public long getTimeLeft()
	{
		return endTime - startTime;
	}

}
