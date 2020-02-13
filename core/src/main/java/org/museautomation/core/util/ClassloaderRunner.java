package org.museautomation.core.util;

import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ClassloaderRunner
    {
    public static void executeWithClassloader(Runnable runnable, ClassLoader loader)
        {
        if (loader == null)
            {
            runnable.run();
            return;
            }

        Thread t = new Thread(runnable);
        t.setContextClassLoader(loader);
        t.start();
        try
            {
            t.join();
            }
        catch (InterruptedException e)
            {
            LOG.warn("ClassloaderRunner.executeWithClassloader() - interrupted", e);
            Thread.currentThread().interrupt();
            }
        }

    final static Logger LOG = LoggerFactory.getLogger(ClassloaderRunner.class);
    }


