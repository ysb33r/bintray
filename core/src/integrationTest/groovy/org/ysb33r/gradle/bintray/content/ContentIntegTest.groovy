package org.ysb33r.gradle.bintray.content

import org.ysb33r.gradle.bintray.core.BintrayConnection
import org.ysb33r.gradle.bintray.files.Files
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

import java.security.MessageDigest

import static org.ysb33r.gradle.bintray.core.SubjectType.orgs

class ContentIntegTest extends Specification {
    @Shared
    BintrayConnection btConnection = new BintrayConnection().with{
        userName = System.getenv('BINTRAY_USERNAME')
        apiKey = System.getenv('BINTRAY_API_KEY')
        return it
    }
    @Shared
    String testOrg = "getgsi"
    @Shared
    String testRepo = "genius"
    @Shared
    String testPkg = "genius-modules"
    @Shared
    String testVersion = "0.1.0"
    @Shared
    String testFile = "azure-rest-${testVersion}.jar"
    @Shared
    String testPath = "com/gsi/genius/gradle/azure-rest/${testVersion}/"
    @Shared
    String testFileDynamic = "azure-rest-\$latest.jar"
    @Shared
    String testPathDynamic = "com/gsi/genius/gradle/azure-rest/\$latest/"

    @Shared
    Closure makeTestContentObj = {
        Content content = new Content().with {
            btConn = btConnection
            subject = testOrg
            return it
        }
        return content
    }
    @Shared
    Closure verifySha1Hash = { content, expectedHash ->
        def digest = MessageDigest.getInstance("SHA1")
        content.eachByte(4096) {bytes, len -> digest.update(bytes, 0, len) }
        def sha1Hex = digest.digest().encodeHex()
        return (sha1Hex.toString() == expectedHash)
    }

    @Ignore
    def "Download (getContent) a statically named path in a repo"() {
        setup:
        Content content = makeTestContentObj()

        def expectedHash = new Files().with {
            btConn = btConnection
            subjectType = orgs
            subject = testOrg
            repo = testRepo
            pkg = testPkg
            ver = testVersion
            return it
        }.getFiles(true).content.find{it['name']==testFile}['sha1']


        when:
        ByteArrayInputStream result = content.with {
            filePath = "${testPath}${testFile}"
            repo = testRepo
            pkg = testPkg
            return it
        }.downloadContent()

        then:
        result instanceof ByteArrayInputStream
        verifySha1Hash(result, expectedHash)
    }

    @Ignore
    def "Download (getContent) a dynamically named file in a repo"() {
        setup:
        Content content = makeTestContentObj()
        def expectedHash = new Files().with {
            btConn = btConnection
            subjectType = orgs
            subject = testOrg
            repo = testRepo
            pkg = testPkg
            ver = testVersion
            return it
        }.getFiles().content.find{it['name']==testFile}['sha1']

        when:
        def result = content.with {
            filePath = "${testPath}${testFile}"
            repo = testRepo
            pkg = testPkg
            return it
        }.downloadContent(true, [bt_package: testPkg])

        then:
        result instanceof ByteArrayInputStream
        verifySha1Hash(result, expectedHash)

    }


}