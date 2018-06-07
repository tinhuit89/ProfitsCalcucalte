package vct.profitscalculate.models


import android.util.Log
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import vct.profitscalculate.common.Utilities
import io.realm.RealmList
import io.realm.Sort
import vct.profitscalculate.AppController
import vct.profitscalculate.common.Constants

open class ReportModel(
        @PrimaryKey var id: Long = Utilities.getTimestamp(),

        open var name: String = "",

        open var percentFromAffiliate: Double = 0.0,

        open var listUser: RealmList<UserModel> = RealmList(),

        open var isQuater: Boolean = false,

        open var totalProfitOfMonth: Double = 0.0, // tổng lợi nhuận các sàn

        open var textReport: String = "",

        open var holdersDiscountDefault: Double = 15.0,

        open var keepFineProfit: Double = 0.0,

        open var exerciseId: Int = Constants.TYPE_EXERCISE,

        open var finalCrossFlatformReceived: Double = 0.0,

        open var type: Int = -1

) : RealmObject() {

    open fun copy(
            id: Long = this.id,
            name: String = this.name,
            percentFromAffiliate: Double = this.percentFromAffiliate,
            listUser: RealmList<UserModel> = this.listUser,
            isQuater: Boolean = this.isQuater,
            totalProfitOfMonth: Double = this.totalProfitOfMonth,
            textReport: String = this.textReport,
            holdersDiscountDefault: Double = this.holdersDiscountDefault,
            keepFineProfit: Double = this.keepFineProfit,
            exerciseId: Int = this.exerciseId,
            finalCrossFlatformReceived: Double = this.finalCrossFlatformReceived,
            type: Int = this.type

    ) = ReportModel(id, name, percentFromAffiliate, listUser, isQuater, totalProfitOfMonth,
            textReport, holdersDiscountDefault, keepFineProfit, exerciseId, finalCrossFlatformReceived, type)


    private fun getTotalCapoHold(): Double {
        var sum = 0.0
        listUser.forEach {
            sum += it.capoVolume
        }
        return sum
    }

    fun executeCalculateCrossMonth() {


        //Calculate for Exchange Relayer
        listUser.filter { it.type == UserModel.TYPE_RELAYER }.forEach { relayerParent ->
            val revenueMonthlyOfRelayer = relayerParent.revenueMonthly
            val discountForRelayer = relayerParent.getDiscountForRelayer()
            val discountForAffiliate = relayerParent.getDiscountForAffiliate()

            val discountForFlatform = relayerParent.getDiscountForFlatform()

            relayerParent.crossForFlatform = (discountForFlatform / 100) * revenueMonthlyOfRelayer

            relayerParent.crossForRelayer = (discountForRelayer / 100) * revenueMonthlyOfRelayer

            relayerParent.crossForAffiliate = (discountForAffiliate / 100) * revenueMonthlyOfRelayer * (100 - percentFromAffiliate) / 100
        }


        if (Constants.TYPE_EXERCISE == 3) {
            listUser.filter { it.type == UserModel.TYPE_AFFILIATE }.forEach { affiliateChild ->
                val revenueFromAffChild = totalProfitOfMonth * (percentFromAffiliate / 100)
                affiliateChild.revenueMonthly = (affiliateChild.percentMonthly / 100) * revenueFromAffChild
                Utilities.showLog("Rev ${affiliateChild.name}: ${affiliateChild.revenueMonthly}", "REV")

            }

            val revAff = percentFromAffiliate / 100 * totalProfitOfMonth
            listUser.filter { it.type == UserModel.TYPE_AFFILIATE }.forEach { affiliateChild ->
                val revAffChild = affiliateChild.percentMonthly / 100 * revAff
                affiliateChild.crossForAffiliate = revAffChild * affiliateChild.getDiscountForAffiliate() / 100

                Utilities.showLog("Rev ${affiliateChild.name}: ${affiliateChild.crossForAffiliate}", "REV")
            }

        } else {
            listUser.filter { it.type == UserModel.TYPE_AFFILIATE }.forEach { affiliateChild ->
                listUser.filter { it.type == UserModel.TYPE_RELAYER }.forEach { relayerParent ->
                    val discountForAffiliate = relayerParent.getDiscountForAffiliate()
                    val revenueMonthlyFromAffiliateChild = (percentFromAffiliate / 100) * (discountForAffiliate / 100 * relayerParent.revenueMonthly)
                    affiliateChild.crossForAffiliate += (affiliateChild.percentMonthly / 100) * revenueMonthlyFromAffiliateChild
                }
            }
        }

        //Calculate for Holder Child
        listUser.filter { it.type == UserModel.TYPE_HOLDER || it.type == UserModel.TYPE_RELAYER }.forEach { userChild ->
            val discountForHolder = userChild.getDiscountForHolder()
            val crossForAllHolder = discountForHolder / 100 * totalProfitOfMonth
            userChild.crossForHolder = (userChild.getPercentHold(getTotalCapoHold()) / 100) * crossForAllHolder
        }


        for (user in listUser) {
            if (user.type == UserModel.TYPE_RELAYER) {
                user.finalCrossReceived = user.crossForRelayer + user.crossForAffiliate
            }

            if (user.type == UserModel.TYPE_AFFILIATE) {
                user.finalCrossReceived = user.crossForAffiliate
            }

            finalCrossFlatformReceived += user.crossForFlatform
        }

        for (user in listUser) {
            if (user.isViolate) {
                if (user.type == UserModel.TYPE_AFFILIATE) {
                    if (this.id - user.blockAtMonthId == 2L) {
                        user.isViolate = false
                        user.blockAtMonthId = -1
                    } else {
                        user.blockAtMonthId = this.id
                        finalCrossFlatformReceived += user.finalCrossReceived
                        user.finalCrossReceived = 0.0
                    }
                } else if (user.type == UserModel.TYPE_RELAYER) {
                    val remainingFines = user.finesMustPaid - user.finesPaided

                    var isUnFine = false
                    if (remainingFines > 0 && user.finalCrossReceived < remainingFines) {
                        keepFineProfit += user.finalCrossReceived
                        this.finalCrossFlatformReceived += user.finalCrossReceived
                        user.finesPaided += user.finalCrossReceived

                        user.finalCrossReceived = 0.0
                        isUnFine = false
                    } else if (remainingFines > 0 && user.finalCrossReceived >= remainingFines) {
                        keepFineProfit += remainingFines
                        user.finesPaided += remainingFines
                        this.finalCrossFlatformReceived += remainingFines
                        user.finalCrossReceived = user.finalCrossReceived - remainingFines
                        isUnFine = true
                    }

                    if (isUnFine) {
                        user.isViolate = false
                        user.blockAtMonthId = -1
                        user.finesPaided = 0.0
                        user.finesMustPaid = 0.0
                    }
                }
            }
        }

        listUser.filter { it.type == UserModel.TYPE_RELAYER }.forEach { relayerParent ->
            if (UserModel.isPlatForm(relayerParent)) {
                relayerParent.finalCrossReceived += finalCrossFlatformReceived
            }
        }


        textReport = ""

        textReport += "\n--> Lợi nhuận của Relayer: \n"

        this.listUser.filter { it.type == UserModel.TYPE_RELAYER }.forEach { relayer ->
            textReport += "${relayer.name} : ${Utilities.getDecimalCurrency(relayer.finalCrossReceived)}$\n"
        }

        textReport += "\n--> Lợi nhuận của Affiliate: \n"

        this.listUser.filter { it.type == UserModel.TYPE_AFFILIATE }.forEach { affiliate ->
            textReport += "${affiliate.name} : ${Utilities.getDecimalCurrency(affiliate.finalCrossReceived)}$\n"
        }

        textReport += "\n--> Lợi nhuận của Holder: \n"

        this.listUser.filter { it.type == UserModel.TYPE_RELAYER || it.type == UserModel.TYPE_HOLDER }.forEach { affiliate ->
            textReport += "${affiliate.name} : ${Utilities.getDecimalCurrency(affiliate.crossForHolder)}$\n"
        }

        Log.d(Constants.TAG, textReport)
    }


    fun executeCrossForQuater() {

        var monthOne: ReportModel? = null
        var monthTwo: ReportModel? = null
        var monthThree: ReportModel? = null

        var listMonth = AppController.realmInstance().where(ReportModel::class.java).equalTo("type", ReportModel.TYPE_MONTH).findAll().sort("id", Sort.DESCENDING)

        if (listMonth.size >= 2) {
            monthThree = listMonth[0]
            monthTwo = listMonth[1]
            monthOne = listMonth[2]
        }
        textReport = ""

        textReport += "--> Lợi nhuận của Holder: \n"

        if (monthOne != null && monthTwo != null && monthThree != null) {

            for ((index, user) in listUser.withIndex()) {
                user.crossForHolder = monthOne.listUser[index]!!.crossForHolder
                user.crossForHolder += monthTwo.listUser[index]!!.crossForHolder
                user.crossForHolder += monthThree.listUser[index]!!.crossForHolder
            }

            this.listUser.filter { it.type == UserModel.TYPE_RELAYER || it.type == UserModel.TYPE_HOLDER }.forEach { userChild ->
                textReport += "${userChild.name} : ${Utilities.getDecimalCurrency(userChild.crossForHolder)}$\n"
            }

            textReport += "--> Tổng lợi nhuận của Quý: \n"

            for ((index, user) in listUser.withIndex()) {

                user.finalCrossReceived = user.crossForHolder
                user.finalCrossReceived += monthOne.listUser[index]!!.finalCrossReceived
                user.finalCrossReceived += monthTwo.listUser[index]!!.finalCrossReceived
                user.finalCrossReceived += monthThree.listUser[index]!!.finalCrossReceived

                textReport += "${user.name} : ${Utilities.getDecimalCurrency(user.finalCrossReceived)}$\n"
            }

        }
    }

    companion object {
        const val finePaidCount: Double = 80000.0
        const val TYPE_MONTH: Int = 1
        const val TYPE_QUATER: Int = 2

    }


}