package org.conventions.archetype.configuration;

import org.conventionsframework.producer.ConfigurationProvider;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.ResourceBundle;

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

    @Inject
    ResourceBundle resourceBundle;

    @PostConstruct
    public void init(){

        addConfig("myConfig", 1L);
        addConfig("myDoubleConfig",2.5);
        addConfig("version",resourceBundle.getString("version"));
        /**
         * to get custom config use:
         * @Inject
         * @Config
         * Long myConfig;
         *
         * or you can access in jsf page through el expression:
         * #{configMap.myConfig} or if the config key contains '.' use:
         * #{configMap['my.config']}
         *
         */
    }

    
    
    public void addConfig(String key, Object value){
        configurationProvider.addConfigEntry(key, value);
    }

}
