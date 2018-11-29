package org.musetest.core.tests.utils;

import org.junit.jupiter.api.*;
import org.musetest.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class OperatingSystemTests
    {
    @Test
    void recognizeWindows10()
        {
        System.setProperty("os.name", "Windows 10");
        OperatingSystem.detect();
        Assertions.assertEquals(OperatingSystem.Windows, OperatingSystem.get());
        }

    @Test
    void recognizeWindows8()
        {
        System.setProperty("os.name", "Windows 8");
        OperatingSystem.detect();
        Assertions.assertEquals(OperatingSystem.Windows, OperatingSystem.get());
        }

    @Test
    void recognizeWindows7()
        {
        System.setProperty("os.name", "Windows 7");
        OperatingSystem.detect();
        Assertions.assertEquals(OperatingSystem.Windows, OperatingSystem.get());
        }

    @Test
    void recognizeMacOS()
        {
        System.setProperty("os.name", "Mac OS X");
        OperatingSystem.detect();
        Assertions.assertEquals(OperatingSystem.macOS, OperatingSystem.get());
        }

    @Test
    void recognizeLinux()
        {
        System.setProperty("os.name", "Linux");
        OperatingSystem.detect();
        Assertions.assertEquals(OperatingSystem.Linux, OperatingSystem.get());
        }

    @BeforeEach
    void saveOsName()
        {
        _original_os_name = System.getProperty("os.name");
        }
    private String _original_os_name;
    @AfterEach
    void restoreOsName()
        {
        System.setProperty("os.name", _original_os_name);
        }
    }


