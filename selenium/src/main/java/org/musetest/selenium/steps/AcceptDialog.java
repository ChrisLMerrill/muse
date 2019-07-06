package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("accept-dialog")
@MuseStepName("Accept Dialog")
@MuseInlineEditString("accept dialog")
@MuseStepIcon("glyph:FontAwesome:CHECK")
@MuseStepTypeGroup("Seleniumm.Dialog")
@MuseStepShortDescription("Accept a Javascript Dialog")
@MuseStepLongDescription("Accepts the currently-open Javascript dialog, which could be an alert, confirm or prompt dialog. This is equivalent to pressing the OK button.")
@SuppressWarnings("unused")  // instantiated via reflection
public class AcceptDialog extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public AcceptDialog(StepConfiguration config, MuseProject project)
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context)
        {
        try
	        {
	        getDriver(context).switchTo().alert().accept();
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
	        }
        catch (Throwable thrown)
	        {
            String message = "Error when attempting to accept an alert: " + thrown.getMessage();
            LOG.error(message);
	        return new BasicStepExecutionResult(StepExecutionStatus.ERROR, message);
	        }
        }

    public final static String TYPE_ID = AcceptDialog.class.getAnnotation(MuseTypeId.class).value();

    private final static Logger LOG = LoggerFactory.getLogger(AcceptDialog.class);
    }