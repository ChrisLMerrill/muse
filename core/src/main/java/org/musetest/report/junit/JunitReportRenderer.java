package org.musetest.report.junit;

import com.google.common.html.*;
import org.musetest.core.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JunitReportRenderer
    {
    public JunitReportRenderer(MuseTestSuiteResult result)
        {
        _result = result;
        }

    public void render(OutputStream outstream)
        {
        PrintWriter writer = new PrintWriter(outstream);

        writer.println("<testsuite>");

        for (MuseTestResult result : _result.getTestResults())
            {
            if (result.getStatus().equals(MuseTestResultStatus.Success))
                writer.println(String.format("    <testcase classname=\"tests\" name=\"%s\">", result.getTest().getDescription()));
            else if (result.getStatus().equals(MuseTestResultStatus.Error))
                {
                writer.println(String.format("    <testcase classname=\"tests\" name=\"%s\">", result.getTest().getDescription()));
                writer.println(String.format("        <error message=\"%s\"/>", "Unable to execute test"));
                writer.println("        <system-out>");
                writer.println(HtmlEscapers.htmlEscaper().escape(result.getLog().toString()));
                writer.println("        </system-out>");
                }
            else if (result.getStatus().equals(MuseTestResultStatus.Failure))
                {
                writer.println(String.format("    <testcase classname=\"tests\" name=\"%s\">", result.getTest().getDescription()));
                writer.println(String.format("        <failure message=\"%s\"/>", "test failed"));
                writer.println("        <system-out>");
                writer.println(HtmlEscapers.htmlEscaper().escape(result.getLog().toString()));
                writer.println("        </system-out>");
                }
            writer.println("    </testcase>");
            }

        writer.println("</testsuite>");
        writer.flush();
        }

    private MuseTestSuiteResult _result;
    }


