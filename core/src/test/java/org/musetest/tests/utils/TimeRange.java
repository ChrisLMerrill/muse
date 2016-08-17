package org.musetest.tests.utils;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TimeRange
    {
    public TimeRange(long time, long plus_minus)
        {
        _start = time - plus_minus;
        _end = time + plus_minus;
        }

    public boolean isInRange(long time)
        {
        return _start <= time && time <= _end;
        }

    private final long _start;
    private final long _end;
    }


