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
      PhantomJsDriverManager.instance.setup("2.1.1")
      def pjsDriver = new PhantomJSDriver()
      pjsDriver.manage().window().size = new Dimension(1024, 768)
      pjsDriver
    }
  }
  chrome {
    driver = {
      ChromeDriverManager.instance.setup(Architecture.x64, "2.41")
      new ChromeDriver()
    }
  }
  chrome_headless {
    driver = {
      ChromeDriverManager.instance.setup(Architecture.x64, "2.41")
      def options = new ChromeOptions()
      options.addArguments("--headless")
      options.addArguments("--disable-gpu")
      new ChromeDriver(options)
    }
  }
  firefox {
    driver = {
      FirefoxDriverManager.instance.setup(Architecture.x64)
      new FirefoxDriver()
    }
  }
  ie {
    driver = {
      InternetExplorerDriverManager.instance.setup(Architecture.x32)
      new InternetExplorerDriver()
    }
  }
  edge {
    driver = {
      EdgeDriverManager.instance.setup()
      new EdgeDriver()
    }
  }
  opera {
    driver = {
      OperaDriverManager.instance.setup("2.38")
      def os = OperatingSystem.current
      def operaBinary = os.windows ? new FileNameByRegexFinder().getFileNames("c:\\Program Files\\Opera", "opera.exe").first()
        : os.macOs ? "/Applications/Opera.app/Contents/MacOS/Opera" : "/usr/bin/opera"
      OperaOptions options = new OperaOptions()
      options.binary = operaBinary
      new OperaDriver(options)
    }
  }
}
