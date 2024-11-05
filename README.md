# Mocker Service

### _Mocking Api response for various Testing_

## Features

* API Response mocking
* API Headers mocking
* HTTP Response Code mocking

## Version 0.1

### Selenium - 4.3.0 or 4.3+ Required

## Add to your project

###### ---------------------------------------------------

Easy to add in your projects using gradle, maven or jar

### Gradle

- Add it in your root build.gradle at the end of repositories:
 ``` groovy
allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		             }
	        }
 ```
- Add the dependency (replace version with the version on top)
``` groovy
dependencies {
	  implementation 'com.github.vigneshwarn:Selenium-API-Response-mocking: 1.0.0'    
	     }
```
Maven:Q
Add the repository in pom.xml file
<repositories>
<repository>
<id>jitpack.io</id>
<url>https://jitpack.io</url>
</repository>
</repositories>

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

Add the dependency (replace version with the version on top)

	<dependency>
	    <groupId>com.github.vigneshwarn</groupId>
	    <artifactId>Selenium-API-Response-mocking</artifactId>
	    <version></version>
	</dependency>


Jar
Download the jar from this repo

out/artifact/Mocker-1.0-SNAPSHOT.jar or Use the Release Section

# **Quick Start**
    mockedResponseBody = {"xyz": "123"}
    MockerUtils mockerUtils = new MockerUtils();
    mockerUtils.mockService(chromeDriver, url ,mockedResponseBody);
    
``