package vct.profitscalculate.interfaces


import android.annotation.SuppressLint
import android.util.Log
import io.realm.Realm
import io.realm.kotlin.where
import vct.profitscalculate.common.Constants
import vct.profitscalculate.models.ReportModel

class MonthProfitInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")

        fun getMaxId(realm: Realm): Long {
            return try {
                realm.beginTransaction()
                var nextID = (realm.where(ReportModel::class.java).max("id"))
                nextID = if (nextID != null) {
                    nextID as Long + 1L
                } else {
                    1L
                }
                realm.commitTransaction()
                nextID
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }

        fun addMonth(realm: Realm, model: ReportModel): ReportModel? {
            return try {
                realm.beginTransaction()
                var nextID = (realm.where(ReportModel::class.java).max("id"))
                nextID = if (nextID != null) {
                    nextID as Long + 1L
                } else {
                    0L
                }
                model.id = nextID
                var monthAdded = realm.copyToRealmOrUpdate(model)
                realm.commitTransaction()
                monthAdded
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun deleteMonth(realm: Realm, id: Long): Boolean {
            realm.beginTransaction()
            return try {
                var modelGet = realm.where(ReportModel::class.java).equalTo("id", id).findFirst()
                if (modelGet != null) {
                    Log.d(Constants.TAG, "Month delete: $modelGet")
                    modelGet.deleteFromRealm()
                } else {
                    Log.d(Constants.TAG, "Month has been delete")
                }
                realm.commitTransaction()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                realm.commitTransaction()
                false
            }
        }

        fun getTotalHold(realm: Realm): Double {
            var listUserFromdb = realm.where<vct.profitscalculate.models.ReportModel>().findAll()
            return listUserFromdb.sum("capoVolume").toDouble()
        }
    }
}