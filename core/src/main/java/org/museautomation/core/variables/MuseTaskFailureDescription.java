package org.museautomation.core.variables;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MuseTaskFailureDescription
    {
    public MuseTaskFailureDescription(FailureType type, String reason)
        {
        _type = type;
        _reason = reason;
        }

    public FailureType getFailureType()
        {
        return _type;
        }

    public String getReason()
        {
        return _reason;
        }

    private FailureType _type;
    private String _reason;

    @Override
    public String toString()
	    {
	    return _type.name() + ": " + _reason;
	    }

    public enum FailureType
        {
        Failure,
        Error,
        Interrupted
        }
    }


