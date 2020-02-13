package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.*;
import org.museautomation.selenium.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("close-browser")
@MuseStepName("Close Browser")
@MuseInlineEditString("close browser")
@MuseStepIcon("glyph:FontAwesome:GLOBE")
@MuseStepTypeGroup("Selenium.Window")
@MuseStepShortDescription("Close a browser and all associated windows")
@MuseStepLongDescription("Closes the currently-selected browser by calling driver.quit(). This will close all browser windows associated with that driver and shutdown the driver.")
public class CloseBrowser extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public CloseBrowser(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws ValueSourceResolutionError
        {
        try
	        {
	        getDriver(context).quit();
	        }
        catch (Throwable thrown)
	        {
	        LOG.error("Error while closing the browser: " + thrown.getMessage());
	        }
        BrowserStepExecutionContext.putDriver(null, context);
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String TYPE_ID = CloseBrowser.class.getAnnotation(MuseTypeId.class).value();

    private final static Logger LOG = LoggerFactory.getLogger(CloseBrowser.class);
    }


