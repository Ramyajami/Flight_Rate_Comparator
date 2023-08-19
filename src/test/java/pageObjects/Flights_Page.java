package pageObjects;
import org.junit.Assert;
import org.junit.Assume;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.Year.isLeap;

public class Flights_Page extends PageFactory {
    public WebDriver driver;
    Properties prop = new ConfigReader().intializeProperties();

    public Flights_Page(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[@placeholder='Where from?']")
    public WebElement CTFromLocation;
    @FindBy(xpath = "//ul[@class='airportList']/li[1]")
    public WebElement CTFromLocationList;
    @FindBy(xpath = "//input[@placeholder='Where to?']")
    public WebElement CTToLocation;
    @FindBy(xpath = "(//ul[@class='airportList'])[1]")
    public WebElement CTToLocationList;
    @FindBy(xpath = "//div[@class='flex flex-middle p-relative homeCalender']/button[1]")
    public WebElement CTselectDate;
    @FindBy(xpath = "//span[text()='Search flights']")
    public WebElement CTSrchFlights;
    @FindBy(xpath ="(//div[@class='pt-3']//span)[1]")
    public WebElement CTNonStopCheckBox;

    @FindBy(id="srcCode")
    public  WebElement PTFromLocation;

    @FindBy(xpath = "//img[@class='_3i2Wp']")
    public WebElement PTLocClearBtn;

    @FindBy(xpath="/html/body/div/div/div[1]/main/section/div[1]/div/div/div[2]/div[1]/div[1]/div[2]/div/div/div/input")
    public  WebElement PTFromLocationIp;
    @FindBy(id="destCode")
    public WebElement PTToLocation;
    @FindBy(xpath="//div[@class='_2kEaD']/input[@id='text-box']")
    public WebElement PTToLocationIp;
    @FindBy(xpath ="(//div[@class='_281ql']//div)[1]")
    public  WebElement PTLocationList;
    @FindBy(xpath = "//div[@class='_1xUOn']//span[@id='departureDate']")
    public WebElement PTDate;
    @FindBy(xpath = "//button[@id='flightSearch']")
    public WebElement PTSrchFlights;
    @FindBy(xpath ="(//label/i[@class='ar-xN'])[1]")
    public WebElement PTNonStopCheckBox;

    public void enterFromToDetails(String fromLoc,String toLoc) throws InterruptedException {
        CTFromLocation.click();
        CTFromLocation.sendKeys(fromLoc);
        Thread.sleep(2000);
        CTFromLocationList.click();
        CTToLocation.click();
        CTToLocation.sendKeys(toLoc);
        Thread.sleep(2000);
        CTToLocationList.click();
    }
    String CT="clear trip";
    String PT="paytm";
    public void enterJourneyDate(String dateOfJourney, String portal) throws InterruptedException, ParseException {
       if(portal.equals(CT)){
           CTselectDate.click();
       }
       if(portal.equals(PT)) {
           PTDate.click();
           Thread.sleep(2000);
       }
        Date d = new Date(dateOfJourney);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        String date = formatter.format(d);
        String splitter[] = date.split("-");
        String month_year = splitter[1] + " " + splitter[2];
        String day = splitter[0];
        String givendate = dateOfJourney;
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
        Date date1 = formatter1.parse(givendate);
        Date currentDate = new Date();
        LocalDate currentLocalDate = LocalDate.now();
        String currentLocalDate1 = givendate.replace("/","-");
        LocalDateTime dateAfterOneYear = LocalDateTime.now().plusYears(1);
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dates2 = LocalDate.parse(givendate, formatter2);
        String[] splitter1 = givendate.split("/");
        Integer y = Integer.parseInt(splitter1[0]);
        Integer m = Integer.parseInt(splitter1[1]);
        Integer dt = Integer.parseInt(splitter1[2]);
        if(!isValidDate(dt,m,y)){
            driver.close();
            Assume.assumeFalse("Please provide valid date",true);
        }
        if(currentLocalDate.toString().equals(currentLocalDate1)){
            selectDate(month_year, day,portal);
        }
        else if (date1.before(currentDate)){
            driver.close();
            Assume.assumeFalse("Please provide valid date. Given date is older than today", true);
        }
        else if (dates2.isAfter(ChronoLocalDate.from(dateAfterOneYear))) {
            driver.close();
            Assume.assumeFalse("Please provide valid date. Given date is beyond one year from today", true);
        }else {
            selectDate(month_year, day,portal);
        }
    }
    static boolean isValidDate(int d,int m, int y){
        if (m < 1 || m > 12)
            return false;
        if (d < 1 || d > 31)
            return false;
        if (m == 2) {
            if (isLeap(y))
                return (d <= 29);
            else
                return (d <= 28);
        }
        if (m == 4 || m == 6 || m == 9 || m == 11)
            return (d <= 30);

        return true;
    }
    public void selectDate(String month_year, String select_day, String portal) throws InterruptedException{
        if(portal.equals(CT)){
        List<WebElement> elements = driver.findElements(By.xpath("(//div[@class='DayPicker-Caption'])//div"));
        for (int i=0; i<elements.size();i++)
        {
            if(elements.get(i).getText().equals(month_year))
            {
                driver.findElement(By.xpath("((//div[@class='DayPicker-Body'])["+(i+1)+"]//div[@aria-label])["+select_day+"]")).click();
                Thread.sleep(2000);
                return;
            }
        }
        driver.findElement(By.xpath("//*[local-name()='svg' and @data-testid = 'rightArrow']")).click();
        Thread.sleep(2000);
        selectDate(month_year,select_day,portal);
        }
        else {
            List<WebElement> elements = driver.findElements(By.xpath("//td[@class='calendar__month _18o18']"));
            for (int i=0; i<elements.size();){
                for(int x=1;x<=2;x++){
                if(driver.findElement(By.xpath("(//td[@class='calendar__month _18o18'])["+(i+1)+"]")).getText().equals(month_year)) {
                    List<WebElement> list= driver.findElements(By.xpath("(//table[@class='calendar'])["+(i+1)+"]/tbody/tr/td/div/div[1]"));
                  for(int j=0;j<list.size();j++) {
                      String value=list.get(j).getText();
                      if(value.equals(select_day)) {
                          driver.findElement(By.xpath("((//table[@class='calendar'])["+(i+1)+"]/tbody/tr/td/div/div[1])["+(j+1)+"]")).click();
                          Thread.sleep(2000);
                          return;
                      }
                  }
                }i++;
                }
                driver.findElement(By.xpath("//i[@class='gA7KZ _3nECU']")).click();
            }
        }
    }

    public void clickSearchButton() {
        CTSrchFlights.click();
    }

    public void selectNonStopFlights() throws InterruptedException {
        CTNonStopCheckBox.click();
        Thread.sleep(2000);
    }

    public void enterFromAndToLocations(String from, String to) throws InterruptedException {
        PTFromLocation.click();
        PTLocClearBtn.click();
        Thread.sleep(2000);
        PTFromLocationIp.sendKeys(from);
        Thread.sleep(2000);
        PTLocationList.click();
        Thread.sleep(2000);
        PTToLocation.click();
        PTLocClearBtn.click();
        Thread.sleep(2000);
        PTToLocationIp.sendKeys(to);
        Thread.sleep(2000);
        PTLocationList.click();
        Thread.sleep(2000);
    }

    public void clickOnSearchFlights() throws InterruptedException {
        PTSrchFlights.click();
        Thread.sleep(3000);
    }
    List<List<String>> listOfLists1 = new ArrayList<>();
    public void CTFlightdata() throws IOException, InterruptedException {
        Thread.sleep(2000);
        JavascriptExecutor js=(JavascriptExecutor)driver;
        js.executeScript("arguments[0].scrollIntoView();",driver.findElement(By.xpath("//ul/li/a[@title='About Us']")));
        List<WebElement> list1=driver.findElements(By.xpath("//*[@data-testid='airlineBlock']"));
        for (int i=1;i<=150;i++) {
            try{
            List<String> l1=new ArrayList<>();
            WebElement S1=  driver.findElement(By.xpath("((//div[@data-testid='airlineBlock'])["+i+"]//div//p)[1]"));
            String value1=S1.getText();
            l1.add(value1);
            WebElement S2=  driver.findElement(By.xpath("((//div[@data-testid='airlineBlock'])["+i+"]//div//p)[2]"));
            String value2=S2.getText();
            l1.add(value2);

            WebElement S3=  driver.findElement(By.xpath("((//div[@data-testid='airlineBlock'])["+i+"]//div//p)[8]"));
            Boolean text= S3.getText().contains("seat");
            if(text){
                WebElement S4=  driver.findElement(By.xpath("((//div[@data-testid='airlineBlock'])["+i+"]//div//p)[9]"));
                String value3=S4.getText().replace("₹","").replace(",","");
                l1.add(value3);
            }else {
                String value3=S3.getText().replace("₹","").replace(",","");
                l1.add(value3);
            }
            listOfLists1.add(l1);
           } catch (Exception ex){
            break;
            }
        }
    }

    public void searchNonStopFlights() throws InterruptedException {
        PTNonStopCheckBox.click();
        Thread.sleep(2000);
    }

    List<List<String>> listOfLists2 = new ArrayList<>();
    public void PTFlightdata() throws InterruptedException {
        Thread.sleep(5000);
        JavascriptExecutor js=(JavascriptExecutor)driver;
        js.executeScript("arguments[0].scrollIntoView();",driver.findElement(By.xpath("//span[text()='Download Paytm App to Pay from anywhere']")));
        List<WebElement> list1=driver.findElements(By.xpath("//section[@id='flightsList']/div"));
        for (int i=1;i<=list1.size();i++) {
            try{
            List<String> l1=new ArrayList<>();
            WebElement S1=  driver.findElement(By.xpath("(//section[@id='flightsList']//div[@class='_5Cbbf'])["+i+"]//div/span[@class='_2cP56']"));
            String value1=S1.getText();
            l1.add(value1);
            driver.findElement(By.xpath("(//section[@id='flightsList']//div[@class='_5Cbbf'])["+i+"]//div[@class='_29dj3']/span[1]")).click();
            WebElement S2=  driver.findElement(By.xpath("(//div[@class='_2ighk _1fnpA'])[1]//div[@class='_3tMEB']/span[2]"));
            String value2=S2.getText();
            l1.add(value2);
            WebElement S4=  driver.findElement(By.xpath("(//section[@id='flightsList']//div[@class='_5Cbbf'])["+i+"]//div/div[@class='_2MkSl']"));
            String value3=S4.getText().replace("₹","").replace(",","");
            l1.add(value3);
            listOfLists2.add(l1);
            } catch (Exception ex){
                break;
            }
        }
    }
    public void comparePrices(String flightOperator, String flightNumber, String priceOnCleartrip, String priceOnPaytm) throws IOException {
        List<List<String>> l3=new ArrayList<>();
        for (int i=0;i<listOfLists1.size();i++) {
            List<String> l4=new ArrayList<>();
            l4.add(listOfLists1.get(i).get(0));
            String x= listOfLists1.get(i).get(1);
            l4.add(x);
            l4.add(listOfLists1.get(i).get(2));
            for(int j=0;j<listOfLists2.size();j++) {
                String y=listOfLists2.get(j).get(1);
                if(x.equals(y)) {
                    l4.add(listOfLists2.get(j).get(2));
                    break;
                }
            }
            l3.add(l4);
        }
        System.out.println("l3:"+l3);
        FileWriter writer = new FileWriter("/Users/potnuru.siva/Downloads/FlightPriceComparator/TestData/Book1.csv");
        List<String> header=Arrays.asList(flightOperator,flightNumber,priceOnCleartrip,priceOnPaytm);
        String collect1 = (String) header.stream().collect(Collectors.joining(","));
        writer.write(collect1);
        writer.append("\n");
        for(List l1:l3){
            String collect2 = (String) l1.stream().collect(Collectors.joining(","));
            writer.write(collect2);
            writer.append("\n");
        }
        writer.close();
    }
}







