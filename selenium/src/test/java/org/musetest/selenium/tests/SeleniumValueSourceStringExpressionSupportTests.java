package org.musetest.selenium.tests;

import org.junit.*;
import org.musetest.core.project.*;
import org.musetest.core.values.*;
import org.musetest.selenium.conditions.*;
import org.musetest.selenium.locators.*;
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

        SimpleProject project = new SimpleProject();
        String stringified = project.getValueSourceStringExpressionSupporters().toString(config);
        Assert.assertEquals(String.format("%s(\"%s\")", function_name, argument), stringified);

        ArrayList<ValueSourceConfiguration> arguments = new ArrayList<>();
        arguments.add(sub_source);
        ValueSourceConfiguration parsed_config = support_class.newInstance().fromArgumentedExpression(function_name, arguments, project);

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

    private void checkCreationFromStringExpression(ValueSourceStringExpressionSupport parser, String expression_id, String muse_type_id)
        {
        ValueSourceConfiguration argument = ValueSourceConfiguration.forValue("qualifier");
        List<ValueSourceConfiguration> arguments = new ArrayList<>();
        arguments.add(argument);
        ValueSourceConfiguration config = parser.fromElementExpression(expression_id, arguments, null);
        Assert.assertEquals(muse_type_id, config.getType());
        Assert.assertEquals(argument, config.getSource());
        }
    }


