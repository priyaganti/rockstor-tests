
import java.io.File;
import java.io.FileInputStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select; // Dropdown menu
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;// Explicit Waits
import org.openqa.selenium.support.ui.WebDriverWait; 
import org.apache.commons.io.FileUtils; // Screenshots
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot; 
import java.io.IOException;
import java.util.Properties;



public class AddPoolRaid0with1Disk {

	public static void main(String[] args) throws IOException {
		// Create a new instance of the Firefox driver
		

		WebDriver driver = new FirefoxDriver();

		try{

			Properties prop = new Properties();
			prop.load(new FileInputStream("config.properties"));
			driver.get(prop.getProperty("RockstorVm"));


			// User Login Input forms
			WebElement username = driver.findElement(By.id("inputUsername"));
			username.sendKeys("admin");

			WebElement password = driver.findElement(By.id("inputPassword"));
			password.sendKeys("admin");

			WebElement submit = driver.findElement(By.id("sign_in"));
			submit.click();
			
			// Select Pools from Navigation bar
			WebElement poolsNav = driver.findElement(By.id("pools_nav"));
			poolsNav.click();

			//Explicit Wait for Pools page to load
			WebElement myWaitElement1 = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(By.id("add_pool")));

			// Click create Pool Button
			WebElement addPool = driver.findElement(By.id("add_pool"));
			addPool.click();

			//Explicit Wait for CreatePools page. 
			WebElement myWaitElement2 = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(By.id("create_pool")));

             //User Input Form to Select Pool Name
			WebElement poolname = driver.findElement(By.id("pool_name"));
			poolname.sendKeys("pool1");

			//Raid Configuration Dropdown box 
			Select raidConfigDroplist = new Select(driver.findElement(By.id("raid_level")));   
			raidConfigDroplist.selectByIndex(0);

			//Select Disks CheckBox
			WebElement diskCheckBox1 = driver.findElement(By.id("sdb"));
			diskCheckBox1.click();
		
			//Create Pool
			WebElement createPool = driver.findElement(By.id("create_pool"));
			createPool.click();
			
			//Wait for Pools page to load
			WebElement myWaitElement3 = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(By.id("delete_pool_pool1")));
		}

		//catch any exceptions by taking screenshots.
		catch(Exception e){

			System.out.println(e.toString());

			File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshotFile,new File("/home/priya/rockstor-tests/webdriver/java/ScreenShots/Raid01DiskPool.png"));

		}

		//click on logout
		WebElement logoutSubmit = driver.findElement(By.id("logout_user"));

		logoutSubmit.click();
		driver.close();
	}
}







