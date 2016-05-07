package org.musetest.core.util;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class Copy
    {
    public static <T extends Serializable> T thisObject(T original)
        {
        try
            {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream outstream = new ObjectOutputStream(bytes);
            outstream.writeObject(original);

            ObjectInputStream instream = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
            return (T) instream.readObject();
            }
        catch (Exception e)
            {
            throw new RuntimeException("Unable to copy the object - does it reference unserializable classes?", e);
            }
        }
    }


