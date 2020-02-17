package org.museautomation.junit.tests;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.junit.*;

import java.io.*;

/**
 * Test the conversion of a MuseTestSuiteResult into a junit-format XML file
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class JunitReportTests
    {
    @Test
    void generateJunitXmlReport()
        {
        JUnitReportData report_data = new JUnitReportData();
        report_data.addResult(TaskResult.create("test 1", "test1", "erred", TaskResult.FailureType.Error, "erred"), null);
        report_data.addResult(TaskResult.create("test 2", "test2", "failed", TaskResult.FailureType.Failure, "failed"), null);
        report_data.addResult(TaskResult.create("test 3", "test3", "success"), null);
        report_data.setSuiteName("suite 1");

        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        report_data.write(outstream);

        String report_content = outstream.toString();

        Assertions.assertTrue(report_content.contains("test 1"));
        Assertions.assertTrue(report_content.contains("test 2"));
        Assertions.assertTrue(report_content.contains("test 3"));
        Assertions.assertTrue(report_content.contains("<error"));
        Assertions.assertTrue(report_content.contains("<failure"));
        }
    }


