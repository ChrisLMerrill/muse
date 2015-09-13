package org.musetest.seleniumide.tests;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.junit.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ParsingTests
    {
    @Test
    public void parseExample() throws IOException
        {
        Document doc = Jsoup.parse(getClass().getResourceAsStream("/example.html"), "UTF-8", "http://example.com/");
        Assert.assertNotNull(doc);

        Elements links = doc.getElementsByTag("link");
        Assert.assertEquals(1, links.size());
        Assert.assertEquals("selenium.base", links.get(0).attr("rel"));
        Assert.assertEquals("http://www.example.com/", links.get(0).attr("href"));

        Elements rows = doc.getElementsByTag("tr");
        Assert.assertEquals(6, rows.size());  // +1 for the header

        Element row1 = rows.get(1);  // first data row
        Assert.assertEquals(3, row1.children().size());

        Assert.assertEquals("open", row1.children().get(0).text());
        Assert.assertEquals("/login/", row1.children().get(1).text());
        Assert.assertEquals("", row1.children().get(2).text());
        }
    }


