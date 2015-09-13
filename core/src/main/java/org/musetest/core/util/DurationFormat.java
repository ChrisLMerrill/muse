package org.musetest.core.util;

import java.util.concurrent.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DurationFormat
    {
    public static String formatMinutesSeconds(long duration_nanos)
        {
        long milliseconds = duration_nanos / 1000000;
        long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60;
        long sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60;
        long ms = milliseconds % 1000;
        return String.format("%02d:%02d.%03d", min, sec, ms);
        }
    }


