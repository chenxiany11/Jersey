package edu.rosehulman.chenx11.jersey

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.*


class MainActivity : AppCompatActivity() {
    private var jersey: Jersey = Jersey("ANDROID", 17, true)
    private var default_Name = "ANDROID"
    private var default_Number = 17
    private var default_IsRed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            showAddDialog()
        }
        restoreData()
        updateView()
    }


    private fun showAddDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.edit_dialog_title))
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add, null, false)
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) {_, _ ->
            jersey.name = view.findViewById<EditText>(R.id.name_edit_text).text.toString()
            try {
                jersey.number = view.findViewById<EditText>(R.id.number_edit_text).text.toString().toInt()
            } catch (e: NumberFormatException) {
                jersey.number = 0
            }
            jersey.isRed = view.findViewById<ToggleButton>(R.id.toggle_button).text == getString(R.string.red)
            updateView()
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
    }

    private fun reset() {
        jersey.name = getString(R.string.default_jersey_name)
        jersey.number = 17
        jersey.isRed = true
    }

    private fun updateView() {
        findViewById<TextView>(R.id.name_text_view).text = jersey.name
        findViewById<TextView>(R.id.number_text_view).text = jersey.number.toString()
        if (jersey.isRed) {
            findViewById<ImageView>(R.id.image_view).setImageResource(R.drawable.red_jersey)
        } else {
            findViewById<ImageView>(R.id.image_view).setImageResource(R.drawable.blue_jersey)
        }
    }

    override fun onPause() {
        super.onPause()
        val prefs = getSharedPreferences(Jersey.PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(Jersey.KEY_JERSEY_NAME, jersey.name)
        editor.putInt(Jersey.KEY_NUMBER, jersey.number)
        editor.putBoolean(Jersey.KEY_IS_RED, jersey.isRed)
        editor.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.reset -> {
                storeInfo()
                reset()
                updateView()
                unSnack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun unSnack() {
        var snackbar = Snackbar.make(findViewById(R.id.main_layout),"Jersey cleared", Snackbar.LENGTH_LONG)
        snackbar.setAction("UNDO", View.OnClickListener {
            undo()
            updateView()
        }).show()
    }

    private fun undo() {
        jersey.name = default_Name
        jersey.number = default_Number
        jersey.isRed = default_IsRed
    }

    private fun storeInfo() {
        default_Name = jersey.name
        default_Number = jersey.number
        default_IsRed = jersey.isRed
    }

    private fun restoreData() {
        val prefs = getSharedPreferences(Jersey.PREFS, Context.MODE_PRIVATE)
        val name = prefs.getString(Jersey.KEY_JERSEY_NAME, getString(R.string.default_jersey_name)) ?: getString(R.string.default_jersey_name)
        val number = prefs.getInt(Jersey.KEY_NUMBER, 17)
        val isRed = prefs.getBoolean(Jersey.KEY_IS_RED, true)
        jersey.name = name
        jersey.number = number
        jersey.isRed = isRed
    }
}
