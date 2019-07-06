package org.musetest.seleniumide;

import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.seleniumide.steps.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SideTestConverter
	{
	public ConversionResult convert(SideTest side_test, SideProject project)
		{
		final SteppedTest muse_test = new SteppedTest();
		StepConfiguration main_step = new StepConfiguration(BasicCompoundStep.TYPE_ID);
		muse_test.setStep(main_step);

		ConversionResult result = new ConversionResult();
		result._test = muse_test;
		StepConverters converters = StepConverters.get();
		for (SideCommand command : side_test.commands)
			{
			String failure_message;
			try
				{
				StepConfiguration step = converters.convertStep(project.getUrl(), command.getCommand(), command.getTarget(), command.getValue());
				if (step != null)
					{
					main_step.addChild(step);
					result._total_steps++;
					}
				}
			catch (UnsupportedError e)
				{
				failure_message = e.getMessage();
                String target = "";
                if (command.getTarget() != null)
                    target = command.getTarget();
                String value = "";
                if (command.getValue() != null)
                    value = command.getValue();
                String error = String.format("%s: %s, %s, %s", failure_message, command.getCommand(), target, value);
                result.recordFailure(error);
                StepConfiguration comment = new StepConfiguration(command.getCommand());
                comment.addSource("target", ValueSourceConfiguration.forValue(target));
                comment.addSource("value", ValueSourceConfiguration.forValue(value));
                comment.setMetadataField(StepConfiguration.META_DESCRIPTION, String.format("%s(%s,%s)", command, target, value));
                main_step.addChild(comment);
				}
			}

		return result;
		}
	}


