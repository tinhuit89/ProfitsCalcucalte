package vct.profitscalculate.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.realm.Sort
import vct.profitscalculate.R
import vct.profitscalculate.activity.MainTabActivity
import vct.profitscalculate.common.Constants
import vct.profitscalculate.controller.ItemUserForMonth
import vct.profitscalculate.interfaces.DataCallback
import vct.profitscalculate.models.UserModel
import vct.profitscalculate.AppController
import vct.profitscalculate.common.Utilities
import vct.profitscalculate.interfaces.MonthProfitInterface
import vct.profitscalculate.interfaces.UserInterface
import vct.profitscalculate.models.ReportModel


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
    private lateinit var cbQuater: CheckBox

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
        cbQuater = view.findViewById(R.id.cbQuater)
    }

    private fun initData() {

        edPercentCoincapo.setText("35")
        edPercentRelayers.setText("35")
        edPercentAffiliates.setText("15")
        edPercentHolders.setText("15")
        btnAddMonth.setOnClickListener(this)

        setDataMonth()

        edMonthName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (edMonthName.text.toString() != "") {
                    setDataDefault(edMonthName.text.toString().toInt())
                }
            }
        })
    }

    private fun setDataMonth() {
        listUserForMonth = ArrayList()
        lnAffiliateProfit.removeAllViewsInLayout()
        lnRelayerProfit.removeAllViewsInLayout()
        lnHolderProfit.removeAllViewsInLayout()
        edPercentFromAffiliates.setText("0")
        for (itemUserController in activity.listItemUserController) {
            when {
                itemUserController.userModel.type == UserModel.TYPE_RELAYER -> listUserForMonth.add(ItemUserForMonth(activity, itemUserController.userModel, lnRelayerProfit, dataCallback))
                itemUserController.userModel.type == UserModel.TYPE_AFFILIATE -> listUserForMonth.add(ItemUserForMonth(activity, itemUserController.userModel, lnAffiliateProfit, dataCallback))
                itemUserController.userModel.type == UserModel.TYPE_HOLDER -> listUserForMonth.add(ItemUserForMonth(activity, itemUserController.userModel, lnHolderProfit, dataCallback))
            }
        }
    }

    override fun onResume() {
        super.onResume()
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
                if (itemUserForMonth.userModel.type == UserModel.TYPE_RELAYER) {
                    if (itemUserForMonth.edVol.text.toString() == "" || itemUserForMonth.edVol.text.toString().toDouble() < 0) {
                        return Utilities.showToast(activity, "Vui lòng nhập đầy đủ fee thu được trên từng Relayer")
                    }

                    if (itemUserForMonth.edDiscount.text.toString() == "" || itemUserForMonth.edDiscount.text.toString().toDouble() < 0) {
                        return Utilities.showToast(activity, "Vui lòng nhập chiết khấu cho Relayer")
                    }
                }

                if (itemUserForMonth.userModel.type == UserModel.TYPE_AFFILIATE) {
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

            var monthProfitModel = ReportModel(name = nameMonth, type = ReportModel.TYPE_MONTH)
            monthProfitModel.isQuater = cbQuater.isChecked
            monthProfitModel.percentFromAffiliate = edPercentFromAffiliates.text.toString().toDouble()


            var totalProfitOfMonth = 0.0
            var maxIdUserDb = UserInterface.getMaxId(realmInstance)
            for ((index, itemUserForMonth) in listUserForMonth.withIndex()) {
                itemUserForMonth.userModel.discountValue = itemUserForMonth.discountValue

                if (itemUserForMonth.userModel.type == UserModel.TYPE_RELAYER) {
                    itemUserForMonth.userModel.revenueMonthly = itemUserForMonth.revenueMonthly
                    totalProfitOfMonth += itemUserForMonth.revenueMonthly
                }

                if (itemUserForMonth.userModel.type == UserModel.TYPE_AFFILIATE) {
                    itemUserForMonth.userModel.percentMonthly = itemUserForMonth.percentMonthly
                    Log.d(Constants.TAG, "Percent: ${itemUserForMonth.userModel.percentMonthly}")
                }

                if (itemUserForMonth.cbViolate.isChecked) {
                    itemUserForMonth.userModel.isViolate = true
                    itemUserForMonth.userModel.remainingBlockMonth += ReportModel.blockMonthCount
                    itemUserForMonth.userModel.finesMustPaid += ReportModel.finePaidCount
                } else {
                    var listMonth = AppController.realmInstance().where(ReportModel::class.java).equalTo("type", ReportModel.TYPE_MONTH).findAll().sort("id", Sort.DESCENDING)
                    var lastMonth: ReportModel? = null
                    if (listMonth.size >= 1) {
                        lastMonth = listMonth[0]
                    }

                    if (lastMonth != null) {
                        lastMonth.listUser.filter { it.name == itemUserForMonth.userModel.name }.map {
                            itemUserForMonth.userModel.isViolate = it.isViolate
                            itemUserForMonth.userModel.remainingBlockMonth = it.remainingBlockMonth
                            itemUserForMonth.userModel.finesPaided = it.finesPaided
                            itemUserForMonth.userModel.finesMustPaid = it.finesMustPaid
                        }
                    }
                }

                var userCopy = itemUserForMonth.userModel.copy()
                userCopy.id = userCopy.id + (index + 1) + maxIdUserDb
                userCopy.isReport = true
                monthProfitModel.listUser.add(userCopy)
            }

            monthProfitModel.totalProfitOfMonth = totalProfitOfMonth

            var quaterReport: ReportModel? = null
            if (monthProfitModel.isQuater) {
                quaterReport = monthProfitModel.copy(name = "Tổng kế Quý", type = ReportModel.TYPE_QUATER)
            }

            monthProfitModel.executeCalculateCrossMonth()

            var monthAdded = MonthProfitInterface.addMonth(realmInstance, monthProfitModel)

            if (quaterReport != null) {
                quaterReport.executeCrossForQuater()
                var quaterReportAdded = MonthProfitInterface.addMonth(realmInstance, quaterReport)
            }

            if (monthAdded != null) {
                Utilities.showToast(activity, "Tạo doanh thu thành công")
            }
        }
    }

    var listOne = listOf(80000, 90000, 40000, 40000, 5, 0, 30, 40, 25)
    var listTwo = listOf(260000, 200000, 300000, 90000, 0, 10, 55, 5, 30)
    var listThree = listOf(800000, 520000, 580000, 900000, 10, 15, 35, 15, 25)
    private fun setDataDefault(month: Int) {
        if (month == 1) {
            for ((index, item) in listOne.withIndex()) {
                listUserForMonth[index].edVol.setText(item.toString())

                listUserForMonth[index].cbViolate.isChecked = false

                if (listUserForMonth[index].userModel.name == "A4") {
                    listUserForMonth[index].cbViolate.isChecked = true
                }

            }
            edPercentFromAffiliates.setText("10")
        } else if (month == 2) {
            for ((index, item) in listTwo.withIndex()) {
                listUserForMonth[index].edVol.setText(item.toString())

                listUserForMonth[index].cbViolate.isChecked = false

                if (listUserForMonth[index].userModel.name == "dex.nami.trade") {
                    listUserForMonth[index].cbViolate.isChecked = true
                }

            }
            edPercentFromAffiliates.setText("15")
        } else if (month == 3) {
            for ((index, item) in listThree.withIndex()) {
                listUserForMonth[index].edVol.setText(item.toString())
                listUserForMonth[index].cbViolate.isChecked = false

            }
            edPercentFromAffiliates.setText("25")
        }
    }

}
