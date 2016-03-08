package org.ysb33r.gradle.bintray.downloadkeys

import groovy.json.JsonBuilder

class DownloadKeys implements DownloadKeysRequest  {

    JsonBuilder getDownloadKeys(){
        assertAttributes(subject, subjectType)
        return btConn.RESTCall("get",getPath())
    }

}