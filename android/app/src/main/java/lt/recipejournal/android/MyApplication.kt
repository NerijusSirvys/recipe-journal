package lt.recipejournal.android

import android.app.Application
import lt.recipejournal.android.core.modules.recipesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                recipesModule
            )
        }
    }
}