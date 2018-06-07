package vct.profitscalculate.interfaces

import android.annotation.SuppressLint
import android.util.Log
import io.realm.Realm
import io.realm.kotlin.where
import vct.profitscalculate.AppController
import vct.profitscalculate.common.Constants
import vct.profitscalculate.models.UserModel

class UserInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")

        fun getMaxId(realm: Realm): Long {
            return try {
                realm.beginTransaction()
                var nextID = (realm.where(UserModel::class.java).max("id"))
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

        fun addUser(realm: Realm, model: UserModel): UserModel? {
            return try {
                realm.beginTransaction()
                var nextID = (realm.where(UserModel::class.java).max("id"))
                nextID = if (nextID != null) {
                    nextID as Long + 1L
                } else {
                    0L
                }
                model.id = nextID
                var userAdded = realm.copyToRealmOrUpdate(model)
                realm.commitTransaction()
                userAdded
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun updateUser(realm: Realm, model: UserModel): Boolean {
            return try {
                realm.beginTransaction()
                realm.copyToRealm(model)
                realm.commitTransaction()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        fun deleteUser(realm: Realm, id: Long): Boolean {
            realm.beginTransaction()
            return try {
                var userGet = realm.where(UserModel::class.java).equalTo("id", id).findFirst()
                if (userGet != null) {
                    Log.d(Constants.TAG, "User delete: $userGet")
                    userGet.deleteFromRealm()
                } else {
                    Log.d(Constants.TAG, "User has been delete")
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
            var sum = realm.where<UserModel>().equalTo("isReport", false).findAll().sum("capoVolume")
            return sum.toDouble()
        }
    }
}