package org.ysb33r.gradle.bintray.downloadkeys

import groovy.json.JsonBuilder
import spock.lang.Shared
import spock.lang.Specification
import static org.ysb33r.gradle.bintray.core.SubjectType.*

class DownloadKeysIntegTest extends Specification {

    @Shared
    String testOrg = "getgsi"
    @Shared
    String testUser = "jerrywiltsegsi"
    @Shared
    String btUserName = System.getenv('BINTRAY_USERNAME')
    @Shared
    String btApiKey = System.getenv('BINTRAY_API_KEY')
    @Shared
    Closure makeTestKey = {
            DownloadKeys downloadKey = new DownloadKeys().with {
            userName = btUserName
            apiKey = btApiKey
            subjectType = orgs
            subject = testOrg
            client()
            return it
        }
        return downloadKey
    }

    def "List all download keys for org"() {
        setup: DownloadKeys downloadKey = makeTestKey()
        when:
        JsonBuilder result = downloadKey.getDownloadKeys()
        then:
        result.getContent().containsKey("download_keys")
    }

    def "List all download keys for user"() {
        setup: DownloadKeys downloadKey = makeTestKey()
        when:
        JsonBuilder result = downloadKey.with{
            subject = testUser
            subjectType = users
            return it
        }.getDownloadKeys()

        then:
        result.toString() == '{"message":"Forbidden","code":403}'
    }

    def "Create a download key for an org"() {
        setup: DownloadKeys downloadKey = makeTestKey()
        when:
        String testDlKey = "testDlKey-CreateTest"
        JsonBuilder testBody = new DownloadKeysBody().with {
            id = testDlKey
            return toJson()
        }

        and:
        JsonBuilder result = downloadKey.with{
            body = testBody
            return it
        }.createDownloadKey()

        then:
        result.getContent().containsKey("username")

        cleanup:
        downloadKey.deleteDownloadKey(testDlKey)
    }

    def "Create a download key for an org, alternate syntax"() {
        //Syntax only works if api call has single parameter (id)
        setup: DownloadKeys downloadKey = makeTestKey()
        when:
        String testDlKey = "testDlKey-CreateTest"
        JsonBuilder testBody = new DownloadKeysBody().with {
            id = testDlKey
            return toJson()
        }

        and:
        JsonBuilder result = downloadKey.with{
            body = testBody
            return it
        }.createDownloadKey(testDlKey)

        then:
        result.getContent().containsKey("username")

        cleanup:
        downloadKey.deleteDownloadKey(testDlKey)
    }

    def "Get specific download key for an org"() {
        setup:
        DownloadKeys downloadKey = makeTestKey()
        String testDlKey = "testDlKey-GetTest"
        JsonBuilder testBody = new DownloadKeysBody().with {
            id = testDlKey
            return toJson()
        }
        downloadKey.with{
            body = testBody
            return it
        }.createDownloadKey()

        when:
        JsonBuilder result = downloadKey.getDownloadKey(testDlKey)

        then:
        result.getContent().containsKey("username")

        cleanup:
        downloadKey.deleteDownloadKey(testDlKey)
    }

    def "Update a download key for an org"() {
        setup:
        DownloadKeys downloadKey = makeTestKey()
        String testDlKey = "testDlKey-UpdateTest"
        JsonBuilder testBodyOld = new DownloadKeysBody().with {
            id = testDlKey
            return toJson()
        }
        downloadKey.with{
            subject = testOrg
            subjectType = orgs
            body = testBodyOld
            return it
        }.createDownloadKey()

        assert downloadKey.getDownloadKey(testDlKey).getContent().white_cidrs == []

        when:
        JsonBuilder testBodyNew = new DownloadKeysBody().with {
            id = testDlKey
            white_cidrs = ["127.0.0.1/22"]
            return toJson()
        }
        and:
        downloadKey.with{
            subject = testOrg
            subjectType = orgs
            body = testBodyNew
            return it
        }.updateDownloadKey(testDlKey)

        and:
        JsonBuilder result = downloadKey.getDownloadKey(testDlKey)

        then:
        result.getContent().white_cidrs == ["127.0.0.1/22"]

        cleanup:
        downloadKey.deleteDownloadKey(testDlKey)
    }

    def "Delete a specific download key for an org"() {
        setup:
        DownloadKeys downloadKey = makeTestKey()
        String testDlKey = "testDlKey-DeleteTest"
        JsonBuilder testBody = new DownloadKeysBody().with {
            id = testDlKey
            return toJson()
        }
        downloadKey.with{
            subject = testOrg
            subjectType = orgs
            body = testBody
            return it
        }.createDownloadKey()

        assert downloadKey.getDownloadKey(testDlKey).getContent()

        when:
        JsonBuilder result = downloadKey.getDownloadKey(testDlKey)
        assert result.toString() == '{"black_cidrs":[],"expiry":-1,"id":"testdlkey-deletetest","username":"testdlkey-deletetest@getgsi","white_cidrs":[]}'

        and:
        downloadKey.deleteDownloadKey(testDlKey)

        then:
        downloadKey.getDownloadKey(testDlKey).toString() == '{"message":"Not Found","code":404}'

    }

}