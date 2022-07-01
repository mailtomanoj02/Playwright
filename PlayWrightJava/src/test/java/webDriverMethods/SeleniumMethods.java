package webDriverMethods;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import utils.JsonReader;


public class SeleniumMethods extends JsonReader {
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
            prop.load(new FileInputStream(new File("./src/test/resources/config.properties")));
            AUTOMATE_USERNAME = prop.getProperty("USERNAME");
            AUTOMATE_ACCESS_KEY = prop.getProperty("PASSWORD");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Page startBrowser(String browserName, String platform, String applicationUrl, String tcname, String runIn) {

        if (runIn.equalsIgnoreCase("local")) {
            Properties properties = init_prop();
            Boolean headless = new Boolean(properties.getProperty("headless"));
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

        } else if(runIn.equalsIgnoreCase("remote")) {
            try (Playwright playwright = Playwright.create()) {
                JsonObject capabilitiesObject = new JsonObject();
                capabilitiesObject.addProperty("browser", "chrome");    // allowed browsers are `chrome`, `edge`, `playwright-chromium`, `playwright-firefox` and `playwright-webkit`
                capabilitiesObject.addProperty("browser_version", "latest");
                capabilitiesObject.addProperty("os", "osx");
                capabilitiesObject.addProperty("os_version", "monterey");
                capabilitiesObject.addProperty("name", "Playwright first single test");
                capabilitiesObject.addProperty("build", "playwright-java-1");
                capabilitiesObject.addProperty("browserstack.username", AUTOMATE_USERNAME);
                capabilitiesObject.addProperty("browserstack.accessKey", AUTOMATE_ACCESS_KEY);

                BrowserType chromium = playwright.chromium();
                String caps = URLEncoder.encode(capabilitiesObject.toString(), "utf-8");
                String ws_endpoint = "wss://cdp.browserstack.com/playwright?caps=" + caps;
                Browser browser = chromium.connect(ws_endpoint);
                page = browser.newPage();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
            FileInputStream ip = new FileInputStream("./src/test/resources/config.properties");
            prop=new Properties();
            prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    public void click(String ele){
        try {
            if(page.locator(ele).isEnabled()) {
                page.click(ele);
                reportStep("Element has clicked", "PASS",true);
            }
        } catch (Exception e) {
            page.locator(ele).scrollIntoViewIfNeeded();
            try {
                if (page.locator(ele).isEnabled()) {
                    page.click(ele);
                }
            }catch (Exception i){
                reportStep("Element has not clicked","FAIL");
            }
        }
    }

    public void verifyDisplayed(String ele) throws IOException {
        try {
            page.locator(ele).isVisible();
            reportStep("Element " + ele +" is visible","PASS",true);
        }catch (Exception e){
            reportStep("Element " + ele +" is not visible","PASS");
        }

    }
    public void enterText(String ele,String enterText){
        try{
            page.locator(ele).waitFor();
            page.locator(ele).fill("");
            page.locator(ele).fill(enterText);
            reportStep("Entered text as "+enterText,"PASS",true);
        }catch (Exception e){
            reportStep("Text cannot be entered","FAIL");
        }
    }

    public void getText(String ele,String attribute){
        try{
            String text=page.locator(ele).getAttribute(attribute);
            System.out.println(text);


        }catch (Exception e){

        }
    }

    public long takeSnap(){
        long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L;
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get("screenshot.png"))
                .setFullPage(true));
        return number;
    }
}