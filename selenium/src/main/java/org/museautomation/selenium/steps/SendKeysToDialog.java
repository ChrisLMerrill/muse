package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("sendkeys-dialog")
@MuseStepName("Send Keys to Dialog")
@MuseInlineEditString("Send {keys} to Dialog")
@MuseStepIcon("glyph:FontAwesome:KEYBOARD_ALT")
@MuseStepTypeGroup("Selenium.Dialog")
@MuseStepShortDescription("Send keys to a Javascript Prompt Dialog")
@MuseStepLongDescription("Types the supplied keystrokes to the dialog.")
@MuseSubsourceDescriptor(displayName = "Keys", description = "Text string containing the keys to send to the dialog", type = SubsourceDescriptor.Type.Named, name = SendKeysToDialog.KEYS_PARAM)
@SuppressWarnings("unused")  // instantiated via reflection
public class SendKeysToDialog extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public SendKeysToDialog(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _keys_source = getValueSource(config, KEYS_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context)
        {
        try
	        {
            String keys = getValue(_keys_source, context, false, String.class);
	        getDriver(context).switchTo().alert().sendKeys(keys);
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
	        }
        catch (Throwable thrown)
	        {
            String message = "Error when attempting to cancel/dismiss an alert: " + thrown.getMessage();
            LOG.error(message);
	        return new BasicStepExecutionResult(StepExecutionStatus.ERROR, message);
	        }
        }

    private MuseValueSource _keys_source;

    public final static String TYPE_ID = SendKeysToDialog.class.getAnnotation(MuseTypeId.class).value();
    public final static String KEYS_PARAM = "keys";

    private final static Logger LOG = LoggerFactory.getLogger(SendKeysToDialog.class);
    }