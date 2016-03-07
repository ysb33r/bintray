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
    String testOrg = "karfunkel"
    @Shared
    String testRepo = "glow"
    @Shared
    String testPkg = "glow"
    @Shared
    String testVersion = "0.1.0"
    @Shared
    String testFileName = "glow-0.1.0.jar"
    @Shared
    String testFileSha1 = "a904fade6a7751350b4639f54a562f7ab0efab38"
    @Shared
    Closure makeTestFilesObj = {
        Files files = new Files().with {
            userName = btUserName
            apiKey = btApiKey
            subjectType = orgs
            subject = testOrg
            return it
        }
        return files
    }

    def "List all Files for a package"() {
        setup: Files files = makeTestFilesObj()
        when:
        JsonBuilder result = files.with{
            repo = testRepo
            pkg = testPkg
            return it
        }.getFiles()

        then:
        result.getContent()
    }

    def "List all Files for version"() {
        setup: Files files = makeTestFilesObj()
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

    //Bintray states search currently not supported on private repos
    def "Search file by name in a repo by name, package, and version"(){
        setup: Files files = makeTestFilesObj()
        when:
        JsonBuilder result = files.searchFile(
            [name:testFileName, subject: testOrg, repo:testRepo]
        )
        then:
        result.getContent()

    }

    //Bintray states search currently not supported on private repos
    def "Search file by name in a repo by SHA1, package, and version"(){
        setup: Files files = makeTestFilesObj()
        when:
        JsonBuilder result = files.searchFile(
                [sha1:testFileSha1, subject: testOrg, repo:testRepo]
        )
        then:
        result.getContent()
    }
}