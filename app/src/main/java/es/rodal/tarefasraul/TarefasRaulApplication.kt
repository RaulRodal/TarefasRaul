package es.rodal.tarefasraul

import android.app.Application
import es.rodal.tarefasraul.data.AppContainer
import es.rodal.tarefasraul.data.AppDataContainer

class TarefasRaulApplication :Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}