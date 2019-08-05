package com.questdev.travelmantics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.questdev.travelmantics.utils.FirebaseUtil
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }

    override fun onResume() {
        super.onResume()

        FirebaseUtil.openFbReference("traveldeals", this)
        val adapter = DealsRecyclerAdapter()
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvDeals.layoutManager = layoutManager
        rvDeals.adapter = adapter

        FirebaseUtil.attachListener()
    }

    override fun onPause() {
        super.onPause()
        FirebaseUtil.detachListener()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_insert -> {
                val intent = Intent(this, DealActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_logout -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        Log.d("Logout", "User Logged out")
                        FirebaseUtil.attachListener()
                    }
                FirebaseUtil.detachListener()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                // ...
            } else {

            }
        }
    }*/
}
