package org.ysb33r.gradle.bintray.entitlements

import groovyx.net.http.URIBuilder
import org.ysb33r.gradle.bintray.core.RequestBase

trait EntitlementsRequest implements RequestBase {
    String subject
    String repo
    String pkg
    String ver

    String getPath(String id = ""){
        URIBuilder uri = new URIBuilder( "" )
        if (pkg) {
            uri.path = "/packages/${subject}/${repo}/${pkg}"
            if (ver) {
                uri.path += "/versions/${ver}"
            }
        } else {
            uri.path = "/repos/${subject}/${repo}"
        }
        uri.path += "/entitlements"
        uri.path += (id) ? "/${id}": ""
        return uri.toString()
    }

    String getPathSearch(){
        return "/search/entitlements"
    }

    Map getEntSearchParams(String downloadKeyId) {
        Map queryParams = [download_key: downloadKeyId]
        String scope = "$subject"
        scope += repo ? "/$repo" : ""
        scope += pkg ? "/$pkg" : ""
        scope += ver ? "/$ver" : ""
        queryParams.scope = scope
        return queryParams
    }

}
