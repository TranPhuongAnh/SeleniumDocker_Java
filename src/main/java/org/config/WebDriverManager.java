package org.config;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class WebDriverManager {
    //    private static final String CHROME_DRIVER = "webdriver.chrome.driver"; //Cấu hình biến trỏ đến file webDriver cứng, selenium v4 trở đi k cần webDriver cứng
    private static final WebDriverManager webDriverManager = new WebDriverManager();
    public static CommonMethod commonMethod;
    private static WebDriver driver;
    private static EnvironmentType environmentType;
    private static DriverType driverType;
    private static OptionsBrowserManager options_manager = new OptionsBrowserManager();


    //Hàm sử dụng được tất cả hàm của class WebDriverManager
    public static WebDriverManager getInstance() {
        return webDriverManager;
    }

    //Hàm chờ theo thời gian trong file config
    public void ImplicitlyWait_Config() {
        long time = FileReaderManager.getInstance().getConfigReader().getImplicitlyWait();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(time));
    }

    //Hàm đặt biến cho giá trị cấu hình từ file config
    public WebDriverManager() {
        driverType = FileReaderManager.getInstance().getConfigReader().getBrowser();
        environmentType = FileReaderManager.getInstance().getConfigReader().getEnvironment();
    }

    // Hàm getDriver() khởi tạo diver
    protected WebDriver getDriver() {
        if (driver == null) {
            driver = createDriver();
        }
        return driver;
    }

    // Hàm createDriver() xác định môi trường cho webDriver
    private WebDriver createDriver() {
        switch (environmentType) {
            case LOCAL:
                driver = createLocalDriver();
                break;
            case DOCKER:
                driver = createDockerDriver();
                break;
        }
        return driver;
    }

    // Hàm connect_to_grid_docker() trả về địa chỉ của Selenium Grid Docker
    private URL connect_to_grid_docker() {
        URL url = null;
        String hub_host = System.getenv().getOrDefault("HUB_HOST", "localhost");
        String grid_url = "http://" + hub_host + ":4444/wd/hub";

        boolean status = isGridAvailable(hub_host);
        System.out.println("Kết nối tới grid docker: " + status);

        // 1. Kiểm tra kết nối đến Grid
        if (!isGridAvailable(hub_host)) {
            return null;
        } else {
            try {
                System.out.println("Đang kết nối tới Selenium Grid Docker tại: " + grid_url + "...");
                url = new URL(grid_url);
                return url;

            } catch (MalformedURLException e) {
                System.out.println("URL của Selenium Grid không hợp lệ: " + e.getMessage());
                return null;
            }
        }
    }

    // Hàm isGridAvailable() kiểm tra kết nối Grid có sẵn không?
    private boolean isGridAvailable(String hub_host) {
        try {
            URL url = new URL("http://" + hub_host + ":4444/status");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000); // Thời gian chờ kết nối tối đa là 3 giây
            connection.connect();

            int code = connection.getResponseCode();
            if (code != 200){
                return false;
            }

            // Đọc nội dung Json trả về
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String body = response.toString();

            if (body.contains("\"ready\":true")){
                return true;
            } else return false;

        } catch (Exception e){
            return false;
        }
    }

    // Hàm createDockerDriver() xác định môi trường browser và trả về webDriver phù hợp
    private WebDriver createDockerDriver() {
        URL grid_url = connect_to_grid_docker(); // Kết nối tới Selenium Grid Docker

        // Nếu không kết nối được tới Grid Docker thì sẽ chạy trên môi trường LOCAL
        if (grid_url == null) {
            System.out.println("❌ Không thể kết nối tới Selenium Grid Docker!");
            System.out.println("👉 Hãy chắc chắn Docker và selenium-hub đã được khởi động!");
            System.out.println("...... Sử dụng local để tiếp tục test! ......");
            return createLocalDriver();
        } else {
            switch (driverType) {
                case CHROME:
                    driver = new RemoteWebDriver(grid_url, options_manager.chrome_options());
                    break;
                case FIREFOX:
                    driver = new RemoteWebDriver(grid_url, options_manager.firefox_options());
                    break;
                case EDGE:
                    driver = new RemoteWebDriver(grid_url, options_manager.edge_options());
                    break;
                case SAFARI:
                    System.out.println("Không hỗ trợ Safari trên Docker.");
                    System.out.println("Sử dụng trình duyệt Chrome thay vì Safari.");
                    driver = new RemoteWebDriver(grid_url, options_manager.chrome_options());
                    break;

            }
            driver.manage().window().maximize();

            ImplicitlyWait_Config();
            return driver;
        }
    }

    //Hàm createLocalDriver() xác định webDriver đang dùng từ file config của môi trường LOCAL
    private WebDriver createLocalDriver() {
        switch (driverType) {
            case CHROME:
                //Chỉ cấu hình khi selenium version < 4, với selenium v4 trở đi thì k cần bản cứng
//                System.setProperty(CHROME_DRIVER, System.getProperty("user.dir") + FileReaderManager.getInstance().getConfigReader().getDriverPath());
                driver = new ChromeDriver(options_manager.chrome_options());
                break;
            case FIREFOX:
                driver = new FirefoxDriver(options_manager.firefox_options());
                break;
            case EDGE:
                driver = new EdgeDriver(options_manager.edge_options());
                break;
            case SAFARI:
                driver = new SafariDriver(options_manager.safari_options());
                break;
        }
        driver.manage().window().maximize();

        ImplicitlyWait_Config();
        return driver;
    }

    // Hàm đóng tất cả tab của web browser
    public void CloseDriver() throws InterruptedException {
        try {
            driver.close();
            Thread.sleep(1000);
            driver.quit();
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
//            System.out.println(exp.getCause());
//            exp.printStackTrace();
        }
    }
}

