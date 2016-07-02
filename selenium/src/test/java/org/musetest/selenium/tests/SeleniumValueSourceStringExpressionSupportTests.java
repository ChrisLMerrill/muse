package org.musetest.selenium.tests;

import org.junit.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.values.*;
import org.musetest.selenium.conditions.*;
import org.musetest.selenium.locators.*;
import org.musetest.selenium.pages.*;
import org.musetest.selenium.values.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SeleniumValueSourceStringExpressionSupportTests
    {
    @Test
    public void elementExists() throws InstantiationException, IllegalAccessException
        {
        checkValueSourceFunction(ElementExistsCondition.TYPE_ID, new ElementExistsConditionStringExpressionSupport().getName(), ElementExistsConditionStringExpressionSupport.class, "abc");
        }

    @Test
    public void elementVisible() throws InstantiationException, IllegalAccessException
        {
        checkValueSourceFunction(ElementExistsCondition.TYPE_ID, new ElementExistsConditionStringExpressionSupport().getName(), ElementExistsConditionStringExpressionSupport.class, "abc");
        }

    @Test
    public void elementEnabled() throws InstantiationException, IllegalAccessException
        {
        checkValueSourceFunction(ElementEnabledCondition.TYPE_ID, new ElementEnabledConditionStringExpressionSupport().getName(), ElementEnabledConditionStringExpressionSupport.class, "abc");
        }

    private void checkValueSourceFunction(String muse_type_id, String function_name, Class<? extends BaseValueSourceStringExpressionSupport> support_class, String argument) throws IllegalAccessException, InstantiationException
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(muse_type_id);
        ValueSourceConfiguration sub_source = ValueSourceConfiguration.forValue(argument);
        config.setSource(sub_source);

        String stringified = PROJECT.getValueSourceStringExpressionSupporters().toString(config);
        Assert.assertEquals(String.format("%s(\"%s\")", function_name, argument), stringified);

        ArrayList<ValueSourceConfiguration> arguments = new ArrayList<>();
        arguments.add(sub_source);
        ValueSourceConfiguration parsed_config = support_class.newInstance().fromArgumentedExpression(function_name, arguments, PROJECT);

        Assert.assertEquals(config, parsed_config);
        }

    @Test
    public void pageTitleStringExpressionSupport()
        {
        ValueSourceConfiguration config = new PageTitleValueSourceStringExpressionSupport().fromElementExpression(PageTitleValueSourceStringExpressionSupport.NAME, new ArrayList<>(), null);
        Assert.assertEquals(PageTitleValueSource.TYPE_ID, config.getType());
        }

    @Test
    public void pageSourceStringExpressionSupport()
        {
        ValueSourceConfiguration config = new PageSourceValueSourceStringExpressionSupport().fromElementExpression(PageSourceValueSourceStringExpressionSupport.NAME, new ArrayList<>(), null);
        Assert.assertEquals(PageSourceValueSource.TYPE_ID, config.getType());
        }

    @Test
    public void currentUrlStringExpressionSupport()
        {
        ValueSourceConfiguration config = new CurrentUrlValueSourceStringExpressionSupport().fromElementExpression(CurrentUrlValueSourceStringExpressionSupport.NAME, new ArrayList<>(), null);
        Assert.assertEquals(CurrentUrlValueSource.TYPE_ID, config.getType());
        }

    @Test
    public void elementByIdStringExpressionSupport() throws IOException
        {
        checkCreationFromStringExpression(new IdElementValueSourceStringExpressionSupport(), IdElementValueSourceStringExpressionSupport.STRING_EXPRESSION_ID, IdElementValueSource.TYPE_ID);
        }

    @Test
    public void elementByXPathStringExpressionSupport() throws IOException
        {
        checkCreationFromStringExpression(new XPathElementValueSourceStringExpressionSupport(), XPathElementValueSourceStringExpressionSupport.STRING_EXPRESSION_ID, XPathElementValueSource.TYPE_ID);
        }

    @Test
    public void elementByNameStringExpressionSupport() throws IOException
        {
        checkCreationFromStringExpression(new NameElementValueSourceStringExpressionSupport(), NameElementValueSourceStringExpressionSupport.STRING_EXPRESSION_ID, NameElementValueSource.TYPE_ID);
        }

    @Test
    public void elementByCssStringExpressionSupport() throws IOException
        {
        checkCreationFromStringExpression(new CssElementValueSourceStringExpressionSupport(), CssElementValueSourceStringExpressionSupport.STRING_EXPRESSION_ID, CssElementValueSource.TYPE_ID);
        }

    @Test
    public void elementByLinkTextExpressionSupport() throws IOException
        {
        checkCreationFromStringExpression(new LinkTextElementValueSourceStringExpressionSupport(), LinkTextElementValueSourceStringExpressionSupport.STRING_EXPRESSION_ID, LinkTextElementValueSource.TYPE_ID);
        }

    @Test
    public void pageElementExpressionSupportWithOneArgument() throws IOException
        {
        PagesElementValueSourceStringExpressionSupport supporter = new PagesElementValueSourceStringExpressionSupport();
        List<ValueSourceConfiguration> configs = new ArrayList<>();
        String user_string = "page.element";
        configs.add(ValueSourceConfiguration.forValue(user_string));
        ValueSourceConfiguration config = supporter.fromElementExpression(PagesElementValueSourceStringExpressionSupport.STRING_TYPE_ID, configs, PROJECT);
        Assert.assertEquals("the page param was not set", "page", config.getSource(PagesElementValueSource.PAGE_PARAM_ID).getValue());
        Assert.assertEquals("the element param was not set", "element", config.getSource(PagesElementValueSource.ELEMENT_PARAM_ID).getValue());

        Assert.assertEquals("not stringified correctly", String.format("<%s:\"%s\">", PagesElementValueSourceStringExpressionSupport.STRING_TYPE_ID, user_string), supporter.toString(config, PROJECT));
        }

    @Test
    public void pageElementExpressionSupportWithTwoArguments() throws IOException
        {
        PagesElementValueSourceStringExpressionSupport supporter = new PagesElementValueSourceStringExpressionSupport();
        List<ValueSourceConfiguration> configs = new ArrayList<>();
        configs.add(ValueSourceConfiguration.forValue("page"));
        configs.add(ValueSourceConfiguration.forValue("element"));
        ValueSourceConfiguration config = supporter.fromElementExpression(PagesElementValueSourceStringExpressionSupport.STRING_TYPE_ID, configs, PROJECT);
        Assert.assertEquals("the page param was not set", "page", config.getSource(PagesElementValueSource.PAGE_PARAM_ID).getValue());
        Assert.assertEquals("the element param was not set", "element", config.getSource(PagesElementValueSource.ELEMENT_PARAM_ID).getValue());

        Assert.assertEquals("not stringified correctly", String.format("<%s:\"%s\">", PagesElementValueSourceStringExpressionSupport.STRING_TYPE_ID, "page.element"), supporter.toString(config, PROJECT));
        }

    @Test
    public void pageElementExpressionSupportWithTwoNonStringArguments() throws IOException
        {
        PagesElementValueSourceStringExpressionSupport supporter = new PagesElementValueSourceStringExpressionSupport();
        List<ValueSourceConfiguration> configs = new ArrayList<>();
        ValueSourceConfiguration page_source = ValueSourceConfiguration.forValue("page_var");
        configs.add(ValueSourceConfiguration.forTypeWithSource(VariableValueSource.TYPE_ID, page_source));
        ValueSourceConfiguration element_source = ValueSourceConfiguration.forValue("element_var");
        configs.add(ValueSourceConfiguration.forTypeWithSource(VariableValueSource.TYPE_ID, element_source));
        ValueSourceConfiguration config = supporter.fromElementExpression(PagesElementValueSourceStringExpressionSupport.STRING_TYPE_ID, configs, PROJECT);
        Assert.assertEquals("the page param was not set", page_source, config.getSource(PagesElementValueSource.PAGE_PARAM_ID).getSource());
        Assert.assertEquals("the element param was not set", element_source, config.getSource(PagesElementValueSource.ELEMENT_PARAM_ID).getSource());

        Assert.assertEquals("not stringified correctly", String.format("<%s:%s:%s>", PagesElementValueSourceStringExpressionSupport.STRING_TYPE_ID, "$\"page_var\"", "$\"element_var\""), supporter.toString(config, PROJECT));
        }

    private void checkCreationFromStringExpression(ValueSourceStringExpressionSupport parser, String expression_id, String muse_type_id)
        {
        ValueSourceConfiguration argument = ValueSourceConfiguration.forValue("qualifier");
        List<ValueSourceConfiguration> arguments = new ArrayList<>();
        arguments.add(argument);
        ValueSourceConfiguration config = parser.fromElementExpression(expression_id, arguments, null);
        Assert.assertEquals(muse_type_id, config.getType());
        Assert.assertEquals(argument, config.getSource());
        }

    private final static MuseProject PROJECT = new SimpleProject();
    }


