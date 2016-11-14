package org.musetest.core.resource;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ResourceOrigin
    {
    String getDescription();
    String suggestId();
    InputStream asInputStream() throws IOException;
    OutputStream asOutputStream() throws IOException;

    ResourceSerializer getSerializer();
    void setSerializer(ResourceSerializer serializer);
    }


