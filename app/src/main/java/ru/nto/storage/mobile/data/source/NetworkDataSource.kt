package ru.nto.storage.mobile.data.source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import ru.nto.storage.mobile.core.Constants
import ru.nto.storage.mobile.data.dto.EquipmentDto
import ru.nto.storage.mobile.data.dto.IssueRequestDto
import ru.nto.storage.mobile.data.dto.UserDto

object NetworkDataSource {
    private val client by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = true
                        encodeDefaults = true
                    }
                )
            }
        }
    }

    suspend fun checkAuth(code: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.get(getUrl(code, Constants.AUTH_URL))
            when (response.status) {
                HttpStatusCode.OK -> true
                else -> false
            }
        }
    }

    suspend fun getInfo(code: String): Result<UserDto> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.get(getUrl(code, Constants.INFO_URL))
            if (response.status == HttpStatusCode.OK) {
                response.body<UserDto>()
            } else {
                println("!!!! getInfo ${response.status} ${response.bodyAsText()}")
                error(response.bodyAsText())
            }
        }
    }

    suspend fun getEquipment(code: String): Result<Map<String, List<EquipmentDto>>?> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.get(getUrl(code, Constants.EQUIPMENT_URL))
            if (response.status == HttpStatusCode.OK) {
                response.body<Map<String, List<EquipmentDto>>>()
            } else {
                error(response.bodyAsText())
            }
        }
    }

    suspend fun createIssue(code: String, data: IssueRequestDto): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.post(getUrl(code, Constants.ISSUE_URL)) {
                contentType(ContentType.Application.Json)
                setBody(data)
            }
            println("!!!! createIssue ${response.status}")
            when (response.status) {
                HttpStatusCode.Created -> true
                HttpStatusCode.Conflict -> false
                else -> error(response.bodyAsText())
            }
        }
    }

    private fun getUrl(code: String, targetUrl: String) = "${Constants.HOST}/api/$code$targetUrl"
}
