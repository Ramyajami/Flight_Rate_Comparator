package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.Flights_Page;
import utils.ConfigReader;

import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

public class FlightPriceComparator_StepDef {
    public WebDriver driver;
    Flights_Page fp;
    Properties prop = new ConfigReader().intializeProperties();

    @Given("User launch chrome browser")
    public void userLaunchChromeBrowser() {
        String path = System.getProperty("user.dir");
        System.setProperty("webdriver.chrome.driver", "" + path + "/Drivers/chromedriver-116-mac-arm64/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
    }
    @When("user opens first portal")
    public void userOpensFirstPortal() {
        driver.get(prop.getProperty("url1"));
        driver.manage().window().maximize();
    }
    @When("user opens second portal")
    public void userOpensSecondPortal() {
        openNewTab(driver, prop.getProperty("url2"), 1);
    }

    public static void openNewTab(WebDriver webDriver, String url, int position) {
        ((JavascriptExecutor) webDriver).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(position));
        webDriver.get(url);
    }

    @And("Enter valid search criteria {string} {string} locations")
    public void enterValidSearchCriteriaLocations(String fromLoc, String toLoc) throws InterruptedException {
        fp = new Flights_Page(driver);
        fp.enterFromToDetails(fromLoc, toLoc);
    }

    @Then("click on search button")
    public void clickOnSearchButton() {
        fp.clickSearchButton();
    }

    @And("Enter valid journey {string}")
    public void enterValidJourney(String dateOfJourney) throws InterruptedException, ParseException {
        fp.enterJourneyDate(dateOfJourney,"clear trip");
    }

    @And("Select non stop flights")
    public void selectNonStopFlights() throws InterruptedException {
        fp.selectNonStopFlights();
    }

    @Then("Enter valid From {string} and To {string} locations")
    public void enterValidFromAndToLocations(String from, String to) throws InterruptedException {
        fp.enterFromAndToLocations(from, to);
    }

    @Then("click on search flights button")
    public void clickOnSearchFlightsButton() throws InterruptedException {
        fp.clickOnSearchFlights();
    }
    @Then("User able to view flights data like Flight Operator,Flight Number,Price on Cleartrip")
    public void userAbleToViewFlightsDataLikeFlightOperatorFlightNumberPriceOnCleartrip() throws IOException, InterruptedException {
        fp.CTFlightdata();
    }

    @And("Enter valid travel {string}")
    public void enterValidTravel(String date) throws ParseException, InterruptedException {
        fp.enterJourneyDate(date,"paytm");
    }

    @And("Search non stop flights")
    public void SearchNonStopFlights() throws InterruptedException {
        fp.searchNonStopFlights();
    }
    @Then("User able to view flights data like Flight Operator,Flight Number,Price on Paytm")
    public void userAbleToViewFlightsDataLikeFlightOperatorFlightNumberPriceOnPaytm() throws InterruptedException {
        fp.PTFlightdata();
    }

    @Then("User should able to view price comparison csv report with {string},{string},{string},{string}")
    public void userShouldAbleToViewPriceComparisonCsvReportWith(String FlightOperator,String FlightNumber,String PriceOnCleartrip,String PriceOnPaytm) throws IOException {
        fp.comparePrices(FlightOperator,FlightNumber,PriceOnCleartrip,PriceOnPaytm);
    }
    @Then("close browser")
    public void closeBrowser() {
        driver.quit();
    }


}
