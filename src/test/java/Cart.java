import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Cart {

    private String itemUrl = "https://www.dns-shop.ru/product/d8257b4df35b3330/mojka-vysokogo-davlenia-zubr-master-avd-100/";
    static WebDriver driver;
    @BeforeAll
    public static void setUp()
    {
        WebDriverManager.chromedriver().browserVersion("112").setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1400, 1000));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    @Test
    public void testAddToCart()
    {
        // Добавление в корзину
        driver.get(itemUrl);

        driver.findElement(By.cssSelector(".button-ui.buy-btn.button-ui_brand")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.textToBe(By.cssSelector(".button-ui.buy-btn.button-ui_brand"), "В корзине"));

        //Проверка наличия в корзине
        driver.get("https://www.dns-shop.ru/cart/");

        WebElement element = driver.findElement(By.xpath("//div[@id='cart-page-new']/div/div/div/div"));
        String textElement = element.getText();
        String message = String.format("В корзине неверное количество товаров");

        Assertions.assertEquals("1 товар", textElement, message);

        //Очистка корзины
        driver.findElement(By.cssSelector(".remove-button__title")).click();
    }

    @Test
    public void testRemoveFromCart()
    {
        // Добавление в корзину
        driver.get(itemUrl);

        driver.findElement(By.cssSelector(".button-ui.buy-btn.button-ui_brand")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.textToBe(By.cssSelector(".button-ui.buy-btn.button-ui_brand"), "В корзине"));

        // Удаление из корзины
        driver.get("https://www.dns-shop.ru/cart/");

        WebElement element = driver.findElement(By.cssSelector(".cart-products-count"));
        String textElement = element.getText();
        String messageCount = String.format("В корзине неверное количество товаров");
        Assertions.assertEquals("1 товар", textElement, messageCount);

        driver.findElement(By.cssSelector(".remove-button__title")).click();

        wait.until(ExpectedConditions.textToBe(By.cssSelector(".empty-message__title-empty-cart"), "Корзина пуста"));

        WebElement emptyMessageElement = driver.findElement(By.cssSelector(".empty-message__title-empty-cart"));

        String notEmptyCartMessage = String.format("Корзина не пустая");
        Assertions.assertEquals("Корзина пуста", emptyMessageElement.getText(), notEmptyCartMessage);
    }

    @AfterAll
    public static void close()
    {
        driver.close();
    }
}
