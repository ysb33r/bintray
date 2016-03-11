package org.ysb33r.gradle.bintray.packages

import groovy.json.JsonBuilder

class Package implements PackagesRequest {
    String name

    JsonBuilder createPackage (String name = "", String description = "") {
        if (name) {
            body.content.name = name // Supports optional argument overriding body
        }
        if (description) {
            body.content.description = description // Supports optional argument overriding body
        }
        assertAttributes(subject, repo, body.content.name, body.content.desc)
        def result = this.bintrayClient.RESTCall("post", getPath(), body.toString())
        this.name = result?.name ?: this.name
        return result
    }

    JsonBuilder getPackage() {
        assertAttributes(name, subject, repo)
        return this.bintrayClient.RESTCall("get", getPath(name))
    }

    JsonBuilder deletePackage() {
        assertAttributes(name, subject, repo)
        return this.bintrayClient.RESTCall("delete", getPath(name))
    }

    JsonBuilder updatePackage() {
        assertAttributes(name, subject, repo, body.content.name, body.content.desc)
        return this.bintrayClient.RESTCall("patch", getPath(name), body.toString())
    }

}