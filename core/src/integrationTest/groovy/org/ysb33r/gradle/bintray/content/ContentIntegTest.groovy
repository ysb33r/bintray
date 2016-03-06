package org.ysb33r.gradle.bintray.content

import groovy.json.JsonBuilder

import spock.lang.Shared
import spock.lang.Specification

class ContentIntegTest extends Specification {

    @Shared
    String btUserName = System.getenv('BINTRAY_USERNAME')
    @Shared
    String btApiKey = System.getenv('BINTRAY_API_KEY')
    @Shared
    String testOrg = "getgsi"
    @Shared
    String testRepo = "genius"
    @Shared
    String testPkg = "genius-modules"
    @Shared
    String testFile = "azure-rest-0.1.0.jar"
    @Shared
    String testPath = "com/gsi/genius/gradle/azure-rest/0.1.0/"
    @Shared
    String testFileDynamic = "azure-rest-\$latest.jar"
    @Shared
    String testPathDynamic = "com/gsi/genius/gradle/azure-rest/\$latest/"
    @Shared
    Closure makeTestContent = {
        Content content = new Content().with {
            userName = btUserName
            apiKey = btApiKey
            subject = testOrg
            client()
            return it
        }
        return content
    }

    def "Download (getContent) a fully named file in a repo"() {
        //TODO, Figure out why this returns 404, URL works
        setup: Content content = makeTestContent()
        when:
        JsonBuilder result = content.with{
            repo = testRepo
            return it
        }.getContent("${testPath}${testFile}")
        println result
        println content.getPath()

        then:
        result instanceof JsonBuilder
    }

    def "Download (getContent) a dynamically named file in a repo"() {
        setup: Content content = makeTestContent()
        when:
        def result = content.with{
            repo = testRepo
            return it
        }.getContent("${testPathDynamic}${testFileDynamic}",[bt_package:testPkg])

        then:
        result instanceof ByteArrayInputStream
    }


}