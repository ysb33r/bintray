package org.ysb33r.gradle.bintray.entitlements

import groovy.json.JsonBuilder
import org.ysb33r.gradle.bintray.core.BintrayConnection

import static groovyx.net.http.ContentType.JSON

class Entitlements implements BintrayConnection, EntitlementsRequest {

    JsonBuilder getEntitlements(
            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
        assertAttributes(subject, repo)
        return RESTCall(onSuccess, onFail) {
            return client().get(
                    contentType: JSON,
                    path: getPath()
            )
        }
    }

    JsonBuilder createEntitlement(
            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
        assertAttributes(subject, repo, body.getContent()."access")
        return RESTCall(onSuccess, onFail) {
            return client().post(
                    contentType: JSON,
                    path: getPath(),
                    body: body.toString()
            )
        }
    }

    JsonBuilder updateEntitlement(
            String entitlementId,
            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
        this.entitlementId = entitlementId
        assertAttributes(this.entitlementId, subject, repo)
        return RESTCall(onSuccess, onFail) {
            return client().patch(
                    contentType: JSON,
                    path: getPath(),
                    body: body.toString()
            )
        }
    }

    JsonBuilder getEntitlement(
            String entitlementId,
            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
        this.entitlementId = entitlementId
        assertAttributes(this.entitlementId, subject, repo)
        return RESTCall(onSuccess, onFail) {
            return client().get(
                    contentType: JSON,
                    path: getPath()
            )
        }
    }

    JsonBuilder deleteEntitlement(
            String entitlementId,
            Closure onSuccess = onSuccessDefault, Closure onFail = onFailDefault) {
        this.entitlementId = entitlementId
        assertAttributes(this.entitlementId, subject, repo)
        return RESTCall(onSuccess, onFail) {
            return client().delete(
                    contentType: JSON,
                    path: getPath()
            )
        }
    }
}