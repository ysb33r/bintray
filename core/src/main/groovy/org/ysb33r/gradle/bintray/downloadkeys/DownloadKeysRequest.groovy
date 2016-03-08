package org.ysb33r.gradle.bintray.downloadkeys

import groovyx.net.http.URIBuilder
import org.ysb33r.gradle.bintray.core.RequestBase
import org.ysb33r.gradle.bintray.core.HasSubject

trait DownloadKeysRequest implements RequestBase, HasSubject {

    String getPath(String keyId = "") {
        URIBuilder uri = new URIBuilder( "" )
        uri.path = "/${subjectType}/${subject}/download_keys"
        uri.path += (keyId) ? "/${keyId}" : ""
        return uri.toString()
    }
}
