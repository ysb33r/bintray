package org.ysb33r.gradle.bintray.files

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

@TupleConstructor
@CompileStatic
class Files implements FilesRequest {

    def getFiles(Boolean include_unpublished=false){
        assertAttributes(subject, repo)
        bintrayClient.RESTCall("get",getPath(), null, getPublishOption(include_unpublished))
    }

    def searchFile(Map queryMap) {
        assertAttributes(queryMap)
        bintrayClient.RESTCall("get",getPathSearch(), null, queryMap)
    }

}