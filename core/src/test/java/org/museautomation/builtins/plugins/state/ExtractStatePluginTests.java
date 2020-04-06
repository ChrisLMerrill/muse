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
        addValueToOutput(field1_name, field1_value);
        addValueDefToStateDef(field1_name, new StringValueType());

        extract();
        Assertions.assertEquals(0, _context.getEventLog().findEvents(new EventTagMatcher(MuseEvent.ERROR, MuseEvent.WARNING)).size());
        Assertions.assertEquals(field1_value, _extracted_state.getValues().get(field1_name));
        }

    @Test
    void canExtractIncomplete() throws MuseExecutionError
        {
        String field1_name = "field1";
        addValueDefToStateDef(field1_name, new StringValueType());

        extract();
        Assertions.assertEquals(1, _context.getEventLog().findEvents(new EventTagMatcher(MuseEvent.WARNING)).size());
        Assertions.assertFalse(_state_def.isValid(_extracted_state));
        Assertions.assertNull(_extracted_state.getValues().get(field1_name));
        }

    @Test
    void errorIfTypesDoNotMatch() throws MuseExecutionError
        {
        String field1_name = "field1";
        Object field1_value = 555L;
        addValueToOutput(field1_name, field1_value);
        addValueDefToStateDef(field1_name, new StringValueType());
        extract();
        Assertions.assertEquals(1, _context.getEventLog().findEvents(new EventTagMatcher(MuseEvent.WARNING)).size());
        Assertions.assertFalse(_state_def.isValid(_extracted_state));
        Assertions.assertNull(_extracted_state.getValues().get(field1_name));
        }

    void addValueDefToStateDef(String name, MuseValueType type)
        {
        StateValueDefinition value = new StateValueDefinition(name, type, true);
        _state_def.getValues().add(value);
        }

    void addValueToOutput(String name, Object value)
        {
        _context.outputs().storeOutput(name, value);
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

        // initialize plugin into context
        _plugin = new ExtractStatePlugin(new ExtractStatePluginConfiguration());
        _plugin.addStateToExtract(_state_def);
        _context.addPlugin(_plugin);
        }

    private void extract() throws MuseExecutionError
        {
        _context.initializePlugins();
        _context.raiseEvent(EndTaskEventType.create());
        _extracted_state = _plugin.getExtractedStates().get(0);
        }

    private TaskExecutionContext _context;
    private StateDefinition _state_def;
    private InterTaskState _extracted_state;
    private ExtractStatePlugin _plugin;
    }