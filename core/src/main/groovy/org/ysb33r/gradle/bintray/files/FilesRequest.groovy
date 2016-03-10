package org.ysb33r.gradle.bintray.files

import groovyx.net.http.URIBuilder
import org.ysb33r.gradle.bintray.core.RequestBase

trait FilesRequest implements RequestBase {

    String subject
    String repo
    String pkg
    String ver

    String getPath(){
        URIBuilder uri = new URIBuilder( "" )
        uri.path = "/packages/${subject}/${repo}/${pkg}"
        if (ver) {
            uri.path += "/versions/${ver}"
        }

        uri.path += "/files"
        return uri.toString()
    }

    Map getPublishOption(Boolean include_unpublished){
        return ["include_unpublished" : (include_unpublished ? 1 : 0)]
    }

    String getPathSearch(){
        URIBuilder uri = new URIBuilder( "/search/file" )
        return uri.toString()
    }

}
