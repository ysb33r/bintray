package org.ysb33r.gradle.bintray.entitlements

import groovy.json.JsonBuilder
import spock.lang.Ignore

import static AccessLevel.*
import spock.lang.Shared
import spock.lang.Specification
import org.ysb33r.gradle.bintray.downloadkeys.DownloadKey
import static org.ysb33r.gradle.bintray.core.SubjectType.orgs

class EntitlementsIntegTest extends Specification {

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
    String testDlKey = "testdlkey-allentitlementtests"
    @Shared
    List testDlKeys = [testDlKey]
    @Shared
    AccessLevel testAccessLevel = READ
    @Shared
    DownloadKey downloadKey
    @Shared
    Closure makeTestEntitlementObj = {
        Entitlement entitlement = new Entitlement().with {
            userName = btUserName
            apiKey = btApiKey
            subject = testOrg
            return it
        }
        return entitlement
    }
    @Shared
    Closure makeTestEntitlementsObj = {
        Entitlements entitlements = new Entitlements().with {
            userName = btUserName
            apiKey = btApiKey
            subject = testOrg
            return it
        }
        return entitlements
    }
    @Shared
    Closure makeTestBody = {entitlementId ->
        JsonBuilder testBody = new EntitlementsBody().with {
            access = testAccessLevel.text
            download_keys = testDlKeys
            id = entitlementId
            return toJson()
        }
        return testBody
    }

    def setupSpec(){
        downloadKey = new DownloadKey().with {
            userName = btUserName
            apiKey = btApiKey
            subjectType = orgs
            subject = testOrg
            body = new JsonBuilder([id:testDlKey])
            return it
        }
        downloadKey.createDownloadKey()
        downloadKey.id = testDlKey
        assert downloadKey.getDownloadKey().content.containsKey("username")
    }


