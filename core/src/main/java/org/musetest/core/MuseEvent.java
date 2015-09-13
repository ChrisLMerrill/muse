package org.musetest.core;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MuseEvent
    {
    public MuseEvent(MuseEventType type)
        {
        _type = type;
        }

    public MuseEventType getType()
        {
        return _type;
        }

    public String getDescription()
        {
        return _type.name();
        }

    public long getTimestampNanos()
        {
        return _timestamp_nanos;
        }

    MuseEventType _type;
    long _timestamp_nanos = System.nanoTime();
    }


