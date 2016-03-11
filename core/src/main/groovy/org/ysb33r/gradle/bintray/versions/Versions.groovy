package org.ysb33r.gradle.bintray.versions

import groovy.transform.TupleConstructor

@TupleConstructor
class Versions implements VersionRequest {

    List getVersions() {
        assertAttributes(subject, repo, pkg)
        return this.bintrayClient.RESTCall("get", getPathGetVersions()).versions
    }
}
