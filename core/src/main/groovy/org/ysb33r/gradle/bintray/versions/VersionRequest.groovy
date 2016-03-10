package org.ysb33r.gradle.bintray.versions

import groovyx.net.http.URIBuilder
import org.ysb33r.gradle.bintray.core.HasPackage
import org.ysb33r.gradle.bintray.core.HasRepo
import org.ysb33r.gradle.bintray.core.HasSubject
import org.ysb33r.gradle.bintray.core.HasVersion
import org.ysb33r.gradle.bintray.core.RequestBase

trait VersionRequest implements RequestBase, HasSubject, HasRepo, HasPackage, HasVersion {
    String getPath(String version = "") {
        URIBuilder uri = new URIBuilder("")
        uri.path += "/packages/${subject}/${repo}/${pkg}/versions"
        uri.path += (version) ? "/$version" : ""
        return uri.toString()
    }

    String getPathGetVersions() {
        URIBuilder uri = new URIBuilder("/packages/${subject}/${repo}/${pkg}")
        return uri.toString()
    }
}