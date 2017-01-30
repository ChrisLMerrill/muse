package org.musetest.core.step.descriptor;

import javafx.scene.paint.*;
import org.musetest.core.step.*;
import org.musetest.core.values.descriptor.*;

/**
 * Provides metadata about steps. The intended uses are for the UI to provide context-relevant text and graphics
 * to the end user to help them understand what this step type or step instance will do.
 * <p>
 * A StepDescriptor could apply to either a step type (e.g. a class implementing MuseStep) or
 * to a specific instance of that step, or to a StepConfiguration (which describes how a MuseStep
 * will be created and configured at runtime). To find StepDescriptor for a step, see the methods
 * on StepDescriptors.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 * @see StepDescriptors
 */
public interface StepDescriptor
    {
    String getName();

    String getShortDescription(StepConfiguration step);

    String getShortDescription();

    String getLongDescription();

    String getIconDescriptor();

    Color getIconColor();

    boolean isCompound();

    String getInlineEditString();

    String getType();

    String getGroupName();

    String getDocumentationDescription();

    SubsourceDescriptor[] getSubsourceDescriptors();
    }


