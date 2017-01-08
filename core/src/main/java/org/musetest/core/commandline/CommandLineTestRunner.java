package org.musetest.core.commandline;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.execution.*;
import org.musetest.core.resource.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // invoked via reflection by RunCommand
public class CommandLineTestRunner implements MuseResourceRunner
    {
    @Override
    public boolean canRun(MuseResource resource)
        {
        return resource instanceof MuseTest;
        }

    @Override
    public boolean run(MuseProject project, MuseResource resource, boolean verbose, String report_path)
        {
        if (!(resource instanceof MuseTest))
            return false;
        MuseTest test = (MuseTest) resource;

        TestRunner runner = TestRunnerFactory.create(project, test, true, false);
        if (verbose)
            {
            System.out.println("--------------------------------------------------------------------------------");
            final EventLogPrinter printer = new EventLogPrinter(System.out);
            runner.getTestContext().addEventListener(printer::print);
            }

        runner.runTest();
        MuseTestResult result = runner.getResult();

        if (verbose)
            System.out.println("--------------------------------------------------------------------------------");
        else
            {
            if (result.isPass())
                System.out.println(String.format("SUCCESS: Test '%s' completed successfully.", result.getTest().getDescription()));
            else
                System.out.println(result.getFailureDescription());
            }
        return true;
        }
    }
