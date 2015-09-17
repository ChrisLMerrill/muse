package org.musetest.seleniumide;

import org.musetest.core.steptest.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ConversionResult
    {
    public void recordFailure(String error)
        {
        _errors.add(error);
        _success = false;
        }

    public boolean _success = true;
    public int _total_steps = 0;
    public List<String> _errors = new ArrayList<>();
    public SteppedTest _test;
    }


