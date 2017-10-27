package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.steptest.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepFinder
	{
	public StepFinder(TestExecutionContext test_context)
		{
		_context = test_context;
		if (test_context.getTest() instanceof SteppedTest)
			_root_step = ((SteppedTest)test_context.getTest()).getStep();
		}

	public StepConfiguration by(Long step_id)
		{
		if (_root_step == null)
			return null;

		return findById(_root_step, step_id);
		}

	public StepConfiguration by(StepEvent event)
		{
		if (event.getConfig() != null)
			return event.getConfig();
		return findById(_root_step, event.getStepId());
		}

	private StepConfiguration findById(StepConfiguration step, Long id)
		{
		if (id.equals(step.getStepId()))
			return step;
		if (step.getChildren() != null)
			for (StepConfiguration child : step.getChildren())
				{
				StepConfiguration found = findById(child, id);
				if (found != null)
					return found;
				}
		if (step.getType().equals(CallFunction.TYPE_ID) || step.getType().equals(CallMacroStep.TYPE_ID))
			{
			String resource_id = null;
			String source_name = step.getType().equals(CallFunction.TYPE_ID) ? CallFunction.ID_PARAM : CallMacroStep.ID_PARAM;

			try
				{
				resource_id = step.getSource(source_name).createSource(_context.getProject()).resolveValue(_context).toString();
				}
			catch (Exception e)
				{
				LOG.error(String.format("Unable to look for step %d within %s...unable to resolve the id source because of: %s", id, step.getType(), e.getMessage()));
				}

			ContainsStep steps = _context.getProject().getResourceStorage().getResource(resource_id, ContainsStep.class);
			if (steps != null)
				{
				StepConfiguration found_in_function = findById(steps.getStep(), id);
				if (found_in_function != null)
					return found_in_function;
				}
			}
		return null;
		}

	private MuseExecutionContext _context;
	private StepConfiguration _root_step = null;

	private final static Logger LOG = LoggerFactory.getLogger(StepFinder.class);
	}


