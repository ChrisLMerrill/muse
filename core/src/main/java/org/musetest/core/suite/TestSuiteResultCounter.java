package org.musetest.core.suite;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestSuiteResultCounter
    {
    public TestSuiteResultCounter(int successes, int failures, int errors)
        {
        _successes = successes;
        _failures = failures;
        _errors = errors;
        }

    public int getSuccesses()
        {
        return _successes;
        }

    public int getFailures()
        {
        return _failures;
        }

    public int getErrors()
        {
        return _errors;
        }

    private int _successes = 0;
    private int _failures = 0;
    private int _errors = 0;
    }


