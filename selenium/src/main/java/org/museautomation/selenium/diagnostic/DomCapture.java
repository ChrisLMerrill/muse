package org.museautomation.selenium.diagnostic;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import org.museautomation.core.datacollection.*;

import javax.annotation.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DomCapture implements TestResultData
    {
    public DomCapture(DomElement root_element)
        {
        _root_element = root_element;
        }

    @Override
    public String getName()
        {
        return _name;
        }

    @Override
    public void setName(@Nonnull String name)
        {
        _name = name;
        }

    @Override
    public String suggestFilename()
        {
        return _name;
        }

    @Override
    public void write(@Nonnull OutputStream outstream) throws IOException
        {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.writerWithDefaultPrettyPrinter().writeValue(outstream, _root_element);
        }

    @Override
    public Object read(@Nonnull InputStream instream) throws IOException
        {
        _root_element = new ObjectMapper().readValue(instream, DomElement.class);
        return _root_element;
        }

    private String _name = "dom-capture.json";
    private DomElement _root_element;
    }