package org.musetest.builtins.tests;

import org.musetest.core.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestValueSourceDescriptor extends DefaultValueSourceDescriptor
    {
    public TestValueSourceDescriptor(MuseProject project)
        {
        super(TestValueSource.class.getAnnotation(MuseTypeId.class).value(), TestValueSource.class, project);
        }

    @Override
    public SubsourceDescriptor[] getSubsourceDescriptors()
        {
        SubsourceDescriptor[] descriptors = new SubsourceDescriptor[1];
        descriptors[0] = new SubsourceDescriptor()
            {
            @Override
            public String getDisplayName()
                {
                return null;
                }

            @Override
            public String getDescription()
                {
                return null;
                }

            @Override
            public String getName()
                {
                return null;
                }

            @Override
            public int getIndex()
                {
                return 0;
                }

            @Override
            public boolean isOptional()
                {
                return false;
                }

            @Override
            public Type getType()
                {
                return null;
                }

            @Override
            public Class getResolutionType()
                {
                return null;
                }

            @Override
            public String getOneLineSummary()
                {
                return null;
                }
            };
        return null;  // TODO
        }


    }


