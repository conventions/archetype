package org.conventions.archetype.test.bdd;

import com.google.common.util.concurrent.MoreExecutors;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.UnderscoredCamelCaseResolver;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jboss.arquillian.jbehave.core.ArquillianInstanceStepsFactory;

import static org.jbehave.core.reporters.Format.*;

/**
 * Created with IntelliJ IDEA.
 * User: rmpestano
 * Date: 10/31/13
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class BaseBahaveTest extends JUnitStory{


    public BaseBahaveTest() {
        configuredEmbedder().useExecutorService(MoreExecutors.sameThreadExecutor());
    }

    @Override
    public Configuration configuration()
    {
        Configuration configuration = new MostUsefulConfiguration()
                .useStoryPathResolver(new UnderscoredCamelCaseResolver())
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withDefaultFormats()
                        .withFormats(CONSOLE, TXT, HTML, XML)
                        .withFailureTrace(true));
        return configuration;
    }

    @Override
    public InjectableStepsFactory stepsFactory()
    {
        return new ArquillianInstanceStepsFactory(configuration(), getSteps());
    }


    public abstract Object getSteps();



}
