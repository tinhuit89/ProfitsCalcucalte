package vct.profitscalculate.models


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import vct.profitscalculate.common.Utilities


open class ReportMonthModel(
        @PrimaryKey open var id: Long = Utilities.getTimestamp(),
        open var user: UserModel? = null,
        open var profitOfMonth: Double = 0.0

) : RealmObject() {

    fun copy(
            id: Long = this.id,
            user: UserModel? = this.user,
            profitOfMonth: Double = this.profitOfMonth
    ) = ReportMonthModel(id, user, profitOfMonth)


}