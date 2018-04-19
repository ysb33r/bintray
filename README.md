Bintray Support
===============

This is my collection of Bintray support libraries and plugins. Currently only a Gradle plugin is provided,
but the plan is to refactor the API into separate JARs that can be consumed by other projects.

## Previous versions of this document
+ 2.0.1 - https://github.com/ysb33r/bintray/blob/RELEASE_2_0/README.md (Current stable)
+ 2.0   - https://github.com/ysb33r/bintray/blob/RELEASE_2_0_1/README.md
+ 1.6   - https://github.com/ysb33r/bintray/blob/RELEASE_1_6/README.md
+ 1.5   - https://github.com/ysb33r/bintray/blob/RELEASE_1_5/README.md
+ 1.4   - https://github.com/ysb33r/bintray/blob/RELEASE_1_4/README.md 
+ 1.3.1 - https://github.com/ysb33r/bintray/blob/RELEASE_1_3_1/README.md
+ 1.3   - https://github.com/ysb33r/bintray/blob/RELEASE_1_3/README.md 
+ 1.2   - https://github.com/ysb33r/bintray/blob/RELEASE_1_2/README.md 
+ 1.1   - https://github.com/ysb33r/bintray/blob/RELEASE_1_1/gradle-plugin/README.md
+ 0.0.9 - https://github.com/ysb33r/Gradle/blob/RELEASE_0_0_9/bintray/README.md
+ 0.0.7 - https://github.com/ysb33r/Gradle/blob/RELEASE_0_0_7/bintray/README.md
+ 0.0.6 - https://github.com/ysb33r/Gradle/blob/RELEASE_0_0_6/bintray/README.md

The Unofficial Bintray Gradle Plugin
------------------------------------

A plugin to assist with publishing to. It was originally created before
Bintray published their own plugin. At the time, it was thought that this
plugin might become deprecated once Bintray launched theirs, but the
approach of this plugin is slightly different, therefore it remains active.

It will publish to Bintray as either a Ivy or a Maven style repository. A separate
task is also available for purely creating package metadata on Bintray.

### Release notes

