import io.appium.java_client.windows.WindowsDriver
import io.github.bonigarcia.wdm.*
import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.opera.OperaDriver
import org.openqa.selenium.opera.OperaOptions
import org.openqa.selenium.remote.DesiredCapabilities
import spock.util.environment.OperatingSystem

import java.util.concurrent.TimeUnit

reportsDir = "target/geb-reports"

driver = {
  // Supports JS, but no screenshots
  new HtmlUnitDriver(true)
}

environments {
  html_unit {
    driver = {
      // Supports JS, but no screenshots
      new HtmlUnitDriver(true)
    }
  }
  // PhantomJS is no longer supported by WebDriverManager -> use Chrome headless instead
  /*
  phantomjs {
    driver = {
      WebDriverManager.phantomjs().version("2.1.1").setup()
      def pjsDriver = new PhantomJSDriver()
      pjsDriver.manage().window().size = new Dimension(1024, 768)
      pjsDriver
    }
  }
  */
  chrome {
    driver = {
      createChromeDriver()
    }
  }
  chrome_headless {
    driver = {
      createChromeDriver(true)
    }
  }
  firefox {
    driver = {
      WebDriverManager.firefoxdriver().arch64().setup()
      FirefoxOptions options = new FirefoxOptions()
      // Disable all notifications
      options.addPreference("dom.webnotifications.enabled", false)
      // Disable background updates (push notifications)
      options.addPreference("dom.push.enabled", false)
      new FirefoxDriver(options)
    }
  }
  ie {
    driver = {
      WebDriverManager.iedriver().arch32().setup()
      new InternetExplorerDriver()
    }
  }
  edge {
    driver = {
      WebDriverManager.edgedriver().setup()
      new EdgeDriver()
    }
  }
  opera {
    driver = {
      WebDriverManager.operadriver().version("79.0.3945.79").setup()
      def os = OperatingSystem.current
      def operaBinary = os.windows
        ? new FileNameByRegexFinder().getFileNames("c:\\Program Files\\Opera", "opera.exe\$").sort().last()
        : os.macOs
          ? "/Applications/Opera.app/Contents/MacOS/Opera"
          : "/usr/bin/opera"
      OperaOptions options = new OperaOptions()
      options.binary = operaBinary
      // Do ask for permission to show push notifications
      options.addArguments("--disable-notifications")
      new OperaDriver(options)
    }
  }
  win_app {
    driver = {
      DesiredCapabilities capabilities = new DesiredCapabilities()
      capabilities.setCapability("app", "Microsoft.WindowsCalculator_8wekyb3d8bbwe!App")
      def windowsDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities)
      windowsDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS)
      windowsDriver
    }
  }
}

ChromeDriver createChromeDriver(boolean headless = false) {
  def options = new ChromeOptions()
  if (headless) {
    System.setProperty("webdriver.chrome.logfile", "chromedriver.log")
    System.setProperty("webdriver.chrome.verboseLogging", "true")
    options.addArguments("--headless")
    options.addArguments("--disable-gpu")
  }
  WebDriverManager.chromedriver().arch32().version("79.0.3945.36").setup()
  // Avoid "Chrome is being controlled by automated test software" pop-up
  options.addArguments("disable-infobars")
  // Do ask for permission to show push notifications
  options.addArguments("--disable-notifications")
  new ChromeDriver(options)
}
