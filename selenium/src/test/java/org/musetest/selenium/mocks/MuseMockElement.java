package org.musetest.selenium.mocks;

import org.openqa.selenium.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MuseMockElement implements WebElement
    {
    @Override
    public void click()
        {
        _is_clicked = true;
        }

    @Override
    public void submit()
        {

        }

    @Override
    public void sendKeys(CharSequence... keysToSend)
        {

        }

    @Override
    public void clear()
        {

        }

    @Override
    public String getTagName()
        {
        return null;
        }

    @Override
    public String getAttribute(String name)
        {
        return null;
        }

    @Override
    public boolean isSelected()
        {
        return false;
        }

    @Override
    public boolean isEnabled()
        {
        return false;
        }

    @Override
    public String getText()
        {
        return null;
        }

    @Override
    public List<WebElement> findElements(By by)
        {
        return null;
        }

    @Override
    public WebElement findElement(By by)
        {
        return null;
        }

    @Override
    public boolean isDisplayed()
        {
        return false;
        }

    @Override
    public Point getLocation()
        {
        return null;
        }

    @Override
    public Dimension getSize()
        {
        return null;
        }

    @Override
    public String getCssValue(String propertyName)
        {
        return null;
        }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException
        {
        return null;
        }

    public boolean isClicked()
        {
        return _is_clicked;
        }

    private boolean _is_clicked = false;
    }


