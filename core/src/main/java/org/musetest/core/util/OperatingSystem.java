package org.musetest.core.util;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public enum OperatingSystem
    {
    Windows,
    macOS,
    Linux,
    Unknown;

    /**
     * Return the current operating system.
     */
    public static OperatingSystem get()
        {
        if (OS == null)
            detect();
        return OS;
        }

    /**
     * Primarily for unit testing. Use OperatingSystem.get() in non-test code.
     */
    public static void detect()
        {
        String os_name = System.getProperty("os.name").toLowerCase();
        if (os_name.contains("win"))
            OS = Windows;
        else if (os_name.contains("mac"))
            OS = macOS;
        else if (os_name.contains("linux") || os_name.contains("nux") || os_name.contains("aix"))
            OS = Linux;
        else
            OS = Unknown;
        }

    public static OperatingSystem OS;
    }


