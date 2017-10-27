package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepFinderTests
	{
	@Test
	public void findRootFromContext()
	    {
	    setupTestInContext();
	    Assert.assertEquals(_root_step, new StepFinder(_context).by(ROOT_ID));
	    }

	@Test
	public void findChildFromContext()
	    {
	    setupTestInContext();
	    Assert.assertEquals(_log_step, new StepFinder(_context).by(LOG_ID));
	    }

	@Test
	public void findMissing()
	    {
	    setupTestInContext();
	    Assert.assertNull(new StepFinder(_context).by(123L));
	    }

	@Test
	public void findStepInFunction() throws IOException
		{
		setupTestInContext();
	    StepConfiguration log_step = new StepConfiguration(LogMessage.TYPE_ID);
	    log_step.setStepId(234L);
	    StepConfiguration function_step = new StepConfiguration(BasicCompoundStep.TYPE_ID);
	    function_step.addChild(log_step);
	    Function function = new Function();
	    function.setStep(function_step);
	    function.setId("function1");
	    _context.getProject().getResourceStorage().addResource(function);

	    StepConfiguration call_step = new StepConfiguration(CallFunction.TYPE_ID);
	    call_step.addSource(CallFunction.ID_PARAM, ValueSourceConfiguration.forValue("function1"));
	    _root_step.addChild(call_step);

	    Assert.assertEquals(log_step, new StepFinder(_context).by(234L));
	    }

	@Test
	public void findStepInMacro() throws IOException
		{
		setupTestInContext();
	    StepConfiguration log_step = new StepConfiguration(LogMessage.TYPE_ID);
	    log_step.setStepId(987L);
	    StepConfiguration macro_step = new StepConfiguration(BasicCompoundStep.TYPE_ID);
	    macro_step.addChild(log_step);
	    Macro macro = new Macro();
	    macro.setStep(macro_step);
	    macro.setId("macro1");
	    _context.getProject().getResourceStorage().addResource(macro);

	    StepConfiguration call_step = new StepConfiguration(CallMacroStep.TYPE_ID);
	    call_step.addSource(CallMacroStep.ID_PARAM, ValueSourceConfiguration.forValue("macro1"));
	    _root_step.addChild(call_step);

	    Assert.assertEquals(log_step, new StepFinder(_context).by(987L));
	    }

	private void setupTestInContext()
		{
		_root_step = new StepConfiguration(BasicCompoundStep.TYPE_ID);
		_root_step.setStepId(ROOT_ID);
		_log_step = new StepConfiguration(LogMessage.TYPE_ID);
		_log_step.setStepId(LOG_ID);
		_root_step.addChild(_log_step);
		SteppedTest test = new SteppedTest();
		test.setStep(_root_step);
		_context = new DefaultSteppedTestExecutionContext(new DefaultTestExecutionContext(new SimpleProject(), test));
		}

	private StepConfiguration _root_step;
	private StepConfiguration _log_step;
	private TestExecutionContext _context;
	private final static Long ROOT_ID = new Random().nextLong();
	private final static Long LOG_ID = new Random().nextLong();
	}


