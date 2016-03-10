package org.ysb33r.gradle.bintray.versions

class Versions implements VersionRequest {

    List getVersions() {
        assertAttributes(subject, repo, pkg)
        return btConn.RESTCall("get", getPathGetVersions()).versions
    }
}
