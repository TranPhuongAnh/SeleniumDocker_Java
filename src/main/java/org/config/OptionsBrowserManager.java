package org.config;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;

public class OptionsBrowserManager {

    public ChromeOptions chrome_options() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
//        options.addArguments("--headless");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--autoplay-policy=no-user-gesture-required"); // Tắt chính sách autoplay
        options.addArguments("--window-size=1920,1080");

        return options;
    }

    public FirefoxOptions firefox_options(){
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--no-sandbox");
//        options.addArguments("--headless");
//        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--autoplay-policy=no-user-gesture-required"); // Tắt chính sách autoplay
        options.addArguments("--window-size=1920,1080");

        return options;
    }

    public EdgeOptions edge_options(){
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--no-sandbox");
//        options.addArguments("--headless");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--autoplay-policy=no-user-gesture-required"); // Tắt chính sách autoplay
        options.addArguments("--window-size=1920,1080");

        return options;
    }

    public SafariOptions safari_options(){
        SafariOptions options = new SafariOptions();
        options.setAutomaticInspection(false);
        options.setAutomaticProfiling(false);
        options.setUseTechnologyPreview(true);

        return options;
    }
}
