package org.ysb33r.gradle.bintray.content

import org.ysb33r.gradle.bintray.core.BaseIntegTest
import org.ysb33r.gradle.bintray.files.Files
import spock.lang.Shared
import java.security.MessageDigest


class ContentIntegTest extends BaseIntegTest {

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

    def "Download (getContent) a statically named path in a repo"() {
        setup:
        Content content = makeTestContentObj()

        def expectedHash = new Files().with {
            btConn = btConnection
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

    def "Download (getContent) a dynamically named file in a repo"() {
        setup:
        Content content = makeTestContentObj()
        def expectedHash = new Files().with {
            btConn = btConnection
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