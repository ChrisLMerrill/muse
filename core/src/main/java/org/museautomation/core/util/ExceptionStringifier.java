package org.museautomation.core.util;

import org.slf4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ExceptionStringifier extends Stringifier
	{
	@Override
	public Object create(Object target)
		{
		if (target instanceof Throwable)
			{
			final Throwable t = (Throwable) target;
			return new Object()
				{
				@Override
				public String toString()
					{
					try (ByteArrayOutputStream bytestream = new ByteArrayOutputStream(); PrintStream printstream = new PrintStream(bytestream))
						{
						t.printStackTrace(printstream);
						printstream.flush();
						bytestream.flush();
						return bytestream.toString();
						}
					catch (IOException e)
						{
						LOG.error("can't stringify?", e);
						return e.getMessage();
						}
					}
				};
			}
		return null;
		}

	private final static Logger LOG = LoggerFactory.getLogger(ExceptionStringifier.class);
	}


