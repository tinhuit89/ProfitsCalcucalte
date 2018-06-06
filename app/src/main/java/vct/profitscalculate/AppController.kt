package vct.profitscalculate

import android.app.Application
import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

class AppController : Application() {

//    open lateinit var realm: Realm

    init {
        instance = this
    }

    companion object {
        open lateinit var instance: AppController

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun realmInstance(): Realm {
            return Realm.getDefaultInstance()
        }
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(applicationContext)
        var config = RealmConfiguration.Builder()
                .name("capo_profit")
                .deleteRealmIfMigrationNeeded()
        Realm.setDefaultConfiguration(config.build())
//        realm = Realm.getDefaultInstance()

//        val context: Context = AppController.applicationContext()
    }
}