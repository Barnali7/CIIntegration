package com.integration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ExtentReportWithLatestVersion {
	public WebDriver driver;
	public ExtentReports extent; //create entries in ur test case who executed testcase browser name
	public ExtentHtmlReporter htmlReporter; // look aND feel of your report
	public ExtentTest test;
	
	@BeforeTest
	public void setExtent() {
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")+"/test-output/myReport.html");
		htmlReporter.config().setDocumentTitle("Automation Report"); // Title of report
		htmlReporter.config().setReportName("Functional Report"); // Name of report
		htmlReporter.config().setTheme(Theme.DARK);
		
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Hostname", "LocalHost");
		extent.setSystemInfo("OS", "Windows7");
		extent.setSystemInfo("Tester Name", "Barnali");
		extent.setSystemInfo("Browser", "Chrome");


	}
   @AfterTest
   public void endReport() {
	   extent.flush();
   }
   @BeforeMethod
   public void setUp() {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://demo.nopcommerce.com/");
   }
   
   @Test
   public void noCommerceTitleTest() {
	    test = extent.createTest("noCommerceTitleTest"); //create a new entry for every test  in your report
	    
	    String title = driver.getTitle();
	    System.out.println(title);
	    Assert.assertEquals(title, "noCommerce demo store");
   }
   @Test
   public void noCommerceLogoTest() {
	   test = extent.createTest("noCommerceLogoTest");
	   Boolean status = driver.findElement(By.xpath("//img[@alt= 'nopCommerce demo store']")).isDisplayed();
	   Assert.assertTrue(status);
	   
   }
   
   @Test
   public void noCommerceLoginTest() {
	   test = extent.createTest("noCommerceLoginTest");
	   test.createNode("Login with Valid Input");//sub node of main node
	   Assert.assertTrue(true);

	   test.createNode("Login with InValid Input");
	   Assert.assertTrue(true);
	   
   }
   
   @AfterMethod
   public void tearDown(ITestResult result) throws IOException {  //itestresult will store status of test which just execute and after this will send status to report
	   if(result.getStatus()  == ITestResult.FAILURE) {
		   test.log(Status.FAIL, "TEST CASE FAILED IS" + result.getName());
		   test.log(Status.FAIL, "TEST CASE FAILED IS" + result.getThrowable()); //to send log to report throw all the exception to report
		  String screenshotPath = ExtentReportWithLatestVersion.getScreenshot(driver, result.getName());
		  
		   test.addScreenCaptureFromPath(screenshotPath);
		   
	   }else if (result.getStatus() == ITestResult.SKIP) {
		   test.log(Status.SKIP, "TEST CASE SKIPPED IS" + result.getName()); 
	   }
	   else if(result.getStatus() == ITestResult.SUCCESS) {
		   test.log(Status.PASS, "TEST CASE SKIPPED IS" + result.getName());
	   }
	   
   }
   
   public static String getScreenshot(WebDriver driver,String screenshotName) throws IOException {
	   String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());// timestamp 
	   TakesScreenshot ts = (TakesScreenshot)driver;
	   File source = ts.getScreenshotAs(OutputType.FILE);
	   //after execution,you could see a folder "FailedtestScreenshots" under src
	   String destination = System.getProperty("user.dir")+"/Screenshots/" + screenshotName +dateName + ".png";
	   File finalDestination = new File(destination);
	   FileUtils.copyFile(source, finalDestination);
	   return destination;
   }
}
