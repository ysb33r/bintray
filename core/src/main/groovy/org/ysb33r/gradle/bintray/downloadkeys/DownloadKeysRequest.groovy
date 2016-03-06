package org.ysb33r.gradle.bintray.downloadkeys

import groovyx.net.http.URIBuilder
import org.ysb33r.gradle.bintray.core.ApiBase
import org.ysb33r.gradle.bintray.core.HasSubject

trait DownloadKeysRequest implements ApiBase, HasSubject{
    String keyId
    URIBuilder getPath(){
        URIBuilder uri = new URIBuilder( baseUrl )
        uri.path = "/${subjectType}/${subject}/download_keys"
        uri.path += (keyId) ? "/${keyId}": ""
        return uri
    }
}
