import io.github.bonigarcia.wdm.*
import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.opera.OperaDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver

reportsDir = "target/geb-reports"

driver = {
  // Supports JS, but no screenshots
  new HtmlUnitDriver(true)
}

environments {
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
      ChromeDriverManager.instance.setup(Architecture.x64, "2.27")
      new ChromeDriver()
    }
  }
  firefox {
    driver = {
      FirefoxDriverManager.instance.setup(Architecture.x64, "0.14.0")
      new FirefoxDriver()
    }
  }
  ie {
    driver = {
      InternetExplorerDriverManager.instance.setup(Architecture.x32, "3.0")
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
    // TODO: Currently Opera Chromium driver is not actively maintained and out of order.
    // So this will not work. Opera will start, but Selenium cannot connect to the browser instance.
    XXX_driver_XXX = {
      OperaDriverManager.instance.setup()
      new OperaDriver()
    }
  }
}
