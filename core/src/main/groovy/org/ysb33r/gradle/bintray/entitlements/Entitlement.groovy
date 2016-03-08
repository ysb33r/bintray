package org.ysb33r.gradle.bintray.entitlements

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection


class Entitlement implements EntitlementsRequest{
    String id
    JsonBuilder createEntitlement(String id = "") {
        if (id) {
            body.content."id" = id // Supports optional argument overriding body
        }
        assertAttributes(subject, repo, body.content."access")
        def result = btConn.RESTCall("post", getPath(), body.toString())
        this.id = result?.content?."id" ?: this.id
        return result
    }

    JsonBuilder getEntitlement(){
        assertAttributes(id, subject)
        return btConn.RESTCall("get", getPath(id))
    }

    JsonBuilder deleteEntitlement() {
        assertAttributes(id, subject)
        return btConn.RESTCall("delete", getPath(id))
    }

    JsonBuilder updateEntitlement() {
        assertAttributes(id, subject, body.content)
        return btConn.RESTCall("patch", getPath(id), body.toString())
    }
}
