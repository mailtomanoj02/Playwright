package testCases;

import java.nio.file.Paths;
import com.microsoft.playwright.Tracing;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pageFields.LoginPage;
import webDriverMethods.ProjectMethods;

public class TC_HomePage extends ProjectMethods {

    @BeforeTest
    public void setValues() {
        testCaseName = "FoodHub Login";
        testDescription = "To verify FoodHub Login";
        testNodes = "Nodes";
        category = "Regression";
        authors = "vinoth";
    }

    @Test
    public void HomePage() {
        new LoginPage(page)
                .clickLogin();
        browserContext.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("manoj.zip")));


    }

}
