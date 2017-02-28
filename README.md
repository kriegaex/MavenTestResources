# Test dependencies + resource

Maven artifact to be imported for getting all necessary dependencies for
  * JUnit,
  * Spock,
  * Geb,
  * Selenium + web drivers,
  * web driver manager by bonigarcia

plus basic configuration like `GebConfig`.

Please add this artifact as a dependency with `scope=test` to your project.

In order to also get the right dependency versions please also import `de.scrum-master.test:test-bom`
with `type=pom`, `scope=import` from the `dependencyManagement` section of your project.

Example:

```xml
<dependencyManagement>
  <dependencies>
    <!-- BoM with test dependency versions -->
    <dependency>
      <groupId>de.scrum-master.test</groupId>
      <artifactId>test-bom</artifactId>
      <version>1.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
    <!-- Test resources + base configuration + base classes for Spock, Geb, Selenium -->
    <dependency>
      <groupId>de.scrum-master</groupId>
      <artifactId>test-resources</artifactId>
      <version>1.0</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependencies>
  <!-- Test resources + base configuration + base classes for Spock, Geb, Selenium -->
  <dependency>
    <groupId>de.scrum-master</groupId>
    <artifactId>test-resources</artifactId>
    <scope>test</scope>
  </dependency>
</dependencies>
```
