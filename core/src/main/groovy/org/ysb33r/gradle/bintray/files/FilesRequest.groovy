package org.ysb33r.gradle.bintray.files

import groovyx.net.http.URIBuilder
import org.ysb33r.gradle.bintray.core.HasPackage
import org.ysb33r.gradle.bintray.core.HasRepo
import org.ysb33r.gradle.bintray.core.HasSubject
import org.ysb33r.gradle.bintray.core.HasVersion
import org.ysb33r.gradle.bintray.core.RequestBase

trait FilesRequest implements RequestBase, HasSubject, HasRepo, HasPackage, HasVersion {

    String getPath(){
        URIBuilder uri = new URIBuilder( "" )
        uri.path = "/packages/${subject}/${repo}/${pkg}"
        if (this.ver) {
            uri.path += "/versions/${this.ver}"
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
