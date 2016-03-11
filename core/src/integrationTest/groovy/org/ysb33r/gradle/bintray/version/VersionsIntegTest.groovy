package org.ysb33r.gradle.bintray.version

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.BetamaxSpecification
import org.ysb33r.gradle.bintray.versions.Version
import org.ysb33r.gradle.bintray.versions.Versions
import software.betamax.junit.Betamax
import spock.lang.Ignore

class VersionsIntegTest extends BetamaxSpecification {


//    @Shared
//    BintrayConnection btConnection = new BintrayConnection().with{
//        userName = System.getenv('BINTRAY_USERNAME')
//        apiKey = System.getenv('BINTRAY_API_KEY')
//        return it
//    }
//    @Shared
//    String testOrg = "getgsi"
//    @Shared
//    String testRepo = "genius-public"
//    @Shared
//    String testPkg = "genius-bootstrapper"
//
//    @Shared
//    Closure makeTestVersionObj = {testVersionName ->
//        Version version = new Version().with {
//            name = testVersionName
//            bintrayClient = btConnection
//            subject = testOrg
//            repo = testRepo
//            pkg = testPkg
//            return it
//        }
//        return version
//    }
//
//    @Shared
//    Closure makeTestVersionsObj = {
//        Versions versions = new Versions().with {
//            bintrayClient = btConnection
//            subject = testOrg
//            repo = testRepo
//            pkg = testPkg
//            return it
//        }
//        return versions
//    }
//
//    Closure makeTestBody = {versionName ->
//        JsonBuilder testBody = new VersionsBody().with {
//            name = versionName
//            desc = "Bintray Library Integration Test Description"
//            vcs_tag = "0.0.1"
//            return toJson()
//        }
//        return testBody
//    }

//    @Betamax(tape="versions",mode=TapeMode.READ_WRITE)
    @Betamax(tape="versions")
    def "List all versions for a package"() {


        given:
        Versions versions = new Versions(
            bintrayClient: apiClient,
            subject : BINTRAY_RO_ORG,
            repo    : BINTRAY_RO_REPO,
            pkg     : BINTRAY_RO_PKG
        )

        when:
        def list = versions.getVersions()

        then:
        list.contains('1.6.1')
        list.contains('1.1')
        list.contains('0.0.5')
    }

    @Ignore
    def "Get the latest version for a package"() {

        setup:
        String testVersion = "testVersion-GetLatestTest"
        Version version = makeTestVersionObj(testVersion)
        assert version.with {
            body = makeTestBody(testVersion)
            return it
        }.createVersion().content.name == testVersion
        assert version.getVersion().content.name ==  testVersion

        when:
        JsonBuilder result = version.getLatestVersion()

        then:
        result.content.created

        cleanup:
        assert version.deleteVersion().toString() == '{"message":"success"}'
        assert version.getVersion().toString() == '{"message":"Not Found","code":404}'

    }

    @Ignore
    def "Create a version"(){
        setup:
        String testVersion = "testVersion-CreateTest"
        Version version = makeTestVersionObj(testVersion)
        when:
        JsonBuilder result = version.with {
            body = makeTestBody(testVersion)
            return it
        }.createVersion()

        then:
        result.content.name == testVersion
        version.getVersion().content.name ==  testVersion

        cleanup:
        assert version.deleteVersion().toString() == '{"message":"success"}'
        assert version.getVersion().toString() == '{"message":"Not Found","code":404}'

    }

    @Ignore
    def "Get a version"(){
        setup:
        String testVersion = "testVersion-GetTest"
        Version version = makeTestVersionObj(testVersion)
        JsonBuilder result = version.with {
            body = makeTestBody(testVersion)
            return it
        }.createVersion()
        assert result.content.name == testVersion

        expect:
        version.getVersion().content.name ==  testVersion

        cleanup:
        assert version.deleteVersion().toString() == '{"message":"success"}'
        assert version.getVersion().toString() == '{"message":"Not Found","code":404}'

    }

    @Ignore
    def "Update a version"(){
        setup:
        String testVersion = "testVersion-UpdateTest"
        Version version = makeTestVersionObj(testVersion)
        JsonBuilder result = version.with {
            body = makeTestBody(testVersion)
            return it
        }.createVersion()
        assert result.content.name == testVersion
        assert version.getVersion().content.name ==  testVersion

        when:
        JsonBuilder result2 = version.with {
            body = makeTestBody(testVersion)
            body.content.desc = "Successfully Changed Description"
            return it
        }.updateVersion()

        then:
        result2.toString() == '{"message":"success"}'
        version.getVersion().content.desc ==  "Successfully Changed Description"

        cleanup:
        assert version.deleteVersion().toString() == '{"message":"success"}'
        assert version.getVersion().toString() == '{"message":"Not Found","code":404}'

    }

    @Ignore
    def "Delete a version"(){
        setup:
        String testVersion = "testVersion-DeleteTest"
        Version version = makeTestVersionObj(testVersion)
        JsonBuilder result = version.with {
            body = makeTestBody(testVersion)
            return it
        }.createVersion()
        assert result.content.name == testVersion
        assert version.getVersion().content.name ==  testVersion

        expect:
        version.deleteVersion().toString() == '{"message":"success"}'
        version.getVersion().toString() == '{"message":"Not Found","code":404}'

        cleanup:
        version.deleteVersion()
    }


}