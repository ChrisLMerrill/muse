package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("capture-new-window")
@MuseStepName("Capture New Window")
@MuseInlineEditString("Capture New Window")
@MuseStepIcon("glyph:FontAwesome:\uf256")
@MuseStepTypeGroup("Selenium.Window")
@MuseStepShortDescription("Capture the handle of a window created during execution of child steps.")
@MuseSubsourceDescriptor(displayName = "Window Handle", description = "Name of the variable to store the window handle into (optional).", type = SubsourceDescriptor.Type.Named, name = SwitchToNewWindow.HANDLE_PARAM, optional = true, defaultValue = CaptureNewWindowStep.HANDLE_DEFAULT)
@SuppressWarnings("unused")  // instantiated via reflection
public class CaptureNewWindowStep extends BasicCompoundStep
    {
    public CaptureNewWindowStep(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _handle_source = getValueSource(config, HANDLE_PARAM, false, project);
        }

    @Override
    protected void beforeChildrenExecuted(StepExecutionContext context) throws MuseExecutionError
        {
        _start_handles = BrowserStep.getDriver(context).getWindowHandles();
        }

    @Override
    protected void afterChildrenExecuted(StepExecutionContext context) throws MuseExecutionError
        {
        _children_complete = true;

        String handle_var = null;
        if (_handle_source != null)
            handle_var = getValue(_handle_source, context, true, String.class);
        if (handle_var == null)
            handle_var = HANDLE_DEFAULT;
        Set<String> new_handles = BrowserStep.getDriver(context).getWindowHandles();
        new_handles.removeAll(_start_handles);
        if (new_handles.size() != 1)
            {
            context.raiseEvent(MessageEventType.create(String.format("After creating new window, found %d new handles.", new_handles.size())));
            throw new MuseExecutionError("Unable to capture window handle. See log.");
            }
        _new_handle = new_handles.iterator().next();
        context.setVariable(handle_var, _new_handle, ContextVariableScope.Execution);
        context.setVariable(handle_var, _new_handle);

        super.afterChildrenExecuted(context);
        }

    @Override
    protected boolean shouldEnter(StepExecutionContext context) throws MuseExecutionError
        {
        return super.shouldEnter(context);
        }

    @Override
    protected StepExecutionResult createResult(StepExecutionStatus status)
        {
        if (_start_handles != null && _children_complete && _new_handle == null)
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR);
        return super.createResult(status);
        }

    @Override
    public boolean equals(Object obj)
        {
        if (!super.equals(obj))
            return false;
        return obj instanceof CaptureNewWindowStep && Objects.equals(_handle_source, ((CaptureNewWindowStep)obj)._handle_source);
        }

    private MuseValueSource _handle_source;
    private Set<String> _start_handles;
    private boolean _children_complete = false;
    private String _new_handle;

    public final static String HANDLE_PARAM = "handle";
    public final static String HANDLE_DEFAULT = "_window_handle";
    public final static String TYPE_ID = CaptureNewWindowStep.class.getAnnotation(MuseTypeId.class).value();
    }