package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.test.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess") // part of public API
public class BlockingThreadedTestRunner extends ThreadedTestRunner
    {
    @SuppressWarnings("unused,WeakerAccess")  // part of public API
    public BlockingThreadedTestRunner(MuseExecutionContext context, TestConfiguration test)
        {
        super(context, test);
        }

    @SuppressWarnings("unused,WeakerAccess")  // part of public API
    public BlockingThreadedTestRunner(TestExecutionContext context)
        {
        super(context);
        }

    @Override
    public void runTest()
        {
        super.runTest();
        try
            {
            _thread.join();
            }
        catch (InterruptedException e)
            {
            _test_context.raiseEvent(new MuseEvent(InterruptedEventType.TYPE));
            }
        }
    }


