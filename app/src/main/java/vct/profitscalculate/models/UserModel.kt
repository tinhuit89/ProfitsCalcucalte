package vct.profitscalculate.models

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.where
import vct.profitscalculate.common.Constants
import vct.profitscalculate.common.Utilities


open class UserModel(
        @PrimaryKey open var id: Long = Utilities.getTimestamp(),
        open var name: String = "",
        open var capoVolume: Double = 0.0,
        open var type: Int = -1,
        open var discountValue: Double = 0.0,

        open var revenueMonthly: Double = 0.0, // lợi nhuận mang về trong tháng (RELAYER)
        open var percentMonthly: Double = 0.0, // phần trăm mang về trong tháng (AFFILIATE)

        open var crossForRelayer: Double = 0.0, // lợi nhuận nhận với tư cách Relayer
        open var crossForFlatform: Double = 0.0, // lợi nhuận nhận với tư cách Relayer
        open var crossForAffiliate: Double = 0.0, // lợi nhuận nhận với tư cách Relayer
        open var crossForHolder: Double = 0.0, // lợi nhuận nhận với tư cách Relayer

        open var finalCrossReceived: Double = 0.0, // Thực nhận cuối cùng

        open var profitOfKeepFine: Double = 0.0,

        open var isViolate: Boolean = false,

        open var blockAtMonthId: Long = -1L,

        open var finesPaided: Double = 0.0,

        open var finesMustPaid: Double = 0.0,

        open var exerciseId: Int = Constants.TYPE_EXERCISE,

        open var crossForHolderQuater: Double = 0.0,

        open var finalCrossQuater: Double = 0.0,

        open var isReport: Boolean = true


) : RealmObject() {

    fun copy(
            id: Long = this.id,
            name: String = this.name,
            capoVolume: Double = this.capoVolume,
            type: Int = this.type,
            discountValue: Double = this.discountValue,
            revenueMonthly: Double = this.revenueMonthly,
            percentMonthly: Double = this.percentMonthly,
            crossForRelayer: Double = this.crossForRelayer,
            crossForFlatform: Double = this.crossForFlatform,
            crossForAffiliate: Double = this.crossForAffiliate,
            crossForHolder: Double = this.crossForHolder,
            finalCrossReceived: Double = this.finalCrossReceived,
            profitOfKeepFine: Double = this.profitOfKeepFine,
            isViolate: Boolean = this.isViolate,
            blockAtMonthId: Long = this.blockAtMonthId,
            finesPaided: Double = this.finesPaided,
            finesMustPaid: Double = this.finesMustPaid,
            exerciseId: Int = this.exerciseId,
            crossForHolderQuater: Double = this.crossForHolderQuater,
            finalCrossQuater: Double = this.finalCrossQuater,
            isReport: Boolean = this.isReport

    ) = UserModel(id, name, capoVolume, type, discountValue,
            revenueMonthly, percentMonthly, crossForRelayer,
            crossForFlatform, crossForAffiliate, crossForHolder, finalCrossReceived,
            profitOfKeepFine, isViolate, blockAtMonthId, finesPaided, finesMustPaid,
            exerciseId, crossForHolderQuater, finalCrossQuater, isReport)


    companion object {
        const val TYPE_RELAYER = 1
        const val TYPE_AFFILIATE = 2
        const val TYPE_HOLDER = 3

        fun isPlatForm(userModel: UserModel): Boolean {
            if (userModel.type == TYPE_RELAYER && userModel.name == "capodex.com") {
                return true
            }
            return false
        }
    }

    fun getPercentHold(totalHold: Double): Double {
        var percentHold = capoVolume * 100 / totalHold
        return percentHold
    }


    fun getDiscountForRelayer(): Double {
        if (type == UserModel.TYPE_RELAYER) {
            return discountValue
        } else {
            return 0.0
        }
    }


//    fun getDiscountForAffiliate(): Double {
//        if (type == UserModel.TYPE_RELAYER) {
//            return 15.0
//        } else {
//            return 0.0
//        }
//    }

    fun getDiscountForHolder(): Double {
        return 15.0
    }

    fun getDiscountForFlatform(): Double {
        if (type == UserModel.TYPE_RELAYER) {
            return 100 - (getDiscountForRelayer() + getDiscountForAffiliate() + getDiscountForHolder())
        } else {
            return 0.0
        }
    }

    //bài tập 3
    fun getDiscountForAffiliate(typeExercise: Int = Constants.TYPE_EXERCISE): Double {
        if (typeExercise == 1) {
            return 15.0
        } else if (typeExercise == 2) {
            return when (type) {
                UserModel.TYPE_AFFILIATE -> 15.0
                UserModel.TYPE_RELAYER -> 15.0
                else -> 0.0
            }
        } else if (typeExercise == 3) {
            return when (type) {
                UserModel.TYPE_AFFILIATE -> return if (revenueMonthly <= 0) {
                    0.0
                } else if (revenueMonthly > 0 && revenueMonthly <= 5000) {
                    5.0
                } else if (revenueMonthly > 5000 && revenueMonthly <= 10000) {
                    10.0
                } else {
                    15.0
                }
                UserModel.TYPE_RELAYER -> 15.0
                else -> 0.0
            }
        } else {
            return 0.0
        }

    }
}

