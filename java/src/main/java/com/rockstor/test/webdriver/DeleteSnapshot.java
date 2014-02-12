package com.rockstor.test.webdriver;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.rockstor.test.util.RSProps;
public class DeleteSnapshot {

	private static WebDriver driver;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		driver = new FirefoxDriver();
	}

	@Test
	public void testDelSnapshot() throws Exception {
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

			// Select shares from storage side bar
			WebElement sharesNav = driver.findElement(By.xpath("//div[@id='sidebar-inner']/ul/li/a[contains(@href,'shares')]"));
			sharesNav.click();

			 WebElement myWaitElement = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(By.id("add_share")));

			// Share link
			WebElement shareLink = driver.findElement(By.linkText("share1"));
			shareLink.click();

			// wait for share details page to load
			 myWaitElement = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(By.id("js-delete")));

			//Select Snapshot from navigation
			WebElement snapNav = driver.findElement(By.xpath("//div/ul/li/a[contains(text(), 'Snapshots')]"));
			snapNav.click();

			
			// Wait for snapshot page to load
			myWaitElement = (new WebDriverWait(driver, 150))
					.until(ExpectedConditions.elementToBeClickable(By.id("js-snapshot-add")));
			
			// searching for partial text of snapshot
			WebElement delSnapRow = driver.findElement(
					By.xpath("//*[@id='snapshots-table']/tbody/tr[td[contains(text(),' snap')]]"));
			WebElement delSnap = delSnapRow.findElement(By.xpath("td/a/i[contains(@class,'icon-trash')]"));
			delSnap.click();
			
			//Popup window
			Alert alertDeleteSnap = driver.switchTo().alert();
			alertDeleteSnap.accept();
			
			
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
		driver.quit();
	}
}


