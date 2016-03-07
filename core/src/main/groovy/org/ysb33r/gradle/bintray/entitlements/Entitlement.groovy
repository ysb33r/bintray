package org.ysb33r.gradle.bintray.entitlements

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection


class Entitlement implements BintrayConnection, EntitlementsRequest{
    String id
    JsonBuilder createEntitlement(String id = "") {
        if (id) {
            body.content."id" = id // Supports optional argument overriding body
        }
        def result = RESTCall("post", getPath(), body.toString())
        assertAttributes(subject, repo, body.content."access")
        this.id = result?.content?."id" ?: this.id
        return result
    }

    JsonBuilder getEntitlement(){
        assertAttributes(id, subject)
        return RESTCall("get", getPath(id))
    }

    JsonBuilder deleteEntitlement() {
        assertAttributes(id, subject)
        return RESTCall("delete", getPath(id))
    }

    JsonBuilder updateEntitlement() {
        assertAttributes(id, subject, body.content)
        return RESTCall("patch", getPath(id), body.toString())
    }
}
