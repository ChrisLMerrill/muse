package org.museautomation.builtins.value.file;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FileValueSourceTests
    {
    @Test
    public void filnameFromUrl() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forTypeWithSource(FilenameFromUrlValueSource.TYPE_ID, "https://a.1stdibscdn.com/classical-marble-bust-of-hermes-holding-dionysus-after-the-antique-by-praxiteles-for-sale-picture-2/f_10590/1579695197244/Large_Marble_Sculpture_Male_Bust03_master.jpg?width=768");
        MuseValueSource source = config.createSource();
        Assertions.assertEquals("Large_Marble_Sculpture_Male_Bust03_master.jpg", source.resolveValue(new MockStepExecutionContext()));

        config = ValueSourceConfiguration.forTypeWithSource(FilenameFromUrlValueSource.TYPE_ID, "https://cdn0.rubylane.com/_pod/item/943369/001XBP28A/x93Faust-et-Margueritex94-Antique-French-Bronze-pic-1a-180-3b985df5-f.jpg");
        source = config.createSource();
        Assertions.assertEquals("x93Faust-et-Margueritex94-Antique-French-Bronze-pic-1a-180-3b985df5-f.jpg", source.resolveValue(new MockStepExecutionContext()));

        config = ValueSourceConfiguration.forTypeWithSource(FilenameFromUrlValueSource.TYPE_ID, "https://cdn0.rubylane.com/_pod/item/943369/001XBP28A");
        source = config.createSource();
        Assertions.assertEquals("001XBP28A", source.resolveValue(new MockStepExecutionContext()));
        }

    @Test
    public void tempFilePath() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forTypeWithSource(TemporaryFilenameValueSource.TYPE_ID, "filename.test");
        MuseValueSource source = config.createSource();
        Assertions.assertEquals(new File(new File(System.getProperty("java.io.tmpdir")), "filename.test").getAbsolutePath(), source.resolveValue(new MockStepExecutionContext()));
        }
    }