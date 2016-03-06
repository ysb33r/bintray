package org.ysb33r.gradle.bintray.files

import groovyx.net.http.URIBuilder
import org.ysb33r.gradle.bintray.core.ApiBase
import org.ysb33r.gradle.bintray.core.HasPackage
import org.ysb33r.gradle.bintray.core.HasRepo
import org.ysb33r.gradle.bintray.core.HasSubject
import org.ysb33r.gradle.bintray.core.HasVersion

trait FilesRequest implements ApiBase, HasSubject, HasRepo, HasPackage, HasVersion {

    Boolean include_unpublished
    URIBuilder getPath(){
        URIBuilder uri = new URIBuilder( baseUrl )
        uri.path = "/packages/${subject}/${repo}/${pkg}"
        if (version) {
            uri.path += "/versions/${version}"
        }

        uri.path += "/files"
        return uri
    }

    Map getPublishOption(){
        return ["include_unpublished" : (include_unpublished ? 1 : 0)]
    }

    URIBuilder getPathSearch(){
        URIBuilder uri = new URIBuilder( baseUrl )
        uri.path = "/search/file"
        return uri
    }

}
