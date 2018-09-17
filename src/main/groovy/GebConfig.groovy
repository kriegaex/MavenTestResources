import io.github.bonigarcia.wdm.*
import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.opera.OperaDriver
import org.openqa.selenium.opera.OperaOptions
import org.openqa.selenium.phantomjs.PhantomJSDriver
import spock.util.environment.OperatingSystem

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
  phantomjs {
    driver = {
      WebDriverManager.phantomjs().version("2.1.1").setup()
      def pjsDriver = new PhantomJSDriver()
      pjsDriver.manage().window().size = new Dimension(1024, 768)
      pjsDriver
    }
  }
  chrome {
    driver = {
      WebDriverManager.chromedriver().arch64().version("2.41").setup()
      new ChromeDriver()
    }
  }
  chrome_headless {
    driver = {
      WebDriverManager.chromedriver().arch64().version("2.41").setup()
      def options = new ChromeOptions()
      options.addArguments("--headless")
      options.addArguments("--disable-gpu")
      new ChromeDriver(options)
    }
  }
  firefox {
    driver = {
      WebDriverManager.firefoxdriver().arch64().setup()
      new FirefoxDriver()
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
      WebDriverManager.operadriver().version("2.38").setup()
      def os = OperatingSystem.current
      def operaBinary = os.windows ? new FileNameByRegexFinder().getFileNames("c:\\Program Files\\Opera", "opera.exe").first()
        : os.macOs ? "/Applications/Opera.app/Contents/MacOS/Opera" : "/usr/bin/opera"
      OperaOptions options = new OperaOptions()
      options.binary = operaBinary
      new OperaDriver(options)
    }
  }
}
