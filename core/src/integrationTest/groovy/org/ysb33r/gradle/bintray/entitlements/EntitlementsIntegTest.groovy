package org.ysb33r.gradle.bintray.entitlements

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.downloadkeys.DownloadKeys
import org.ysb33r.gradle.bintray.downloadkeys.DownloadKeysBody

import static AccessLevel.*
import spock.lang.Shared
import spock.lang.Specification

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
    DownloadKeys downloadKey
    @Shared
    Closure makeTestEntitlement = {
        Entitlements entitlement = new Entitlements().with {
            userName = btUserName
            apiKey = btApiKey
            subject = testOrg
            client()
            return it
        }
        return entitlement
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
        JsonBuilder testBody = new DownloadKeysBody().with {
            id = testDlKey
            return toJson()
        }

        downloadKey = new DownloadKeys().with {
            userName = btUserName
            apiKey = btApiKey
            subjectType = orgs
            subject = testOrg
            body = testBody
            client()
            return it
        }
        assert downloadKey.getDownloadKey(testDlKey).getContent().containsKey("username")
    }


    def "List all entitlements for repo"() {
        setup: Entitlements entitlement = makeTestEntitlement()
        when:
        JsonBuilder result = entitlement.with{
            repo = testRepo
            return it
        }.getEntitlements()

        then:
        result instanceof JsonBuilder
    }

    def "List all entitlements for package"() {
        setup: Entitlements entitlement = makeTestEntitlement()
        when: JsonBuilder result = entitlement.with{
            repo = testRepo
            pkg = testPkg
            return it
        }.getEntitlements()

        then:
        result instanceof JsonBuilder
    }

    def "List all entitlements for version"() {
        setup: Entitlements entitlement = makeTestEntitlement()
        when: JsonBuilder result = entitlement.with{
            repo = testRepo
            pkg = testPkg
            version = testVersion
            return it
        }.getEntitlements()

        then:
        result instanceof JsonBuilder
    }

    def "Get an entitlement for a repo"(){
        setup: Entitlements entitlement = makeTestEntitlement()
        String testEntitlementId = "testEntitlement-CreateTest"
        entitlement.with{
            repo = testRepo
            subject = testOrg
            pkg = testPkg
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()

        expect:
        entitlement.getEntitlement(testEntitlementId).toString() == '{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-CreateTest"}'

        cleanup:
        assert entitlement.deleteEntitlement(testEntitlementId).toString() == '{"message":"success"}'

    }

    def "Create an entitlement for a repo"() {

        setup: Entitlements entitlement = makeTestEntitlement()

        when:
        String testEntitlementId = "testEntitlement-CreateAtRepoTest"
        entitlement.with{
            repo = testRepo
            subject = testOrg
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()

        then:
        entitlement.getEntitlement(testEntitlementId).toString() == '{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-CreateAtRepoTest","path":""}'

        cleanup:
        assert entitlement.deleteEntitlement(testEntitlementId).toString() == '{"message":"success"}'
    }

    def "Create an entitlement for a package"() {

        setup: Entitlements entitlement = makeTestEntitlement()

        when:
        String testEntitlementId = "testEntitlement-CreateAtPkgTest"
        entitlement.with{
            repo = testRepo
            subject = testOrg
            pkg = testPkg
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()

        then:
        entitlement.getEntitlement(testEntitlementId).toString() == '{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-CreateAtPkgTest"}'

        cleanup:
        assert entitlement.deleteEntitlement(testEntitlementId).toString() == '{"message":"success"}'

    }

    def "Create an entitlement for a version"() {
        setup: Entitlements entitlement = makeTestEntitlement()
        when:
        String testEntitlementId = "testEntitlement-CreateAtVersionTest"
        JsonBuilder result = entitlement.with{
            repo = testRepo
            subject = testOrg
            pkg = testPkg
            version = testVersion
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()

        then:
        result.toString() == ''
        entitlement.getEntitlement(testEntitlementId).toString() == '{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-CreateAtVersionTest"}'

        cleanup:
        entitlement.deleteEntitlement(testEntitlementId).toString()

    }

    def "Update an entitlement for a repo"() {

        setup: Entitlements entitlement = makeTestEntitlement()
        when:
        String testEntitlementId = "testEntitlement-UpdateAtRepoTest"
        entitlement.with{
            repo = testRepo
            subject = testOrg
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()

        then:
        entitlement.getEntitlement(testEntitlementId).toString() == '{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-UpdateAtRepoTest","path":""}'

        when:
        entitlement.body.access = READWRITE
        entitlement.updateEntitlement(testEntitlementId)

        then:
        entitlement.getEntitlement(testEntitlementId).toString() == '{"access":"rw","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-UpdateAtRepoTest","path":""}'

        cleanup:
        assert entitlement.deleteEntitlement(testEntitlementId).toString() == '{"message":"success"}'
    }

    def "Delete an entitlement for an repo"() {
        setup: Entitlements entitlement = makeTestEntitlement()

        when:
        String testEntitlementId = "testEntitlement-DeleteAtRepoTest"
        JsonBuilder result = entitlement.with{
            repo = testRepo
            subject = testOrg
            body = makeTestBody(testEntitlementId)
            return it
        }.createEntitlement()

        then:
        result.toString() == '{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-DeleteAtRepoTest","path":""}'

        when:
        result = entitlement.getEntitlement(testEntitlementId)

        then:
        result.toString() == '{"access":"r","download_keys":["testdlkey-allentitlementtests"],"id":"testEntitlement-DeleteAtRepoTest","path":""}'

        when:
        result = entitlement.deleteEntitlement(testEntitlementId)

        then:
        result.toString() == '{"message":"success"}'

        cleanup:
        entitlement.deleteEntitlement(testEntitlementId)
    }
}