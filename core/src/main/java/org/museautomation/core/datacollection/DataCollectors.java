package org.museautomation.core.datacollection;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DataCollectors
	{
	/**
	 * Get the Plugins in the context that are DataCollectors
	 */
	public static List<DataCollector> list(MuseExecutionContext context)
		{
		List<DataCollector> data_collectors = new ArrayList<>();
		for (MusePlugin plugin : context.getPlugins())   // TODO this needs to walk up the plugin heirarchy?
			if (plugin instanceof DataCollector)
				data_collectors.add((DataCollector) plugin);
		return data_collectors;
		}

	/**
	 * Gets the single collector of the specified type.
	 *
	 * @throws IllegalArgumentException if there are multiple collectors of that type.
	 */
	public static <T extends DataCollector> T find(MuseExecutionContext context, Class<T> type)
		{
		T the_collector = null;
		for (MusePlugin plugin : context.getPlugins())  // TODO this needs to walk up the plugin heirarchy?
			{
			if (type.isAssignableFrom(plugin.getClass()))
				{
				if (the_collector != null)
					throw new IllegalArgumentException("Cannot use this method when there are more than one DataCollectors of the desired type");
				the_collector = (T) plugin;
				}
			}
		return the_collector;
		}
	}


