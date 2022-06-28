package webDriverMethods;

import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.*;

public class ProjectMethods extends SeleniumMethods {

    public String testCaseName;
    public String testDescription;
    Properties prop;

    @BeforeSuite
    public void beforeSuite() {
        startResult();
    }

    @BeforeClass
    public void beforeClass() {

    }

    @Parameters({ "browser", "platform", "url", "runIn"})
    @BeforeMethod
    public void beforeMethod(String browser, String platform, String applicationUrl, String runIn) {
        test = startTestModule(testCaseName, testDescription);
        test = startTestCase(testNodes);
        test.assignCategory(category);
        test.assignAuthor(authors);
        startBrowser(browser, platform, applicationUrl, testCaseName, runIn);

    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() throws IOException {
        endResult();
        closeAllBrowsers();
    }

    @AfterTest
    public void afterTest() {

    }

//    @AfterClass
//    public void afterClass() {
//
//    }

    @AfterSuite
    public void afterSuite() {
        endResult();

    }

}
