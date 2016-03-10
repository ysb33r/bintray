package org.ysb33r.gradle.bintray.content

import groovyx.net.http.URIBuilder
import org.ysb33r.gradle.bintray.core.HasPackage
import org.ysb33r.gradle.bintray.core.HasRepo
import org.ysb33r.gradle.bintray.core.HasSubject
import org.ysb33r.gradle.bintray.core.HasVersion
import org.ysb33r.gradle.bintray.core.RequestBase

trait ContentRequest implements RequestBase, HasSubject, HasRepo, HasPackage, HasVersion {

    String getPath(String filePath, Boolean dynamicMode = false) {
        URIBuilder uri
        if (dynamicMode) {
            uri = new URIBuilder("/content")
        } else {
            uri = new URIBuilder("")
        }
        uri.path += "/${subject}/${repo}/${filePath}"
        return uri.toString()
    }

    //TODO : Complete and Test
    String getPathMavenUpload(String filePath, Boolean publish) {
        URIBuilder uri = new URIBuilder("")
        uri.path = "/maven/${subject}/${repo}/${pkg}/${filePath};publish="
        uri.path += (publish) ? "1" : "0"
        return uri.toString()
    }

    //TODO : Complete and Test
    Map getHeaders(Boolean publish, Boolean override, Boolean explode) {
        return headers = [
                "X-Bintray-Package" : pkg,
                "X-Bintray-Package" : this.ver,
                "X-Bintray-Package" : publish ? 1 : 0,
                "X-Bintray-Override": override ? 1 : 0,
                "X-Bintray-Explode" : explode ? 1 : 0
        ]
    }

}


