package org.musetest.core.commandline;

import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.execution.*;
import org.musetest.core.resource.*;

import java.io.*;

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
    public boolean run(MuseProject project, MuseResource resource, boolean verbose, String output_path, String runner_id)
        {
        if (!(resource instanceof MuseTest))
            return false;
        MuseTest test = (MuseTest) resource;

        File output_folder = null;
        if (output_path != null)
        	output_folder = new File(output_path);

        TestRunner runner = TestRunnerFactory.createSynchronousRunner(project, test);
        if (output_folder != null)
        	runner.getExecutionContext().addTestPlugin(new EventLogger());
        if (verbose)
            {
            System.out.println("--------------------------------------------------------------------------------");
            final EventLogPrinter printer = new EventLogPrinter(System.out);
            runner.getExecutionContext().addEventListener(printer::print);
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

		if (output_folder != null)
			{
			String test_name = test.getId();
			File base_file = new File(output_folder, test_name);
			for (DataCollector collector : runner.getExecutionContext().getDataCollectors())
				{
				final File data_file = new File(output_folder, collector.getData().suggestFilename());
				// TODO add numbers to the filenames to avoid overwriting other related files
				// TODO get DataCollector metadata associated with this collector and store it?
				try (FileOutputStream outstream = new FileOutputStream(data_file))
					{
					collector.getData().write(outstream);
					}
				catch (IOException e)
					{
					System.out.println(String.format("Unable to store results of test %s in %s due to: %s", test_name, data_file.getAbsolutePath(), e.getMessage()));
					}
				}
			}

        return true;
        }
    }
