package org.ysb33r.gradle.bintray.versions

import groovy.json.JsonBuilder
import groovy.transform.TupleConstructor

@TupleConstructor
class Version implements VersionRequest {
    String name

    JsonBuilder createVersion (String name = "", String description = "") {
        if (name) {
            body.content.name = name // Supports optional argument overriding body
        }
        if (description) {
            body.content.description = description // Supports optional argument overriding body
        }
        assertAttributes(subject, repo, pkg, body.content.name, body.content.desc)
        def result = this.bintrayClient.RESTCall("post", getPath(), body.toString())
        this.name = result?.name ?: this.name
        return result
    }

    JsonBuilder getVersion() {
        assertAttributes(name, subject, repo, pkg)
        return this.bintrayClient.RESTCall("get", getPath(name))
    }

    JsonBuilder deleteVersion() {
        assertAttributes(name, subject, repo, pkg)
        return this.bintrayClient.RESTCall("delete", getPath(name))
    }

    JsonBuilder updateVersion() {
        assertAttributes(name, subject, repo, pkg, body.content.name, body.content.desc)
        return this.bintrayClient.RESTCall("patch", getPath(name), body.toString())
    }

    JsonBuilder getLatestVersion() {
        this.name = '_latest'
        return getVersion()
    }
}