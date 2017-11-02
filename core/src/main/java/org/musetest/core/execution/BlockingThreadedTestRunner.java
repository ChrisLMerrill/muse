package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.context.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess") // part of public API
public class BlockingThreadedTestRunner extends ThreadedTestRunner
    {
    @SuppressWarnings("unused,WeakerAccess")  // part of public API
    public BlockingThreadedTestRunner(MuseProject project, MuseTest test)
        {
        super(project, test);
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
            _context.raiseEvent(new MuseEvent(InterruptedEventType.TYPE));
            }
        }
    }


