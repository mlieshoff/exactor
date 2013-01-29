package com.exoftware.exactor.command.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.jwebunit.TestContext;

import com.meterware.httpunit.WebClient;

/**
 * @author Brian Swan
 */
public class MockTestContext extends TestContext
{
    private String baseUrl;

    public void setAuthorization( String s, String s1 )
    {
    }

    public void addCookie( String s, String s1 )
    {
    }

    public boolean hasAuthorization()
    {
        return false;
    }

    public boolean hasCookies()
    {
        return false;
    }

    public String getUser()
    {
        return null;
    }

    public String getPassword()
    {
        return null;
    }

    public List getCookies()
    {
        return new ArrayList();
    }

    public String getUserAgent()
    {
        return null;
    }

    public void setUserAgent( String s )
    {
    }

    public boolean hasUserAgent()
    {
        return false;
    }

    public Locale getLocale()
    {
        return null;
    }

    public void setLocale( Locale locale )
    {
    }

    public String getEncodingScheme()
    {
        return null;
    }

    public void setEncodingScheme( String s )
    {
    }

    public String toEncodedString( String s )
    {
        return null;
    }

    public void setResourceBundleName( String s )
    {
    }

    public String getResourceBundleName()
    {
        return null;
    }

    public String getProxyName()
    {
        return null;
    }

    public void setProxyName( String s )
    {
    }

    public int getProxyPort()
    {
        return 0;
    }

    public void setProxyPort( int i )
    {
    }

    public boolean hasProxy()
    {
        return false;
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public void setBaseUrl( String s )
    {
        baseUrl = s;
    }

    public void setWebClient( WebClient webClient )
    {
    }

    public WebClient getWebClient()
    {
        return null;
    }
}
