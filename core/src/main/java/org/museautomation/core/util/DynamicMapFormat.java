package org.museautomation.core.util;

import javax.annotation.*;
import java.text.*;
import java.util.*;

/**
 * A formatter than can take a dynamic Map-like provider for substituting values into a string formatter.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 *         <p>
 *         This originally came from http://www.java2s.com/Code/Java/I18N/AtextformatsimilartoMessageFormatbutusingstringratherthannumerickeys.htm,
 *         but has been heavily modified since...it is no longer the original work.
 */
public class DynamicMapFormat extends Format
    {
    /**
     * Create the format with a provider that will be consulted for the named parameters in the pattern.
     *
     * @param value_map A provider of values for names in the format
     */
    public DynamicMapFormat(DynamicMapProvider value_map)
        {
        this._value_map = value_map;
        }

    /**
     * Scans the pattern and prepares internal variables.
     *
     * @param new_pattern String to be parsed.
     * @return the pattern
     * @throws IllegalArgumentException if number of arguments exceeds BUFSIZE or
     * parser found unmatched brackets (this exception should be switched off using setExactMatch(false)).
     */
    public String processPattern(String new_pattern) throws IllegalArgumentException
        {
        int index = 0;
        int offset_number = -1;
        StringBuilder output_pattern = new StringBuilder();
        _offsets = new int[BUFSIZE];
        _arguments = new String[BUFSIZE];
        _max_offset = -1;

        while (true)
            {
            int right_index;
            int left_index = new_pattern.indexOf(LEFT, index);

            if (left_index >= 0)
                right_index = new_pattern.indexOf(RIGHT, left_index + LEFT.length());
            else
                break;

            if (++offset_number >= BUFSIZE)
                throw new IllegalArgumentException("TooManyArguments");

            if (right_index < 0)
                throw new IllegalArgumentException("UnmatchedBraces");

            output_pattern.append(new_pattern.substring(index, left_index));
            _offsets[offset_number] = output_pattern.length();
            _arguments[offset_number] = new_pattern.substring(left_index + LEFT.length(), right_index);
            index = right_index + RIGHT.length();
            _max_offset++;
            }

        output_pattern.append(new_pattern.substring(index));

        return output_pattern.toString();
        }

    /**
     * Formats object.
     *
     * @param obj Object to be formatted into string
     * @return Formatted object
     */
    private String formatObject(Object obj)
        {
        if (obj == null)
            return null;

        if (obj instanceof Number)
            return NumberFormat.getInstance(Locale.getDefault()).format(obj); // fix
        else if (obj instanceof Date)
            return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(obj); //fix
        else if (obj instanceof String)
            return (String) obj;

        return obj.toString();
        }

    /**
     * Formats the parsed string by inserting maps's values.
     *
     * @param pattern_source    a string pattern
     * @param result Buffer to be used for result.
     * @param field_position   position
     * @return Formatted string
     */
    public StringBuffer format(Object pattern_source, @Nonnull StringBuffer result, @Nonnull FieldPosition field_position)
        {
        String pattern = processPattern((String) pattern_source);
        int last_offset = 0;

        for (int i = 0; i <= _max_offset; ++i)
            {
            int offset_index = _offsets[i];
            result.append(pattern.substring(last_offset, _offsets[i]));
            last_offset = offset_index;

            String key = _arguments[i];
            String obj;
            if (key.length() > 0)
                obj = formatObject(_value_map.get(key));
            else
                {
                // else just copy the left and right braces
                result.append(LEFT);
                result.append(RIGHT);
                continue;
                }

            if (obj == null)
                {
                // try less-greedy match; useful for e.g. PROP___PROPNAME__ where
                // 'PROPNAME' is a key and delims are both '__'
                // this does not solve all possible cases, surely, but it should catch
                // the most common ones
                String lessgreedy = LEFT + key;
                int fromright = lessgreedy.lastIndexOf(LEFT);

                if (fromright > 0)
                    {
                    String newkey = lessgreedy.substring(fromright + LEFT.length());
                    String newsubst = formatObject(_value_map.get(newkey));

                    if (newsubst != null)
                        obj = lessgreedy.substring(0, fromright) + newsubst;
                    }
                }

            if (obj == null)
                obj = LEFT + key + RIGHT;

            result.append(obj);
            }

        result.append(pattern.substring(last_offset, pattern.length()));

        return result;
        }

    /** Unneeded for my purposes, but implmentation is required by the superclass. */
    public Object parseObject(String text, @Nonnull ParsePosition status)
        {
        throw new UnsupportedOperationException("This format does not support parsing.");
        }


    /** formatting map */
    private DynamicMapProvider _value_map;

    /** Offsets to {} expressions */
    private int[] _offsets;

    /** Keys enclosed by {} brackets */
    private String[] _arguments;

    /** Max offset in use */
    private int _max_offset;

    private static final int BUFSIZE = 255;
    private static String LEFT = "{"; // NOI18N
    private static String RIGHT = "}"; // NOI18N
    }


