package amazon;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseClass {
	
	WebDriver driver;
	
	public BaseClass(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@FindBy(id="twotabsearchtextbox")
	WebElement searchBox;
	
	@FindBy(id="nav-search-submit-button")
	WebElement searchBtn;
	
	@FindBy(xpath="//div[@id='brandsRefinements']//li[@aria-label='Titan']//div[contains(@class,'checkbox')]")
	WebElement titanBrand;
	
	@FindBy(xpath="//ul[@aria-labelledby='p_n_pct-off-with-tax-title']//li//span[contains(text(),'25% Off or more')]")
	WebElement discount;
	
	@FindBy(xpath="//ul[@aria-labelledby='p_n_feature_seven_browse-bin-title']//li[@aria-label='Analogue']//label/i")
	WebElement dialAnalogue;
	
	@FindBy(xpath="//ul[@aria-labelledby='p_n_material_browse-title']//li[@aria-label='Leather']//label/i")
	WebElement materialLeather;
	
	@FindBy(xpath="//div[@class='nav-logo-base nav-sprite']")
	WebElement baseLogo;
	
	@FindBy(xpath="//span[contains(@class,'savingsPercentage')]")
	WebElement offerPercentage;
	
	@FindBy(xpath="//div[@id='corePrice_feature_div']//span[@class='a-offscreen']'")
	WebElement sellingPrice;
	
	@FindBy(xpath="//div[@id='corePrice_feature_div']//span[@class='a-offscreen']'")
	WebElement mrp;
	
	public void search(String searchItem) {
		searchBox.sendKeys(searchItem);
		searchBtn.click();
		waitForPageLoadToComplete();
	}
	
	public void searchByDiscount() {
		waitForElementToBeClickable(discount);
		discount.click();
		waitForPageLoadToComplete();
	}
	
	public void searchByMaterialType() {
		waitForElementToBeClickable(materialLeather);
		materialLeather.click();
		waitForPageLoadToComplete();
	}
	
	public void searchByBrand() {
		waitForElementToBeClickable(titanBrand);
		titanBrand.click();
		waitForPageLoadToComplete();
	}
	
	public void searchByDialType() {
		waitForElementToBeClickable(dialAnalogue);
		dialAnalogue.click();
		waitForPageLoadToComplete();
	}
	
	public HashMap<String,String> getNthItem(int itemNumber) {
		String parentWindow=driver.getWindowHandle();
		String searchResultPath="//div[contains(@class,'widgetId=search-results_" +itemNumber+"')]";
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.findElement(By.xpath(searchResultPath)).click();
		Set<String> windows=driver.getWindowHandles();
		Iterator<String> i=windows.iterator();
		while(i.hasNext()) {
			String childWindow=i.next();
			driver.switchTo().window(childWindow);
		}
		HashMap<String,String> searchItemDetails=getItemDetails();
		driver.close();
		driver.switchTo().window(parentWindow);
		return searchItemDetails;
	}

	public HashMap<String,String> getItemDetails() {
		List<WebElement> tableContent=driver.findElements(By.xpath("//table[@id='technicalSpecifications_section_1']/tbody/tr"));
		int tableSize=tableContent.size();
		HashMap<String,String> itemDetails=new HashMap<String,String>();
		String offer=offerPercentage.getText().substring(1,3);
		itemDetails.put("discount", offer);
		itemDetails.put("bandType", null);
		itemDetails.put("brand", null);
		itemDetails.put("display", null);
		for(int i=1;i<=tableSize;i++) {	
			String materialPath="//table[@id='technicalSpecifications_section_1']/tbody/tr["+i+"]/th";
			String typePath="//table[@id='technicalSpecifications_section_1']/tbody/tr["+i+"]/td";
			String contentValue=driver.findElement(By.xpath(materialPath)).getText();
			if(contentValue.equalsIgnoreCase("Band Material") && itemDetails.get("bandType")==null)
				itemDetails.put("bandType",driver.findElement(By.xpath(typePath)).getText());
			else if(contentValue.equalsIgnoreCase("Brand") && itemDetails.get("brand")==null)
				itemDetails.put("brand",driver.findElement(By.xpath(typePath)).getText()); 
			else if(contentValue.equalsIgnoreCase("Display Type") && itemDetails.get("display")==null)
				itemDetails.put("display",driver.findElement(By.xpath(typePath)).getText());
		}
		System.out.println(itemDetails);
		return itemDetails;
	}


	public void waitForPageLoadToComplete() {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(20));
		wait.until(ExpectedConditions.elementToBeClickable(baseLogo));
	}
	
	public void waitForElementToBeClickable(WebElement element) {
		WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public void searchWatch(String keyWord) {
		  search(keyWord);
		  searchByDialType();
		  searchByMaterialType();
		  searchByBrand();
		  searchByDiscount();	
	}

}
