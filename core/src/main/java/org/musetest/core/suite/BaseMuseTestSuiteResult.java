package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BaseMuseTestSuiteResult implements MuseTestSuiteResult
    {
    public BaseMuseTestSuiteResult(MuseTestSuite suite)
        {
        _suite = suite;
        }

    @Override
    public int getSuccessCount()
        {
        return _success;
        }

    @Override
    public int getFailureCount()
        {
        return _fail;
        }

    @Override
    public int getErrorCount()
        {
        return _error;
        }

    public void addTestResult(MuseTestResult result)
        {
        _test_results.add(result);
        if (result.isPass())
            _success++;
        else
            {
            if (result.getFailureDescription().getFailureType().equals(MuseTestFailureDescription.FailureType.Error))
                _error++;
            else if (result.getFailureDescription().getFailureType().equals(MuseTestFailureDescription.FailureType.Failure))
                _fail++;
            else if (result.getFailureDescription().getFailureType().equals(MuseTestFailureDescription.FailureType.Interrupted))
                _error++;
            }
        }

    @Override
    public MuseTestSuite getSuite()
        {
        return _suite;
        }

    @Override
    public List<MuseTestResult> getTestResults()
        {
        return _test_results;
        }

    private int _success;
    private int _fail;
    private int _error;

    private ArrayList<MuseTestResult> _test_results = new ArrayList<>();
    private MuseTestSuite _suite;
    }


