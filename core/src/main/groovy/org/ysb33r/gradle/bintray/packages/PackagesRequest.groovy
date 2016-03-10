package org.ysb33r.gradle.bintray.packages

import groovyx.net.http.URIBuilder
import org.ysb33r.gradle.bintray.core.*

trait PackagesRequest implements RequestBase {
    String subject
    String repo

    String getPath(String pkg = "") {
        URIBuilder uri = new URIBuilder("")
        uri.path += "/packages/${subject}/${repo}"
        uri.path += (pkg) ? "/$pkg" : ""
        return uri.toString()
    }

    String getPathGetPackages(){
        return "/repos/${subject}/${repo}/packages"
    }

    String getPathSearch(){
        return "/search/packages"
    }

    String getPathSearchMaven (String groupId, String artifactId){
        URIBuilder uri = new URIBuilder(getPathSearch(true))
        uri.path += "/maven/g=$groupId/a=$artifactId/q="
        return uri.toString()
    }
}