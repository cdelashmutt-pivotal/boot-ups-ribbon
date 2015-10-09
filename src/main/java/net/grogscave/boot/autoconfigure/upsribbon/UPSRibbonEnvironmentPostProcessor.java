package net.grogscave.boot.autoconfigure.upsribbon;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

public class UPSRibbonEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return Ordered.HIGHEST_PRECEDENCE + 15;
	}

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		MutablePropertySources propertySources = environment.getPropertySources();
		
		Map<String,Object> serviceMap = new HashMap<String,Object>();
		for(PropertySource<?> source : propertySources)
		{
			if(source instanceof EnumerablePropertySource<?>)
			{
				EnumerablePropertySource<?> enumerableSource = (EnumerablePropertySource<?>)source;
				for(String name : enumerableSource.getPropertyNames())
				{
					if(name.startsWith("vcap.services."))
					{
						StringTokenizer tokenizer = new StringTokenizer(name.substring(14), ".");
						String serviceName = tokenizer.nextToken();
						if("credentials".equals(tokenizer.nextToken()))
						{
							String prop = tokenizer.nextToken();
							if("listOfServers".equals(prop))
							{
								serviceMap.put(serviceName+".ribbon.listOfServers", source.getProperty(name));
							}
						}
					}
				}
			}
		}
		
		MapPropertySource newSource = new MapPropertySource("ups-ribbon", serviceMap);
		
		if (propertySources
				.contains(CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME)) {
			propertySources.addAfter(
					CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME,
					newSource);
		}
		else {
			propertySources.addFirst(newSource);
		}
	}
}
