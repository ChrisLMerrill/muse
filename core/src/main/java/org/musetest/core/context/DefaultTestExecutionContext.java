package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.project.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultTestExecutionContext extends BaseExecutionContext implements TestExecutionContext
    {
    public DefaultTestExecutionContext(MuseProject project, MuseTest test)
        {
        super(project);
        _test = test;
        }

    @Override
    public MuseTest getTest()
        {
        return _test;
        }

    private final MuseTest _test;
    }


