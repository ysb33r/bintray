package org.ysb33r.gradle.bintray.downloadkeys

def body = new DownloadKeyBody().with{id = "tester"}

println body.dump()