package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;
import org.openqa.selenium.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("new-window")
@MuseStepName("Open New Window")
@MuseInlineEditString("Open new window")
@MuseStepIcon("glyph:FontAwesome:PLUS_SQUARE")
@MuseStepTypeGroup("Selenium.Window")
@MuseStepShortDescription("Opens a new window/tab")
@MuseStepLongDescription("Opens a new window/tab using keyboard entry")
@MuseSubsourceDescriptor(displayName = "Window Handle", description = "Name of the variable to store the window handle into (optional).", type = SubsourceDescriptor.Type.Named, name = SwitchToNewWindow.HANDLE_PARAM, optional = true, defaultValue = SwitchToNewWindow.HANDLE_DEFAULT)
@MuseSubsourceDescriptor(displayName = "Script", description = "The script to run", type = SubsourceDescriptor.Type.Named, name = SwitchToNewWindow.SCRIPT_PARAM, optional = true, defaultValue = SwitchToNewWindow.SCRIPT_DEFAULT)
public class SwitchToNewWindow extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public SwitchToNewWindow(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _handle_source = getValueSource(config, HANDLE_PARAM, false, project);
        _script_source = getValueSource(config, SCRIPT_PARAM, false, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        WebDriver driver = getDriver(context);
        Set<String> original_handles = driver.getWindowHandles();
        String script = getValue(_script_source, context, String.class, SCRIPT_DEFAULT);
        ((JavascriptExecutor) driver).executeScript(script);
        Set<String> new_handles = driver.getWindowHandles();
        new_handles.removeAll(original_handles);
        if (new_handles.size() != 1)
            {
            context.raiseEvent(MessageEventType.create(String.format("After creating new window, found %d new handles.", new_handles.size())));
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR);
            }
        String new_handle = new_handles.iterator().next();
        
        String handle_var = getValue(_handle_source, context, true, String.class);
        if (handle_var != null)
            {
            context.setVariable(handle_var, new_handle, ContextVariableScope.Execution);
            context.raiseEvent(SetVariableEventType.create(handle_var, new_handle, ContextVariableScope.Execution));
            }
        driver.switchTo().window(new_handle);
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _handle_source;
    private MuseValueSource _script_source;

    final static String HANDLE_PARAM = "handle";
    final static String HANDLE_DEFAULT = "_window_handle";
    final static String SCRIPT_PARAM = "script";
    final static String SCRIPT_DEFAULT = "window.open();";

    public final static String TYPE_ID = SwitchToNewWindow.class.getAnnotation(MuseTypeId.class).value();
    }