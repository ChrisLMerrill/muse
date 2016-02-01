package org.musetest.core.resource;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ResourceOrigin
    {
    String getDescription();
    String suggestId();
    InputStream asStream() throws IOException;
    }


