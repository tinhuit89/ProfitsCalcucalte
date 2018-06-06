package vct.profitscalculate.models


import vct.profitscalculate.common.Utilities
import java.io.Serializable


data class MonthProfitModel(var name: String) : Serializable {
    var id = Utilities.getTimestamp()

    var percentFromAffiliate: Double = 0.0

    var listUser: ArrayList<UserModel> = arrayListOf()

    var totalProfitOfMonth: Double = 0.0 // tổng lợi nhuận các sàn

    var percentHold: Double = 0.0

    init {

    }


    private fun getTotalCapoHold(): Double {
        var totalHold = 0.0
        for (userModel in listUser) {
            totalHold += userModel.capoVolume
        }
        return totalHold
    }


    fun calculateProfitAffiliate(): ArrayList<UserModel> {
        var listAffiliates: ArrayList<UserModel> = arrayListOf()

        for (userModel in listUser) {

            var profitAllAffiliates = userModel.discountValue * totalProfitOfMonth / 100

            var totalDolarAffiliates = percentFromAffiliate * profitAllAffiliates / 100

            var totalDolarAffiliatesRelayer = (100 - percentFromAffiliate) * profitAllAffiliates / 100

            if (userModel.type == UserModel.UserModel.TYPE_AFFILIATE) {
                userModel.profitOfAffiliate = userModel.percentTakeMonth * totalDolarAffiliates / 100
                listAffiliates.add(userModel)
            } else if (userModel.type == UserModel.UserModel.TYPE_RELAYER) {
                userModel.profitOfAffiliate = userModel.percentTakeMonth * totalDolarAffiliatesRelayer / 100
                listAffiliates.add(userModel)
            }
        }
        return listAffiliates
    }

    fun calculateProfitRelayer(): ArrayList<UserModel> {
        var listRelayers: ArrayList<UserModel> = arrayListOf()
        for (userModel in listUser) {

            var profitAllRelayer = userModel.discountValue * totalProfitOfMonth / 100

            if (userModel.type == UserModel.UserModel.TYPE_RELAYER) {
                userModel.profitOfAffiliate = userModel.percentTakeMonth * profitAllRelayer / 100
                listRelayers.add(userModel)
            }
        }

        return listRelayers
    }

    companion object {

        var coinCapoDiscoint: Double = 35.0
        var relayersDiscoint: Double = 35.0
        var affiliatesDiscoint: Double = 15.0
        var holdersDiscoint: Double = 15.0

        fun calculateProfitHolder(month1: MonthProfitModel, month2: MonthProfitModel, month3: MonthProfitModel): ArrayList<UserModel> {
            var listHolder: ArrayList<UserModel> = arrayListOf()

            for (userModel in month1.listUser) {
                var totalProfitQuarter = (month1.totalProfitOfMonth + month2.totalProfitOfMonth + month3.totalProfitOfMonth) * holdersDiscoint / 100
                if (userModel.type == UserModel.UserModel.TYPE_RELAYER || userModel.type == UserModel.UserModel.TYPE_HOLDER) {
                    userModel.profitOfHolder = userModel.percentHold * totalProfitQuarter / 100
                    listHolder.add(userModel)
                }
            }
            return listHolder
        }
    }
}