package com.example.data.service.work.status

interface SyncSubscriber {
    suspend fun subscribe()
}