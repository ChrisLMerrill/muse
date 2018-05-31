package org.musetest.core.values.descriptor;

import org.musetest.core.values.*;

/**
 * Provides metadata about a sub-source of a MuseValueSource or a MuseStep
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface SubsourceDescriptor
    {
    String getDisplayName();
    String getDescription();
    String getName();
    int getIndex();
    boolean isOptional();
    Type getType();
    Class getResolutionType();
    String getOneLineSummary();
    ValueSourceConfiguration getDefault();

    enum Type
        {
        Value,
        Single,
        Named,
        List,
        Map,
//        Indexed,   No GUI support implemented for this yet...is there a usecase for it?
        }

    static SubsourceDescriptor[] getSubsourceDescriptors(Class target)
        {
        MuseSubsourceDescriptor[] annotations = (MuseSubsourceDescriptor[]) target.getAnnotationsByType(MuseSubsourceDescriptor.class);
        if (annotations == null || annotations.length == 0)
            return null;

        SubsourceDescriptor[] descriptors = new SubsourceDescriptor[annotations.length];
        for (int i = 0; i < annotations.length; i++)
            {
            MuseSubsourceDescriptor annotation = annotations[i];
            descriptors[i] = new SimpleSubsourceDescriptor(annotation);
            }
        return descriptors;
        }

    }


