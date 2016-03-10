package org.ysb33r.gradle.bintray.downloadkeys

import groovyx.net.http.URIBuilder
import org.ysb33r.gradle.bintray.core.RequestBase

trait DownloadKeysRequest implements RequestBase {

    String subject
    String subjectType

    String getPath(String keyId = "") {
        URIBuilder uri = new URIBuilder( "" )
        uri.path = "/${subjectType}/${subject}/download_keys"
        uri.path += (keyId) ? "/${keyId}" : ""
        return uri.toString()
    }
}
