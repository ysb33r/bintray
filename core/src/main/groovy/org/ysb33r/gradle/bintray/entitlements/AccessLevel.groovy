package org.ysb33r.gradle.bintray.entitlements
import groovy.transform.CompileStatic

@CompileStatic
enum AccessLevel {

    READWRITE('rw'),
    READ('r')

    final String text

    AccessLevel(final String t) {text = t}
}