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

    private final char[] ILLEGAL_CHARS = {'*', '\"', '\'', '/', '\\', '[', ']', '<', '>', ':', ';', '|', '=', ','};
    }


