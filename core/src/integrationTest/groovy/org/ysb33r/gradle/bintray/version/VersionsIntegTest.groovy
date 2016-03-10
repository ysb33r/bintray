package org.ysb33r.gradle.bintray.version

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection
import org.ysb33r.gradle.bintray.versions.Version
import org.ysb33r.gradle.bintray.versions.VersionsBody
import org.ysb33r.gradle.bintray.versions.Versions
import spock.lang.Shared
import spock.lang.Specification


class VersionsIntegTest extends Specification {

    @Shared
    BintrayConnection btConnection = new BintrayConnection().with{
        userName = System.getenv('BINTRAY_USERNAME')
        apiKey = System.getenv('BINTRAY_API_KEY')
        return it
    }
    @Shared
    String testOrg = "getgsi"
    @Shared
    String testRepo = "genius-public"
    @Shared
    String testPkg = "genius-bootstrapper"

    @Shared
    Closure makeTestVersionObj = {testVersionName ->
        Version version = new Version().with {
            name = testVersionName
            btConn = btConnection
            subject = testOrg
            repo = testRepo
            pkg = testPkg
            return it
        }
        return version
    }

    @Shared
    Closure makeTestVersionsObj = {
        Versions versions = new Versions().with {
            btConn = btConnection
            subject = testOrg
            repo = testRepo
            pkg = testPkg
            return it
        }
        return versions
    }

    Closure makeTestBody = {versionName ->
        JsonBuilder testBody = new VersionsBody().with {
            name = versionName
            desc = "Bintray Library Integration Test Description"
            vcs_tag = "0.0.1"
            return toJson()
        }
        return testBody
    }

    def "List all versions for a package"() {
        setup:
        String testVersion = "testVersion-ListAllTest"
        Version version = makeTestVersionObj(testVersion)
        assert version.with {
            body = makeTestBody(testVersion)
            return it
        }.createVersion().content.name == testVersion
        assert version.getVersion().content.name ==  testVersion

        when:
        Versions versions = makeTestVersionsObj()

        then:
        versions.getVersions().contains("testVersion-ListAllTest")

        cleanup:
        assert version.deleteVersion().toString() == '{"message":"success"}'
        assert version.getVersion().toString() == '{"message":"Not Found","code":404}'
    }

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