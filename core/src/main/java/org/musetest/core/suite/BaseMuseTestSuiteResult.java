package org.musetest.core.suite;

import org.musetest.core.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BaseMuseTestSuiteResult implements MuseTestSuiteResult
    {
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
        switch (result.getStatus())
            {
            case Success:
                _success++;
                break;
            case Failure:
                _fail++;
                break;
            case Error:
                _error++;
                break;
            }
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
    }


