package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.context.initializers.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultTestExecutionContext extends BaseExecutionContext implements TestExecutionContext
    {
    public DefaultTestExecutionContext(MuseProject project, MuseTest test)
        {
        super(project);
        _test = test;

        ContextInitializers.setup(this);
        addInitializer(new TestDefaultsInitializer(this));
        }

    @Override
    public MuseTest getTest()
        {
        return _test;
        }

    private final MuseTest _test;
    }


