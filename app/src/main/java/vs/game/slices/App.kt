package vs.game.slices

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import vs.game.slices.di.appModule
import vs.game.slices.di.gameModule
import vs.game.slices.di.gameResultModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule, gameModule, gameResultModule))
        }
    }
}