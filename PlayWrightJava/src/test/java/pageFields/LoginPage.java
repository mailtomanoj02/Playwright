package pageFields;


import com.microsoft.playwright.Page;
import webDriverMethods.ProjectMethods;

public class LoginPage extends ProjectMethods{

    //private Logger log = Logger.getLogger(this.getClass().getName());

    public LoginPage(Page page) {
        this.page = page;
    }
    private  String elePostcode="(//a[@id='header-logout'])";
    public String clickLogin() {
    page.click(elePostcode);
        return elePostcode;
    }


}
