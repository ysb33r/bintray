package org.ysb33r.gradle.bintray.core

import groovy.json.JsonBuilder

trait RequestBase {

    JsonBuilder body

    static def assertAttributes(Object... attributes) {
        attributes.each {
            assert it?.toString().size()
        }
    }

}