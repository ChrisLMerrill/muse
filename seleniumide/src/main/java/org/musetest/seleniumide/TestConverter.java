package org.musetest.seleniumide;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.musetest.builtins.value.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.steps.*;

import java.io.*;

/**
 * Converts a SeleniumIDE test(html) into a SteppedTest
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestConverter
    {
    public TestConverter(InputStream instream)
        {
        _instream = instream;
        }

    public SteppedTest convert() throws IOException, UnsupportedError
        {
        Document doc = Jsoup.parse(_instream, "UTF-8", "http://ignored.com/");
        _test = new SteppedTest(new StepConfiguration(BasicCompoundStep.TYPE_ID));

        _base_url = doc.getElementsByTag("link").get(0).attr("href");
        if (!_base_url.endsWith("/"))
            _base_url += "/";

        generateOpenBrowserStep();
        generateSteps(doc);
        generateCloseBrowserStep();

        return _test;
        }

    private void generateOpenBrowserStep()
        {
        StepConfiguration step = new StepConfiguration(OpenBrowser.TYPE_ID);
        step.addSource(OpenBrowser.PROVIDER_PARAM, ValueSourceConfiguration.forTypeWithSource(VariableValueSource.TYPE_ID, DEFAULT_PROVIDER_NAME));
        step.addSource(OpenBrowser.BROWSER_PARAM, ValueSourceConfiguration.forTypeWithSource(VariableValueSource.TYPE_ID, DEFAULT_BROWSER_NAME));
        _test.getStep().addChild(step);

        _test.setDefaultVariable(DEFAULT_PROVIDER_NAME, ValueSourceConfiguration.forValue(DEFAULT_PROVIDER_VALUE));
        _test.setDefaultVariable(DEFAULT_BROWSER_NAME, ValueSourceConfiguration.forValue(DEFAULT_BROWSER_VALUE));
        }

    private void generateSteps(Document doc) throws UnsupportedError
        {
        Elements rows = doc.getElementsByTag("tr");
        for (int i = 1; i < rows.size(); i++)
            {
            Element row = rows.get(i);
            String command = row.children().get(0).text();
            String param1 = row.children().get(1).text();
            String param2 = row.children().get(2).text();
            generateStepForCommand(command, param1, param2);
            }
        }

    private void generateStepForCommand(String command, String param1, String param2) throws UnsupportedError
        {
        StepConverter converter = _converters.getConverter(command);
        if (converter == null)
            throw new UnsupportedError(String.format("SeleniumIDE command %s is not yet supported.", command));

        _test.getStep().addChild(converter.convertStep(this, command, param1, param2));
        }

    private void generateCloseBrowserStep()
        {
        StepConfiguration step = new StepConfiguration(CloseBrowser.TYPE_ID);
        _test.getStep().addChild(step);
        }

    public String getBaseUrl()
        {
        return _base_url;
        }

    private final InputStream _instream;
    private String _base_url;
    private SteppedTest _test;
    private StepConverters _converters = StepConverters.get();

    private final static String DEFAULT_PROVIDER_NAME = "provider";
    private final static String DEFAULT_PROVIDER_VALUE = "local-provider";
    private final static String DEFAULT_BROWSER_NAME = "browser";
    private final static String DEFAULT_BROWSER_VALUE = "firefox";
    }


