import com.gargoylesoftware.htmlunit.SilentCssErrorHandler
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.javascript.SilentJavaScriptErrorListener
import io.appium.java_client.windows.WindowsDriver
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.GeckoDriverService
import org.openqa.selenium.htmlunit.HtmlUnitDriver
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
      new JSProblemsIgnoringHtmlUnitDriver(true)
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
      createFirefoxDriver()
    }
  }
  firefox_headless {
    driver = {
      createFirefoxDriver(true)
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
      WebDriverManager.operadriver().setup()
      def os = OperatingSystem.current
      def operaBinary = os.windows
        ? new FileNameByRegexFinder().getFileNames("c:\\Program Files\\Opera", "opera.exe\$").sort().last()
        : os.macOs
          ? "/Applications/Opera.app/Contents/MacOS/Opera"
          : "/usr/bin/opera"
      OperaOptions options = new OperaOptions()
      options.binary = operaBinary
      // Explicitly ask for permission to show push notifications
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

static ChromeDriver createChromeDriver(boolean headless = false) {
  def options = new ChromeOptions()
  if (headless) {
    System.setProperty("webdriver.chrome.logfile", "chromedriver.log")
    System.setProperty("webdriver.chrome.verboseLogging", "true")
    options.addArguments("--headless")
    options.addArguments("--disable-gpu")
    // Set similar to normal Chrome window default size on 1980x1080 display, overriding headless default of 800x600.
    // This can make a difference in drag & drop tests when measuring how far something really has been dragged, if the
    // target view is resizable.
    options.addArguments("--window-size=900,900")
  }
  WebDriverManager.chromedriver().arch32().setup()
  /*
  LoggingPreferences loggingPreferences = new LoggingPreferences()
  loggingPreferences.enable(LogType.BROWSER, Level.ALL)
  options.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences)
  */

  // Explicitly ask for permission to show push notifications
  options.addArguments("--disable-notifications")
  // Include cookie banners in screenshots, see https://stackoverflow.com/a/70509053/1082681
  options.addArguments("--disable-blink-features=AutomationControlled")
  new ChromeDriver(options)
}

static FirefoxDriver createFirefoxDriver(boolean headless = false) {
  WebDriverManager.firefoxdriver().arch64().setup()
  FirefoxOptions options = new FirefoxOptions()
  options.headless = headless
  // Disable all notifications
  options.addPreference("dom.webnotifications.enabled", false)
  // Disable background updates (push notifications)
  options.addPreference("dom.push.enabled", false)
  GeckoDriverService service = new GeckoDriverService.Builder()
    .usingAnyFreePort()
    .withEnvironment([
      'MOZ_HEADLESS_WIDTH'  : '1920',
      'MOZ_HEADLESS_HEIGHT' : '1080'
    ])
    .build()
  new FirefoxDriver(service, options)
}

class JSProblemsIgnoringHtmlUnitDriver extends HtmlUnitDriver {
  JSProblemsIgnoringHtmlUnitDriver(boolean enableJavascript) {
    super(enableJavascript)
  }

  @Override
  protected WebClient modifyWebClient(WebClient client) {
    WebClient modifiedClient = super.modifyWebClient(client)
    // Ignore JS errors. This makes more tests work which would otherwise fail.
    // See https://sqa.stackexchange.com/a/20476/52456.
    modifiedClient.options.throwExceptionOnScriptError = false
    // Ignore JS problems spamming the test log.
    // See https://htmlunit.sourceforge.io/logging.html#Logging_JavaScript_messages.
    modifiedClient.javaScriptErrorListener = new SilentJavaScriptErrorListener()
    // Ignore CSS problems spamming the test log.
    // See https://htmlunit.sourceforge.io/logging.html#Logging_CSS_parsing_messages.
    //
    // FYI, in order to avoid excessive logging of warnings about limitations in HtmlUnit's own CSS parser
    // (see https://github.com/HtmlUnit/htmlunit/issues/446), we also need to configure something like this in the
    // application's log configuration (Logback example):
    //   <logger name="com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet" level="ERROR" />
    modifiedClient.cssErrorHandler = new SilentCssErrorHandler()
    modifiedClient
  }
}
