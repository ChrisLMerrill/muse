package org.museautomation.core.events;

import org.apache.commons.io.*;
import org.junit.jupiter.api.*;
import org.museautomation.builtins.step.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resultstorage.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptest.*;
import org.museautomation.core.suite.*;

import java.io.*;
import java.nio.file.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class EventLogWriterPluginTests
    {
    @Test
    void writeEventsAsTheyAreReceived() throws MuseExecutionError, IOException
        {
        MuseExecutionContext context = new DefaultSteppedTestExecutionContext(new SimpleProject(), new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID)));
        EventLogWriterPlugin writer = new EventLogWriterPlugin();
        context.addPlugin(writer);
        context.addPlugin(new StoragePlugin());
        context.initializePlugins();

        // nothing will be written until the start event is received
        context.raiseEvent(StartTestEventType.create("test1", "Test #1"));
        context.raiseEvent(MessageEventType.create("message1"));

        // check the log files were started
        File json_log = new File(_test_folder, EventLogWriterPlugin.PARTIAL_EVENT_FILE);
        Assertions.assertTrue(json_log.exists());
        File plain_log = new File(_test_folder, EventLogWriterPlugin.READABLE_EVENT_FILE);
        Assertions.assertTrue(plain_log.exists());

        Assertions.assertTrue(json_log.length() > 0);
        String json_log_start = new String(FileUtils.readFileToByteArray(json_log));
        Assertions.assertTrue(json_log_start.startsWith("["));  // it is the JSOn versino
        Assertions.assertTrue(json_log_start.contains("message1"));
        Assertions.assertTrue(plain_log.length() > 0);
        String plain_log_start = new String(FileUtils.readFileToByteArray(plain_log));
        Assertions.assertTrue(plain_log_start.startsWith("First event raised at")); // it is the plain-text version
        Assertions.assertTrue(plain_log_start.contains("message1"));

        final long json_length = json_log.length();
        final long plain_length = plain_log.length();
        context.raiseEvent(MessageEventType.create("message2"));

        Assertions.assertTrue(json_log.length() > json_length);
        Assertions.assertTrue(new String(FileUtils.readFileToByteArray(json_log)).contains("message2"));

        Assertions.assertTrue(plain_log.length() > plain_length);
        Assertions.assertTrue(new String(FileUtils.readFileToByteArray(plain_log)).contains("message2"));

        context.cleanup();
        json_log.deleteOnExit();
        plain_log.deleteOnExit();
        }

    @Test
    void logTestAndSuiteEvents() throws MuseExecutionError, IOException
        {
        SimpleProject project = new SimpleProject();
        IdListTestSuite suite = new IdListTestSuite();
        MuseExecutionContext suite_context = new DefaultTestSuiteExecutionContext(project, suite);
        suite_context.addPlugin(new EventLogWriterPlugin());
        suite_context.addPlugin(new StoragePlugin());
        suite_context.initializePlugins();

        suite_context.raiseEvent(StartSuiteEventType.create(suite));
        suite_context.raiseEvent(MessageEventType.create("suite-message1"));

        File suite_json_log = new File(_base_folder, EventLogWriterPlugin.PARTIAL_EVENT_FILE);
        Assertions.assertTrue(suite_json_log.exists());
        File suite_plain_log = new File(_base_folder, EventLogWriterPlugin.READABLE_EVENT_FILE);
        Assertions.assertTrue(suite_plain_log.exists());
        Assertions.assertTrue(new String(FileUtils.readFileToByteArray(suite_json_log)).contains("suite-message1"));
        Assertions.assertTrue(new String(FileUtils.readFileToByteArray(suite_plain_log)).contains("suite-message1"));

        MuseExecutionContext test_context = new DefaultSteppedTestExecutionContext(suite_context, new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID)));
        EventLogWriterPlugin writer = new EventLogWriterPlugin();
        test_context.addPlugin(writer);
        test_context.initializePlugins();

        test_context.raiseEvent(StartSuiteTestEventType.create("suite-test-var1"));
        test_context.raiseEvent(MessageEventType.create("test-m1"));
        File test_json_log = new File(_test_folder, EventLogWriterPlugin.PARTIAL_EVENT_FILE);
        Assertions.assertTrue(test_json_log.exists());
        File test_plain_log = new File(_test_folder, EventLogWriterPlugin.READABLE_EVENT_FILE);
        Assertions.assertTrue(test_plain_log.exists());
        Assertions.assertTrue(new String(FileUtils.readFileToByteArray(test_json_log)).contains("test-m1"));
        Assertions.assertTrue(new String(FileUtils.readFileToByteArray(test_plain_log)).contains("test-m1"));
        }


    @BeforeEach
    void setup() throws IOException
        {
        _base_folder = Files.createTempDirectory("-test.tmp").toFile();
        _base_folder.deleteOnExit();
        _test_folder = new File(_base_folder, ((Long) System.currentTimeMillis()).toString());
        if (!_test_folder.exists())
            if (!_test_folder.mkdir())
                throw new IOException("Unable to create test folder");
        }

    private File _base_folder;
    private File _test_folder;

    class StoragePlugin implements LocalStorageLocationProvider, MusePlugin
        {
        @Override
        public boolean conditionallyAddToContext(MuseExecutionContext context, boolean automatic)
            {
            return true;
            }

        @Override
        public void initialize(MuseExecutionContext context)
            {
            }

        @Override
        public void shutdown()
            {
            }

        @Override
        public String getId()
            {
            return null;
            }

        @Override
        public File getBaseFolder()
            {
            return _base_folder;
            }

        @Override
        public File getTestFolder(TestExecutionContext test_context)
            {
            return _test_folder;
            }
        }
    }
