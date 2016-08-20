package org.musetest.builtins.value.sysvar;

import org.slf4j.*;

import java.net.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")  // May need access from other places. Could be extended.
public class DefaultEnvironmentProvider implements EnvironmentProviderInterface
    {
    @Override
    public Map<String, String> getVars()
        {
        return System.getenv();
        }

    @Override
    public Dictionary getProps()
        {
        return System.getProperties();
        }

    @Override
    public String getUsername()
        {
        return System.getProperties().getProperty("user.name");
        }

    @Override
    public String getHostname()
        {
        String hostname = System.getenv().get("COMPUTERNAME");  // windows
        if (hostname == null)
            hostname = System.getenv().get("HOSTNAME");  // linux
        if (hostname == null)
            try
                {
                hostname = InetAddress.getLocalHost().getHostName();
                }
            catch (UnknownHostException ex)
                {
                LOG.warn("Unable to determine hostname from network subsystem: ", ex);
                }
        if (hostname == null)
            hostname = "unknown.host";
        return hostname;
        }

    @Override
    public String toString()
        {
        return getClass().getSimpleName();
        }

    private final static Logger LOG = LoggerFactory.getLogger(DefaultEnvironmentProvider.class);
    }



