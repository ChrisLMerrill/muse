package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess") // part of public API
public class BlockingThreadedTestRunner extends ThreadedTestRunner
    {
    @SuppressWarnings("WeakerAccess")  // part of public API
    public BlockingThreadedTestRunner(MuseProject project, MuseTest test, TestExecutionContext context)
        {
        super(project, test, context);
        }

    @Override
    public void runTest()
        {
        Thread t = new Thread(this);
        if (_loader != null)
            t.setContextClassLoader(_loader);
        t.start();

        try
            {
            t.join();
            }
        catch (InterruptedException e)
            {
            LOG.error("Interrupted: Unable to re-join the test execution thread.");
            }
        }

    private final static Logger LOG = LoggerFactory.getLogger(BlockingThreadedTestRunner.class);
    }


