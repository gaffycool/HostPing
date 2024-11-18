package com.app.commondata.dto

import com.app.commondomain.model.HostModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class HostListResponseDTO : ArrayList<HostItemDTO>()

@JsonClass(generateAdapter = true)
data class HostItemDTO(
    val icon: String,
    val name: String,
    val url: String
) {
    fun transform() = HostModel(icon, name, url)
}