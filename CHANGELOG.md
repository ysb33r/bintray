BINTRAY GRADLE PLUGIN RELEASE NOTES
===================================

## Version 3.0
* Requires JDK8 minimum.
* Updated to use Grolifant 0.13.

## Version 2.0.4
* `BintrayPackage` tasks allow lazy evaluation of `vcsUrl`.

## Version 2.0.3
* `BintrayPackage` tasks allow lazy evaluation of `repoOwner` and `repoName`.

## Version 2.0.2
* `BintrayPackage` tasks allow lazy evaluation of `tags`, `licenses` and `description`.

## Version 2.0.1
* Add additional debug messages for when operations fail.

## Version 2.0
* Upgradle to Gradle 4.6
* Replace HttpBuilder with HttpBuilder-NG.

## Version 1.6
* [ISSUE #15](https://github.com/ysb33r/bintray/issues/15) - Fixed `MissingPropertyException` in BintrayGenericUpload.
* Upgraded to Gradle 2.0
* Added Gradle Plugin Portal support

## Version 1.5
* [ISSUE #11](https://github.com/ysb33r/bintray/issues/11) - Changed the plugin id to ```org.ysb33r.bintray```.
* [ISSUE #12](https://github.com/ysb33r/bintray/issues/12) - Added support for Bintray version attributes for Maven Repositories

## Version 1.4
* Never released

## Version 1.3.1
* [ISSUE #9](https://github.com/ysb33r/bintray/issues/9) - Corrected issue when creating new Bintray package metadata

## Version 1.3
* [ISSUE #3](https://github.com/ysb33r/bintray/issues/3) - Ability to create packages if they don't exist on Bintray. (via a configurable option).
Added ```vcsUrl```, and ```licenses``` keywords to ```bintrayMavenDeployer```, ```bintrayIvyDeployer```and ```BintrayGenericUpload```
* [ISSUE #4](https://github.com/ysb33r/bintray/issues/4) - Ability to auto-publish (via a configurable option).
Added ```autoCreatePackage``` and ```updatePackage``` keywords to ```bintrayMavenDeployer```, ```bintrayIvyDeployer```and ```BintrayGenericUpload```
* [ISSUE #5](https://github.com/ysb33r/bintray/issues/5) - Add generation of MD5s when uploading to Generic Bintray repository
Added ```md5```, ```sha1``` and ```sha256``` keywords to ```BintrayGenericUpload```.
* [ISSUE #6](https://github.com/ysb33r/bintray/issues/6) - Ability to auto-sign packages upon upload.
Added ```gpgSign```, ```gpgPassphrase``` keywords to ```bintrayMavenDeployer```, ```bintrayIvyDeployer```and ```BintrayGenericUpload```
* Added ```BintraySignVersion``` task for people who would like to separate the uploading and signing part.

## Version 1.2
* Fixed a critical bug in 1.1 that affected uploading to generic Bintray repositories

## Version 1.1 (Don't use this version)
* Added support for uploading to generic bintray repositories via the ```BintrayGenericUpload``` task

## Version 1.0
* Now supports Bintray V1 API.
* Dropped ```JCenter()``` from the codebase as Gradle now natively supports ```jcenter()```.
* Dropped ```Bintray``` class as it's only function was to shorted the typing of Bintray repositories.

## Version 0.0.9 and earlier
* Moved Bintray code to it's own repository. Earlier code lived [here](https://github.com/ysb33r/Gradle/tree/RELEASE_0_0_9/bintray).
* Support uploading to Bintray.
* Support for Maven and Ivy style as part of the uploadArchives.
