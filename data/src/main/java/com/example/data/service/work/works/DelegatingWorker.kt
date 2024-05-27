package com.example.data.service.work.works

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlin.reflect.KClass

private const val WORK_CLASS_NAME = "router_work_delegate_class_name"

@EntryPoint
@InstallIn(SingletonComponent::class)
interface HiltWorkerFactoryEntryPoint {
    fun hiltWorkerFactory(): HiltWorkerFactory
}

internal fun KClass<out CoroutineWorker>.delegateData() =
    Data.Builder().putString(WORK_CLASS_NAME, qualifiedName).build()

class DelegatingWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    private val workClassName = workerParams.inputData.getString(WORK_CLASS_NAME) ?: ""
    private val delegatingWorker =
        EntryPointAccessors.fromApplication<HiltWorkerFactoryEntryPoint>(context)
            .hiltWorkerFactory()
            .createWorker(context, workClassName, workerParams) as? CoroutineWorker
            ?: throw IllegalArgumentException("unable to find appropriate work")

    override suspend fun getForegroundInfo(): ForegroundInfo =
        delegatingWorker.getForegroundInfo()

    override suspend fun doWork(): Result =
        delegatingWorker.doWork()
}