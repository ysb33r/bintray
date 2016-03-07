package org.ysb33r.gradle.bintray.downloadkeys

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection

class DownloadKeys implements BintrayConnection, DownloadKeysRequest  {

    JsonBuilder getDownloadKeys(){
        assertAttributes(subject, subjectType)
        return RESTCall("get",getPath())
    }

}