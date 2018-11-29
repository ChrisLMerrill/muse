package org.musetest.core.tests.utils;

import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.musetest.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class OperatingSystemTests
    {
    @Test
    public void recognizeWindows10()
        {
        System.setProperty("os.name", "Windows 10");
        OperatingSystem.detect();
        Assertions.assertEquals(OperatingSystem.Windows, OperatingSystem.get());
        }

    @Test
    public void recognizeWindows8()
        {
        System.setProperty("os.name", "Windows 8");
        OperatingSystem.detect();
        Assertions.assertEquals(OperatingSystem.Windows, OperatingSystem.get());
        }

    @Test
    public void recognizeWindows7()
        {
        System.setProperty("os.name", "Windows 7");
        OperatingSystem.detect();
        Assertions.assertEquals(OperatingSystem.Windows, OperatingSystem.get());
        }

    @Test
    public void recognizeMacOS()
        {
        System.setProperty("os.name", "Mac OS X");
        OperatingSystem.detect();
        Assertions.assertEquals(OperatingSystem.macOS, OperatingSystem.get());
        }

    @Test
    public void recognizeLinux()
        {
        System.setProperty("os.name", "Linux");
        OperatingSystem.detect();
        Assertions.assertEquals(OperatingSystem.Linux, OperatingSystem.get());
        }

    @BeforeEach
    public void saveOsName()
        {
        _original_os_name = System.getProperty("os.name");
        }
    private String _original_os_name;
    @AfterEach
    public void restoreOsName()
        {
        System.setProperty("os.name", _original_os_name);
        }
    }


