BINTRAY GRADLE PLUGIN RELEASE NOTES
===================================

Version 1.3 - Roadmap
---------------------
* Support for uploading snapshots to oss.jfrog.
* Transparent integration in repositories so that snapshots and production are automatically switched between oss.jfrog and Bintray.
* Ability to create packages if they don't exist on Bintray. (via a configurable option).
* Ability to auto-publish (via a configurable option).

Version 1.2
-----------
* Fixed a critical bug in 1.1 that affected uploading to generic Bintray repositories

Version 1.1 (Don't use this version)
-----------
* Added support for uploading to generic bintray repositories via the ```BintrayGenericUpload``` task

Version 1.0
-----------
* Now supports Bintray V1 API.
* Dropped ```JCenter()``` from the codebase as Geadle now nat8vely supports ```jcenter()```.
* Drooped ```Bintray``` class as it's only function was to shorted the typing of Bintray repositories.

Version 0.0.9 and earlier
-------------------------
* Moved Bintray code to it's own repository. Earlier code lived [here]().
* Support uploading to Bintray.
* Support for Maven and Ivy style as part of the uploadArchives.
