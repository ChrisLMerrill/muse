package org.museautomation.builtins.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("store-map-to-vars")
@MuseStepName("Store Map to Vars")
@MuseInlineEditString("Store contents of {map} as variables")
@MuseStepIcon("glyph:Icons525:SITEMAP")
@MuseStepTypeGroup("Variables")
@MuseStepShortDescription("Store map values to variables")
@MuseStepLongDescription("The 'map' source is resolved to a Map and then each name/value pair in the map is stored as a variable. This stores each item in the map as a separate variable.")
@MuseSubsourceDescriptor(displayName = "Map", description = "The map to store to variables", type = SubsourceDescriptor.Type.Named, name = StoreMapToVariablesStep.MAP_PARAM)
public class StoreMapToVariablesStep extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public StoreMapToVariablesStep(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _map = getValueSource(config, MAP_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws ValueSourceResolutionError
        {
        Map<Object,Object> map = getValue(_map, context, false, Map.class);
        for (Object key : map.keySet())
            context.setVariable(key.toString(), map.get(key));
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _map;

    public final static String MAP_PARAM = "map";
    public final static String TYPE_ID = StoreMapToVariablesStep.class.getAnnotation(MuseTypeId.class).value();
    }


