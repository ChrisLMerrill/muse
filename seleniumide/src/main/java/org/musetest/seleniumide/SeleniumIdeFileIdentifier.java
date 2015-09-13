package org.musetest.seleniumide;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.slf4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SeleniumIdeFileIdentifier
    {
    public static Object identify(File file)
        {
        if (!file.exists())
            return "File " + file.getPath() + " does not exist.";

        if (!file.getPath().endsWith(".html"))
            return "This does not appear to be a SeleniumIDE file (*.html).";

        FileInputStream instream = null;
        try
            {
            instream = new FileInputStream(file);
            Document doc = Jsoup.parse(instream, "UTF-8", "http://ignored.com/");

            if (doc.getElementsByTag("head").get(0).attr("profile").contains("selenium-ide.openqa.org/profiles/test-case")
                && doc.getElementsByTag("link").get(0).attr("href") != null)
                return SeleniumIdeFileType.Test;

            if (doc.getElementsByTag("table").get(0).attr("id").equals("suiteTable"))
                return SeleniumIdeFileType.Suite;

            return "This does not appear to be a SeleniumIDE file (expected to find a profile attribute, link or table matching the SeleniumIDE standard.";
            }
        catch (IOException e)
            {
            String message = "Unable to read file " + file.getPath();
            LOG.error(message, e);
            return message;
            }
        finally
            {
            if (instream != null)
                try { instream.close(); } catch (IOException e) { /* noop */ }
            }
        }

    final static Logger LOG = LoggerFactory.getLogger(SeleniumIdeFileIdentifier.class);
    }


