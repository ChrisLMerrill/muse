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
public class JavascriptStepResource implements MuseResource
    {
    public JavascriptStepResource(ResourceOrigin origin, String script, Invocable script_runner)
        {
        _origin = origin;
        _metadata.setId(origin.suggestId());
        _script = script;
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
    public ResourceMetadata getMetadata()
        {
        return _metadata;
        }

    public String getScript()
        {
        return _script;
        }

    public MuseStep createStep(StepConfiguration configuration)
        {
        return new JavascriptStep(configuration, _origin, _script);
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
                return _metadata.getId();
                }

            @Override
            public String getName()
                {
                return result.getMember("name").toString();
                }

            @Override
            public String getGroupName()
                {
                return result.getMember("group").toString();
                }

            @Override
            public String getIconDescriptor()
                {
                return result.getMember("icon").toString();
                }

            @Override
            public String getShortDescription()
                {
                return result.getMember("shortDescription").toString();
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
                return getMetadata().getId();
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
                return "see the source code at: " + getMetadata().getOrigin().getDescription();
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
                return getMetadata().getId();
                }

            @Override
            public String getGroupName()
                {
                return "javascript";
                }
            };
        }

    private String _script;
    private ResourceOrigin _origin;
    private ResourceMetadata _metadata = new ResourceMetadata(ResourceTypes.jsStep);
    private ScriptObjectMirror _get_descriptor_result;
    private String _get_descriptor_error;
    private StepDescriptor _descriptor;
    }


