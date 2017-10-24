package org.musetest.report.junit;

import com.google.common.html.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
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
            String suite_name = _result.getSuite().getId();
            String test_name = result.getName();
            String failure_type = null;
            String failure_message = null;

            // create error info
            if (!result.isPass())
                {
                if (result.getFailureDescription().getFailureType().equals(MuseTestFailureDescription.FailureType.Error))
                    {
                    failure_type = "error";
                    failure_message = HtmlEscapers.htmlEscaper().escape("Unable to complete test due to: " + result.getFailureDescription().getReason());
                    }
                else if (result.getFailureDescription().getFailureType().equals(MuseTestFailureDescription.FailureType.Failure))
                    {
                    failure_type = "failure";
                    failure_message = HtmlEscapers.htmlEscaper().escape("test failed due to: " + result.getFailureDescription().getReason());
                    }
                }

            // write the output
            writer.println(String.format("    <testcase classname=\"%s\" name=\"%s\">", suite_name, test_name));
            if (failure_type != null)
                writer.println(String.format("        <%s message=\"%s\"/>", failure_type, failure_message));
            EventLog log = result.getLog();
            if (log != null)
	            {
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


