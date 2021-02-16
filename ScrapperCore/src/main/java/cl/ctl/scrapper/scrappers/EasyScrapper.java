package cl.ctl.scrapper.scrappers;

import cl.ctl.scrapper.helpers.CaptchaHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by des01c7 on 16-12-20.
 */
public class EasyScrapper extends AbstractScrapper {

    public EasyScrapper() throws IOException {
        super();
        CADENA = "Easy";
        URL = "https://www.cenconlineb2b.com/auth/realms/cencosud/protocol/openid-connect/auth?response_type=code&client_id=easycl-client-prod&redirect_uri=https%3A%2F%2Fwww.cenconlineb2b.com%2FEasyCL%2FBBRe-commerce%2Fswf%2Fmain.html&state=bad15b30-d2d2-4738-8409-ffaad6602ac6&login=true&scope=openid";
    }

     void login() throws Exception {
        try {
            // *SolveCaptcha
            CaptchaHelper captchaHelper = new CaptchaHelper(driver, URL);
            captchaHelper.solveCaptcha();
            Thread.sleep(2000);
            driver.findElement(By.id("username")).sendKeys("michel.lotissier@legrand.cl");
            driver.findElement(By.id("password")).sendKeys("diy12easy2020");
            driver.getPageSource();
            driver.findElement(By.id("kc-login")).click();

            Thread.sleep(2000);

            // Redirect Home
            redirectHome();

            Thread.sleep(5000);

            // Select country
            selectCountry();

            Thread.sleep(2000);

        }
        catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    private void redirectHome() {
        try {
            driver.get("https://www.cenconlineb2b.com/");
        }
        catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    private void selectCountry() throws InterruptedException {
        try {
            Select pais = new Select(driver.findElement(By.id("pais")));
            Thread.sleep(3000);
            pais.selectByValue("chi");
            Thread.sleep(2000);
            driver.findElement(By.id("btnIngresar")).click();
        }
        catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    private void closeTab() throws InterruptedException {
        try {
            driver.findElement(By.xpath("//span[@class='v-tabsheet-caption-close']")).click();
            Thread.sleep(5000);
        }
        catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    void doScrap(int startDay, int endDay, int count) throws InterruptedException {

            // GoTo Comercial
            int cont = 0;

            while(cont < 10) {

                cont++;

                try {
                    WebElement menuCommerce = driver.findElement(By.xpath("//div[@class='v-menubar v-widget mainMenuBar v-menubar-mainMenuBar v-has-width']")).findElements(By.cssSelector("span:nth-child(3)")).get(0);
                    WebDriverWait wait = new WebDriverWait(driver, 10);
                    wait.until(ExpectedConditions.elementToBeClickable(menuCommerce));

                    Thread.sleep(2000);

                    menuCommerce.click();

                    Thread.sleep(2000);

                    WebElement submenuCommerce = driver.findElement(By.xpath("//div[@class='v-menubar-submenu v-widget mainMenuBar v-menubar-submenu-mainMenuBar v-has-width']")).findElements(By.cssSelector("span:nth-child(1)")).get(0);
                    wait = new WebDriverWait(driver, 10);
                    wait.until(ExpectedConditions.elementToBeClickable(submenuCommerce));

                    Thread.sleep(2000);

                    submenuCommerce.click();

                    break;
                }
                catch(Exception e) {
                    if(cont >= 10) {
                        logger.log(Level.SEVERE, e.getMessage());
                        throw e;
                    }
                }

            }

            Thread.sleep(3000);

            Actions actions;

            String check1Id = "";
            String check2Id = "";

            switch(count) {
                case 1:
                    check1Id = "gwt-uid-8";
                    check2Id = "gwt-uid-9";
                    break;
                case 2:
                    check1Id = "gwt-uid-25";
                    check2Id = "gwt-uid-26";
                    break;
                case 3:
                    check1Id = "gwt-uid-42";
                    check2Id = "gwt-uid-43";
                    break;
            }

            cont = 0;

            // *SelectParameters
            while(cont < 10) {

                cont++;

                try {
                    WebElement checkDisplayStock = driver.findElement(By.xpath("//input[@id='" + check1Id + "']"));
                    actions = new Actions(driver);
                    actions.moveToElement(checkDisplayStock).click().build().perform();

                    Thread.sleep(1000);

                    WebElement excludeProductsWithoutStock = driver.findElement(By.xpath("//input[@id='" + check2Id + "']"));
                    actions = new Actions(driver);
                    actions.moveToElement(excludeProductsWithoutStock).click().build().perform();

                    Thread.sleep(1000);

                    WebElement sinceCalendar = driver.findElements(By.xpath("//button[@class='v-datefield-button']")).get(0);

                    sinceCalendar.click();

                    Thread.sleep(1000);

                    WebElement day = driver.findElements(By.xpath("//span[text()='" + startDay + "']")).get(0);
                    actions = new Actions(driver);
                    actions.moveToElement(day).click().build().perform();

                    WebElement toCalendar = driver.findElements(By.xpath("//button[@class='v-datefield-button']")).get(1);

                    toCalendar.click();

                    Thread.sleep(1000);

                    day = driver.findElements(By.xpath("//span[text()='" + endDay + "']")).get(0);
                    actions = new Actions(driver);
                    actions.moveToElement(day).click().build().perform();

                    break;
                }
                catch (Throwable e) {
                    e.printStackTrace();
                    if(cont >= 10) {
                        logger.log(Level.SEVERE, e.getMessage());
                        throw e;
                    }
                }

            }

            Thread.sleep(10000);

            // GenerateFile

            cont = 0;

            while(true) {

                cont++;

                try {
                    WebElement generateReport = driver.findElement(By.xpath("//div[@class='v-button v-widget btn-filter-search v-button-btn-filter-search']"));
                    actions = new Actions(driver);
                    actions.moveToElement(generateReport).click().build().perform();

                    Thread.sleep(20000);

                    WebElement downloadReportMenu = driver.findElement(By.xpath("//div[@class='v-button v-widget toolbar-button v-button-toolbar-button bbr-popupbutton']"));
                    actions = new Actions(driver);
                    actions.moveToElement(downloadReportMenu).click().build().perform();

                    Thread.sleep(2000);

                    WebElement downloadReportOption = driver.findElement(By.xpath("//div[@class='v-verticallayout v-layout v-vertical v-widget v-has-width v-margin-right v-margin-left']")).findElements(By.cssSelector("div:nth-child(2)")).get(0);
                    actions = new Actions(driver);
                    actions.moveToElement(downloadReportOption).click().build().perform();

                    Thread.sleep(2000);

                    WebElement downloadReportButton = driver.findElement(By.xpath("//div[@class='v-button v-widget primary v-button-primary btn-generic v-button-btn-generic v-has-width']"));
                    actions = new Actions(driver);
                    actions.moveToElement(downloadReportButton).click().build().perform();

                    Thread.sleep(30000);

                    WebElement downloadReportLink = driver.findElement(By.xpath("//div[@class='v-horizontallayout v-layout v-horizontal v-widget']")).findElements(By.xpath("//div[@class='v-slot']")).get(0).findElements(By.xpath("//div[@class='v-link v-widget']")).get(0);
                    actions = new Actions(driver);
                    actions.moveToElement(downloadReportLink).click().build().perform();

                    break;
                }
                catch (Throwable e) {
                    e.printStackTrace();
                    if(cont >= 10) {
                        logger.log(Level.SEVERE, e.getMessage());
                        throw e;
                    }
                }
            }

        Thread.sleep(2000);
        // Cerrar Tab
        closeTab();
    }
}
