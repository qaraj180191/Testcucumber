package cucumberJava.StepDefinition;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.junit.runner.Computer;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;


public class annotation { 
   WebDriver driver = null; 
	
   @Given("I have open the {string}browser")
   public void openBrowser(String arg0) {
      switch (arg0)
      {
         case "FireFox":
         {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            break;
         }
         case "Chrome":
         {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            break;
         }
      }

   } 
	
   @When("^I open Computer database website$")
   public void goToFacebook() { 
      driver.navigate().to("http://computer-database.gatling.io/computers");
   } 
	
   @Then("^Verify Landing page with Add a new Computer button is getting displayed$")
   public void loginButton() {
      Assert.assertTrue(driver.findElement(By.xpath("//*[contains(text(),'computers found')]")).isDisplayed());
      Assert.assertTrue(driver.findElement(By.id("add")).isDisplayed());

   }

   @Then("Add a new computer")
   public void add_a_new_computer(io.cucumber.datatable.DataTable dataTable) {
      List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
      driver.findElement(By.id("add")).click();

      driver.findElement(By.id("name")).sendKeys(rows.get(0).get("Name"));
      driver.findElement(By.id("introduced")).sendKeys(convertDate("currentDate-365","yyyy-MM-dd"));
      Select s1 = new Select(driver.findElement(By.id("company")));
      s1.selectByVisibleText(rows.get(0).get("Brand"));
      driver.findElement(By.xpath("//*[@value='Create this computer']")).click();
      WebDriverWait wait = new WebDriverWait(driver,30);
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("warning")));
      Assert.assertTrue(driver.findElement(By.className("warning")).getText().contains("Computer "+rows.get(0).get("Name")+" has been created"));

   }
   @Then("Search a computer")
   public void verify_the_computer_is_added_with_below_details(io.cucumber.datatable.DataTable dataTable) {
      List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

      driver.findElement(By.id("searchbox")).sendKeys(rows.get(0).get("Name"));
      driver.findElement(By.id("searchsubmit")).click();
      Assert.assertTrue(driver.findElement(By.xpath("//a[text()='"+rows.get(0).get("Name")+"']")).isDisplayed());

   }



   @Then("Update a Computer")
   public void update_a_new_computer(io.cucumber.datatable.DataTable dataTable) {
      List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
      driver.findElement(By.xpath("//a[text()='"+rows.get(0).get("Name")+"']")).click();


      driver.findElement(By.id("discontinued")).sendKeys(convertDate("currentDate+365","yyyy-MM-dd"));

      driver.findElement(By.xpath("//*[@value='Save this computer']")).click();
      WebDriverWait wait = new WebDriverWait(driver,30);
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("warning")));
      Assert.assertTrue(driver.findElement(By.className("warning")).getText().contains("Computer "+rows.get(0).get("Name")+" has been updated"));

   }

   public String convertDate(String date, String... dateFormat) {
      date = date.replace("currentDate", "");
      Calendar c = Calendar.getInstance();
      c.setTime(new Date());
      int intDay = date.length() > 0 ? Integer.parseInt(date.charAt(0) + date.substring(1)) : 0;
      String format = dateFormat != null ? dateFormat[0] : "yyyy-MM-dd";
      c.add(Calendar.DATE, intDay);
      date = new SimpleDateFormat(format).format(c.getTime());
      return date;
   }
   @After()
   public void tearDown(Scenario scenario) {
      if (scenario.isFailed()) {
         final byte[] screenshot = ((TakesScreenshot) driver)
                 .getScreenshotAs(OutputType.BYTES);
         scenario.attach(screenshot, "image/png", "Error Screen");


      }
      driver.quit();
   }
}
