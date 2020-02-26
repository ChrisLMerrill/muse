package org.museautomation.builtins.plugins.input;

import org.junit.jupiter.api.*;
import org.museautomation.*;
import org.museautomation.builtins.plugins.state.*;
import org.museautomation.builtins.valuetypes.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.matching.*;
import org.museautomation.core.project.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.task.*;
import org.museautomation.core.task.state.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class InjectInputPluginTests
    {
    @Test
    void injectInputCorrectly() throws MuseExecutionError
        {
        String field1_name = "field1";
        String field1_value = "123";
        addTaskInput(field1_name, new StringValueType(), true);
        addValueToProvider(field1_name, field1_value);

        inject();
        Assertions.assertEquals(0, _context.getEventLog().findEvents(new EventTagMatcher(MuseEvent.WARNING)).size());
        Assertions.assertEquals(0, _context.getEventLog().findEvents(new EventTagMatcher(MuseEvent.ERROR)).size());
        Assertions.assertEquals(field1_value, _context.getVariable(field1_name));
        }

    @Test
    void warningIfTypesDoNotMatch() throws MuseExecutionError
        {
        String field1_name = "field1";
        addTaskInput(field1_name, new StringValueType(), true);
        addValueToProvider(field1_name, 123L);
        inject();
        Assertions.assertEquals(1, _context.getEventLog().findEvents(new EventTagMatcher(MuseEvent.WARNING)).size());
        Assertions.assertNull(_context.getVariable(field1_name));
        }

    void addTaskInput(String name, MuseValueType type, boolean required)
        {
        TaskInput input = new TaskInput();
        input.setName(name);
        input.setRequired(required);
        input.setType(type);
        _task.getInputs().addInput(input);
        }

    void addValueToProvider(String name, Object value)
        {
        _provider.put(name, value);
        }

    @BeforeEach
    void setup()
        {
        _project = new SimpleProject();

        // create Task with required input states
        _task = new SteppedTask();
        TaskInputSet input_set = new TaskInputSet();
        _task.setInputs(input_set);

        // create TaskExecutionContext
        _context = new DefaultTaskExecutionContext(_project, _task);

        // setup InputProvider
        _provider = new SimpleFixedInputProvider();

        // initialize plugin into context
        _context.addPlugin(new InjectInputsPlugin(new InjectInputsPluginConfiguration()));
        _context.addPlugin(new InputProviderPlugin(_provider));
        }

    private void inject() throws MuseExecutionError
        {
        _context.initializePlugins();
        }

    private SimpleFixedInputProvider _provider;
    private TaskExecutionContext _context;
    private SteppedTask _task;
    private SimpleProject _project;
    }