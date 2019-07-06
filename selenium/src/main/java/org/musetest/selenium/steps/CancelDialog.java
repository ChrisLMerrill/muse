package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("cancel-dialog")
@MuseStepName("Cancel Dialog")
@MuseInlineEditString("cancel dialog")
@MuseStepIcon("glyph:FontAwesome:TIMES")
@MuseStepTypeGroup("Selenium.Dialog")
@MuseStepShortDescription("Cancel a Javascript Dialog")
@MuseStepLongDescription("Cancels (dismisses) the currently-open Javascript dialog, which could be an alert, confirm or prompt dialog. This is equivalent to pressing the Cancel button.")
@SuppressWarnings("unused")  // instantiated via reflection
public class CancelDialog extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public CancelDialog(StepConfiguration config, MuseProject project)
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context)
        {
        try
	        {
	        getDriver(context).switchTo().alert().dismiss();
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
	        }
        catch (Throwable thrown)
	        {
            String message = "Error when attempting to cancel/dismiss an alert: " + thrown.getMessage();
            LOG.error(message);
	        return new BasicStepExecutionResult(StepExecutionStatus.ERROR, message);
	        }
        }

    public final static String TYPE_ID = CancelDialog.class.getAnnotation(MuseTypeId.class).value();

    private final static Logger LOG = LoggerFactory.getLogger(CancelDialog.class);
    }