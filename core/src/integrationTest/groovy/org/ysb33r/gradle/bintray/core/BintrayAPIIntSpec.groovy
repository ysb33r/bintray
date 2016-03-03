package org.ysb33r.gradle.bintray.core

import groovy.json.internal.LazyMap
import spock.lang.*

class BintrayAPIIntSpec extends Specification {

    BintrayAPI api = new BintrayAPI(
        new HashMap().with {
            baseUrl = "https://api.bintray.com"
            repoOwner = System.getenv('BINTRAY_ORG')
            repoName = System.getenv('BINTRAY_REPO')
            packageName = System.getenv('BINTRAY_PACKAGE')
            userName = System.getenv('BINTRAY_USERNAME')
            apiKey = System.getenv('BINTRAY_API_KEY')
            return it
        }
    )

    def "Get latest version of a package" () {
        when:
        def response = api.getLatestVersion()
        then:
        response.data."vcs_tag" == "0.1.0"
    }

    def "Get the files from a version of a package." () {
        when:
        def response = api.getFileListForVersion("0.1.0",true)
        response.data.each{LazyMap map -> println map}

        then:
        response.data[0].containsValue("azure-rest-0.1.0.jar")
    }

    def "Check to see if a file has been modified since last time." () {
        when:
        def response = api.getFileListForVersion("0.1.0",true)
        response.data.find{it.name == "azure-rest-0.1.0.jar"}."Last MOdif"

        then:
        response.data[0].containsValue("azure-rest-0.1.0.jar")
    }

}