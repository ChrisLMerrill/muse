package org.museautomation.selenium.tests;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.project.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.strings.*;
import org.museautomation.selenium.conditions.*;
import org.museautomation.selenium.locators.*;
import org.museautomation.selenium.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class SeleniumValueSourceStringExpressionSupportTests
    {
    @Test
    void elementExists() throws InstantiationException, IllegalAccessException
        {
        checkValueSourceFunction(ElementExistsCondition.TYPE_ID, new ElementExistsCondition.StringExpressionSupport().getName(), ElementExistsCondition.StringExpressionSupport.class);
        }

    @Test
    void elementVisible() throws InstantiationException, IllegalAccessException
        {
        checkValueSourceFunction(ElementExistsCondition.TYPE_ID, new ElementExistsCondition.StringExpressionSupport().getName(), ElementExistsCondition.StringExpressionSupport.class);
        }

    @Test
    void elementEnabled() throws InstantiationException, IllegalAccessException
        {
        checkValueSourceFunction(ElementEnabledCondition.TYPE_ID, new ElementEnabledCondition.StringExpressionSupport().getName(), ElementEnabledCondition.StringExpressionSupport.class);
        }

    private void checkValueSourceFunction(String muse_type_id, String function_name, Class<? extends BaseValueSourceStringExpressionSupport> support_class) throws IllegalAccessException, InstantiationException
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(muse_type_id);
        ValueSourceConfiguration sub_source = ValueSourceConfiguration.forValue("abc");
        config.setSource(sub_source);

        String stringified = PROJECT.getValueSourceStringExpressionSupporters().toString(config, new RootStringExpressionContext(PROJECT));
        Assertions.assertEquals(String.format("%s(\"%s\")", function_name, "abc"), stringified);

        ArrayList<ValueSourceConfiguration> arguments = new ArrayList<>();
        arguments.add(sub_source);
        ValueSourceConfiguration parsed_config = support_class.newInstance().fromArgumentedExpression(function_name, arguments, PROJECT);

        Assertions.assertEquals(config, parsed_config);
        }

    @Test
    void pageTitleStringExpressionSupport()
        {
        ValueSourceConfiguration config = new PageTitleValueSource.StringExpressionSupport().fromElementExpression(PageTitleValueSource.StringExpressionSupport.NAME, new ArrayList<>(), null);
        Assertions.assertEquals(PageTitleValueSource.TYPE_ID, config.getType());
        }

    @Test
    void pageSourceStringExpressionSupport()
        {
        ValueSourceConfiguration config = new PageSourceValueSource.StringExpressionSupport().fromElementExpression(PageSourceValueSource.StringExpressionSupport.NAME, new ArrayList<>(), null);
        Assertions.assertEquals(PageSourceValueSource.TYPE_ID, config.getType());
        }

    @Test
    void currentUrlStringExpressionSupport()
        {
        ValueSourceConfiguration config = new CurrentUrlValueSource.StringExpressionSupport().fromElementExpression(CurrentUrlValueSource.StringExpressionSupport.NAME, new ArrayList<>(), null);
        Assertions.assertEquals(CurrentUrlValueSource.TYPE_ID, config.getType());
        }

    @Test
    void elementByIdStringExpressionSupport()
        {
        checkCreationFromStringExpression(new IdElementValueSource.StringExpressionSupport(), IdElementValueSource.StringExpressionSupport.STRING_EXPRESSION_ID, IdElementValueSource.TYPE_ID);
        }

    @Test
    void elementByXPathStringExpressionSupport()
        {
        checkCreationFromStringExpression(new XPathElementValueSource.StringExpressionSupport(), XPathElementValueSource.StringExpressionSupport.STRING_EXPRESSION_ID, XPathElementValueSource.TYPE_ID);
        }

    @Test
    void elementByNameStringExpressionSupport()
        {
        checkCreationFromStringExpression(new NameElementValueSource.StringExpressionSupport(), NameElementValueSource.StringExpressionSupport.STRING_EXPRESSION_ID, NameElementValueSource.TYPE_ID);
        }

    @Test
    void elementByCssStringExpressionSupport()
        {
        checkCreationFromStringExpression(new CssElementValueSource.StringExpressionSupport(), CssElementValueSource.StringExpressionSupport.STRING_EXPRESSION_ID, CssElementValueSource.TYPE_ID);
        }

    @Test
    void elementByLinkTextExpressionSupport()
        {
        checkCreationFromStringExpression(new LinkTextElementValueSource.StringExpressionSupport(), LinkTextElementValueSource.StringExpressionSupport.STRING_EXPRESSION_ID, LinkTextElementValueSource.TYPE_ID);
        }

    private void checkCreationFromStringExpression(ValueSourceStringExpressionSupport parser, String expression_id, String muse_type_id)
        {
        ValueSourceConfiguration argument = ValueSourceConfiguration.forValue("qualifier");
        List<ValueSourceConfiguration> arguments = new ArrayList<>();
        arguments.add(argument);
        ValueSourceConfiguration config = parser.fromElementExpression(expression_id, arguments, null);
        Assertions.assertEquals(muse_type_id, config.getType());
        Assertions.assertEquals(argument, config.getSource());
        }

    private final static MuseProject PROJECT = new SimpleProject();
    }


