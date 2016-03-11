package org.ysb33r.gradle.bintray.packages

import groovy.json.JsonBuilder
import groovy.transform.TupleConstructor

@TupleConstructor
class Packages implements PackagesRequest {

    List getPackages(Map queryMap = [:]) {
        assertAttributes(subject, repo)
        return btConn.RESTCall("get", getPathGetPackages(), null, queryMap)
    }

    List searchPackages(Map queryMap) {
        assertAttributes(queryMap)
        return btConn.RESTCall("get",getPathSearch(), null, queryMap)
    }

    List searchPackagesMaven(Map queryMap, String groupId, String artifactId) {
        assertAttributes(queryMap)
        return btConn.RESTCall("get",getPathSearchMaven(groupId, artifactId), null, queryMap)
    }
}
