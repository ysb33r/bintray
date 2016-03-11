package org.ysb33r.gradle.bintray.entitlements

import groovy.json.JsonBuilder

class Entitlement implements EntitlementsRequest{
    String id
    JsonBuilder createEntitlement(String id = "") {
        if (id) {
            body.content.id = id // Supports optional argument overriding body
        }
        assertAttributes(subject, repo, body.content."access")
        def result = this.bintrayClient.RESTCall("post", getPath(), body.toString())
        this.id = result?.id ?: this.id
        return result
    }

    JsonBuilder getEntitlement(){
        assertAttributes(id, subject)
        return this.bintrayClient.RESTCall("get", getPath(id))
    }

    JsonBuilder deleteEntitlement() {
        assertAttributes(id, subject)
        return this.bintrayClient.RESTCall("delete", getPath(id))
    }

    JsonBuilder updateEntitlement() {
        assertAttributes(id, subject, body.content)
        return this.bintrayClient.RESTCall("patch", getPath(id), body.toString())
    }
}
