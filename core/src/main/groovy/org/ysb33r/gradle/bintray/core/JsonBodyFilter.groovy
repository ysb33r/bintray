package org.ysb33r.gradle.bintray.core

import groovy.json.JsonBuilder

trait JsonBodyFilter {
    JsonBuilder toJson(){
        new JsonBuilder(this.properties.findAll {it.key!="class" && it.value})
    }
}