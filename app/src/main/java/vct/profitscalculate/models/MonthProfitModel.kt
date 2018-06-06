package vct.profitscalculate.models


import android.util.Log
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import vct.profitscalculate.common.Utilities
import vct.profitscalculate.interfaces.MonthProfitInterface
import io.realm.RealmList
import vct.profitscalculate.AppController
import vct.profitscalculate.common.Constants

open class MonthProfitModel(
        @PrimaryKey var id: Long = Utilities.getTimestamp(),

        open var name: String = "",

        open var percentFromAffiliate: Double = 0.0,

        open var listUser: RealmList<UserModel> = RealmList(),

        open var isQuater: Boolean = false,

        open var totalProfitOfMonth: Double = 0.0, // tổng lợi nhuận các sàn

        open var textReport: String = "",

        open var holdersDiscountDefault: Double = 15.0,

        open var keepFineProfit: Double = 0.0

) : RealmObject() {


    init {
        this.id = MonthProfitInterface.getMaxId(AppController.realmInstance())
    }

    open fun copy(
            id: Long = this.id,
            name: String = this.name,
            percentFromAffiliate: Double = this.percentFromAffiliate,
            listUser: RealmList<UserModel> = this.listUser,
            isQuater: Boolean = this.isQuater,
            totalProfitOfMonth: Double = this.totalProfitOfMonth,
            textReport: String = this.textReport,
            holdersDiscountDefault: Double = this.holdersDiscountDefault,
            keepFineProfit: Double = this.keepFineProfit

    ) = MonthProfitModel(id, name, percentFromAffiliate, listUser, isQuater, totalProfitOfMonth, textReport, holdersDiscountDefault, keepFineProfit)

    companion object {
        const val finePaidCount: Double = 80000.0
    }


    private fun getTotalCapoHold(): Double {
        var totalHold = 0.0
        for (userModel in listUser) {
            totalHold += userModel.capoVolume
        }
        return totalHold
    }

    fun executeCalculateProfit() {

        //Calculate profit for User
        for (userModel in listUser) {
            val profitShareAffiliates = (userModel.getDiscountForAff() / 100) * totalProfitOfMonth

            val profitNormalAffiliates = (percentFromAffiliate / 100) * profitShareAffiliates

            val profitExchangeAffiliates = ((100 - percentFromAffiliate) / 100) * profitShareAffiliates

            if (userModel.type == UserModel.UserModel.TYPE_AFFILIATE) {
                userModel.profitOfAffiliate = userModel.percentTakeMonth * profitNormalAffiliates / 100
            } else if (userModel.type == UserModel.UserModel.TYPE_RELAYER) {
                userModel.profitOfAffiliate = userModel.percentTakeMonth * profitExchangeAffiliates / 100
                var profitShareRelayers = (userModel.discountValue / 100) * (totalProfitOfMonth)
                userModel.profitOfRelayer = (userModel.percentTakeMonth * profitShareRelayers / 100) + userModel.profitOfAffiliate
            }


            if (userModel.isViolate) {
                if (userModel.type == UserModel.UserModel.TYPE_AFFILIATE) {
                    if (this.id - userModel.blockAtMonthId == 2L) {
                        userModel.isViolate = false
                        userModel.blockAtMonthId = -1
                    } else {
                        userModel.blockAtMonthId = this.id
                        keepFineProfit += userModel.profitOfAffiliate
                        userModel.profitOfAffiliate = 0.0
                    }
                } else if (userModel.type == UserModel.UserModel.TYPE_RELAYER) {

                    if (userModel.profitOfRelayer <= MonthProfitModel.finePaidCount - userModel.finesPaid) {
                        keepFineProfit += userModel.profitOfRelayer
                        userModel.profitOfRelayer = 0.0
                    } else {
                        keepFineProfit += MonthProfitModel.finePaidCount - userModel.finesPaid
                        userModel.profitOfRelayer = userModel.profitOfRelayer - (MonthProfitModel.finePaidCount - userModel.finesPaid)
                    }

                    if (userModel.finesPaid == MonthProfitModel.finePaidCount) {
                        userModel.isViolate = false
                        userModel.blockAtMonthId = -1
                        userModel.finesPaid = 0.0
                    }

                    userModel.blockAtMonthId = this.id
                    keepFineProfit += userModel.profitOfAffiliate
                }
            }
        }

        for (userModel in listUser) {
            if (userModel.name == "capodex.com" || userModel.id == 0L) {
                userModel.profitOfKeepFine = keepFineProfit
                break
            }
        }

        if (isQuater) {
            var monthOneDb = AppController.realmInstance().where(MonthProfitModel::class.java).equalTo("id", this.id - 1).findFirst()
            var monthTwoDb = AppController.realmInstance().where(MonthProfitModel::class.java).equalTo("id", this.id - 2).findFirst()

            if (monthOneDb != null && monthTwoDb != null) {
                var monthOne = monthOneDb.copy()
                var monthTwo = monthTwoDb.copy()

                var totalProfitQuarter = (monthOne.totalProfitOfMonth + monthTwo.totalProfitOfMonth + this.totalProfitOfMonth) * holdersDiscountDefault / 100
                for (userModel in this.listUser) {
                    if (userModel.type == UserModel.UserModel.TYPE_RELAYER || userModel.type == UserModel.UserModel.TYPE_HOLDER) {
                        userModel.profitOfHolder = userModel.getPercentHold(AppController.realmInstance()) * totalProfitQuarter / 100
                    }
                }
            }
        }

        textReport = ""
        textReport += "\n--> Lợi nhuận của Relayer: \n"
        for (userModel in listUser) {
            if (userModel.type == UserModel.UserModel.TYPE_RELAYER) {
                textReport += "${userModel.name} : ${Utilities.getDecimalCurrency(userModel.profitOfRelayer)} + ${Utilities.getDecimalCurrency(userModel.profitOfKeepFine)} = ${Utilities.getDecimalCurrency(userModel.getTotalProfitRelayer())}$\n"
            }
        }

        textReport += "--> Lợi nhuận của Affiliate: \n"

        for (userModel in listUser) {
            if (userModel.type == UserModel.UserModel.TYPE_AFFILIATE) {
                textReport += userModel.name + ": " + Utilities.getDecimalCurrency(userModel.profitOfAffiliate) + "$\n"
            }
        }

        if (isQuater) {
            for (userModel in listUser) {
                if (userModel.type == UserModel.UserModel.TYPE_RELAYER || userModel.type == UserModel.UserModel.TYPE_HOLDER) {
                    textReport += userModel.name + ": " + Utilities.getDecimalCurrency(userModel.profitOfHolder) + "$\n"
                }
            }
        }

        Log.d(Constants.TAG, textReport)
    }

}