package org.musetest.builtins.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("log-message")
@MuseInlineEditString("write to log: {message}")
@MuseStepName("Log a message")
@MuseStepShortDescription("Write a message to the test event log")
@MuseStepIcon("glyph:FontAwesome:COMMENTING_O")
public class LogMessage extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public LogMessage(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        _message = getValueSource(config, MESSAGE_PARAM, true, project);
        }

    @Override
    public StepExecutionResult execute(StepExecutionContext context) throws StepConfigurationError
        {
        Object value = getValue(_message, context, true, Object.class);
        String message;
        if (value == null)
            message = "null";
        else
            message = value.toString();
        context.getTestExecutionContext().raiseEvent(new MessageEvent(message));
        LOG.info(message);
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, message);
        }

    private MuseValueSource _message;

    public final static String MESSAGE_PARAM = "message";

    public final static String TYPE_ID = LogMessage.class.getAnnotation(MuseTypeId.class).value();

    final static Logger LOG = LoggerFactory.getLogger(LogMessage.class);
    }


