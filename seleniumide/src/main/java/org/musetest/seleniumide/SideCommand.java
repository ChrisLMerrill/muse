package org.musetest.seleniumide;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SideCommand
	{
	public String getId()
		{
		return id;
		}

	public void setId(String id)
		{
		this.id = id;
		}

	public String getComment()
		{
		return comment;
		}

	public void setComment(String comment)
		{
		this.comment = comment;
		}

	public String getCommand()
		{
		return command;
		}

	public void setCommand(String command)
		{
		this.command = command;
		}

	public String getTarget()
		{
		return target;
		}

	public void setTarget(String target)
		{
		this.target = target;
		}

	public String getValue()
		{
		return value;
		}

	public void setValue(String value)
		{
		this.value = value;
		}

    public String[][] getTargets()
        {
        return targets;
        }

    public void setTargets(String[][] targets)
        {
        this.targets = targets;
        }

    String id;
	String comment;
	String command;
	String target;
	String[][] targets;
	String value;
	}