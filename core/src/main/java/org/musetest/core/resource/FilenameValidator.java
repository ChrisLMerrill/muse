package org.musetest.core.resource;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FilenameValidator
	{
	public boolean isValid(String filename)
		{
		for (int i = 0; i < filename.length(); i++)
			{
			char the_char = filename.charAt(i);
			for (char illegal : ILLEGAL_CHARS)
				if (the_char == illegal)
					return false;
			}
		return true;
		}

	public String suggestValidName(String filename)
		{
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < filename.length(); i++)
			{
			char the_char = filename.charAt(i);
			for (char illegal : ILLEGAL_CHARS)
				if (the_char == illegal)
					{
					the_char = 0;
					break;
					}
				else if (the_char == ' ')
					{
					the_char = '_';
					break;
					}
			if (the_char != 0)
				builder.append(the_char);
			}
		return builder.toString();
		}

	private final char[] ILLEGAL_CHARS = {'*', '\"', '\'', '/', '\\', '[', ']', '<', '>', ':', ';', '|', '=', ','};
	}


