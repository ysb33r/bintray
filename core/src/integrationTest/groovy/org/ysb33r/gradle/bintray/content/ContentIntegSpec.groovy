package org.ysb33r.gradle.bintray.content

import org.ysb33r.gradle.bintray.BetamaxSpecification
import org.ysb33r.gradle.bintray.core.BintrayClientFactory
import org.ysb33r.gradle.bintray.files.Files
import software.betamax.TapeMode
import software.betamax.junit.Betamax
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

import java.security.MessageDigest

import static org.ysb33r.gradle.bintray.core.SubjectType.orgs

class ContentIntegSpec extends BetamaxSpecification {

//    @Shared
//    BintrayClientFactory btConnection = new BintrayClientFactory().with{
//        userName = System.getenv('BINTRAY_USERNAME')
//        apiKey = System.getenv('BINTRAY_API_KEY')
//        return it
//    }
//    @Shared
//    String testOrg = "getgsi"
//    @Shared
//    String testRepo = "genius"
//    @Shared
//    String testPkg = "genius-modules"
//    @Shared
//    String testVersion = "0.1.0"
//    @Shared
//    String testFile = "azure-rest-${testVersion}.jar"
//    @Shared
//    String testPath = "com/gsi/genius/gradle/azure-rest/${testVersion}/"
//    @Shared
//    String testFileDynamic = "azure-rest-\$latest.jar"
//    @Shared
//    String testPathDynamic = "com/gsi/genius/gradle/azure-rest/\$latest/"
//
//    @Shared
//    Closure makeTestContentObj = {
////        Content content = new Content().with {
////            Content.this.bintrayClient = btConnection
////            subject = testOrg
////            return it
////        }
////        return content
//    }
    boolean sha1Hash ( InputStream content ) {
        def digest = MessageDigest.getInstance("SHA1")
        content.eachByte(4096) {bytes, len -> digest.update(bytes, 0, len) }
        def sha1Hex = digest.digest().encodeHex()
        sha1Hex.toString()
    }

//    @Betamax(tape='content',mode=TapeMode.READ_WRITE)
    @Betamax(tape='content')
    @Ignore
    def "Download (getContent) a statically named path in a repo"() {
        given:
        Files files = new Files(
            bintrayClient: apiClient,
            subject : BINTRAY_RO_ORG,
            repo : BINTRAY_RO_REPO,
            pkg : BINTRAY_RO_PKG,
            ver : '1.2'
        )
        def file = files.getFiles().find { it.name = 'bintray-1.2-sources.jar.sha1' }
        String sha1_expected = file.text.sha1
        String sha1_downloaded

        Content content = new Content(
            bintrayClient: dlClient,
//            subject : BINTRAY_RO_ORG,
            repo : BINTRAY_RO_REPO,
            pkg : BINTRAY_RO_PKG,
            filePath : file.path
        )

        when:
        content.withInputStream { istream ->
            sha1_downloaded = sha1Hash(istream)
        }

        then:
        sha1_expected == sha1_downloaded

//        setup:
//        Content content = makeTestContentObj()
//
//        def expectedHash = new Files().with {
////            Files.this.bintrayClient = btConnection
//            subjectType = orgs
//            subject = testOrg
//            repo = testRepo
//            pkg = testPkg
//            ver = testVersion
//            return it
//        }.getFiles(true).content.find{it['name']==testFile}['sha1']
//
//
//        when:
//        ByteArrayInputStream result = content.with {
//            filePath = "${testPath}${testFile}"
//            repo = testRepo
//            pkg = testPkg
//            return it
//        }.downloadContent()
//
//        then:
//        result instanceof ByteArrayInputStream
//        verifySha1Hash(result, expectedHash)
    }

    @Ignore
    def "Download (getContent) a dynamically named file in a repo"() {
        setup:
        Content content = makeTestContentObj()
        def expectedHash = new Files().with {
//            Files.this.bintrayClient = btConnection
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