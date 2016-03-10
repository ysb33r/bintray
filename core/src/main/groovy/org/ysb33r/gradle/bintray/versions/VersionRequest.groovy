package org.ysb33r.gradle.bintray.versions

import org.ysb33r.gradle.bintray.core.RequestBase

trait VersionRequest implements RequestBase {
    String subject
    String repo
    String pkg

    String getPath(String version = "") {
        String uri = "/packages/${subject}/${repo}/${pkg}/versions"
        uri += (version) ? "/$version" : ""
        return uri
    }

    String getPathGetVersions() {
        return "/packages/${subject}/${repo}/${pkg}"
    }
}