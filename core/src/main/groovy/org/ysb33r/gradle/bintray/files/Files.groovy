package org.ysb33r.gradle.bintray.files

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection

class Files implements BintrayConnection, FilesRequest {

    JsonBuilder getFiles(Boolean include_unpublished){
        assertAttributes(subject, repo)
        return RESTCall("get",getPath(), null, getPublishOption(include_unpublished))
    }

    JsonBuilder searchFile(Map queryMap) {
        assertAttributes(queryMap)
        return RESTCall("get",getPathSearch(), null, queryMap)
    }

}