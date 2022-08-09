package amazon;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class TestClass {
	
WebDriver driver;
BaseClass baseClass;

  @BeforeTest
  public void openBrowser() {
	  WebDriverManager.chromedriver().setup();
	  driver=new ChromeDriver();
//	  WebDriverManager.firefoxdriver().setup();
//	  driver=new FirefoxDriver();
	  driver.manage().window().maximize();
	  driver.get("https://amazon.in");
	  baseClass=new BaseClass(driver);
  }
  
  @Test
  public void validateWatchSearch() {
	  baseClass.searchWatch("wrist watches");
	  HashMap<String,String>itemDetails=new HashMap<String,String>();
	  //validate 7th item
	  itemDetails=baseClass.getNthItem(7);
	  Assert.assertTrue(assertSearchResults(itemDetails));
	  
	//validate 10th item
	  itemDetails=baseClass.getNthItem(10);
	  Assert.assertTrue(assertSearchResults(itemDetails));

	  //validate 15th item
	  itemDetails=baseClass.getNthItem(15);
	  Assert.assertTrue(assertSearchResults(itemDetails));
	  
  }
  
  public boolean assertSearchResults(HashMap<String,String> itemDetails) {
	  Assert.assertTrue(itemDetails.get("bandType").equalsIgnoreCase("Leather"),"Band Material is not Leather");
	  Assert.assertTrue(itemDetails.get("brand").equalsIgnoreCase("Titan"),"Band Material is not Titan");
	  Assert.assertTrue(itemDetails.get("display").equalsIgnoreCase("Analog"),"Display type is not Analogue");
	  Assert.assertTrue(Integer.parseInt(itemDetails.get("discount"))>25);
	  return true;
  }

  @AfterTest
  public void closeBrowser() {
	  driver.close();
	  driver.quit();
  }

}
