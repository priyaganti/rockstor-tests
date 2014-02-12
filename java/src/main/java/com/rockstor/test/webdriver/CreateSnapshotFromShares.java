package com.rockstor.test.webdriver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.io.File;
import java.io.FileInputStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
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

import com.rockstor.test.util.RSProps;

import java.io.IOException;
import java.util.List;
import java.util.Properties;


public class CreateSnapshotFromShares {

	private static WebDriver driver;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		driver = new FirefoxDriver();
	}

	@Test
	public void testUserNoWebUI() throws Exception {
		try{

			driver.get(RSProps.getProperty("RockstorVm"));

			//User Login Input Forms
			WebElement username = driver.findElement(
					By.id("username"));
			username.sendKeys("admin");

			WebElement password = driver.findElement(
					By.id("password"));
			password.sendKeys("admin");

			WebElement submitButton = driver.findElement(
					By.id("sign_in"));
			submitButton.click();

			// Select Storage from Navigation bar
			WebElement systemNav = driver.findElement(By.id("storage_nav"));
			systemNav.click();


			// Select pools from storage side bar
			WebElement poolNav = driver.findElement(By.xpath("//div[@id='sidebar-inner']/ul/li/a[contains(@href,'pools')]"));
			poolNav.click();

			//Explicit Wait for Create Pool button to load
			WebElement myWaitElement = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(
							By.id("add_pool")));

			WebElement addPool = driver.findElement(By.id("add_pool"));
			addPool.click();

			//Explicit Wait for Create Pool page. 
			myWaitElement = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(
							By.id("create_pool")));

			///Fill up form for pool creation

			//Pool Name
			WebElement poolname = driver.findElement(By.id("pool_name"));
			poolname.sendKeys("newtestpool");

			//Raid Configuration Dropdown box 
			Select raidConfigDroplist = new Select(
					driver.findElement(By.id("raid_level")));   
			raidConfigDroplist.selectByIndex(1);

			//Select Disks CheckBox
			WebElement diskCheckBox1 = driver.findElement(By.id("xvdb"));
			diskCheckBox1.click();
			WebElement diskCheckBox2 = driver.findElement(By.id("xvdc"));
			diskCheckBox2.click();

			//Create Pool
			WebElement createPool = driver.findElement(By.id("create_pool"));
			createPool.click();


			myWaitElement = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(By.id("add_pool")));

			//verify that pool is created
			WebElement verifyPoolCreated = driver.findElement(
					By.linkText("newtestpool"));
			assertTrue(verifyPoolCreated.getText(),true);

			// Select shares from storage side bar
			WebElement sharesNav = driver.findElement(By.xpath("//div[@id='sidebar-inner']/ul/li/a[contains(@href,'shares')]"));
			sharesNav.click();

			myWaitElement = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(By.id("add_share")));

			// Create share Button
			WebElement createShare = driver.findElement(By.id("add_share"));
			createShare.click();


			myWaitElement = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(By.id("create_share")));

			/// Fillup form for Share Creation

			//Share Name
			WebElement shareName = driver.findElement(By.id("share_name"));
			shareName.sendKeys("newshare");

			//Select Pool
			Select selectPoolDroplist = new Select(driver.findElement(By.id("pool_name")));   
			selectPoolDroplist.selectByValue("newtestpool"); 

			//Share Size
			WebElement shareSize = driver.findElement(By.id("share_size"));
			shareSize.sendKeys("1000"); 

			//Select Share Size
			Select selectSizeDroplist = new Select(driver.findElement(By.id("size_format")));   
			selectSizeDroplist.selectByIndex(0);//Index 0 is KB


			//Submit button to create share
			WebElement shareSubmitButton = driver.findElement(By.id("create_share"));
			shareSubmitButton.click();


			// Wait for shares page to load up
			myWaitElement = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(By.id("add_share")));

			//verify that share is created
			WebElement verifyShareCreated = driver.findElement(
					By.linkText("newshare"));
			assertTrue(verifyShareCreated.getText(),true);


			// Share link
			WebElement shareLink = driver.findElement(By.linkText("newshare"));
			shareLink.click();

			// wait for newshare page to load
			myWaitElement = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(By.id("js-delete")));

			//Select Snapshot from navigation

			WebElement snapNav = driver.findElement(By.xpath("//div/ul/li/a[contains(text(), 'Snapshots')]"));
			snapNav.click();

			// Wait for snapshot page to load
			myWaitElement = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(By.id("js-snapshot-add")));

			// Create snapshot			
			WebElement snapshotButton = driver.findElement(By.id("js-snapshot-add"));
			snapshotButton.click();

			///Fill up Snapshot form

			//Snapshot Name
			WebElement snapshotName = driver.findElement(By.id("snapshot-name"));
			snapshotName.sendKeys("newsnap");

			// Check box to make visible for users
			WebElement makeVisible = driver.findElement(By.cssSelector("input[id='snapshot-visible']"));
			makeVisible.click();

			// Submit button
			WebElement createSnapshot = driver.findElement(By.id("js-snapshot-save"));
			createSnapshot.click();

			// Wait for snapshot page to load
			myWaitElement = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(By.id("js-snapshot-add")));

			//verify that snapshot is created
			List <WebElement> verifySnapCreated = driver.findElements(
					By.xpath("//*[@id='snapshots-table']/tbody/tr[td[contains(text(),'newsnap')]]"));
			assertEquals(verifySnapCreated.size(), 1);


			//Logout
			WebElement logoutSubmit = driver.findElement(
					By.id("logout_user"));

			logoutSubmit.click();


		}
		//catch any exceptions by taking screenshots
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
		//driver.quit();
	}
}


















