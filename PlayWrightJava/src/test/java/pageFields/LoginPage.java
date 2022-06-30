package pageFields;


import com.microsoft.playwright.Page;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.devtools.DevTools;
import webDriverMethods.ProjectMethods;

import java.io.IOException;

public class LoginPage extends ProjectMethods{
    JSONObject data;


    public LoginPage(Page page) {
        this.page = page;
    }

    private  String elePostcode="(//a[@id='header-logout'])";
    public String clickLogin() {
        try {
             data=getDataFromJson();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        click(data.get("loginButton").toString());
        enterText(data.get("postcodeSearch").toString(),"AA11AA");
        getText(data.get("findTakeaway").toString(),"Inner Text");
        getText(data.get("findTakeaway").toString(),"type");
        return elePostcode;


    }



}
