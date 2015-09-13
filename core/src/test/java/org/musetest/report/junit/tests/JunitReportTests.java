package org.musetest.report.junit.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.mocks.*;
import org.musetest.core.suite.*;
import org.musetest.core.test.*;
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
        BaseMuseTestSuiteResult suite_result = new BaseMuseTestSuiteResult();
        suite_result.addTestResult(new BaseMuseTestResult(MuseTestResultStatus.Error, new MockTest(MuseTestResultStatus.Error, "test 1"), new EventLog()));
        suite_result.addTestResult(new BaseMuseTestResult(MuseTestResultStatus.Failure, new MockTest(MuseTestResultStatus.Failure, "test 2"), new EventLog()));
        suite_result.addTestResult(new BaseMuseTestResult(MuseTestResultStatus.Failure, new MockTest(MuseTestResultStatus.Success, "test 3"), new EventLog()));
        suite_result.addTestResult(new BaseMuseTestResult(MuseTestResultStatus.Success, new MockTest(MuseTestResultStatus.Success, "test 4"), new EventLog()));
        suite_result.addTestResult(new BaseMuseTestResult(MuseTestResultStatus.Success, new MockTest(MuseTestResultStatus.Success, "test 5"), new EventLog()));
        suite_result.addTestResult(new BaseMuseTestResult(MuseTestResultStatus.Success, new MockTest(MuseTestResultStatus.Success, "test 6"), new EventLog()));
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


