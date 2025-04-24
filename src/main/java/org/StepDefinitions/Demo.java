package org.StepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.config.CommonMethod;
import org.config.ConfigFileReader;
import org.config.ExcelHelpers;
import org.config.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;


public class Demo extends WebDriverManager {
    WebDriver driver = null;
    ConfigFileReader config = new ConfigFileReader();
    CommonMethod common = new CommonMethod(getDriver());
    ExcelHelpers excelHelpers = new ExcelHelpers();


    @Given("open browser and url")
    public void open_browser_and_url() {
        Demo.getInstance().ImplicitlyWait_Config();

        driver = getDriver();
        driver.get(config.getApplicationUrl());
        common.waitForPageLoaded();
        System.out.println("Mở trình duyệt và website thành công!");
    }

    @When("Check title page")
    public void check_title_page(){
        String title = driver.getTitle();
        String url = driver.getCurrentUrl();
        String expect_url = "https://dantri.com.vn/";
        String expect_title = "Tin tức Việt Nam và quốc tế nóng, nhanh, cập nhật 24h | Báo Dân trí";
        System.out.println("Tên website: " + driver.getTitle());

        Assert.assertEquals(title, expect_title);
        Assert.assertEquals(url, expect_url);
        System.out.println("Đã kiểm tra tên website và URL chính xác!");
    }

    @Then("close browser")
    public void close_browser(){
        driver.quit();
    }
}
