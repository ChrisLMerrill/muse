package org.museautomation.core.tests.mocks.unknownresource;

import org.museautomation.core.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockStepDescriptor implements StepDescriptor
    {
    public final static String TYPE = "mock_type";
    public final static String NAME = "mock_name";
    public final static String SHORT_DESCRIPTION = "short_description {var1}";
    public final static String SOURCE_NAME = "var1";
    public final static String ICON = "icon descriptor";
    public final static String EDIT_STRING = "inline edit {string}";

    @Override
    public String getName()
        {
        return NAME;
        }

    public String getInlineEditString()
        {
        return EDIT_STRING;
        }

    public String getType()
        {
        return TYPE;
        }

    public String getIconDescriptor()
        {
        return ICON;
        }

    @Override
    public ColorDescriptor getIconColor()
        {
        return RgbColorDescriptor.GREEN;
        }

    public String getShortDescription()
        {
        return SHORT_DESCRIPTION;
        }

    public String getShortDescription(StepConfiguration config)
        {
        return "this does " + config.getSources().get(SOURCE_NAME).getValue() + " with something";
        }

    @Override
    public String getGroupName()
        {
        return null;
        }

    @Override
    public String getDocumentationDescription()
        {
        return null;
        }

    @Override
    public String getLongDescription()
        {
        return null;
        }

    @Override
    public SubsourceDescriptor[] getSubsourceDescriptors()
        {
        return new SubsourceDescriptor[0];
        }

    @SuppressWarnings("unused")
    public MockStepDescriptor(MuseProject project) { }
    public boolean isCompound() { return false; }
    }

