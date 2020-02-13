package org.museautomation.settings;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class Settings
    {
    static File getPreferenceFileLocation(String filename)
        {
        File base_folder = new File(System.getProperty("user.home"), SETTINGS_FOLDER);
        if (!base_folder.exists())
            {
            if (!base_folder.mkdir())
                {
                base_folder = new File(System.getProperty("user.dir"), SETTINGS_FOLDER);
                if (!base_folder.exists())
                    if (!base_folder.mkdir())
                        base_folder = new File(System.getProperty("user.dir"));
                }
            }
        return new File(base_folder, filename);
        }

    private final static String SETTINGS_FOLDER = ".muse";
    }


