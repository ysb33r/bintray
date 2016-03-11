package org.ysb33r.gradle.bintray.entitlements

import groovy.json.JsonBuilder

class Entitlements implements EntitlementsRequest {

    JsonBuilder getEntitlements(){
        assertAttributes(subject, repo)
        return this.bintrayClient.RESTCall("get",getPath())
    }

}