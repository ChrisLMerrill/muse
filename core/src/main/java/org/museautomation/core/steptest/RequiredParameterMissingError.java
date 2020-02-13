package org.museautomation.core.steptest;

import org.museautomation.core.resource.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class RequiredParameterMissingError extends MuseInstantiationException
    {
    public RequiredParameterMissingError(String parameter_name)
        {
        super("configuration is missing a required parameter: " + parameter_name);
        }
    }


