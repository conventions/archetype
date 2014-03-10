package org.conventions.archetype.test.ft;

import org.apache.commons.io.FileUtils;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: rafael-pestano
 * Date: 23/10/13
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */
public class ScreenshotTestRule implements MethodRule {

    private final File screenshotDir = new File("target/screenshots/");
    TakesScreenshot takesScreenshot;


    public ScreenshotTestRule(TakesScreenshot takesScreenshot) {
        this.takesScreenshot = takesScreenshot;
    }

    public Statement apply(final Statement statement, final FrameworkMethod frameworkMethod, final Object o) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    statement.evaluate();
                } catch (Throwable t) {
                    captureScreenshot(frameworkMethod.getName());
                    throw t; // rethrow to allow the failure to be reported to JUnit
                }
            }

            public void captureScreenshot(String fileName) {
                try {
                    File tempFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
                    screenshotDir.mkdirs();
                    File screenshotFile = new File(screenshotDir, "screenshot-"+fileName + ".png");
                    FileUtils.copyFile(tempFile, screenshotFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
