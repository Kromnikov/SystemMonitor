package dao.netcracker.testdao.selenium;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Created by ANTON on 16.04.2016.
 */
public class  WebTest {

    static WebDriver driver;
    @BeforeClass
    public static void setUpClassAndLogin() throws Exception {
        driver = new FirefoxDriver();
        driver.get("http://localhost:8080/login");
        Thread.sleep(100);  // Let the user actually see something!
        WebElement loginInput = driver.findElement(By.name("username"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement confirmButton = driver.findElement(By.name("confirm"));
        loginInput.sendKeys("admin");
        passwordInput.sendKeys("admin");
        confirmButton.submit();
    }

   /* @Test
    public  void testLogin() throws InterruptedException {

        driver.get("http://localhost:8080/login");
        Thread.sleep(1000);  // Let the user actually see something!
        WebElement loginInput = driver.findElement(By.name("username"));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement confirmButton = driver.findElement(By.name("confirm"));
        loginInput.sendKeys("admin");
        passwordInput.sendKeys("admin");
        confirmButton.submit();
        Thread.sleep(1000);  // Let the user actually see something!
        System.out.println("Page title is: " + driver.getTitle());
    }*/

    @Test
    public void testPage() {
        driver.get("http://localhost:8080/problems");
        driver.get("http://localhost:8080/addHostPage");
        driver.get("http://localhost:8080/hostedit");
        driver.get("http://localhost:8080/templMetrics");
        driver.get("http://localhost:8080/accounts");
        driver.get("http://localhost:8080/alarms");
    }

   /* @Test
    public void testButtons(){
        driver.get("http://localhost:8080");
        WebElement href = driver.findElement(new By.ByLinkText("hosts"));
        href.click();
        assert (driver.getCurrentUrl().equals("http://localhost:8080/hosts"));
    }*/

    @Test
    public void testAddHost() throws InterruptedException {
        driver.get("http://localhost:8080/addHostPage");
        WebElement hostNameInput = driver.findElement(By.name("hostName"));
        WebElement hostIPInput = driver.findElement(By.name("hostIP"));
        WebElement hostPortInput = driver.findElement(By.name("port"));
        WebElement hostLoginInput = driver.findElement(By.name("login"));
        WebElement hostPasswordInput = driver.findElement(By.name("password"));
        WebElement hostLoactionInput = driver.findElement(By.name("location"));
        WebElement savehostButton = driver.findElement(By.name("saveHost"));
        hostNameInput.sendKeys("testSelenium");
        hostIPInput.sendKeys("111.111.111.111");
        hostPortInput.sendKeys("11");
        hostLoginInput.sendKeys("Selenium");
        hostPasswordInput.sendKeys("Selenium");
        hostLoactionInput.sendKeys("Selenium valleys");
        savehostButton.submit();
        assert (driver.getCurrentUrl().equals("http://localhost:8080/hosts"));
    }

    @Test
    public void testLogOut() throws InterruptedException {
        driver.get("http://localhost:8080/");
        WebElement logoutButton = driver.findElement(By.name("logout"));
        logoutButton.click();
        assert (driver.getCurrentUrl().equals("http://localhost:8080/login?logout"));
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
        driver.quit();
    }
}
