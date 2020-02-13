package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class SelectOptionBaseStep extends BrowserStep
    {
    SelectOptionBaseStep(StepConfiguration config, MuseProject project, String option_param_name) throws MuseInstantiationException
        {
        super(config);
        _element_source = getValueSource(config, ELEMENT_PARAM, true, project);
        _option_source = getValueSource(config, option_param_name, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        WebElement element = getElement(_element_source, context);
        String option_string = getValue(_option_source, context, false, String.class);
        try
            {
            Select select = new Select(element);
            executeSelection(select, option_string, context);
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
            }
        catch (UnexpectedTagNameException e)
            {
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR, "The element is not a <select> tag.");
            }
        }

    protected abstract void  executeSelection(Select select, String option, StepExecutionContext context);

    private MuseValueSource _element_source;
    private MuseValueSource _option_source;

    public final static String ELEMENT_PARAM = "element";
    }


