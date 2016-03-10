package org.ysb33r.gradle.bintray.downloadkeys

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection
import spock.lang.Shared
import spock.lang.Specification
import static org.ysb33r.gradle.bintray.core.SubjectType.*

class DownloadKeysIntegTest extends Specification {

    @Shared
    BintrayConnection btConnection = new BintrayConnection().with{
        userName = System.getenv('BINTRAY_USERNAME')
        apiKey = System.getenv('BINTRAY_API_KEY')
        return it
    }
    @Shared
    String testOrg = "getgsi"
    @Shared
    String testUser = "jerrywiltsegsi"
    @Shared
    Closure makeTestKeyObj = {String testDlKey ->
        DownloadKey downloadKey = new DownloadKey().with {
            btConn = btConnection
            subjectType = orgs
            subject = testOrg
            id = testDlKey
            return it
        }
        println downloadKey.dump()
        return downloadKey
    }

    @Shared
    Closure makeTestKeysObj = {
        DownloadKeys downloadKeys = new DownloadKeys().with {
            btConn = btConnection
            subjectType = orgs
            subject = testOrg
            return it
        }
        return downloadKeys
    }

    def "List all download keys for org"() {
        when:
        JsonBuilder result = makeTestKeysObj().getDownloadKeys()
        then:
        result.content.containsKey("download_keys")
    }

    def "List all download keys for user"() {
        setup: DownloadKeys downloadKeys = makeTestKeysObj()
        when:
        JsonBuilder result = downloadKeys.with{
            subject = testUser
            subjectType = users
            return it
        }.getDownloadKeys()

        then:
        result.toString() == '{"message":"Forbidden","code":403}'
    }

    def "Create a download key for an org"() {
        setup:
        String testDlKey = "testDlKey-CreateTest"
        DownloadKey downloadKey = makeTestKeyObj(testDlKey)
        JsonBuilder testBody = new DownloadKeyBody().with {
            id = testDlKey
            return toJson()
        }

        when:
        JsonBuilder result = downloadKey.with{
            body = testBody
            return it
        }.createDownloadKey()

        then:
        result.content.containsKey("username")
        downloadKey.getDownloadKey().toString() == '{"black_cidrs":[],"expiry":-1,"id":"testdlkey-createtest","username":"testdlkey-createtest@getgsi","white_cidrs":[]}'

        cleanup:
        assert downloadKey.deleteDownloadKey().toString() == '{"message":"success"}'
        assert downloadKey.getDownloadKey().toString() == '{"message":"Not Found","code":404}'
    }

    def "Get specific download key for an org"() {
        setup:
        String testDlKey = "testDlKey-GetTest"
        DownloadKey downloadKey = makeTestKeyObj(testDlKey)
        JsonBuilder testBody = new DownloadKeyBody().with {
            id = testDlKey
            return toJson()
        }
        downloadKey.with{
            body = testBody
            return it
        }.createDownloadKey()

        when:
        JsonBuilder result = downloadKey.getDownloadKey()

        then:
        result.content.containsKey("username")

        cleanup:
        downloadKey.deleteDownloadKey().toString() == '{"message":"success"}'
        downloadKey.getDownloadKey().toString() == '{"message":"Not Found","code":404}'
    }

    def "Update a download key for an org"() {
        setup:
        String testDlKey = "testDlKey-UpdateTest"
        DownloadKey downloadKey = makeTestKeyObj(testDlKey)
        JsonBuilder testBody = new DownloadKeyBody().with {
            id = testDlKey
            return toJson()
        }
        downloadKey.with{
            body = testBody
            return it
        }.createDownloadKey()

        assert downloadKey.getDownloadKey().content.white_cidrs == []
        when:
        JsonBuilder testBodyNew = new DownloadKeyBody().with {
            white_cidrs = ["127.0.0.1/22"]
            return toJson()
        }
        and:
        downloadKey.with{
            subject = testOrg
            subjectType = orgs
            body = testBodyNew
            return it
        }.updateDownloadKey()

        then:
        downloadKey.getDownloadKey().content.white_cidrs == ["127.0.0.1/22"]

        cleanup:
        downloadKey.deleteDownloadKey().toString() == '{"message":"success"}'
        downloadKey.getDownloadKey().toString() == '{"message":"Not Found","code":404}'
    }

    def "Delete a specific download key for an org"() {
        setup:
        String testDlKey = "testDlKey-DeleteTest"
        DownloadKey downloadKey = makeTestKeyObj(testDlKey)
        JsonBuilder testBody = new DownloadKeyBody().with {
            id = testDlKey
            return toJson()
        }
        downloadKey.with{
            subject = testOrg
            subjectType = orgs
            body = testBody
            return it
        }.createDownloadKey()
        assert downloadKey.getDownloadKey().toString() == '{"black_cidrs":[],"expiry":-1,"id":"testdlkey-deletetest","username":"testdlkey-deletetest@getgsi","white_cidrs":[]}'

        expect:
        downloadKey.deleteDownloadKey().toString() == '{"message":"success"}'
        downloadKey.getDownloadKey().toString() == '{"message":"Not Found","code":404}'

    }

}