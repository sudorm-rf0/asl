package com.hackfuture.core.network.alltick

object AllTickConstants {
    const val BASE_URL = "https://alltick.co/"
    // API Token 从环境变量读取（安全：不再硬编码进代码库）
    val API_TOKEN: String = System.getenv("ALLTICK_API_TOKEN") ?: ""
    const val COMPANY_ID = "c"
    const val PATH_PREFIX = "api/v1/market"
}
