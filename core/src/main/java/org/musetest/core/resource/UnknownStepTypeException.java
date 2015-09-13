package org.musetest.core.resource;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class UnknownStepTypeException extends Exception
    {
    public UnknownStepTypeException(String type)
        {
        super("Unknown step type: " + type);
        }
    }


