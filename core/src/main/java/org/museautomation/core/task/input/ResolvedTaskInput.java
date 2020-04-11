package org.museautomation.core.task.input;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ResolvedTaskInput
    {
    public ResolvedTaskInput(String name, Object value, ResolvedInputSource source)
        {
        _name = name;
        _value = value;
        _source = source;
        }

    public String getName()
        {
        return _name;
        }

    public Object getValue()
        {
        return _value;
        }

    public ResolvedInputSource getSource()
        {
        return _source;
        }

    private final String _name;
    private final Object _value;
    private final ResolvedInputSource _source;
    }