package org.ysb33r.gradle.bintray.core

import groovyx.net.http.URIBuilder

def testUri = new URIBuilder(".")
testUri.path = "help"
println testUri