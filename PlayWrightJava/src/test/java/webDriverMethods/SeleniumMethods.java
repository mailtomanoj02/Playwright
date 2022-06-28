package webDriverMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.microsoft.playwright.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.Reports;



public class SeleniumMethods extends Reports {
    Browser browsers;
    public Page page;
    private Logger log = Logger.getLogger(this.getClass().getName());
    public String AUTOMATE_USERNAME = "";
    public String AUTOMATE_ACCESS_KEY = "";
    public String URL;
    public DesiredCapabilities dc;
    public String version = "";
    Playwright playwrite;
    public BrowserContext browserContext;
    Properties prop;

    public SeleniumMethods() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(new File("/Users/manojs/Documents/Automation/Messy/src/test/resources/config.properties")));
            AUTOMATE_USERNAME = prop.getProperty("USERNAME");
            AUTOMATE_ACCESS_KEY = prop.getProperty("PASSWORD");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DesiredCapabilities getBrowser(String browser) {
        DesiredCapabilities dc = new DesiredCapabilities();

        switch (browser) {
            case "Chrome":
                version = "latest";
                dc = new DesiredCapabilities();
                break;

            case "Firefox":
                version = "latest";
                dc = new DesiredCapabilities();
                break;

            case "Edge":
                version = "latest";
                dc = new DesiredCapabilities();
                break;

            case "InternetExplorer":
                version = "latest";
                dc = new DesiredCapabilities();
                break;

            case "Safari":
                version = "latest";
                dc = new DesiredCapabilities();
                break;
        }
        dc.setCapability("version", version);
        return dc;
    }

    public Page startBrowser(String browserName, String platform, String applicationUrl, String tcname, String runIn) {

        if (runIn.equalsIgnoreCase("local")) {
            Properties properties=init_prop();
            Boolean headless=new Boolean(properties.getProperty("headless"));
            System.out.println(headless);
            playwrite = Playwright.create();

            switch (browserName.toLowerCase()) {
                case "chromium":
                    browsers = playwrite.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                    break;
                case "firefox":
                    browsers = playwrite.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                    break;
                case "safari":
                    browsers = playwrite.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                    break;
                case "chrome":
                    browsers = playwrite.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(headless));
                    break;
                default:
                    System.out.println("***** Please pass the right browser name *****");
                    reportStep("***** Please pass the right browser name *****", "FAIL");
                    break;

            }

            browserContext = browsers.newContext();
            browserContext.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(false));

            page = browserContext.newPage();

        }
        try {
            page.navigate(applicationUrl);
            reportStep("[" + browserName + "] launched successfully", "INFO");
        } catch (Exception e) {
            reportStep("[" + browserName + "]: could not be launched", "FAIL");
        }
        return page;
    }



    public void closeAllBrowsers() throws IOException {
        try {
            if (page == null) {
                return;
            }
            page.context().browser().close();
            page = null;
            reportStep("The opened browsers are closed", "PASS", false);
        } catch (Exception e) {
            reportStep("Unexpected error occured in Browser: \n Error: " + e.getMessage(), "INFO", false);
        }
    }

    public static void sleep(int mSec) {
        try {
            Thread.sleep(mSec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getTitle(){
        return page.title();
    }
    public String getHomePageUrl(){
        return  page.url();
    }
    public Properties init_prop(){
        try {
            FileInputStream ip = new FileInputStream("/Users/manojs/PlayWrightJava/src/test/resources/config.properties");
            prop=new Properties();
            prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}