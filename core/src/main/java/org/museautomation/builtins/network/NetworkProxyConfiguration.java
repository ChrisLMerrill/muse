package org.museautomation.builtins.network;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("proxy-config")
public class NetworkProxyConfiguration extends BaseMuseResource
    {
    public NetworkProxyConfiguration()
        {
        }

    public ProxyConfigType getProxyType()
        {
        return _type;
        }

    public void setProxyType(ProxyConfigType type)
        {
        if (!type.equals(_type))
            {
            ProxyConfigType old_type = _type;
            _type = type;
            for (ChangeListener listener : _listeners)
                listener.typeChanged(old_type, type);
            }
        }

    public String getPacUrl()
        {
        return _pac_url;
        }

    public void setPacUrl(String pac_url)
        {
        if (!pac_url.equals(_pac_url))
            {
            String old_url = _pac_url;
            _pac_url = pac_url;
            for (ChangeListener listener : _listeners)
                listener.pacUrlChanged(old_url, pac_url);
            }
        }

    public String getHostname()
        {
        return _hostname;
        }

    public void setHostname(String hostname)
        {
        if (!hostname.equals(_hostname))
            {
            String old_hostname = _hostname;
            _hostname = hostname;
            for (ChangeListener listener : _listeners)
                listener.hostnameChanged(old_hostname, hostname);
            }
        }

    public Integer getPort()
        {
        return _port;
        }

    public void setPort(Integer port)
        {
        if (!Objects.equals(port, _port))
            {
            Integer old_port = _port;
            _port = port;
            for (ChangeListener listener : _listeners)
                listener.portChanged(old_port, port);
            }
        _port = port;
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetworkProxyConfiguration that = (NetworkProxyConfiguration) o;
        return _type == that._type && _pac_url.equals(that._pac_url) && _hostname.equals(that._hostname) && _port.equals(that._port);
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(_type, _pac_url, _hostname, _port);
        }

    private ProxyConfigType _type = ProxyConfigType.None;
    private String _pac_url = "";
    private String _hostname = "";
    private Integer _port = null;

    @Override
    public String toString()
        {
        String s = String.format("Proxy (%s)", _type.name());
        switch (_type)
            {
            case None:
                return "No Proxy";
            case System:
                return "Use System Proxy";
            case Fixed:
                return String.format("Proxy (%s:%d)", _hostname, _port);
            case Script:
                return String.format("Proxy (auto config from %s)", _pac_url);
            }
        return s;
        }

    @SuppressWarnings("unused")  // public API
    public void addChangeListener(ChangeListener listener)
        {
        _listeners.add(listener);
        }

    @SuppressWarnings("unused")  // public API
    public void removeChangeListener(ChangeListener listener)
        {
        _listeners.remove(listener);
        }

    private final transient Set<ChangeListener> _listeners = new HashSet<>();

    @Override
    public ResourceType getType()
        {
        return new ProxyResourceType();
        }

    public final static String TYPE_ID = NetworkProxyConfiguration.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class ProxyResourceType extends ResourceType
        {
        public ProxyResourceType()
            {
            super(TYPE_ID, "Proxy Configuration", NetworkProxyConfiguration.class);
            }
        }

    @SuppressWarnings("WeakerAccess")  // public API
    public interface ChangeListener
        {
        void hostnameChanged(String old_value, String new_value);
        void pacUrlChanged(String old_value, String new_value);
        void portChanged(Integer old_value, Integer new_value);
        void typeChanged(ProxyConfigType old_value, ProxyConfigType new_value);
        }

    public enum ProxyConfigType
        {
        None,
        Fixed,
        System,
        Script,
        }
    }
