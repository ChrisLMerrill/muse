package org.museautomation.core.util;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StringUtils
    {
    public static int countSubstrings(String source, String substring)
        {
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1)
            {
            lastIndex = source.indexOf(substring, lastIndex);
            if (lastIndex != -1)
                {
                count++;
                lastIndex += substring.length();
                }
            }
        return count;
        }

    public static int countOccurrences(String source, Character char_to_find)
        {
        int count = 0;
        for (int i = 0; i < source.length(); i++)
            if (source.charAt(i) == char_to_find)
                count++;
        return count;
        }
    }


