package org.ysb33r.gradle.bintray.files

import groovy.json.JsonBuilder

class Files implements FilesRequest {

    JsonBuilder getFiles(Boolean include_unpublished){
        assertAttributes(subject, repo)
        return this.bintrayClient.RESTCall("get",getPath(), null, getPublishOption(include_unpublished))
    }

    JsonBuilder searchFile(Map queryMap) {
        assertAttributes(queryMap)
        return this.bintrayClient.RESTCall("get",getPathSearch(), null, queryMap)
    }

}