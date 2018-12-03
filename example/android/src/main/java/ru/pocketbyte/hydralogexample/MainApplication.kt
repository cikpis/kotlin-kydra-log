package ru.pocketbyte.hydralogexample

import android.app.Application
import ru.pocketbyte.hydra.log.HydraLog
import ru.pocketbyte.hydra.log.initDefault

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        HydraLog.initDefault()
    }

}