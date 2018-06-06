package vct.profitscalculate.models

import vct.profitscalculate.common.Utilities
import java.io.Serializable


data class UserModel(var name: String, var capoVolume: Double = 0.0, var type: Int) : Serializable {
    var id = Utilities.getTimestamp()

    var discountValue: Double = 0.0

    object UserModel {
        const val TYPE_RELAYER = 1
        const val TYPE_AFFILIATE = 2
        const val TYPE_HOLDER = 3

    }

    var percentHold: Double = 0.0

    init {
        setDiscountValueDefault()
    }

    fun holdPercent(capoTotal: Double): Double {
        percentHold = capoVolume * 100 / capoTotal
        return percentHold
    }

    fun setDiscountValueDefault() {
        if (type == UserModel.TYPE_RELAYER) {
            discountValue = 35.0
        }
        if (type == UserModel.TYPE_AFFILIATE) {
            discountValue = 15.0
        }
        if (type == UserModel.TYPE_HOLDER) {
            discountValue = 15.0
        }
    }

    var profitTakeMonth: Double = 0.0 // lợi nhuận mang về trong tháng (RELAYER)

    var percentTakeMonth: Double = 0.0 // phần trăm mang về trong tháng (AFFILIATE)

    var profitOfRelayer: Double = 0.0 // lợi nhuận nhận được từ chức vị Relayer

    var profitOfAffiliate: Double = 0.0 // lợi nhuận nhận được từ chức vị Affiliate

    var profitOfHolder: Double = 0.0 // lợi nhuận nhận được từ chức vị Holder

    fun getTotalProfit(): Double {
        return when (type) {
            UserModel.TYPE_RELAYER -> profitOfRelayer + profitOfAffiliate
            UserModel.TYPE_AFFILIATE -> profitOfAffiliate
            else -> profitOfHolder
        }
    }

//    var profitGetMonth: Double = 0.0 // lợi nhuận sẽ nhận trong tháng ($)

    var profitGetQuarter: Double = 0.0 // lợi nhuận sẽ nhận trong quý ($)
}