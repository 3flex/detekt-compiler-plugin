# WIP: __detekt-compiler-plugin__

![Pre Merge Checks](https://github.com/detekt/detekt-compiler-plugin/workflows/Pre%20Merge%20Checks/badge.svg)
[![gradle plugin](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/io/github/detekt/gradle/compiler-plugin/io.github.detekt.gradle.compiler-plugin.gradle.plugin/maven-metadata.xml.svg?label=Gradle&style=flat-square)](https://plugins.gradle.org/plugin/io.github.detekt.gradle.compiler-plugin)

Experimental support for integrating detekt as a Kotlin compiler plugin

![image](docs/detekt-compiler-plugin.png "image")


### Usage

```kotlin
plugins {
    id("io.github.detekt.gradle.compiler-plugin") version "<latest>"
}

detekt {
    isEnabled = true // or with a property: System.getProperty("runDetekt") != null
    // everything from https://detekt.github.io/detekt/kotlindsl.html#options-for-detekt-configuration-closure
    // is supported to declare, only some options are used. See limitations. 
}
```

### Limitations

Everything our Gradle plugin (`DetektExtension`) supports, is also supported on the declaration side with this plugin.  
However only the following options are implemented/passed down to detekt:
- config
- baseline
- debug
- buildUponDefaultConfig
