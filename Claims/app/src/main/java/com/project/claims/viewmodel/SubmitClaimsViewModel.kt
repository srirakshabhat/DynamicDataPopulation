package com.project.claims.viewmodel

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import androidx.lifecycle.ViewModel
import java.util.*

class SubmitClaimsViewModel : ViewModel() {

    /*to load json data from assets folder*/
    fun loadJSONFromAsserts(fileName: String, context:Context): String {
        return context.assets.open(fileName).bufferedReader().use { reader ->
            reader.readText()
        }
    }

    fun pickDateFromTo(context:Context?, date:TextView) {
        val c = Calendar.getInstance()
        val getYear = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth->
            date.setText("$year-${monthOfYear+1}-$dayOfMonth")
        },getYear,month,day)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH,1)
        dpd.getDatePicker().setMinDate(calendar.getTimeInMillis())
        dpd.show()

    }


}