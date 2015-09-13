package org.musetest.seleniumide.commandline;

import com.fasterxml.jackson.databind.*;
import io.airlift.airline.*;
import org.musetest.core.*;
import org.musetest.core.commandline.*;
import org.musetest.core.resource.json.*;
import org.musetest.core.steptest.*;
import org.musetest.core.util.*;
import org.musetest.seleniumide.*;

import java.io.*;

/**
 * Extension to the Muse command-line launcher to convert a SeleniumIDE test into the Muse format.
 *
 * This class is registered as a service in /META-INF/services/org.musetest.core.commandline.MuseCommand.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Command(name = "convert", description = "Convert a SeleniumIDE file into a Muse project resource")
public class ImportSeleniumIdeTestCommand extends MuseCommand
    {
    @Arguments(description = "Name of SeleniumIDE file to convert", required = true)
    public String filename;

    @Override
    public void run()
        {
        File file = new File(filename);
        Object result = SeleniumIdeFileIdentifier.identify(file);
        if (result instanceof String)
            System.out.println(result.toString());
        else if (result instanceof SeleniumIdeFileType)
            {
            SeleniumIdeFileType type = (SeleniumIdeFileType) result;
            if (type == SeleniumIdeFileType.Test)
                convert(file);
            else
                System.out.println("coversion of type " + type.name() + " is not yet supported");
            }
        else
            System.out.println("Unexpected return from SeleniumIdeFileIdentifier: " + result);
        }

    private void convert(File file)
        {
        FileInputStream instream = null;
        FileOutputStream outstream = null;

        try
            {
            instream = new FileInputStream(file);
            TestConverter converter = new TestConverter(instream);
            SteppedTest test = converter.convert();

            String new_filename = file.getAbsolutePath();
            new_filename = new_filename.substring(0, new_filename.lastIndexOf(".")) + ".json";
            File outfile = new File(new_filename);
            if (outfile.exists())
                {
                System.out.println("Destination file already exists: " + outfile.getPath());
                return;
                }

            ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
            outstream = new FileOutputStream(outfile);
            mapper.writeValue(outstream, test);

            System.out.println("Conversion complete. Written to: " + outfile.getPath());
            }
        catch (Exception e)
            {
            System.out.println("Unable to convert the file: " + e);
            }
        finally
            {
            if (instream != null)
                try { instream.close(); } catch (IOException e) { /* noop */ }
            if (outstream != null)
                try { outstream.close(); } catch (IOException e) { /* noop */ }
            }
        }
    }
