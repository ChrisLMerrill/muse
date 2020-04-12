package org.museautomation.builtins.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("log-message")
@MuseInlineEditString("write to log: {message}")
@MuseStepName("Log a message")
@MuseStepIcon("glyph:FontAwesome:COMMENTING_ALT")
@MuseStepShortDescription("Write a message to the test event log")
@MuseStepLongDescription("The 'message' source will be resolved and converted to a string. The result is added to a MessageEvent and sent to the event log for the test. It is also sent to the logging output (by default, standard out) at INFO level.")
@MuseSubsourceDescriptor(displayName = "Message", description = "The message to log", type = SubsourceDescriptor.Type.Named, name = LogMessage.MESSAGE_PARAM, defaultValue = "log this message")
public class LogMessage extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public LogMessage(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _message = getValueSource(config, MESSAGE_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws ValueSourceResolutionError
        {
        Object value = getValue(_message, context, true, Object.class);
        String message;
        if (value == null)
            message = "null";
        else
            message = value.toString();
        context.raiseEvent(MessageEventType.create(message));
        LOG.info(message);
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private final MuseValueSource _message;

    public final static String MESSAGE_PARAM = "message";
    public final static String TYPE_ID = LogMessage.class.getAnnotation(MuseTypeId.class).value();

    private final static Logger LOG = LoggerFactory.getLogger(LogMessage.class);
    }