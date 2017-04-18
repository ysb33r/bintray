package org.ysb33r.gradle.bintray.core
import groovy.transform.CompileStatic

@CompileStatic
enum SubjectType {

    users('users'),
    orgs('orgs')

    final String text

    SubjectType(final String t) {text = t}
}