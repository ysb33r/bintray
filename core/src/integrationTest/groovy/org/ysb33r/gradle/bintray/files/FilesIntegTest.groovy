package org.ysb33r.gradle.bintray.files

import groovy.json.JsonBuilder
import spock.lang.Shared
import spock.lang.Specification
import static org.ysb33r.gradle.bintray.core.SubjectType.orgs


class FilesIntegTest extends Specification {

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
    String testVersion = "0.1.0"
    @Shared
    String testFileName = "azure-rest-0.1.0.jar"
    @Shared
    String testFileSha1 = "01a3cb87127d3cc886d5d485f31304c2ac5239e5"
    @Shared
    Closure makeTestFiles = {
        Files files = new Files().with {
            userName = btUserName
            apiKey = btApiKey
            subjectType = orgs
            subject = testOrg
            client()
            return it
        }
        return files
    }

    def "List all Files for a package"() {
        setup: Files files = makeTestFiles()
        when:
        JsonBuilder result = files.with{
            repo = testRepo
            pkg = testPkg
            return it
        }.getFiles()

        then:
        result.getContent()[0].containsKey("name")
    }

    def "List all Files for version"() {
        setup: Files files = makeTestFiles()
        when:
        JsonBuilder result = files.with{
            repo = testRepo
            pkg = testPkg
            version = testVersion
            return it
        }.getFiles()

        then:
        println result
        result.getContent()[0].containsKey("name")
    }

    def "Search file by name in a repo"(){
        setup: Files files = makeTestFiles()

        when:
        JsonBuilder result = files.searchFile(
            [name:testFileName, subject: testOrg, repo:testRepo]
        )

        then:
        result.getContent().toString() == '{"access":"r","download_keys":["testdlkey-allFiletests"],"id":"testFile-CreateTest"}'

    }

}