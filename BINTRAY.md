# Bintray

KPotPourri is using bintray for storing it's Maven artifacts.

## Access

Using fancy new Gradle, copy the following in your `build.gradle`:

```groovy
repositories { 
  maven { 
    url "http://dl.bintray.com/christophpickl/cpickl" 
  }
}
```

Using good old Maven, copy the following in your `settings.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd'
          xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
    
    <profiles>
        <profile>
            <repositories>
                <repository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>bintray-christophpickl-cpickl</id>
                    <name>bintray</name>
                    <url>http://dl.bintray.com/christophpickl/cpickl</url>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>bintray-christophpickl-cpickl</id>
                    <name>bintray-plugins</name>
                    <url>http://dl.bintray.com/christophpickl/cpickl</url>
                </pluginRepository>
            </pluginRepositories>
            <id>bintray</id>
        </profile>
    </profiles>
    <activeProfiles>
        <activeProfile>bintray</activeProfile>
    </activeProfiles>
</settings>
```


