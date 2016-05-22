package org.musetest.core.values.descriptor;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleSubsourceDescriptor implements SubsourceDescriptor
    {
    public SimpleSubsourceDescriptor(String display_name, String name, String description, int index, boolean optional, Type type, Class resolution_type)
        {
        _display_name = display_name;
        _name = name;
        _description = description;
        _index = index;
        _optional = optional;
        _type = type;
        _resolution_type = resolution_type;
        }

    public SimpleSubsourceDescriptor(MuseSubsourceDescriptor annotation)
        {
        _display_name = annotation.displayName();
        _name = annotation.name();
        _description = annotation.description();
        _index = annotation.index();
        _optional = annotation.optional();
        _type = annotation.type();
        _resolution_type = annotation.resolutionType();
        }

    @Override
    public String getDisplayName()
        {
        return _display_name;
        }

    @Override
    public String getDescription()
        {
        return _description;
        }

    @Override
    public String getName()
        {
        return _name;
        }

    @Override
    public int getIndex()
        {
        return _index;
        }

    @Override
    public boolean isOptional()
        {
        return _optional;
        }

    @Override
    public Type getType()
        {
        return _type;
        }

    @Override
    public Class getResolutionType()
        {
        return _resolution_type;
        }

    private String _display_name;
    private String _name;
    private String _description;
    private int _index;
    private boolean _optional;
    private Type _type;
    private Class _resolution_type;
    }


