package org.ysb33r.gradle.bintray.downloadkeys

import groovy.json.JsonBuilder
import spock.lang.Specification

class DownloadKeyBodySpec extends Specification {

    def "Get a minimal properly formatted JSON body"(){
        when:
        JsonBuilder dkb = new DownloadKeyBody().with{
            id = "TestId"
            return toJson()
        }

        then:
        dkb.toString() == '{"id":"TestId"}'
    }

    def "Get a complete and properly formatted JSON body"(){
        when:
        JsonBuilder dkb = new DownloadKeyBody().with{ dkp ->
            id = "TestId"
            white_cidrs = ["127.0.0.1/22", "193.5.0.1/92"]
            black_cidrs =  ["197.4.0.1/4", "137.0.6.1/78"]
            expiry = 7956915742000
            existence_check = new ExistenceCheck().with{ ec ->
                url =  "http://callbacks.myci.org/username=:username,password=:password"
                cache_for_secs = 60
                return ec
            }
            return toJson()
        }

        then:
        dkb.toString() == '{"expiry":-1658657488,"white_cidrs":["127.0.0.1/22","193.5.0.1/92"],"id":"TestId","existence_check":{"cache_for_secs":60,"url":"http://callbacks.myci.org/username=:username,password=:password"},"black_cidrs":["197.4.0.1/4","137.0.6.1/78"]}'
    }


}