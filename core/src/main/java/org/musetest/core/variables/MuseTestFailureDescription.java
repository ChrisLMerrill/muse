package org.musetest.core.variables;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MuseTestFailureDescription
    {
    public MuseTestFailureDescription(FailureType type, String reason)
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

    public enum FailureType
        {
        Failure,
        Error,
        Interrupted
        }
    }


