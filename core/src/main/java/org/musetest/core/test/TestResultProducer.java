package org.musetest.core.test;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestResultProducer extends MuseEventListener
    {
    MuseTestResult getTestResult();
    }


