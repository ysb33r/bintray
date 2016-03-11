package org.ysb33r.gradle.bintray.packages

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection
import org.ysb33r.gradle.bintray.versions.Version
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Ignore


class PackagesIntegTest extends Specification {

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
    String testDescription = "Bintray Library Test Package Description"

    @Shared
    Closure makeTestPackageObj = {testPackageName ->
        Package pkg = new Package().with {
            name = testPackageName
            btConn = btConnection
            subject = testOrg
            repo = testRepo
            return it
        }
        return pkg
    }

    @Shared
    Closure makeTestPackagesObj = {
        Packages pkgs = new Packages().with {
            btConn = btConnection
            subject = testOrg
            repo = testRepo
            return it
        }
        return pkgs
    }

    Closure makeTestBody = {pkgName ->
        JsonBuilder testBody = new PackagesBody().with {
            name = pkgName
            desc = "Bintray Library Integration Test Description"
            return toJson()
        }
        return testBody
    }

    @Ignore
    def "List all pkgs for a repo"() {
        setup:
        String testPkg = "testPkg-ListAllTest"
        Package pkg = makeTestPackageObj (testPkg)
        assert pkg.with {
            body = makeTestBody(testPkg)
            return it
        }.createPackage().content.name == testPkg
        assert pkg.getPackage().content.name ==  testPkg

        when:
        Packages pkgs = makeTestPackagesObj()

        then:
        pkgs.getPackages().find{it.name == "testPkg-ListAllTest"}

        cleanup:
        assert pkg.deletePackage().toString() == '{"message":"success"}'
        assert pkg.getPackage().toString() == '{"message":"Not Found","code":404}'
    }

    @Ignore
    def "Create a pkg"(){
        setup:
        String testPackage = "testPackage-CreateTest"
        Package pkg = makeTestPackageObj(testPackage)
        when:
        JsonBuilder result = pkg.with {
            body = makeTestBody(testPackage)
            return it
        }.createPackage()

        then:
        result.content.name == testPackage
        pkg.getPackage().content.name ==  testPackage

        cleanup:
        assert pkg.deletePackage().toString() == '{"message":"success"}'
        assert pkg.getPackage().toString() == '{"message":"Not Found","code":404}'

    }

    @Ignore
    def "Get a pkg"(){
        setup:
        String testPackage = "testPackage-GetTest"
        Package pkg = makeTestPackageObj(testPackage)
        JsonBuilder result = pkg.with {
            body = makeTestBody(testPackage)
            return it
        }.createPackage()
        assert result.content.name == testPackage

        expect:
        pkg.getPackage().content.name ==  testPackage

        cleanup:
        assert pkg.deletePackage().toString() == '{"message":"success"}'
        assert pkg.getPackage().toString() == '{"message":"Not Found","code":404}'
    }

    @Ignore
    def "Update a pkg"(){
        setup:
        String testPackage = "testPackage-UpdateTest"
        Package pkg = makeTestPackageObj(testPackage)
        JsonBuilder result = pkg.with {
            body = makeTestBody(testPackage)
            return it
        }.createPackage()
        assert result.content.name == testPackage
        assert pkg.getPackage().content.name ==  testPackage

        when:
        JsonBuilder result2 = pkg.with {
            body = makeTestBody(testPackage)
            body.content.desc = "Successfully Changed Description"
            return it
        }.updatePackage()

        then:
        result2.toString() == '{"message":"success"}'
        pkg.getPackage().content.desc ==  "Successfully Changed Description"

        cleanup:
        assert pkg.deletePackage().toString() == '{"message":"success"}'
        assert pkg.getPackage().toString() == '{"message":"Not Found","code":404}'
    }

    @Ignore
    def "Delete a pkg"(){
        setup:
        String testPackage = "testPackage-DeleteTest"
        Package pkg = makeTestPackageObj(testPackage)
        JsonBuilder result = pkg.with {
            body = makeTestBody(testPackage)
            return it
        }.createPackage()
        assert result.content.name == testPackage
        assert pkg.getPackage().content.name ==  testPackage

        expect:
        pkg.deletePackage().toString() == '{"message":"success"}'
        pkg.getPackage().toString() == '{"message":"Not Found","code":404}'

        cleanup:
        pkg.deletePackage()
    }


}