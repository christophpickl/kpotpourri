package com.github.christophpickl.kpotpourri.build

import com.github.christophpickl.kpotpourri.common.http.httpRequest
import java.io.File
import java.net.URLEncoder

class GitHub(
    private val build4k: Build4k,
    private val repoOwner: String,
    private val repoName: String,
    private val authToken: String
) {
    /**
     * @return the upload URL to use to upload assets to this release
     */
    fun createRelease(tagName: String, releaseBody: String): String {
        val url = "https://api.github.com/repos/$repoOwner/$repoName/releases"
        Out.info("Creating release '$tagName' at: $url")
        return httpRequest(
            url = url,
            method = "POST"
        ) {
            addHeader("Authorization" to "token $authToken")
            addHeader("Content-Type" to "application/json")
            doWithOutput { out ->
                out.bufferedWriter().use { writer ->
                    writer.write("""
                        {
                          "tag_name": "$tagName",
                          "name": "$tagName",
                          "body": "${releaseBody.replace("\"", "\\\"").replace("\n", "<br/>")}",
                          "target_commitish": "master",
                          "draft": false,
                          "prerelease": false
                        }
                        """.trimIndent())
                }
            }
            readResponseBody = true
            val response = execute()
            if (response.statusCode != 201) {
                build4k.fail("GitHub returned status code ${response.statusCode} while requesting URL: $url")
            }
            extractUploadUrl(response.responseBody!!)
        }
    }

    fun uploadArtifact(uploadUrl: String, contentType: String, file: File) {
        if (!file.exists()) {
            build4k.fail("Upload file does not exist: ${file.canonicalPath}")
        }

        val fullUrl = "$uploadUrl?name=${URLEncoder.encode(file.name, Charsets.UTF_8.name())}"
        Out.info("Uploading release artifact: ${file.canonicalPath}")
        Out.info("Upload URL: $fullUrl")
        httpRequest(
            url = fullUrl,
            method = "POST"
        ) {
            addHeader("Authorization" to "token $authToken")
            addHeader("Content-Type" to contentType)
            doWithOutput { out ->
                out.write(file.readBytes())
            }
            val response = execute()
            if (response.statusCode != 201) {
                build4k.fail("GitHub returned status code ${response.statusCode} while requesting URL: $fullUrl")
            }
        }
    }

    // same as: $response | jq -r '.upload_url' | sed -e 's/{?name,label}//g'`
    private fun extractUploadUrl(response: String): String {
        val key = "\"upload_url\""
        val index = response.indexOf(key)
        require(index > 0) { "JSON did not contain $key key!" }
        val responseAfterUploadUrl = response.substring(index + key.length)
        val start = responseAfterUploadUrl.indexOf("\"") + 1
        val end = responseAfterUploadUrl.indexOf("\"", startIndex = start)
        return responseAfterUploadUrl.substring(start, end).replace("{?name,label}", "")
    }

}
