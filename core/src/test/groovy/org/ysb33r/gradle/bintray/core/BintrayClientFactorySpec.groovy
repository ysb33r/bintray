package org.ysb33r.gradle.bintray.core

import spock.lang.Specification


class BintrayClientFactorySpec extends Specification {

    def "Get a bintray API Client with null creds"(){
        when:
        BintrayClient btClient = new BintrayClientFactory().apiClient

        then:
        btClient instanceof BintrayClient
    }

    def "Get a bintray API Client with some creds"(){
        when:
        BintrayClient btClient = new BintrayClientFactory("testuser","testpass").apiClient

        then:
        btClient.headers.Authorization

    }

}