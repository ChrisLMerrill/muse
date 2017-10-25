package org.musetest.core.util;

import com.fasterxml.jackson.databind.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class Copy
    {
    public static <T extends Serializable> T withJavaSerialization(T original)
        {
        try
            {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream outstream = new ObjectOutputStream(bytes);
            outstream.writeObject(original);
            outstream.close();
            ObjectInputStream instream = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
            return (T) instream.readObject();
            }
        catch (Exception e)
            {
            throw new RuntimeException("Unable to copy the object - does it reference unserializable classes?", e);
            }
        }

    public static <T> T withJsonSerialization(T original)
        {
        try
            {
            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(outstream, original);

            ByteArrayInputStream instream = new ByteArrayInputStream(outstream.toByteArray());
            return (T) mapper.readValue(instream, original.getClass());
            }
        catch (Exception e)
            {
            throw new RuntimeException("Unable to copy the object - does it reference unserializable classes?", e);
            }
        }
    }


