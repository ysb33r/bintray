package org.ysb33r.gradle.bintray.entitlements

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection

class Entitlements implements EntitlementsRequest {

    JsonBuilder getEntitlements(){
        assertAttributes(subject, repo)
        return btConn.RESTCall("get",getPath())
    }

}