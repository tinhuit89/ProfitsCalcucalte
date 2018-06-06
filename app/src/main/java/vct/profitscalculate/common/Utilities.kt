package vct.profitscalculate.common

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import java.io.ObjectInputStream
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import io.realm.Realm
import io.realm.internal.IOException
import java.io.File


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

    fun getTimestamp(): Long {
        return (System.currentTimeMillis())
    }


    fun getDecimalCurrency(money: Double): String {
        val format = DecimalFormatSymbols()
        format.decimalSeparator = '.'
        format.groupingSeparator = ','
        val df = DecimalFormat("#,###,###,###", format)
        return df.format(money)
    }

//    fun saveObJectToFile(objectToWrite: Any, activity: Context, _fileName: String) {
//        try {
//            val fos = activity.openFileOutput(_fileName, Context.MODE_PRIVATE)
//            val os: ObjectOutputStream
//            os = ObjectOutputStream(fos)
//            os.writeObject(objectToWrite)
//            os.close()
//            Log.d(Constants.TAG, "Save $_fileName file success")
//        } catch (e: Throwable) {
//            Log.d(Constants.TAG, "Save file error: " + {e.localizedMessage })
//            e.printStackTrace()
//        }
//
////        val mPrefs = activity.getSharedPreferences(_fileName, Context.MODE_PRIVATE)
////        val prefsEditor = mPrefs.edit()
////        val gson = Gson()
////        val json = gson.toJson(objectToWrite)
////        prefsEditor.putString(_fileName, json)
////        prefsEditor.commit()
//    }

//    fun getListUserFromFile(activity: Activity, _fileName: String): ArrayList<UserModel> {
//        var simpleClass: ArrayList<UserModel> = arrayListOf()
//        try {
//            val fis = activity.openFileInput(_fileName)
//            val `is` = ObjectInputStream(fis)
//            simpleClass = `is`.readObject() as ArrayList<UserModel>
//            `is`.close()
//        } catch (e: Throwable) {
//            e.printStackTrace()
//        }
//        return  simpleClass
////
////        val mPrefs = activity.getSharedPreferences(_fileName, Context.MODE_PRIVATE)
////        val gson = Gson()
////        val json = mPrefs.getString(_fileName, "")
////        val obj = gson.fromJson<ArrayList<UserModel>>(json, ArrayList<UserModel>()::class.java!!)
//
////        return obj
//    }

    @Suppress("UNCHECKED_CAST")
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
        return simpleClass
    }

    fun getNumberFromString(text: String): String {
        val numberOnly = text.replace("[^0-9]".toRegex(), "")
        return if (numberOnly.equals("", ignoreCase = true)) {
            "0"
        } else numberOnly
    }

    fun backupDatabase(realm: Realm) {
        try {
            val exportRealmPath: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val exportRealmFileName: String = "database_capo_profit.realm"
            // create a backup file
            val exportRealmFile: File
            exportRealmFile = File(exportRealmPath, exportRealmFileName)

            // if backup file already exists, delete it
            exportRealmFile.delete()

            // copy current realm to backup file
            realm.writeCopyTo(exportRealmFile)
            Log.d(Constants.TAG, "Backup database success")
        } catch (e: IOException) {
            Log.d(Constants.TAG, "Backup database error ${e.printStackTrace()}")
            e.printStackTrace()
        }
    }
}
