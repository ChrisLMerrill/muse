package org.museautomation.core.format;

import org.museautomation.core.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface Reformatter
	{
	boolean canReformat(MuseProject project, Object object, String format);
	boolean canReformat(MuseProject project, File file, String type, String format);

	void reformat(MuseProject project, Object object, String format, OutputStream outstream);
	void reformat(MuseProject project, File file, String type, String format, OutputStream outstream);
	}