    def "List all entitlements for repo"() {
        setup:
        Entitlement entitlement = makeTestEntitlementObj()
        String testEntitlementId = "testEntitlement-ListAllEntitlementsAtRepoTest"
        entitlement.with{
            repo = testRepo
            subject = testOrg
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()
        entitlement.id = testEntitlementId

        when:
        Entitlements entitlements = makeTestEntitlementsObj()
        JsonBuilder result = entitlements.with{
            repo = testRepo
            return it
        }.getEntitlements()

        then:
        result.toString() == '{"id":"testEntitlement-ListAllEntitlementsAtRepoTest"}'

        cleanup:
        assert entitlement.deleteEntitlement().toString() == '{"message":"success"}'
        assert entitlement.getEntitlement().toString() == '{"message":"Not Found","code":404}'
    }

    def "List all entitlements for package"() {
        setup:
        Entitlement entitlement = makeTestEntitlementObj()
        String testEntitlementId = "testEntitlement-ListAllEntitlementsAtPkgTest"
        entitlement.with{
            repo = testRepo
            subject = testOrg
            pkg = testPkg
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()
        entitlement.id = testEntitlementId

        when:
        Entitlements entitlements = makeTestEntitlementsObj()
        JsonBuilder result = entitlements.with{
            repo = testRepo
            pkg = testPkg
            return it
        }.getEntitlements()

        then:
        result.toString() == '{"id":"testEntitlement-ListAllEntitlementsAtPkgTest"}'

        cleanup:
        assert entitlement.deleteEntitlement().toString() == '{"message":"success"}'
        assert entitlement.getEntitlement().toString() == '{"message":"Not Found","code":404}'
    }

    @Ignore //entitlements currently do not work for versions of maven repos, bintray said working on it
    def "List all entitlements for version"() {
        setup:
        Entitlement entitlement = makeTestEntitlementObj()
        String testEntitlementId = "testEntitlement-ListAllEntitlementsAtVersionTest"
        entitlement.with{
            repo = testRepo
            subject = testOrg
            pkg = testPkg
            version = testVersion
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()
        entitlement.id = testEntitlementId

        when:
        Entitlements entitlements = makeTestEntitlementsObj()
        JsonBuilder result = entitlements.with{
            repo = testRepo
            pkg = testPkg
            version = testVersion
            return it
        }.getEntitlements()

        then:
        result.toString() == '{"id":"testEntitlement-ListAllEntitlementsAtVersionTest"}'

        cleanup:
        assert entitlement.deleteEntitlement().toString() == '{"message":"success"}'
        assert entitlement.getEntitlement().toString() == '{"message":"Not Found","code":404}'
    }

    def "Create an entitlement for a repo"() {
        when:
        Entitlement entitlement = makeTestEntitlementObj()
        String testEntitlementId = "testEntitlement-CreateAtRepoTest"
        entitlement.with{
            repo = testRepo
            subject = testOrg
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()

        then:
        entitlement.getEntitlement().toString() == '{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-CreateAtRepoTest","path":""}'

        cleanup:
        assert entitlement.deleteEntitlement().toString() == '{"message":"success"}'
        assert entitlement.getEntitlement().toString() == '{"message":"Not Found","code":404}'
    }

    def "Create an entitlement for a package"() {
        when:
        Entitlement entitlement = makeTestEntitlementObj()
        String testEntitlementId = "testEntitlement-CreateAtPkgTest"
        entitlement.with{
            repo = testRepo
            subject = testOrg
            pkg = testPkg
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()

        then:
        entitlement.getEntitlement().toString() == '{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-CreateAtPkgTest"}'

        cleanup:
        assert entitlement.deleteEntitlement().toString() == '{"message":"success"}'
        assert entitlement.getEntitlement().toString() == '{"message":"Not Found","code":404}'
    }

    @Ignore //entitlements currently do not work for versions of maven repos, bintray said working on it
    def "Create an entitlement for a version"() {
        when:
        Entitlement entitlement = makeTestEntitlementObj()
        String testEntitlementId = "testEntitlement-CreateAtVersionTest"
        entitlement.with{
            repo = testRepo
            subject = testOrg
            pkg = testPkg
            version = testVersion
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()

        then:
        entitlement.getEntitlement().toString() == '{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-CreateAtVersionTest","path":""}'

        cleanup:
        assert entitlement.deleteEntitlement().toString() == '{"message":"success"}'
        assert entitlement.getEntitlement().toString() == '{"message":"Not Found","code":404}'
    }

    def "Get a specific entitlement for a repo"() {
        setup:
        Entitlement entitlement = makeTestEntitlementObj()
        String testEntitlementId = "testEntitlement-GetRepoTest"
        entitlement.with{
            repo = testRepo
            subject = testOrg
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()

        expect:
        entitlement.getEntitlement().toString() == '{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-GetRepoTest","path":""}'

        cleanup:
        assert entitlement.deleteEntitlement().toString() == '{"message":"success"}'
        assert entitlement.getEntitlement().toString() == '{"message":"Not Found","code":404}'
    }

    def "Update a specific entitlement for a repo"() {
        setup:
        Entitlement entitlement = makeTestEntitlementObj()
        String testEntitlementId = "testEntitlement-UpdateRepoTest"
        entitlement.with{
            repo = testRepo
            subject = testOrg
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()
        assert entitlement.getEntitlement().toString() == '{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-UpdateRepoTest","path":""}'

        when:
        entitlement.body.content.access = READWRITE.text
        entitlement.updateEntitlement()

        then:
        entitlement.getEntitlement().toString() == '{"access":"rw","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-UpdateRepoTest","path":""}'

        cleanup:
        assert entitlement.deleteEntitlement().toString() == '{"message":"success"}'
        assert entitlement.getEntitlement().toString() == '{"message":"Not Found","code":404}'
    }

    def "Delete a specific entitlement for a repo"() {
        setup:
        Entitlement entitlement = makeTestEntitlementObj()
        String testEntitlementId = "testEntitlement-DeleteRepoTest"
        entitlement.with{
            repo = testRepo
            subject = testOrg
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()
        assert entitlement.getEntitlement().toString() == '{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-DeleteRepoTest","path":""}'

        expect:
        entitlement.deleteEntitlement().toString() == '{"message":"success"}'
        entitlement.getEntitlement().toString() == '{"message":"Not Found","code":404}'

    }

    def cleanupSpec(){
        assert downloadKey.deleteDownloadKey().toString() == '{"message":"success"}'
        assert downloadKey.getDownloadKey().toString() == '{"message":"Not Found","code":404}'
    }

}