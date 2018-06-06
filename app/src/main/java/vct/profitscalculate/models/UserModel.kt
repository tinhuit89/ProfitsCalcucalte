package vct.profitscalculate.models

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.where
import vct.profitscalculate.common.Utilities


open class UserModel(
        @PrimaryKey open var id: Long = Utilities.getTimestamp(),
        open var name: String = "",
        open var capoVolume: Double = 0.0,
        open var type: Int = -1,
        open var discountValue: Double = 0.0,
        open var profitTakeMonth: Double = 0.0, // lợi nhuận mang về trong tháng (RELAYER)
        open var percentTakeMonth: Double = 0.0, // phần trăm mang về trong tháng (AFFILIATE)
        open var profitOfRelayer: Double = 0.0, // lợi nhuận nhận được từ chức vị Relayer
        open var profitOfAffiliate: Double = 0.0, // lợi nhuận nhận được từ chức vị Affiliate
        open var profitOfHolder: Double = 0.0, // lợi nhuận nhận được từ chức vị Holder
        open var profitOfKeepFine: Double = 0.0,
        open var isViolate: Boolean = false,
        open var blockAtMonthId: Long = -1L,
        open var finesPaid: Double = 0.0


) : RealmObject() {

    fun copy(
            id: Long = this.id,
            name: String = this.name,
            capoVolume: Double = this.capoVolume,
            type: Int = this.type,
            discountValue: Double = this.discountValue,
            profitTakeMonth: Double = this.profitTakeMonth,
            percentTakeMonth: Double = this.percentTakeMonth,
            profitOfRelayer: Double = this.profitOfRelayer,
            profitOfAffiliate: Double = this.profitOfAffiliate,
            profitOfHolder: Double = this.profitOfHolder,
            profitOfKeepFine: Double = this.profitOfKeepFine,
            isViolate: Boolean = this.isViolate,
            blockAtMonthId: Long = this.blockAtMonthId,
            finesPaid: Double = this.finesPaid

    ) = UserModel(id, name, capoVolume, type, discountValue,
            profitTakeMonth, percentTakeMonth, profitOfRelayer,
            profitOfAffiliate, profitOfHolder, profitOfKeepFine, isViolate, blockAtMonthId, finesPaid)


    object UserModel {
        const val TYPE_RELAYER = 1
        const val TYPE_AFFILIATE = 2
        const val TYPE_HOLDER = 3

    }

    fun getPercentHold(realm: Realm): Double {
        var listUserFromdb = realm.where<vct.profitscalculate.models.UserModel>().findAll()
        var capoTotal = listUserFromdb.sum("capoVolume").toDouble()
        var percentHold = capoVolume * 100 / capoTotal
        return percentHold
    }

    fun getTotalProfitRelayer(): Double {
        return profitOfRelayer + profitOfKeepFine
    }

    //bài tập 2
//    fun getDiscountForAff(): Double {
//        return when (type) {
//            UserModel.TYPE_AFFILIATE -> 15.0
//            UserModel.TYPE_RELAYER -> 15.0
//            else -> 0.0
//        }
//    }

    //bài tập 3
    fun getDiscountForAff(): Double {
        return when (type) {
            UserModel.TYPE_AFFILIATE -> return if (profitTakeMonth <= 5000) {
                5.0
            } else if (profitTakeMonth > 50000 && profitTakeMonth <= 10000) {
                10.0
            } else {
                15.0
            }
            UserModel.TYPE_RELAYER -> 15.0
            else -> 0.0
        }
    }
}

