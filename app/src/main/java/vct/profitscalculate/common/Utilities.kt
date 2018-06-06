package vct.profitscalculate.common

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import vct.profitscalculate.models.UserModel
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


object Utilities {
    fun showToast(activity: Activity, mess: String) {
        Toast.makeText(activity, mess, Toast.LENGTH_SHORT).show()
    }

    fun showHideKeyBoard(activity: Activity, isShow: Boolean?, view: View?) {
        if (view != null) {
            try {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (isShow!!) {
                    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
                    if (view is EditText) {
                        view.requestFocus()
                    }
                } else {
                    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun getTimestamp(): String {
        return (System.currentTimeMillis() / 1000).toString()
    }


    fun getDecimalCurrency(money: Double): String {
        val format = DecimalFormatSymbols()
        format.decimalSeparator = '.'
        format.groupingSeparator = ','
        val df = DecimalFormat("#,###,###,###", format)
        return df.format(money)
    }

    fun saveObJectToFile(objectToWrite: Any, activity: Context, _fileName: String) {
        try {
            val fos = activity.openFileOutput(_fileName, Context.MODE_PRIVATE)
            val os: ObjectOutputStream
            os = ObjectOutputStream(fos)
            os.writeObject(objectToWrite)
            os.close()
            Log.d("tinhvc", "Save $_fileName file success")
        } catch (e: Throwable) {
            Log.d("tinhvc", "Save file error: " + {e.localizedMessage })
            e.printStackTrace()
        }

//        val mPrefs = activity.getSharedPreferences(_fileName, Context.MODE_PRIVATE)
//        val prefsEditor = mPrefs.edit()
//        val gson = Gson()
//        val json = gson.toJson(objectToWrite)
//        prefsEditor.putString(_fileName, json)
//        prefsEditor.commit()
    }

    fun getListUserFromFile(activity: Activity, _fileName: String): ArrayList<UserModel> {
        var simpleClass: ArrayList<UserModel> = arrayListOf()
        try {
            val fis = activity.openFileInput(_fileName)
            val `is` = ObjectInputStream(fis)
            simpleClass = `is`.readObject() as ArrayList<UserModel>
            `is`.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return  simpleClass
//
//        val mPrefs = activity.getSharedPreferences(_fileName, Context.MODE_PRIVATE)
//        val gson = Gson()
//        val json = mPrefs.getString(_fileName, "")
//        val obj = gson.fromJson<ArrayList<UserModel>>(json, ArrayList<UserModel>()::class.java!!)

//        return obj
    }

    fun <T> getListDataFromFile(activity: Activity, _fileName: String): ArrayList<T> {
        var simpleClass: ArrayList<T> = arrayListOf()
        try {
            val fis = activity.openFileInput(_fileName)
            val `is` = ObjectInputStream(fis)
            simpleClass = `is`.readObject() as ArrayList<T>
            `is`.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return  simpleClass
    }

    fun getNumberFromString(text: String): String {
        val numberOnly = text.replace("[^0-9]".toRegex(), "")
        return if (numberOnly.equals("", ignoreCase = true)) {
            "0"
        } else numberOnly
    }

}
