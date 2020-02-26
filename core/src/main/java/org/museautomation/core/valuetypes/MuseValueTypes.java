package org.museautomation.core.valuetypes;

import org.museautomation.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MuseValueTypes
    {
    public MuseValueTypes()
        {
        _types = new ArrayList<>();
        try
            {
            Iterator<ValueTypeProvider> iterator = _loader.iterator();
            while (iterator.hasNext())
                _types.addAll(iterator.next().getValueTypes());
            }
        catch (Exception e)
            {
            LOG.debug("Loading of MuseValueTypes terminated prematurely due to: " + e, e);
            }
        }

    public List<MuseValueType> getValueTypes()
        {
        return _types;
        }

    public MuseValueType forTypeId(String id)
        {
        for (MuseValueType type : _types)
            if (id.equals(type.getId()))
                return type;
        return null;
        }

    public static MuseValueTypes get()  // TODO this will not work for types loaded in a custom project
        {
        if (INSTANCE == null)
            INSTANCE = new MuseValueTypes();
        return INSTANCE;
        }

    private List<MuseValueType> _types;

    private static ServiceLoader<ValueTypeProvider> _loader = ServiceLoader.load(ValueTypeProvider.class);
    private static MuseValueTypes INSTANCE = null;

    private final static Logger LOG = LoggerFactory.getLogger(MuseValueTypes.class);
    }