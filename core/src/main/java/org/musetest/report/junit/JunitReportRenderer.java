package org.musetest.report.junit;

import com.google.common.html.*;
import org.musetest.core.*;
import org.musetest.core.variables.*;

import java.io.*;
import java.nio.charset.*;

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
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outstream, StandardCharsets.UTF_8));   // use the UTF-8 so that chars are encoded correctly for JUnit XML parser

        writer.println("<testsuite>");

        for (MuseTestResult result : _result.getTestResults())
            {
            if (result.isPass())
                writer.println(String.format("    <testcase classname=\"tests\" name=\"%s\">", result.getTest().getDescription()));
            else if (result.getFailureDescription().getFailureType().equals(MuseTestFailureDescription.FailureType.Error))
                {
                writer.println(String.format("    <testcase classname=\"tests\" name=\"%s\">", result.getTest().getDescription()));
                String message = "Unable to complete test due to: " + result.getFailureDescription().getReason();
                message = HtmlEscapers.htmlEscaper().escape(message);
                writer.println(String.format("        <error message=\"%s\"/>", message));
                writer.println("        <system-out>");
                writer.println(HtmlEscapers.htmlEscaper().escape(result.getLog().toString()));
                writer.println("        </system-out>");
                }
            else if (result.getFailureDescription().getFailureType().equals(MuseTestFailureDescription.FailureType.Failure))
                {
                writer.println(String.format("    <testcase classname=\"tests\" name=\"%s\">", result.getTest().getDescription()));
                String message = "test failed due to: " + result.getFailureDescription().getReason();
                message = HtmlEscapers.htmlEscaper().escape(message);
                writer.println(String.format("        <failure message=\"%s\"/>", message));
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


