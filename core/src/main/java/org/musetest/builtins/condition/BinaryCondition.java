package org.musetest.builtins.condition;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BinaryCondition extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public BinaryCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        ValueSourceConfiguration left = config.getSourceMap().get(LEFT_PARAM);
        ValueSourceConfiguration right = config.getSourceMap().get(RIGHT_PARAM);
        if (left == null || right == null)
            throw new MuseInstantiationException("Missing required parameter (sourceMap.left or sourceMap.right)");

        _left = left.createSource(project);
        _right = right.createSource(project);
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


