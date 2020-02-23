package org.museautomation.core.valuetypes;

import org.museautomation.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MuseValueTypes
    {
    public List<MuseValueType> getValueTypes()
        {
        List<MuseValueType> types = new ArrayList<>();
        try
            {
            Iterator<ValueTypeProvider> iterator = _loader.iterator();
            while (iterator.hasNext())
                types.addAll(iterator.next().getValueTypes());
            return types;
            }
        catch (Exception e)
            {
            LOG.debug("Loading of MuseValueTypes terminated prematurely due to: " + e, e);
            return types;
            }
        }

    private ServiceLoader<ValueTypeProvider> _loader = ServiceLoader.load(ValueTypeProvider.class);

    private final static Logger LOG = LoggerFactory.getLogger(MuseValueTypes.class);
    }