package com.rockstor.test.webdriver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.apache.commons.io.FileUtils; // Screenshots
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot; 
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select; // Dropdown menu
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.List;
import com.rockstor.test.util.RSProps;

public class PoolRaid0ShareSnapshot {

	private static WebDriver driver;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(
				Integer.parseInt(RSProps.getProperty("waitTimeout")), 
				TimeUnit.SECONDS);	
	}

	@Test
	public void testShareSnapshot() throws Exception {
		try{

			driver.get(RSProps.getProperty("RockstorVm"));

			// Login 
			WebElement username = driver.findElement(By.id("inputUsername"));
			username.sendKeys("admin");

			WebElement password = driver.findElement(By.id("inputPassword"));
			password.sendKeys("admin");

			WebElement submit = driver.findElement(By.id("sign_in"));
			submit.click();

			// Add Pool with Raid 0
			WebElement poolsNav = driver.findElement(By.id("pools_nav"));
			poolsNav.click();

			WebElement addPool = driver.findElement(By.id("add_pool"));
			addPool.click();

			WebElement poolname = driver.findElement(By.id("pool_name"));
			poolname.sendKeys("pool1");

			// Raid Configuration Dropdown box 
			Select raidConfigDroplist = new Select(driver.findElement(
					By.id("raid_level")));   
			raidConfigDroplist.selectByIndex(0);

			//Select Disks CheckBox
			WebElement diskCheckBox1 = driver.findElement(By.id("sdb"));
			diskCheckBox1.click();
			WebElement diskCheckBox2 = driver.findElement(By.id("sdc"));
			diskCheckBox2.click();

			// Create Pool
			WebElement createPool = driver.findElement(By.id("create_pool"));
			createPool.click();

			/// wait and check for pool1 to appear
			WebElement poolLink = driver.findElement(By.linkText("pool1"));
			poolLink.click();
			///// Create a share

			//Shares navigation bar
			WebElement sharenav = driver.findElement(By.id("shares_nav"));
			sharenav.click();

			//Add share
			WebElement addShareButton = driver.findElement(By.id("add_share"));
			addShareButton.click();

			WebElement shareName = driver.findElement(By.id("share_name"));
			shareName.sendKeys("share1");

			Select selectPoolDroplist = new Select(driver.findElement(
					By.id("pool_name")));   
			selectPoolDroplist.selectByIndex(0); 

			WebElement shareSize = driver.findElement(By.id("share_size"));
			shareSize.sendKeys("100"); 

			Select selectSizeDroplist = new Select(driver.findElement(
					By.id("size_format")));   
			selectSizeDroplist.selectByIndex(0);//Index 0 is KB

			// Submit button to create share
			WebElement shareSubmitButton = driver.findElement(
					By.id("create_share"));
			shareSubmitButton.click();

			//Shares Detail page
			WebElement shareLink = driver.findElement(By.linkText("share1"));
			shareLink.click();

			/// Snapshot
			WebElement snapshotButton = driver.findElement(
					By.id("js-snapshot-popup"));
			snapshotButton.click();

			//Snapshot name input form
			WebElement snapshotName = driver.findElement(By.id("snapshot-name"));
			snapshotName.sendKeys("snapshot1");

			// Create snapshot
			WebElement snapshotSubmitButton = driver.findElement(
					By.id("create-snapshot"));
			snapshotSubmitButton.click();

			// test  if the row got created
			List<WebElement> newSnapshotRow = driver.findElements(
					By.xpath("//*[@id='snapshots-table']/tbody/tr[td[contains(.,'snapshot1')]]"));
			assertEquals(newSnapshotRow.size(), 1);


			// Delete snapshot
			WebElement snapshotRow = driver.findElement(
					By.xpath("//*[@id='snapshots-table']/tbody/tr[td[contains(.,'snapshot1')]]"));
			WebElement deleteSnapshot = snapshotRow.findElement(
					By.xpath("td/button[contains(@data-name,'snapshot1') and contains(@data-action,'delete')]"));
			deleteSnapshot.click();
						
			//Browser Popup asking confirmation to delete 
			Alert alertDeleteSnapshot = driver.switchTo().alert();
			alertDeleteSnapshot.accept();


			// check for the text present after delete snapshots
			WebElement lookForText = driver.findElement(
					By.xpath("//div/*[@id='snapshots-table']/tbody/tr/td[h4/text()='No snapshots have been created']"));
			assertTrue(lookForText.getText(), true);


			// Delete Share

			WebElement sharesNav = driver.findElement(By.id("shares_nav"));
			sharesNav.click();

			WebElement shareRow = driver.findElement(
					By.xpath("//*[@id='shares-table']/tbody/tr[td[contains(.,'share1')]]"));
			WebElement deleteShare = shareRow.findElement(
					By.xpath("td/button[contains(@data-name,'share1') and contains(@data-action,'delete')]"));
			deleteShare.click();
			
			//Browser Popup asking confirmation to delete 
			Alert alertDeleteShare = driver.switchTo().alert();
			alertDeleteShare.accept();
			
			// Delete Pool
			poolsNav = driver.findElement(By.id("pools_nav"));
			poolsNav.click();

			WebElement poolRow = driver.findElement(
					By.xpath("//*[@id='pools-table']/tbody/tr[td[contains(.,'pool1')]]"));
			WebElement deletePool = poolRow.findElement(
					By.xpath("td/button[contains(@data-name,'pool1') and contains(@data-action,'delete')]"));
			deletePool.click();
						
			//Browser Popup asking confirmation to delete 
			Alert alertDeletePool = driver.switchTo().alert();
			alertDeletePool.accept();

			// Logout 
			WebElement logoutSubmit = driver.findElement(
					By.id("logout_user"));

			logoutSubmit.click();

		}
		catch(Exception e){
			File screenshotFile = ((TakesScreenshot)driver)
					.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshotFile,
					new File(RSProps.getProperty("screenshotDir") 
							+ "/" + this.getClass().getName()+".png"));
			throw e;

		}

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		driver.quit();
	}

}

