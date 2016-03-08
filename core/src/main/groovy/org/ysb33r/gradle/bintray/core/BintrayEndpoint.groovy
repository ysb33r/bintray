package org.ysb33r.gradle.bintray.core
import groovy.transform.CompileStatic

@CompileStatic
enum BintrayEndpoint {

    API_BASE_URL('https://api.bintray.com'),
    API_DL_URL('https://dl.bintray.com')

    final String text

    BintrayEndpoint(final String t) {text = t}
}