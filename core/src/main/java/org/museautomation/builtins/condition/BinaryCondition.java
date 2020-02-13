package org.museautomation.builtins.condition;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.steptest.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BinaryCondition extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public BinaryCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException, RequiredParameterMissingError
        {
        super(config, project);
        _left = getValueSource(config, LEFT_PARAM, true, project);
        _right = getValueSource(config, RIGHT_PARAM, true, project);
        }

    public void setLeft(MuseValueSource left)
        {
        _left = left;
        }

    public void setRight(MuseValueSource right)
        {
        _right = right;
        }

    public MuseValueSource getLeft()
        {
        return _left;
        }

    public MuseValueSource getRight()
        {
        return _right;
        }

    @Override
    public boolean equals(Object obj)
        {
        return getClass().equals(obj.getClass()) && Objects.equals(_left, ((EqualityCondition)obj)._left) && Objects.equals(_right, ((EqualityCondition)obj)._right);
        }

    public static ValueSourceConfiguration forSources(String type, ValueSourceConfiguration left, ValueSourceConfiguration right)
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(type);
        config.addSource(LEFT_PARAM, left);
        config.addSource(RIGHT_PARAM, right);
        return config;
        }

    public final static String LEFT_PARAM = "left";
    public final static String RIGHT_PARAM = "right";

    protected MuseValueSource _left;
    protected MuseValueSource _right;
    }


