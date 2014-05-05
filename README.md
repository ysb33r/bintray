Unofficial Bintray Support
==========================

This is my collection of unofficial Bintray support. Currently only a Gradle plugin is provided,
but the plan is to refactor the API into separate JARs that can be consumed by other projects.

Previous versions of this document
----------------------------------

This is version 1.2 of the document.

+ 1.1   - https://github.com/ysb33r/bintray/blob/RELEASE_1_1/gradle-plugin/README.md
+ 0.0.9 - https://github.com/ysb33r/Gradle/blob/RELEASE_0_0_9/bintray/README.md
+ 0.0.7 - https://github.com/ysb33r/Gradle/blob/RELEASE_0_0_7/bintray/README.md
+ 0.0.6 - https://github.com/ysb33r/Gradle/blob/RELEASE_0_0_6/bintray/README.md

The Unofficial Bintray Gradle Plugin
------------------------------------

A plugin to assist with publishing to. It was originally created before
Bintray published their own plugin. At the time, it was thought that this
plugin might become deprecated once Bintray launched theirs, but the
approach of this plugin is slightly different, therefore it remains active

It will publish to Bintray as either a Ivy or a Maven style repository. A separate
task is also available for purely creating package metadata on Bintray.

### Release notes

Please see [RELEASE.md](./gradle-plugin/RELEASE.md)
### Known compatibility

+ 1.2 - Gradle 1.12, V1 Bintray API
+ 1.1 - Gradle 1.11, V1 Bintray API
+ 1.0 - Gradle 1.11, V1 Bintray API
+ 0.0.6 - Gradle 1.6, Old Bintray API

### Adding the plugin

```groovy
buildscript {
    repositories {
        jcenter()
    	mavenRepo(url: 'http://repository.codehaus.org')
      }
      dependencies {
        classpath 'org.ysb33r.gradle:bintray:1.1'
      }
}
```

### Publishing to Bintray Maven Repository

The plugin hooks in the Upload task type. In the below example we
configure the uploadArchives task which is created through the java 
plugin to use Bintray.

```groovy
apply plugin : 'bintray-publish'
apply plugin : 'java'

uploadArchives  {
    repositories {
 
        // Publishing as ivy              
		bintrayIvyDeployer {
			username    'someBintrayUser'
            apiKey      'SomeBinTrayUsersApiKey'
         	repoOwner   'ysb33r'
         	repoName    'grysb33r'
         	packageName 'someNewPackageToBePublished'
            description 'This is an example to simplifying bintray publishing'
            descUrl     'https://github.com/ysb33r/Gradle/blob/master/bintray/README.md'
            tags        'gradle','bintray'
        }
       
		// Publishing as maven
		bintrayMavenDeployer {
			username    'someBintrayUser'
            apiKey      'SomeBintrayUsersApiKey'
         	repoOwner   'ysb33r'
         	repoName    'grysb33r'
         	packageName 'someNewPackageToBePublished'
            description 'This is an example to simplifying bintray publishing'
            descUrl     'https://github.com/ysb33r/Gradle/blob/master/bintray/README.md'
            tags        'gradle','bintray'
       }
    }
}

```

### Publishing to Bintray Generic Repository

Version 1.1 adds the ```BintrayGenericUpload``` task type, allowing upload of any file to a generic Bintray repository.

```groovy
apply plugin : 'bintray-publish'

import org.ysb33r.gradle.bintray.BintrayGenericUpload

task uploadArchives (type:BintrayGenericUpload ) {
    username    'someBintrayUser'
    apiKey      'SomeBintrayUsersApiKey'
    repoOwner   'ysb33r'
    repoName    'nanook'
    packageName 'someNewPackageToBePublished'
    // 'sources' can be called more than once
    sources "${buildDir}/distributions/${applicationName}-${version}.tar"
}
```

It is a great combination with the ```application``` plugin

```groovy
apply plugin : 'bintray-publish'
apply plugin : 'application'

import org.ysb33r.gradle.bintray.BintrayGenericUpload

task uploadArchives (type:BintrayGenericUpload ) {
    username    'someBintrayUser'
    apiKey      'SomeBintrayUsersApiKey'
    repoOwner   'ysb33r'
    repoName    'nanook'
    packageName 'someNewPackageToBePublished'
    sources distZip.outputs.files
    sources distTar.outputs.files
    dependsOn distZip, distTar
    onlyIf { !version.endsWith("SNAPSHOT") }
}
```

### Publishing using new Gradle Publishing mechanism

This is not yet supported, but will be in a future version. Currently the publication feature is still in incubation in
1.x and a number of changes are expected, so I am holding off for the moment, but will try to be ready for Gradle 2.x.


Acknowledgements
----------------

Writing this was not possible without the help of others. The following links were specifically helpful

- http://mrhaki.blogspot.co.uk/2010/09/gradle-goodness-define-short-plugin-id.html
- https://github.com/Ullink/gradle-repositories-plugin/blob/master/src/main/groovy/com/ullink/RepositoriesPlugin.groovy
- https://github.com/bintray/bintray-examples/blob/master/gradle-example/build.gradle
