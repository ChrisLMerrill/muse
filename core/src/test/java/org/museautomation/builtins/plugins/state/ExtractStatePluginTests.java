package org.museautomation.builtins.plugins.state;

import org.junit.jupiter.api.*;
import org.museautomation.*;
import org.museautomation.builtins.valuetypes.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
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
class ExtractStatePluginTests
    {
    @Test
    void extractStateCorrectly() throws MuseExecutionError
        {
        String field1_name = "field1";
        String field1_value = "123";
        addValueToContext(field1_name, field1_value);
        addValueDefToStateDef(field1_name, new StringValueType());

        extract();
        Assertions.assertEquals(0, _context.getEventLog().findEvents(new EventTagMatcher(MuseEvent.ERROR, MuseEvent.WARNING)).size());
        Assertions.assertEquals(field1_value, _extracted_state.getValues().get(field1_name));
        }

    @Test
    void errorIfMissingValue() throws MuseExecutionError
        {
        String field1_name = "field1";
        addValueDefToStateDef(field1_name, new StringValueType());

        extract();
        Assertions.assertEquals(1, _context.getEventLog().findEvents(new EventTagMatcher(MuseEvent.ERROR)).size());
        Assertions.assertNull(_extracted_state.getValues().get(field1_name));
        }

    @Test
    void errorIfTypesDoNotMatch() throws MuseExecutionError
        {
        String field1_name = "field1";
        Object field1_value = 555L;
        addValueToContext(field1_name, field1_value);
        addValueDefToStateDef(field1_name, new StringValueType());
        extract();
        Assertions.assertEquals(1, _context.getEventLog().findEvents(new EventTagMatcher(MuseEvent.ERROR)).size());
        Assertions.assertNull(_extracted_state.getValues().get(field1_name));
        }

    void addValueDefToStateDef(String name, MuseValueType type)
        {
        StateValueDefinition value = new StateValueDefinition(name, type, true);
        _state_def.getValues().add(value);
        }

    void addValueToContext(String name, Object value)
        {
        _context.setVariable(name, value);
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
        SteppedTask task = new SteppedTask();
        TaskOutputSet output_set = new TaskOutputSet();
        task.setOutputSet(output_set);
        task.setOutputStates(new TaskOutputStates(state_type));

        // create TaskExecutionContext
        _context = new DefaultTaskExecutionContext(project, task);

        _state_store = new StateContainerPlugin(new BasicStateContainer());

        // initialize plugin into context
        _context.addPlugin(_state_store);
        ExtractStatePlugin plugin = new ExtractStatePlugin(new ExtractStatePluginConfiguration());
        plugin.addState(_state_def);
        _context.addPlugin(plugin);
        }

    private void extract() throws MuseExecutionError
        {
        _context.initializePlugins();
        _context.raiseEvent(EndTaskEventType.create());
        _extracted_state = _state_store.getContainer().findStates("state_type1").get(0);
        }

    private TaskExecutionContext _context;
    private StateDefinition _state_def;
    private InterTaskState _extracted_state;
    private StateContainerPlugin _state_store;
    }