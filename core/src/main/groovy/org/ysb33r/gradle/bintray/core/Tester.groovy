package org.ysb33r.gradle.bintray.core

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.entitlements.AccessLevel
import static AccessLevel.*

AccessLevel level = READ

def myObj = [level:level]

println new JsonBuilder(myObj)