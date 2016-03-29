package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("refresh")
@MuseStepName("Refresh")
@MuseInlineEditString("Refresh")
@MuseStepIcon("glyph:FontAwesome:REFRESH")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Refresh the page.")
@MuseStepLongDescription("Reload the current page. Equivalent to pressing the browser refresh button.")
public class RefreshPage extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public RefreshPage(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws StepConfigurationError
        {
        getDriver(context).navigate().refresh();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String TYPE_ID = RefreshPage.class.getAnnotation(MuseTypeId.class).value();
    }


