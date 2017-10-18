package org.musetest.core.context.initializers;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.slf4j.*;

import java.util.*;

/**
 * Represents configuration data for a ContextInitializer.  It has a type, which is used to
 * lookup the implementation of the initializer. It also has an ApplyCondition, which should
 * be evaluated before applying to the context. It contains named ValueSourceConfigurations which
 * should be supplied to the ContextInitializer.configure() method after construction (or anytime prior
 * to use).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess") // publicly extensible UI
public class ContextInitializerConfiguration implements ContainsNamedSources
    {
    // required for de/serialization
   	public String getInitializerType()
   		{
   		return _initializer_type;
   		}

   	// required for de/serialization
   	public void setInitializerType(String initializer_type)
   		{
	    _initializer_type = initializer_type;
   		}

   	// required for de/serialization
   	public Map<String, ValueSourceConfiguration> getParameters()
   		{
   		return _named_sources.getSourceMap();
   		}

   	// required for de/serialization
   	public void setParameters(Map<String, ValueSourceConfiguration> parameters)
   		{
   		_named_sources.setSourceMap(parameters);
   		}

    public void addParameter(String name, ValueSourceConfiguration source)
	    {
	    _named_sources.addSource(name, source);
	    }

   	public ValueSourceConfiguration getApplyCondition()
   		{
   		return _apply_condition;
   		}

   	public void setApplyCondition(ValueSourceConfiguration apply_condition)
   		{
   		_apply_condition = apply_condition;
   		}

   	@Override
   	public ValueSourceConfiguration getSource(String name)
   		{
   		return _named_sources.getSource(name);
   		}

   	@Override
   	public void addSource(String name, ValueSourceConfiguration source)
   		{
   		_named_sources.addSource(name, source);
   		}

   	@Override
   	public ValueSourceConfiguration removeSource(String name)
   		{
   		return _named_sources.removeSource(name);
   		}

   	@Override
   	public boolean renameSource(String old_name, String new_name)
   		{
   		return _named_sources.renameSource(old_name, new_name);
   		}

   	@Override
   	public ValueSourceConfiguration replaceSource(String name, ValueSourceConfiguration new_source)
   		{
   		return _named_sources.replaceSource(name, new_source);
   		}

   	@JsonIgnore
   	@Override
   	public Set<String> getSourceNames()
   		{
   		return _named_sources.getSourceNames();
   		}

   	@Override
   	public void addChangeListener(ChangeEventListener listener)
   		{
   		_named_sources.addChangeListener(listener);
   		}

   	@Override
   	public boolean removeChangeListener(ChangeEventListener listener)
   		{
   		return _named_sources.removeChangeListener(listener);
   		}

    public ContextInitializer createInitializer(MuseProject project)
	    {
	    final List<Class> intializers = project.getClassLocator().getImplementors(ContextInitializer.class);
	    for (Class initializer_class : intializers)
		    {
		    try
			    {
			    ContextInitializer initializer = (ContextInitializer) initializer_class.newInstance();
			    if (getInitializerType().equals(initializer.getType()))
				    {
				    initializer.configure(this);
				    return initializer;
				    }
			    }
		    catch (Exception e)
			    {
			    LOG.warn(String.format("WARNING: %s implements ContextInitializer but does not have a default constructor. Thus it cannot be instantiated automatically (which may or may not be a problem).", initializer_class.getSimpleName()));
			    }
		    }
	    // TODO lookup the implementation by the type
	    // TODO apply the configuration
	    return new EventLogger();
	    }

    public boolean shouldApply(MuseExecutionContext context) throws ValueSourceResolutionError, MuseInstantiationException
	    {
	    return BaseValueSource.getValue(_apply_condition.createSource(context.getProject()), context, false, Boolean.class);
	    }

    private NamedSourcesContainer _named_sources = new NamedSourcesContainer();
   	private String _initializer_type;
   	private ValueSourceConfiguration _apply_condition;

   	private final static Logger LOG = LoggerFactory.getLogger(ContextInitializerConfiguration.class);
    }


