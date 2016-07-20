package org.musetest.core.commandline;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.suite.*;
import org.musetest.core.variables.*;
import org.musetest.report.junit.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // invoked via reflection by RunCommand
public class CommandLineTestSuiteRunner implements MuseResourceRunner
    {
    @Override
    public boolean canRun(MuseResource resource)
        {
        return resource instanceof MuseTestSuite;
        }

    @Override
    public boolean run(MuseProject project, MuseResource resource, boolean verbose, String report_path)
        {
        if (!(resource instanceof MuseTestSuite))
            return false;
        MuseTestSuite suite = (MuseTestSuite) resource;

        try
            {
            MuseTestSuiteRunner runner = new SimpleTestSuiteRunner(suite);
            MuseTestSuiteResult result = runner.execute(project);
            if (result.getFailureCount() == 0 && result.getErrorCount() == 0)
                System.out.println(String.format("%d tests completed successfully.", result.getSuccessCount()));
            else if (result.getErrorCount() == 0)
                System.out.println(String.format("%d tests failed and %d tests completed successfully.", result.getFailureCount(), result.getSuccessCount()));
            else if (result.getFailureCount() == 0)
                System.out.println(String.format("%d tests could not be executed due to errors and %d tests completed successfully.", result.getErrorCount(), result.getSuccessCount()));
            else
                System.out.println(String.format("%d tests could not be executed due to errors, %d tests failed and %d tests completed successfully.", result.getErrorCount(), result.getFailureCount(), result.getSuccessCount()));

            if (result.getErrorCount() > 0 && verbose)
                {
                StringBuilder builder = new StringBuilder();
                builder.append("Tests that could not be completed: ");
                int counter = 0;
                for (MuseTestResult test_result : result.getTestResults())
                    if (test_result.getFailureDescription().getFailureType().equals(MuseTestFailureDescription.FailureType.Error))
                        {
                        if (counter > 0)
                            builder.append(", ");
                        builder.append(test_result.getTest().getDescription());
                        counter++;
                        }
                builder.append("\n");
                System.out.println(builder.toString());
                }
            if (result.getFailureCount() > 0 && verbose)
                {
                StringBuilder builder = new StringBuilder();
                builder.append("Failing tests: ");
                int counter = 0;
                for (MuseTestResult test_result : result.getTestResults())
                    if (test_result.getFailureDescription().getFailureType().equals(MuseTestFailureDescription.FailureType.Failure))
                        {
                        if (counter > 0)
                            builder.append(", ");
                        builder.append(test_result.getTest().getDescription());
                        counter++;
                        }
                builder.append("\n");
                System.out.println(builder.toString());
                }

            if (result.getErrorCount() + result.getFailureCount() > 0 && verbose)
                {
                System.out.println("Test Logs (for failing and errored tests):");
                for (MuseTestResult test_result : result.getTestResults())
                    if (!(test_result.isPass()))
                        System.out.println(buildResultDetails(test_result));
                }

            try
                {
                if (report_path != null)
                    {
                    JunitReportRenderer renderer = new JunitReportRenderer(result);
                    File report_file = new File(report_path);
                    if (report_file.isDirectory())
                        report_file = new File(report_file, "testresults.xml");
                    FileOutputStream outstream = new FileOutputStream(report_file);
                    renderer.render(outstream);
                    outstream.close();
                    }
                }
            catch (IOException e)
                {
                System.err.println("Unable to write JUnit test report");
                e.printStackTrace(System.err);
                }
            if (result.getErrorCount() > 0)
                return false;
            }
        catch (Exception e)
            {
            System.err.println("Unable to run test suite " + suite.getMetadata().getId());
            e.printStackTrace(System.err);
            return false;
            }
        return true;
        }

    public String buildResultDetails(MuseTestResult result)
        {
        StringBuilder builder = new StringBuilder();
        builder.append("\n-------------------\n");
        builder.append("Test: ").append(result.getTest().getDescription()).append("\n");
        builder.append(result.getLog().toString()).append("\n");
        builder.append("-------------------");
        return builder.toString();
        }
    }