Please see [RELEASE.md](https://github.com/ysb33r/bintray/blob/master/gradle-plugin/RELEASE.md)

### Known compatibility

+ 2.0 - Gradle 4.6, V1 Bintray API
+ 1.4 - Gradle 1.12, V1 Bintray API
+ 1.3.x - Gradle 1.12, V1 Bintray API
+ 1.2 - Gradle 1.12, V1 Bintray API
+ 1.1 - Gradle 1.11, V1 Bintray API
+ 1.0 - Gradle 1.11, V1 Bintray API
+ 0.0.6 - Gradle 1.6, Old Bintray API

### Publishing to Bintray Maven Repository

The plugin hooks in the Upload task type. In the below example we
configure the uploadArchives task which is created through the java 
plugin to use Bintray.

Version 1.3 adds the following keywords to ```bintrayIvyDeployer``` and ```bintrayMavenDeployer``` :

* ```autoCreatePackage``` - Create the package metadata on Bintray if it does not exist. Previously this has been a
manual task.  Default is false. If you the auto-create feature for an OSS project, you have to speciffy `vcsUrl` and at least 
one license. Licenses have to be one from the predefined Bintray list, aitherise a Bad Request will be sent back by the 
Bintray gateway and the upload will fail.
* ```updatePackage``` - Update package metadata on Bintray. Default is false
* ```vcsUrl``` - The Source Control URL. Optional.
* ```licenses``` - One of more licenses. Values must be as per Bintray standard list. Optional.
* ```gpgSign``` - Set to ```true``` if signing is required
* ```gpgPassphrase``` - If the GPG key requires a passphrase, then set it here. Recommendation is to read this from
```grade.properties``` or to supply via ```-P```.

Version 1.4 adds the ```versionAttributes``` keyword. This allows for arbitrary attributes to be added to a package version

```groovy
apply plugin : 'org.ysb33r.bintray'
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
            versionAttributes 'name1' : ['value1','value2'], 'name2' : ['value3','value4']
        }
       
		// Publishing as maven
		bintrayMavenDeployer {
			username          'someBintrayUser'
            apiKey            'SomeBintrayUsersApiKey'
         	repoOwner         'ysb33r'
         	repoName          'grysb33r'
         	packageName       'someNewPackageToBePublished'
            description       'This is an example to simplifying bintray publishing'
            descUrl           'https://github.com/ysb33r/Gradle/blob/master/bintray/README.md'
            tags              'gradle','bintray'
            licenses          'Apache-2.0', 'LGPL'
            vcsUrl            'https://github.com/ysb33r/bintray.git'
            autoCreatePackage true
            updatePackage     true
            versionAttributes 'name1' : ['value1','value2'], 'name2' : ['value3','value4']
       }
    }
}

```

### Publishing to Bintray Generic Repository

Version 1.1 added the ```BintrayGenericUpload``` task type, allowing upload of any file to a generic Bintray repository.
Version 1.3 extended it with the following keywords ```md5```, ```sha1```, ```sha256```, ```autoCreatePackage```,
```updatePackage```, ```vcsUrl```, ```tags```, ```description``` and ```licenses```.

```groovy
apply plugin : 'org.ysb33r.bintray'

import org.ysb33r.gradle.bintray.BintrayGenericUpload

task uploadArchives (type:BintrayGenericUpload ) {
    username    'someBintrayUser'
    apiKey      'SomeBintrayUsersApiKey'
    repoOwner   'ysb33r'
    repoName    'nanook'
    packageName 'someNewPackageToBePublished'

    // 'sources' can be called more than once
    sources "${buildDir}/distributions/${applicationName}-${version}.tar"

    // From v1.3+ add any of these three to have the checksums published along with the artifacts
    md5 true
    sha1 false
    sha256 false

    // From v1.3+ the following two can be added to create and update package metadata. Prior to this
    // it has always been a manual task.
    // Tags & Licenses can be a single string or an array.
    // License values need to be valid as per Bintray.
    autoCreatePackage false
    updatePackage     false
    description       'This package is used to demonstate Bintray & Gradle interaction'
    tags              'groovy', 'gradle', 'bintray'
    vcsUrl            'https://github.com/ysb33r/bintray.git'
    licenses          'Apache-2.0'

    // From V1.3+ it is also possible to sign the version
    // Set gpgSign to true to activate Bintray signing
    // If the gpgKey of the repoOwner requires a passphase then specifiy gpgPassphrase
    gpgSign    true
    gpgPassphrase 'someBintrayUserPassphrase'
}
```

It is a great combination with the ```application``` plugin

```groovy
apply plugin : 'org.ysb33r.bintray'
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

### Signing a version

Although the uploading process allows for automatic signing of packages in generic repositories, this s not possible for
at the moment for Maven repositories. There is a separate task for those who would like to control
the signing of an uploaded version.

```groovy
apply plugin : 'org.ysb33r.bintray'

import org.ysb33r.gradle.bintray.BintraySignVersion

task signArchives (type:BintraySignVersion ) {
    username    'someBintrayUser'
    apiKey      'SomeBintrayUsersApiKey'
    repoOwner   'ysb33r'
    repoName    'nanook'
    packageName 'someNewPackageToBePublished'

    // This is optional, if not supplied, the current project.version will be used
    version      project.version

    // Necessary when GPG key requires a passphrase
    gpgPassphrase 'SomePassphrase'
}

```

Acknowledgements
----------------

Writing this was not possible without the help of others. The following links were specifically helpful

- http://mrhaki.blogspot.co.uk/2010/09/gradle-goodness-define-short-plugin-id.html
- https://github.com/Ullink/gradle-repositories-plugin/blob/master/src/main/groovy/com/ullink/RepositoriesPlugin.groovy
- https://github.com/bintray/bintray-examples/blob/master/gradle-example/build.gradle
