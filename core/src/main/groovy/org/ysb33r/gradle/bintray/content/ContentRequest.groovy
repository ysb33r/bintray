package org.ysb33r.gradle.bintray.content

import groovyx.net.http.URIBuilder
import org.ysb33r.gradle.bintray.core.ApiBase
import org.ysb33r.gradle.bintray.core.HasRepo
import org.ysb33r.gradle.bintray.core.HasSubject

trait ContentRequest implements ApiBase, HasSubject, HasRepo {

    String filePath
    Map queryMap
    URIBuilder getPath(){
        URIBuilder uri
        if (queryMap){
            uri = new URIBuilder( baseUrl )
            uri.path = "/content"
        } else {
            uri = new URIBuilder( dlUrl )
        }
        uri.path += "/${subject}/${repo}/${filePath}"
        return uri
    }
}


