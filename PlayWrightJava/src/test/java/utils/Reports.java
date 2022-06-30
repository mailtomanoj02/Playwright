package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;

public abstract class Reports {

    public ExtentSparkReporter html;
    public static ExtentReports extent;
    public static ExtentTest test, suiteTest;
    public String testCaseName, testNodes, testDescription, category, authors, imagePath;


    public void startResult() {
        html = new ExtentSparkReporter(System.getProperty("user.dir") + "/reports/result.html");
        html.config().setEncoding("utf-8");
        html.config().setProtocol(Protocol.HTTPS);
        html.config().setDocumentTitle("Automation Report");
        html.config().setReportName("Regression Testing");
        html.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        html.config().setTheme(Theme.STANDARD);
        extent = new ExtentReports();
        extent.attachReporter(html);
    }


    public ExtentTest startTestModule(String testCaseName, String testDescription) {
        suiteTest = extent.createTest(testCaseName, testDescription);
        return suiteTest;
    }

    public ExtentTest startTestCase(String testNodes) {
        test = suiteTest.createNode(testNodes);
        return test;
    }

    //public abstract long takeScreenShot();

    public void reportStep(String desc, String status, boolean bSnap) throws IOException {
        System.out.println(test);

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(new File("./src/test/resources/Json/foodhub.json")));

            imagePath = prop.getProperty("Imagepath");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Media img = null;
        if (bSnap && !status.equalsIgnoreCase("INFO")) {

            long snapNumber = 1000000L;
            //snapNumber = takeScreenShot();
            if (imagePath == null) {
                img = MediaEntityBuilder.createScreenCaptureFromPath("./../reports/images/" + snapNumber + ".png")
                        .build();
            } else {
                img = MediaEntityBuilder.createScreenCaptureFromPath(imagePath + "/" + snapNumber + ".png").build();
            }
        }

        if (status.equalsIgnoreCase("PASS")) {
            test.pass(desc, img);
            test.log(Status.PASS, MarkupHelper.createLabel(" PASSED ", ExtentColor.GREEN));
        }
        else if (status.equalsIgnoreCase("FAIL")) {
            test.fail(desc, img);
            test.log(Status.FAIL, MarkupHelper.createLabel(" FAILED ", ExtentColor.RED));
        }
        else if (status.equalsIgnoreCase("WARNING")) {
            test.warning(desc, img);
            test.log(Status.WARNING, MarkupHelper.createLabel(" WARNING ", ExtentColor.YELLOW));
        }
        else if (status.equalsIgnoreCase("SKIP")) {
            test.warning(desc, img);
            test.log(Status.SKIP, MarkupHelper.createLabel(" SKIP ", ExtentColor.ORANGE));
        }
        else if (status.equalsIgnoreCase("INFO")) {
            test.info(desc);
            test.log(Status.INFO, MarkupHelper.createLabel(" INFO ", ExtentColor.PINK));
        }
    }

    public void reportStep(String desc, String status) {
        System.out.println("Web Application : " + desc + " " + status);
        try {
            reportStep(desc, status, true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void endResult() {
        extent.flush();
    }

    public void endTestcase(){
        extent.removeTest(test);
    }

}


