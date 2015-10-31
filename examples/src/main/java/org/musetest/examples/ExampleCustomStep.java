package org.musetest.examples;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseStepName("Log all")
@MuseStepIcon("glyph:FontAwesome:COMMENTING_ALT")
@MuseStepTypeGroup("custom")
@MuseStepShortDescription("Log all parameters to the test event log")
@MuseStepLongDescription("Each parameter will be resolved and the name and value will be formatted into a MessageEvent.")
@MuseTypeId("log-params")
@MuseInlineEditString("log all params")
public class ExampleCustomStep extends BaseStep
    {
    public ExampleCustomStep(StepConfiguration config, MuseProject project) throws StepConfigurationError
        {
        super(config);
        _sources = config.getSources();
        _project = project;
        }
        
    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws StepConfigurationError
        {
        for (String name : _sources.keySet())
        	{
        	ValueSourceConfiguration config = _sources.get(name);
        	MuseValueSource source = config.createSource(_project); 
        	Object value = getValue(source, context, true, Object.class);
        	context.getTestExecutionContext().raiseEvent(new MessageEvent(name + "=" + value));
        	}
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseProject _project;
    private Map<String, ValueSourceConfiguration> _sources;

    final static Logger LOG = LoggerFactory.getLogger(ExampleCustomStep.class);
    }


