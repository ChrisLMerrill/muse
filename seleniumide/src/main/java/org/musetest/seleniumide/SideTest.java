package org.musetest.seleniumide;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SideTest
	{
	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public String getId()
		{
		return id;
		}

	public void setId(String id)
		{
		this.id = id;
		}

	public SideCommand[] getCommands()
		{
		return commands;
		}

	public void setCommands(SideCommand[] commands)
		{
		this.commands = commands;
		}

	String name;
	String id;
	SideCommand[] commands;
	}