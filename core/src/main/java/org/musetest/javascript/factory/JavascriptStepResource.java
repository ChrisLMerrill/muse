package org.musetest.javascript.factory;

import jdk.nashorn.api.scripting.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.javascript.*;

import javax.script.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("removal")
public class JavascriptStepResource extends BaseMuseResource
    {
    JavascriptStepResource(ResourceOrigin origin, Invocable script_runner)
        {
        _origin = origin;
        setId(origin.suggestId());
        try
            {
            _get_descriptor_result = (ScriptObjectMirror) script_runner.invokeFunction("getStepDescriptor");
            }
        catch (ScriptException e)
            {
            _get_descriptor_error = e.getMessage();
            }
        catch (NoSuchMethodException e)
            {
            // do nothing
            }
        }

    @Override
    public ResourceType getType()
        {
        return new JavascriptStepResourceType();
        }

    public MuseStep createStep(StepConfiguration configuration)
        {
        return new JavascriptStep(configuration, _origin);
        }

    public StepDescriptor getStepDescriptor(MuseProject project)
        {
        if (_descriptor == null)
            {
            if (_get_descriptor_result != null)
                _descriptor = createStepDescriptor(_get_descriptor_result, project);
            else if (_get_descriptor_error == null)
                _descriptor = createFailedEvaluationDescriptor(project);
            else
                _descriptor = createGenericDescriptor(project);
            }
        return _descriptor;
        }

    private StepDescriptor createStepDescriptor(final ScriptObjectMirror result, MuseProject project)
        {
        return new DefaultStepDescriptor(JavascriptStep.class, project)
            {
            @Override
            public String getType()
                {
                return getId();
                }

            @Override
            public String getName()
                {
                if (result.containsKey("name"))
                    return result.getMember("name").toString();
                return super.getName();
                }

            @Override
            public String getGroupName()
                {
                if (result.containsKey("group"))
                    return result.getMember("group").toString();
                return super.getGroupName();
                }

            @Override
            public String getIconDescriptor()
                {
                if (result.containsKey("icon"))
                    return result.getMember("icon").toString();
                return super.getIconDescriptor();
                }

            @Override
            public String getShortDescription()
                {
                if (result.containsKey("shortDescription"))
                    return result.getMember("shortDescription").toString();
                return super.getShortDescription();
                }

            @Override
            public String getLongDescription()
                {
                if (result.containsKey("longDescription"))
                    return result.getMember("longDescription").toString();
                return super.getLongDescription();
                }
            };
        }

    private StepDescriptor createFailedEvaluationDescriptor(MuseProject project)
        {
        return new DefaultStepDescriptor(JavascriptStep.class, project)
            {
            @Override
            public String getName()
                {
                return "Malfunctioning Javascript step";
                }

            @Override
            public String getShortDescription(StepConfiguration step)
                {
                return "Javascript step that failed to evaluate.";
                }

            @Override
            public String getShortDescription()
                {
                return "A custom step developed in Javascript";
                }

            @Override
            public String getLongDescription()
                {
                return "Javascript step that failed to evaluate: " + _get_descriptor_error;
                }

            @Override
            public String getIconDescriptor()
                {
                return null;
                }

            @Override
            public boolean isCompound()
                {
                return false;
                }

            @Override
            public String getInlineEditString()
                {
                return null;
                }

            @Override
            public String getType()
                {
                return getId();
                }

            @Override
            public String getGroupName()
                {
                return "javascript";
                }
            };
        }

    private StepDescriptor createGenericDescriptor(MuseProject project)
        {
        return new DefaultStepDescriptor(JavascriptStep.class, project)
            {
            @Override
            public String getName()
                {
                return "Javascript step";
                }

            @Override
            public String getShortDescription(StepConfiguration step)
                {
                return "Javascript step, type=" + step.getType();
                }

            @Override
            public String getShortDescription()
                {
                return "A custom step developed in Javascript";
                }

            @Override
            public String getLongDescription()
                {
                return "see the source code at: " + _origin.getDescription();
                }

            @Override
            public String getIconDescriptor()
                {
                return null;
                }

            @Override
            public boolean isCompound()
                {
                return false;
                }

            @Override
            public String getInlineEditString()
                {
                return null;
                }

            @Override
            public String getType()
                {
                return getId();
                }

            @Override
            public String getGroupName()
                {
                return "javascript";
                }
            };
        }

    private ResourceOrigin _origin;
    private ScriptObjectMirror _get_descriptor_result;
    private String _get_descriptor_error;
    private StepDescriptor _descriptor;

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class JavascriptStepResourceType extends ResourceType
        {
        public JavascriptStepResourceType()
            {
            super("jsstep", "Javascript Step", JavascriptStepResourceType.class);
            }
        }
    }


