package vct.profitscalculate.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import vct.profitscalculate.R
import vct.profitscalculate.activity.MainTabActivity
import vct.profitscalculate.common.Constants
import vct.profitscalculate.controller.ItemUserForMonth
import vct.profitscalculate.interfaces.DataCallback
import vct.profitscalculate.models.UserModel
import android.widget.LinearLayout
import android.widget.EditText
import android.widget.TextView
import io.realm.Realm
import vct.profitscalculate.AppController
import vct.profitscalculate.common.Utilities
import vct.profitscalculate.interfaces.MonthProfitInterface
import vct.profitscalculate.models.MonthProfitModel


class TwoFragment : Fragment(), View.OnClickListener {


    private lateinit var activity: MainTabActivity

    private lateinit var edPercentCoincapo: EditText
    private lateinit var edPercentRelayers: EditText
    private lateinit var edPercentAffiliates: EditText
    private lateinit var edPercentHolders: EditText
    private lateinit var lnScrollRoot: LinearLayout
    private lateinit var lnItemMonth: LinearLayout
    private lateinit var lnRelayerProfit: LinearLayout
    private lateinit var edPercentFromAffiliates: EditText
    private lateinit var lnAffiliateProfit: LinearLayout
    private lateinit var btnAddMonth: Button
    private lateinit var lnAddItem: LinearLayout
    private lateinit var tvResult: TextView
    private lateinit var lnHolderProfit: LinearLayout
    private lateinit var edMonthName: EditText

    private var listUserForMonth: ArrayList<ItemUserForMonth> = ArrayList()

