package org.musetest.core.tests.mocks.unknownresource;

import javafx.scene.paint.*;
import org.musetest.core.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestStepDescriptor implements StepDescriptor
    {
    public final static String TYPE = "test_type";
    public final static String NAME = "test_name";
    public final static String SHORT_DESCRIPTION = "short_description {var1}";
    public final static String SOURCE_NAME = "var1";
    public final static String ICON = "icon descriptor";
    public final static String TEST_STEP = "inline edit {string}";

    @Override
    public String getName()
        {
        return NAME;
        }

    public String getInlineEditString()
        {
        return TEST_STEP;
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
    public Color getIconColor()
        {
        return Color.GREEN;
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
    public TestStepDescriptor(MuseProject project) { }
    public boolean isCompound() { return false; }
    }

