package org.musetest.core.steptest;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class RequiredParameterMissingError extends StepConfigurationError
    {
    public RequiredParameterMissingError(String parameter_name)
        {
        super("Step configuration is missing a required parameter: " + parameter_name);
        }
    }


