package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.Map;

public class UIStepDefs{

    LoginPage loginPage = new LoginPage();
    BookPage bookPage = new BookPage();


    @Given("I logged in Library UI as {string}")
    public void i_logged_in_library_ui_as(String userRole) {
        loginPage.login(userRole);
    }

    @Given("I navigate to {string} page")
    public void i_navigate_to_page(String uiPage) {
        bookPage.navigateModule(uiPage);

    }

    @Then("created user should be able to login Library UI")
    public void created_user_should_be_able_to_login_library_ui() {

        Map<String, Object> userResponseMap = APIStepDefs.userResponseMap;
        Object email = userResponseMap.get("email");
        Object password = ConfigurationReader.getProperty("allOther_password");

        BrowserUtil.waitFor(1);
        loginPage.login((String)email, (String)password);
        BrowserUtil.waitFor(5);

    }

    @Then("created user name should appear in Dashboard Page")
    public void created_user_name_should_appear_in_dashboard_page() {

        BrowserUtil.verifyElementDisplayed(bookPage.accountHolderName);

    }

}
