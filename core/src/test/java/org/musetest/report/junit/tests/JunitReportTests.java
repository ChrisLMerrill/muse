package org.musetest.report.junit.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.suite.*;
import org.musetest.report.junit.*;

import java.io.*;

/**
 * Test the conversion of a MuseTestSuiteResult into a junit-format XML file
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JunitReportTests
    {
    @Test
    public void generateJunitXmlReport()
        {
        JUnitReportData report_data = new JUnitReportData();
        report_data.addResult(TestResult.create("test 1", "test1", "erred", TestResult.FailureType.Error, "erred"), null);
        report_data.addResult(TestResult.create("test 2", "test2", "failed", TestResult.FailureType.Failure, "failed"), null);
        report_data.addResult(TestResult.create("test 3", "test3", "success"), null);
        report_data.setSuiteName("suite 1");

        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        report_data.write(outstream);

        String report_content = outstream.toString();

        Assert.assertTrue(report_content.contains("test 1"));
        Assert.assertTrue(report_content.contains("test 2"));
        Assert.assertTrue(report_content.contains("test 3"));
        Assert.assertTrue(report_content.contains("<error"));
        Assert.assertTrue(report_content.contains("<failure"));
        }
    }


