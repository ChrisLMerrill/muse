package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("refresh")
@MuseStepName("Refresh")
@MuseInlineEditString("Refresh")
@MuseStepIcon("glyph:FontAwesome:REFRESH")
@MuseStepTypeGroup("Selenium.Navigate")
@MuseStepShortDescription("Refresh the page")
@MuseStepLongDescription("Reload the current page. Equivalent to pressing the browser refresh button.")
@SuppressWarnings("unused")  // instantiated via reflectin
public class RefreshPage extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public RefreshPage(StepConfiguration config, MuseProject project)
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        getDriver(context).navigate().refresh();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String TYPE_ID = RefreshPage.class.getAnnotation(MuseTypeId.class).value();
    }


