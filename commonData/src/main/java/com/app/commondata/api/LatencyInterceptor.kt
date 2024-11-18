package com.app.commondata.api

import okhttp3.Interceptor
import okhttp3.Response

/**
 * This class used to intercept chain and print the latency in millis
 */
class LatencyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val startTime = System.nanoTime()
        val response = chain.proceed(chain.request())
        val endTime = System.nanoTime()
        
        val latencyMs = (endTime - startTime) / 1_000_000
        println("Ping latency: $latencyMs ms")
        return response
    }
}