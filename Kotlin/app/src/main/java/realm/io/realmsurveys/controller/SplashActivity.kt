package realm.io.realmsurveys.controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.realm.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import realm.io.realmsurveys.R

class SplashActivity : AppCompatActivity(), AnkoLogger {

    companion object {
        val HOST = "192.168.1.162" // <-- Enter your Realm Object Server IP here
        val REALM_URL = "realm://$HOST:9080/~/survey"
        val SERVER_URL = "http://$HOST:9080"
        val ID = "survey@demo.io"
        val PASSWORD = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        login()
    }

    private fun login() {

        val syncCredentials = SyncCredentials.usernamePassword(ID, PASSWORD, false)

        SyncUser.loginAsync(syncCredentials, SERVER_URL, object : SyncUser.Callback {
            override fun onSuccess(user: SyncUser) {
                postLogin(user)
            }

            override fun onError(error: ObjectServerError) {
                logError(error)
            }
        })

    }

    private fun postLogin(user: SyncUser) {
        setRealmDefaultConfig(user)
        goTo(SurveyActivity::class.java)
    }


    private fun setRealmDefaultConfig(user: SyncUser) {
        info("Connecting to Sync Server at : [" + REALM_URL.replace("~".toRegex(), user.identity) + "]")
        val syncConfiguration = SyncConfiguration.Builder(user, REALM_URL).build()
        Realm.setDefaultConfiguration(syncConfiguration)
    }

    private fun goTo(activityClass: Class<out AppCompatActivity>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish()
    }

    private fun logError(e: Throwable) {
        error("Error", e)
    }

}
