package org.ysb33r.gradle.bintray.files

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

@TupleConstructor
@CompileStatic
class Files implements FilesRequest {

    JsonSlurper slurper = new JsonSlurper()

    List getFiles(Boolean include_unpublished=false){
        assertAttributes(subject, repo)
        bintrayClient.RESTCall("get",getPath(), null, getPublishOption(include_unpublished)) as List
    }

    def searchFile(Map queryMap) {
        assertAttributes(queryMap)
        return this.bintrayClient.RESTCall("get",getPathSearch(), null, queryMap)
    }

}