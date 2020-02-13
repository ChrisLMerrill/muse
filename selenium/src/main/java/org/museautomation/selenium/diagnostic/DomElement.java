package org.museautomation.selenium.diagnostic;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DomElement
    {
    public String getText()
        {
        return _text;
        }

    public void setText(String text)
        {
        _text = text;
        }

    public String getTag()
        {
        return _tag;
        }

    public void setTag(String tag)
        {
        _tag = tag;
        }

    public Map<String, String> getAttributes()
        {
        return _attributes;
        }

    public void setAttributes(Map<String, String> attributes)
        {
        _attributes = attributes;
        }

    public List<DomElement> getChildren()
        {
        return _children;
        }

    public void setChildren(List<DomElement> children)
        {
        _children = children;
        }

    @JsonIgnore
    public String getChildText()
        {
        if (_text != null)
            return _text;
        else
            {
            StringBuilder builder = new StringBuilder();
            for (DomElement child : _children)
                {
                String child_text = child.getChildText();
                if (child_text != null)
                    builder.append(child_text);
                }
            return builder.toString();
            }
        }

    private Map<String, String> _attributes = new HashMap<>();
    private String _text;
    private String _tag;
    private List<DomElement> _children = new ArrayList<>();
    }