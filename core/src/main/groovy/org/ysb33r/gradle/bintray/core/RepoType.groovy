package org.ysb33r.gradle.bintray.core
import groovy.transform.CompileStatic

@CompileStatic
enum RepoType {

    MAVEN('maven'),
    DEBIAN('debian'),
    YUM('yum'),
    NUGET('nuget'),
    VAGRANT('vagrant'),
    DOCKER('docker')

    final String text

    RepoType(final String t) {text = t}
}