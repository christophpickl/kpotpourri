# KPotpourri

[![Kotlin](https://img.shields.io/badge/kotlin-1.1.1-blue.svg)](http://kotlinlang.org)

[![Travis](https://img.shields.io/travis/christophpickl/kpotpourri.svg)](https://travis-ci.org/christophpickl/kpotpourri)
[![codecov](https://codecov.io/gh/christophpickl/kpotpourri/branch/master/graph/badge.svg)](https://codecov.io/gh/christophpickl/kpotpourri)
[![Issues](https://img.shields.io/github/issues/christophpickl/kpotpourri.svg)](https://github.com/christophpickl/kpotpourri/issues?q=is%3Aopen)
[![Dependency Versions](https://www.versioneye.com/user/projects/58e51229d6c98d0041747763/badge.svg?style=flat)](https://www.versioneye.com/user/projects/58e51229d6c98d0041747763)

Some common extensions to kotlin which i am desperately missing.

# Usage

## Gradle

Add the following to your `build.gradle`:

```groovy
repositories { 
  maven { 
    url "http://dl.bintray.com/christophpickl/cpickl" 
  }
}

dependencies {
  compile 'com.github.christophpickl.kpotpourri:common:1.0'
  compile 'com.github.christophpickl.kpotpourri:swing:1.0'
}
```

## Maven

Add the following to your `settings.xml`:

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

And then the proper Maven dependency to your `pom.xml`.
