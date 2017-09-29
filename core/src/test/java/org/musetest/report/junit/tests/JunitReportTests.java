package org.musetest.report.junit.tests;

import org.junit.*;
import org.musetest.core.events.*;
import org.musetest.core.mocks.*;
import org.musetest.core.suite.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;
import org.musetest.report.junit.*;

import java.io.*;

/**
 * Test the coversion of a MuseTestSuiteResult into a junit-format XML file
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JunitReportTests
    {
    @Test
    public void generateJunitXmlReport() throws IOException
        {
        BaseMuseTestSuiteResult suite_result = new BaseMuseTestSuiteResult(new IdListTestSuite());
        suite_result.addTestResult(new BaseMuseTestResult(new MockTest(new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, "error"), "test 1"), new EventLog(), new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, "error")));
        suite_result.addTestResult(new BaseMuseTestResult(new MockTest(new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Failure, "failed"), "test 2"), new EventLog(), new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Failure, "failed")));
        suite_result.addTestResult(new BaseMuseTestResult(new MockTest(null, "test 3"), new EventLog(), new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Failure, "failed")));
        suite_result.addTestResult(new BaseMuseTestResult(new MockTest(null, "test 4"), new EventLog(), null));
        suite_result.addTestResult(new BaseMuseTestResult(new MockTest(null, "test 5"), new EventLog(), null));
        suite_result.addTestResult(new BaseMuseTestResult(new MockTest(null, "test 6"), new EventLog(), null));
        JunitReportRenderer suite = new JunitReportRenderer(suite_result);

        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        suite.render(outstream);

        String report_content = outstream.toString();

        Assert.assertTrue(report_content.contains("test 1"));
        Assert.assertTrue(report_content.contains("test 2"));
        Assert.assertTrue(report_content.contains("test 3"));
        Assert.assertTrue(report_content.contains("test 4"));
        Assert.assertTrue(report_content.contains("test 5"));
        Assert.assertTrue(report_content.contains("test 6"));
        Assert.assertTrue(report_content.contains("<error"));
        Assert.assertTrue(report_content.contains("<failure"));
        }
    }