    private val realmInstance = AppController.realmInstance()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.two_fragment_layout, container, false)
        activity = getActivity() as MainTabActivity
        initView(view)
        initData()
        return view
    }

    private fun initView(view: View) {
        edPercentCoincapo = view.findViewById(R.id.edPercentCoincapo)
        edPercentRelayers = view.findViewById(R.id.edPercentRelayers)
        edPercentAffiliates = view.findViewById(R.id.edPercentAffiliates)
        edPercentHolders = view.findViewById(R.id.edPercentHolders)
        lnScrollRoot = view.findViewById(R.id.lnScrollRoot)
        lnItemMonth = view.findViewById(R.id.lnItemMonth)
        lnRelayerProfit = view.findViewById(R.id.lnRelayerProfit)
        edPercentFromAffiliates = view.findViewById(R.id.edPercentFromAffiliates)
        lnAffiliateProfit = view.findViewById(R.id.lnAffiliateProfit)
        btnAddMonth = view.findViewById(R.id.btnAddMonth)
        lnAddItem = view.findViewById(R.id.lnAddItem)
        tvResult = view.findViewById(R.id.tvResult)
        lnHolderProfit = view.findViewById(R.id.lnHolderProfit)
        edMonthName = view.findViewById(R.id.edMonthName)
    }

    private fun initData() {

        edPercentCoincapo.setText("35")
        edPercentRelayers.setText("35")
        edPercentAffiliates.setText("15")
        edPercentHolders.setText("15")
        btnAddMonth.setOnClickListener(this)

    }

    private fun setDataMonth() {
        listUserForMonth = ArrayList()
        lnAffiliateProfit.removeAllViewsInLayout()
        lnRelayerProfit.removeAllViewsInLayout()
        lnHolderProfit.removeAllViewsInLayout()
        edPercentFromAffiliates.setText("0")
        for (itemUserController in activity.listItemUserController) {
            when {
                itemUserController.userModel.type == UserModel.UserModel.TYPE_RELAYER -> listUserForMonth.add(ItemUserForMonth(activity, itemUserController.userModel, lnRelayerProfit, dataCallback))
                itemUserController.userModel.type == UserModel.UserModel.TYPE_AFFILIATE -> listUserForMonth.add(ItemUserForMonth(activity, itemUserController.userModel, lnAffiliateProfit, dataCallback))
                itemUserController.userModel.type == UserModel.UserModel.TYPE_HOLDER -> listUserForMonth.add(ItemUserForMonth(activity, itemUserController.userModel, lnHolderProfit, dataCallback))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setDataMonth()
    }

    private val dataCallback: DataCallback = object : DataCallback {
        override fun callback(action: String, pos: Int, objects: Any) {
            if (action == Constants.DELETE_USER_ACTION) {

            }
        }
    }


    override fun onClick(v: View?) {
        if (v == btnAddMonth) {
            if (edPercentAffiliates.text.toString() == "" || edPercentAffiliates.text.toString().toDouble() < 0) {
                return Utilities.showToast(activity, "Vui lòng nhập tổng phí trên sàn phát sinh nhờ từ affiliates")
            }

            for (itemUserForMonth in listUserForMonth) {
                if (itemUserForMonth.userModel.type == UserModel.UserModel.TYPE_RELAYER) {
                    if (itemUserForMonth.edVol.text.toString() == "" || itemUserForMonth.edVol.text.toString().toDouble() < 0) {
                        return Utilities.showToast(activity, "Vui lòng nhập đầy đủ fee thu được trên từng Relayer")
                    }

                    if (itemUserForMonth.edDiscount.text.toString() == "" || itemUserForMonth.edDiscount.text.toString().toDouble() < 0) {
                        return Utilities.showToast(activity, "Vui lòng nhập chiết khấu cho Relayer")
                    }
                }

                if (itemUserForMonth.userModel.type == UserModel.UserModel.TYPE_AFFILIATE) {
                    if (itemUserForMonth.edVol.text.toString() == "" || itemUserForMonth.edVol.text.toString().toDouble() < 0) {
                        return Utilities.showToast(activity, "Vui lòng nhập đầy đủ tỉ lệ mang khách về của affiliate hàng tháng ")
                    }
                }
            }

            if (edMonthName.text.toString() == "") {
                return Utilities.showToast(activity, "Vui lòng nhập tên tháng báo cáo")
            }

            Utilities.showHideKeyBoard(activity, false, edMonthName)

            var nameMonth = "Tháng " + edMonthName.text.toString()

            var monthProfitModel = MonthProfitModel(name = nameMonth)
            monthProfitModel.isQuater = (monthProfitModel.id % 3 == 0L)
            monthProfitModel.percentFromAffiliate = edPercentFromAffiliates.text.toString().toDouble()

            var totalProfitOfMonth = 0.0
            for (itemUserForMonth in listUserForMonth) {
                itemUserForMonth.userModel.discountValue = itemUserForMonth.discountValue

                if (itemUserForMonth.userModel.type == UserModel.UserModel.TYPE_RELAYER) {
                    itemUserForMonth.userModel.profitTakeMonth = itemUserForMonth.dolarProfit //* (100 - monthProfitModel.percentFromAffiliate)
                    totalProfitOfMonth += itemUserForMonth.dolarProfit
                }

                if (itemUserForMonth.userModel.type == UserModel.UserModel.TYPE_AFFILIATE) {
                    itemUserForMonth.userModel.percentTakeMonth = itemUserForMonth.percentProfit
                }

            }

            for (itemUserForMonth in listUserForMonth) {

                if (itemUserForMonth.userModel.type == UserModel.UserModel.TYPE_RELAYER) {
                    itemUserForMonth.userModel.percentTakeMonth = itemUserForMonth.userModel.profitTakeMonth * 100 / totalProfitOfMonth
                }

                if (itemUserForMonth.userModel.type == UserModel.UserModel.TYPE_AFFILIATE) {
                    val amountWithTotal = monthProfitModel.percentFromAffiliate / 100 * totalProfitOfMonth
                    itemUserForMonth.userModel.profitTakeMonth = itemUserForMonth.userModel.percentTakeMonth / 100 * amountWithTotal
                }

                if (itemUserForMonth.cbViolate.isChecked) {
                    itemUserForMonth.userModel.isViolate = true
                }
                monthProfitModel.listUser.add(itemUserForMonth.userModel)
            }

//            for (itemUserForMonth in listUserForMonth) {
//
//                if (itemUserForMonth.userModel.type == UserModel.UserModel.TYPE_RELAYER) {
//                    itemUserForMonth.userModel.percentTakeMonth = itemUserForMonth.userModel.profitTakeMonth * (100 - monthProfitModel.percentFromAffiliate) / totalProfitOfMonth
//                }
//
//                if (itemUserForMonth.userModel.type == UserModel.UserModel.TYPE_AFFILIATE) {
//                    itemUserForMonth.userModel.profitTakeMonth = itemUserForMonth.userModel.percentTakeMonth * (monthProfitModel.percentFromAffiliate * totalProfitOfMonth / 100)
//                }
//
//                if (itemUserForMonth.cbViolate.isChecked) {
//                    itemUserForMonth.userModel.isViolate = true
//                }
//                monthProfitModel.listUser.add(itemUserForMonth.userModel)
//            }

            monthProfitModel.totalProfitOfMonth = totalProfitOfMonth

            monthProfitModel.executeCalculateProfit()

            var monthAdded = MonthProfitInterface.addMonth(realmInstance, monthProfitModel)

            if (monthAdded != null) {
                Log.d(Constants.TAG, "Create with id: ${monthAdded.id}")
                Utilities.showToast(activity, "Tạo doanh thu thành công")
            }
        }
    }

}
