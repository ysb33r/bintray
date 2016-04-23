package org.ysb33r.gradle.bintray.packages

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.BetamaxSpecification
import software.betamax.junit.Betamax
import spock.lang.Ignore


class PackagesIntegSpec extends BetamaxSpecification {

//    @Shared
//    BintrayConnection btClient = new BintrayConnection().with{
//        userName = System.getenv('BINTRAY_USERNAME')
//        apiKey = System.getenv('BINTRAY_API_KEY')
//        return it
//    }
//    @Shared
//    String testOrg = "getgsi"
//    @Shared
//    String testRepo = "genius-public"
//    @Shared
//    String testDescription = "Bintray Library Test Package Description"
//
//    @Shared
//    Closure makeTestPackageObj = {testPackageName ->
//        Package pkg = new Package().with {
//            name = testPackageName
//            bintrayClient = btClient
//            subject = testOrg
//            repo = testRepo
//            return it
//        }
//        return pkg
//    }
//
//    @Shared
//    Closure makeTestPackagesObj = {
//        Packages pkgs = new Packages().with {
//            bintrayClient = btClient
//            subject = testOrg
//            repo = testRepo
//            return it
//        }
//        return pkgs
//    }
//
//    Closure makeTestBody = {pkgName ->
//        JsonBuilder testBody = new PackagesBody().with {
//            name = pkgName
//            desc = "Bintray Library Integration Test Description"
//            return toJson()
//        }
//        return testBody
//    }


    @Betamax(tape='packages')
//    @Betamax(tape='packages',mode=TapeMode.READ_WRITE)
    def "List all pkgs for a repo"() {
        given:
        Packages pkgs = new Packages(
            bintrayClient: apiClient,
            subject : BINTRAY_RO_ORG,
            repo : BINTRAY_RO_REPO
        )

        when:
        def list = pkgs.packages

        then:
        list.any { it.name == 'bintray-gradle-plugin' }
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