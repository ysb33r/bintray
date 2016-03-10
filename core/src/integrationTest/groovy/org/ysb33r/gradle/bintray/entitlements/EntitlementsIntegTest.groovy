package org.ysb33r.gradle.bintray.entitlements

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BaseIntegTest
import org.ysb33r.gradle.bintray.core.BintrayConnection

import static AccessLevel.*
import spock.lang.Ignore
import spock.lang.Shared
import org.ysb33r.gradle.bintray.downloadkeys.DownloadKey
import static org.ysb33r.gradle.bintray.core.SubjectType.orgs

class EntitlementsIntegTest extends BaseIntegTest {

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
    Closure makeTestEntitlementObj = {String entitlementId ->
        Entitlement entitlement = new Entitlement().with {
            id = entitlementId
            btConn = btConnection
            subject = testOrg
            return it
        }
        return entitlement
    }
    @Shared
    Closure makeTestEntitlementsObj = {
        Entitlements entitlements = new Entitlements().with {
            btConn = btConnection
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
            btConn = btConnection
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
        result.toString().contains('{"id":"testEntitlement-ListAllEntitlementsAtRepoTest"}')

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
        result.toString().contains('{"id":"testEntitlement-ListAllEntitlementsAtPkgTest"}')

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
            ver = testVersion
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()
        entitlement.id = testEntitlementId

        when:
        Entitlements entitlements = makeTestEntitlementsObj()
        JsonBuilder result = entitlements.with{
            repo = testRepo
            pkg = testPkg
            ver = testVersion
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
        String testEntitlementId = "testEntitlement-CreateAtRepoTest"
        Entitlement entitlement = makeTestEntitlementObj(testEntitlementId)
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
        String testEntitlementId = "testEntitlement-CreateAtPkgTest"
        Entitlement entitlement = makeTestEntitlementObj(testEntitlementId)
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
        String testEntitlementId = "testEntitlement-CreateAtVersionTest"
        Entitlement entitlement = makeTestEntitlementObj(testEntitlementId)
        entitlement.with{
            repo = testRepo
            subject = testOrg
            pkg = testPkg
            ver = testVersion
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
        String testEntitlementId = "testEntitlement-GetRepoTest"
        Entitlement entitlement = makeTestEntitlementObj(testEntitlementId)
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
        String testEntitlementId = "testEntitlement-UpdateRepoTest"
        Entitlement entitlement = makeTestEntitlementObj(testEntitlementId)
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
        String testEntitlementId = "testEntitlement-DeleteRepoTest"
        Entitlement entitlement = makeTestEntitlementObj(testEntitlementId)
        entitlement.with{
            repo = testRepo
            subject = testOrg
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()
        assert entitlement.getEntitlement().toString().contains('{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-DeleteRepoTest","path":""}')

        expect:
        entitlement.deleteEntitlement().toString() == '{"message":"success"}'
        entitlement.getEntitlement().toString() == '{"message":"Not Found","code":404}'

        cleanup:
        entitlement.deleteEntitlement()
    }

    def cleanupSpec(){
        assert downloadKey.deleteDownloadKey().toString() == '{"message":"success"}'
        assert downloadKey.getDownloadKey().toString() == '{"message":"Not Found","code":404}'
    }

}