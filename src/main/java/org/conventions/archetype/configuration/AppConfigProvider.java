package org.conventions.archetype.configuration;

import org.conventionsframework.producer.ConfigurationProvider;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * Created by rmpestano on 12/27/13.
 *
 * gather app configuration
 */
@Singleton
@Startup
public class AppConfigProvider  {

    @Inject
    ConfigurationProvider configurationProvider;//can also be extended

    @PostConstruct
    public void init(){

        addConfig("myConfig", 1L);
        addConfig("myDoubleConfig",2.5);
        /**
         * to get custom config use:
         * @Inject
         * @Config
         * Long myConfig;
         */
    }

    
    
    public void addConfig(String key, Object value){
        configurationProvider.addConfigEntry(key, value);
    }

}
