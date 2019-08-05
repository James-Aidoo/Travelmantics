package com.questdev.travelmantics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.questdev.travelmantics.utils.FirebaseUtil
import kotlinx.android.synthetic.main.activity_deal.*

class DealActivity : AppCompatActivity() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var deal: TravelDeal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        FirebaseUtil.openFbReference("traveldeals", this)
        firebaseDatabase = FirebaseUtil.firebaseDatabase
        databaseReference = FirebaseUtil.databaseReference

        setTextViewValuesFromIntent()
    }

    private fun setTextViewValuesFromIntent() {
        val intent = intent
        var dealFromIntent = intent.getParcelableExtra<TravelDeal>("Deal")
        if (dealFromIntent == null)
            dealFromIntent = TravelDeal()

        deal = dealFromIntent
        txtTitle.setText(deal.title)
        txtDescription.setText(deal.description)
        txtPrice.setText(deal.price)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.deal_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_save -> {
                saveDeal()
                Toast.makeText(this, "Deal Saved", Toast.LENGTH_LONG).show()
                clean()
                finish()
                true
            }
            R.id.action_delete -> {
                deleteDeal()
                Toast.makeText(this, "Deal Deleted", Toast.LENGTH_LONG).show()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveDeal() {
        deal.title = txtTitle.text.toString()
        deal.description = txtDescription.text.toString()
        deal.price = txtPrice.text.toString()

        if (deal.id == ""){
            deal.apply {
                val key = databaseReference.child("traveldeal").push().key
                id = key!!
            }
            databaseReference.child(deal.id).setValue(deal)
        }
        else
            databaseReference.child(deal.id).setValue(deal)
    }

    private fun deleteDeal() {
        if (deal.id == "")
            Toast.makeText(this, "Please save deal first before deleting", Toast.LENGTH_LONG).show()
        databaseReference.child(deal.id).removeValue()
    }

    private fun clean() {
        txtTitle.setText("")
        txtDescription.setText("")
        txtPrice.setText("")
    }
}
