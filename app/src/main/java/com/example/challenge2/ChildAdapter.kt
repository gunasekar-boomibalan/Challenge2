package com.example.challenge2

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import io.objectbox.Box
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ChildAdapter(var list: List<Child>, val childBox: Box<Child>?) : RecyclerView.Adapter<ChildAdapter.AdapterHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return AdapterHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterHolder, position: Int) {
        val data = list.get(position)
        holder.name.setText(""+data.id+") "+data.name )
        holder.gender.setText(data.gender)
        loadDate(holder.dob,data.dob)
        loadDate(holder.createdDate,data.timeStamp)
        holder.delete.setOnClickListener {
            if(childBox?.remove(data)?:false){
                Toast.makeText(holder.itemView.context,"Child "+data.name+" Removed.",Toast.LENGTH_SHORT).show()
            }
        }
        holder.edit.setOnClickListener {
            updateDialog(holder.itemView.context,data)
        }
    }

    inner class AdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name :TextView = itemView.findViewById(R.id.name)
        var gender :TextView = itemView.findViewById(R.id.gender)
        var dob :TextView = itemView.findViewById(R.id.dob)
        var createdDate :TextView = itemView.findViewById(R.id.createdDate)
        var delete :TextView = itemView.findViewById(R.id.deleteAction)
        var edit :TextView = itemView.findViewById(R.id.editAction)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setChilds(list : List<Child>){
        this.list = list
        notifyDataSetChanged()
    }

    private fun loadDate(view :TextView,date: Date?) {
        try {
            val myFormat = "MMM dd, yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            view.text = sdf.format(date)
        } catch (e : Exception){
            view.setText("")
        }
    }

    fun updateDialog(ctx : Context,data : Child) {

        val dialog = LayoutInflater.from(ctx).inflate(R.layout.add_child_layout, null)
        val builder = AlertDialog.Builder(ctx)
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

        name.setText( data.name?:"")
        if(data.gender == "Male"){
            gender ="Male"
            male.isChecked = true
        }else{
            gender ="Female"
            female.isChecked = true
        }

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
        calendar.time = data?.dob
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var dobDate = calendar.time
        updateDateInView(dob,calendar)
        val alertDialog = builder.show()

        dob.setOnClickListener {
            val dpd = DatePickerDialog(ctx,  { view, year, monthOfYear, dayOfMonth ->
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
                updateChild(data,name.text.toString(),gender,dobDate)
                alertDialog.dismiss()
            }
        }
    }

    private fun updateChild(data : Child,name : String,gender : String, dob : Date) {
        val child = Child()
        child.id = data.id
        child.name = name
        child.gender = gender
        child.dob = dob
        child.timeStamp = data.timeStamp
        childBox?.put(child)
    }

    private fun updateDateInView(dob :TextView,calendar: Calendar) {
        val myFormat = "MMM dd, yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dob.text = sdf.format(calendar.getTime())
    }
}