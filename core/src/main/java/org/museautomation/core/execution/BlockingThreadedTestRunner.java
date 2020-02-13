package org.museautomation.core.execution;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.test.*;

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


