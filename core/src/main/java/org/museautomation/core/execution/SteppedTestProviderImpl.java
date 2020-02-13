package org.museautomation.core.execution;

import org.museautomation.core.*;
import org.museautomation.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SteppedTestProviderImpl implements SteppedTestProvider
    {
    public SteppedTestProviderImpl(MuseProject project, SteppedTest test)
        {
        _project = project;
        _test = test;
        }

    @Override
    public MuseProject getProject()
        {
        return _project;
        }

    @Override
    public SteppedTest getTest()
        {
        return _test;
        }

    private MuseProject _project;
    private SteppedTest _test;
    }


