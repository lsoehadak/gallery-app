package com.lsoehadak.galleryapp.utils.ext

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lsoehadak.galleryapp.data.ImageEntity
import retrofit2.Response

fun Response<JsonObject>.getPaginationData(): JsonObject {
    return this.body()!!.asJsonObject.get("pagination").asJsonObject
}

fun Response<JsonObject>.getDataArr(): JsonArray {
    return this.body()!!.asJsonObject.get("data").asJsonArray
}

fun Response<JsonObject>.getDataObj(): JsonObject {
    return this.body()!!.asJsonObject.get("data").asJsonObject
}

fun Response<JsonObject>.getConfigData(): JsonObject {
    return this.body()!!.asJsonObject.get("config").asJsonObject
}

fun JsonObject.getImageEntity(iiifUrl: String): ImageEntity {
    return ImageEntity(
        id = getString("id")!!,
        url = getImageUrl(iiifUrl),
        title = getString("title")!!,
        inscriptions = getString("inscriptions"),
        creditLine = getString("credit_line"),
        publicationHistory = getString("publication_history"),
        exhibitionHistory = getString("exhibition_history"),
        provenanceText = getString("provenance_text")
    )
}

fun JsonObject.getIiifUrl(): String {
    return this.get("iiif_url").asString.replace("\\", "", false)
}

fun JsonObject.getImageUrl(iiifUrl: String): String {
    val imageId = if (this.get("image_id").isJsonNull) "" else this.getString("image_id")!!
    return "$iiifUrl/$imageId/full/843,/0/default.jpg"
}

fun JsonObject.getInt(key: String): Int {
    return this.get(key).asInt
}

fun JsonObject.getString(key: String): String {
    return if (this.get(key).isJsonNull) {
        "No data"
    } else {
        this.get(key).asString
    }
}
