package org.museautomation.builtins.plugins.state;

import org.junit.jupiter.api.*;
import org.museautomation.*;
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
class InjectStatePluginTests
    {
    @Test
    void injectStateCorrectly() throws MuseExecutionError
        {
        String field1_name = "field1";
        String field1_value = "123";
        addTaskInput(field1_name, new StringValueType());
        addValueDefToStateDef(field1_name, new StringValueType());
        addValueToStartingState(field1_name, field1_value);


        inject();
        Assertions.assertEquals(0, _context.getEventLog().findEvents(new EventTagMatcher(MuseEvent.ERROR, MuseEvent.WARNING)).size());
        Assertions.assertEquals(field1_value, _context.getVariable(field1_name));
        }

    @Test
    void warnIfUnexpectedValueInState() throws MuseExecutionError
        {
        String unexpected_field = "field2";
        addValueToStartingState(unexpected_field, 222L);
        inject();
        Assertions.assertEquals(1, _context.getEventLog().findEvents(new EventTagMatcher(MuseEvent.ERROR, MuseEvent.WARNING)).size());
        Assertions.assertNull(_context.getVariable(unexpected_field));
        }

    @Test
    void errorIfTypesDoNotMatch() throws MuseExecutionError
        {
        String field1_name = "field1";
        String field1_value = "123";
        addTaskInput(field1_name, new StringValueType());
        addValueDefToStateDef(field1_name, new StringValueType());
        addValueToStartingState(field1_name, Long.parseLong(field1_value));
        inject();
        Assertions.assertEquals(1, _context.getEventLog().findEvents(new EventTagMatcher(MuseEvent.ERROR, MuseEvent.WARNING)).size());
        }

    void addTaskInput(String name, MuseValueType type)
        {
        TaskInput input = new TaskInput();
        input.setName(name);
        input.setRequired(true);
        input.setType(type);
        _task.getInputs().getList().add(input);
        }

    void addValueDefToStateDef(String name, MuseValueType type)
        {
        StateValueDefinition value = new StateValueDefinition(name, type, true);
        _state_def.getValues().add(value);
        }

    void addValueToStartingState(String name, Object value)
        {
        _start_state.getValues().put(name, value);
        }

    @BeforeEach
    void setup() throws IOException
        {
        SimpleProject project = new SimpleProject();

        // create State Definitions
        String state_type = "state_type1";
        _state_def = new StateDefinition();
        List<StateValueDefinition> states = new ArrayList<>();
        _state_def.setValues(states);
        _state_def.setId(state_type);
        project.getResourceStorage().addResource(_state_def);

        // create Task with required input states
        _task = new SteppedTask();
        TaskInputSet input_set = new TaskInputSet();
        _task.setInputs(input_set);
        _task.setInputStates(new TaskInputStates(state_type));

        // create TaskExecutionContext
        _context = new DefaultTaskExecutionContext(project, _task);

        // create starting state
        _start_state = new InterTaskState();
        _start_state.setStateDefinitionId(state_type);

        StateStorePlugin state_provider = new StateStorePlugin();
        state_provider.addState(_start_state);

        // initialize plugin into context
        _context.addPlugin(state_provider);
        _context.addPlugin(new InjectStatePlugin(new InjectStatePluginConfiguration()));
        }

    private void inject() throws MuseExecutionError
        {
        _context.initializePlugins();
        }

    private TaskExecutionContext _context;
    private SteppedTask _task;
    private InterTaskState _start_state;
    private StateDefinition _state_def;
    }