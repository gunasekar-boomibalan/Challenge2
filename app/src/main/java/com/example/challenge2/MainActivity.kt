package com.example.challenge2

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.challenge2.ObjectBox.get
import io.objectbox.Box
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.view.LayoutInflater
import androidx.appcompat.app.ActionBar


class MainActivity : AppCompatActivity() {
    private var childBox: Box<Child>? = null
    private var childAdapter: ChildAdapter? = null
    private lateinit var add: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setTitle(getString(R.string.home_page))
        add =findViewById(R.id.add)
        childBox = get()!!.boxFor(Child::class.java)
        setUpViews()
        val model: ChildViewModel = ViewModelProviders.of(this).get(
            ChildViewModel::class.java)
        model.getChildLiveData(childBox).observe(this) { notes -> childAdapter?.setChilds(notes) }
        add.setOnClickListener{
            addDialog()
        }
    }

    protected fun setUpViews() {
        val listView: RecyclerView = findViewById(R.id.listView)
        childAdapter = ChildAdapter(ArrayList(),childBox)
        listView.setAdapter(childAdapter)
    }

    private fun addNote(name : String,gender : String, dob : Date) {
        val child = Child()
        child.name = name
        child.gender = gender
        child.dob = dob
        child.timeStamp = Date()
        childBox?.put(child)
    }

    fun addDialog() {

        val dialog = LayoutInflater.from(this).inflate(R.layout.add_child_layout, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialog)
            .setCancelable(true)

        var gender =""
        val name : EditText = dialog.findViewById(R.id.name)
        val dob : TextView = dialog.findViewById(R.id.dob)
        val male : CheckBox = dialog.findViewById(R.id.male_check)
        val female : CheckBox = dialog.findViewById(R.id.female_check)
        val maleLabel : TextView = dialog.findViewById(R.id.male)
        val femaleLabel : TextView = dialog.findViewById(R.id.female)

        val saveAction : Button = dialog.findViewById(R.id.save)

        maleLabel.setOnClickListener {
            gender ="Male"
            male.isChecked = true
            female.isChecked = false
        }

        male.setOnClickListener {
            gender ="Male"
            female.isChecked = false
        }

        femaleLabel.setOnClickListener {
            gender ="Female"
            female.isChecked = true
            male.isChecked = false
        }

        female.setOnClickListener {
            gender ="Female"
            male.isChecked = false
        }

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var dobDate = calendar.time
        updateDateInView(dob,calendar)
        val alertDialog = builder.show()

        dob.setOnClickListener {
            val dpd = DatePickerDialog(this@MainActivity,  { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                dobDate = calendar.time
                updateDateInView(dob,calendar)
            }, year, month, day)
            dpd.getDatePicker().setMaxDate(Date().time)
            dpd.show()
        }

        saveAction.setOnClickListener {
            if(!name.text.toString().trim().equals("") && !gender.equals("") ) {
                addNote(name.text.toString(),gender,dobDate)
                alertDialog.dismiss()
            }
        }
    }

    private fun updateDateInView(dob :TextView,calendar: Calendar) {
        val myFormat = "MMM dd, yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dob.text = sdf.format(calendar.getTime())
    }
}