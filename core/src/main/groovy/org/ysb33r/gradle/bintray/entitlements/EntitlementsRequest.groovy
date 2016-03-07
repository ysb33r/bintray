package org.ysb33r.gradle.bintray.entitlements

import groovyx.net.http.URIBuilder
import org.ysb33r.gradle.bintray.core.ApiBase
import org.ysb33r.gradle.bintray.core.RequestBase
import org.ysb33r.gradle.bintray.core.HasPackage
import org.ysb33r.gradle.bintray.core.HasRepo
import org.ysb33r.gradle.bintray.core.HasSubject
import org.ysb33r.gradle.bintray.core.HasVersion

trait EntitlementsRequest implements RequestBase, ApiBase, HasSubject, HasRepo, HasPackage, HasVersion {

    String getPath(String id = ""){
        URIBuilder uri = new URIBuilder( baseUrl )
        if (pkg) {
            uri.path = "/packages/${subject}/${repo}/${pkg}"
            if (version) {
                uri.path += "/versions/${version}"
            }
        } else {
            uri.path = "/repos/${subject}/${repo}"
        }
        uri.path += "/entitlements"
        uri.path += (id) ? "/${id}": ""
        return uri.toString()
    }
}
