package com.example.corenetworking.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface RxScheduler {
    val io: Scheduler
    val main: Scheduler
}

class CommonSchedulerProvider @Inject constructor() : RxScheduler {
    override val io: Scheduler
        get() = Schedulers.io()
    override val main: Scheduler
        get() = AndroidSchedulers.mainThread()
}
