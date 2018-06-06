package vct.profitscalculate.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import vct.profitscalculate.R
import vct.profitscalculate.activity.MainTabActivty
import vct.profitscalculate.common.Constants
import vct.profitscalculate.controller.ItemUserForMonth
import vct.profitscalculate.interfaces.DataCallback
import vct.profitscalculate.models.UserModel
import android.widget.LinearLayout
import android.widget.EditText
import android.widget.TextView
import vct.profitscalculate.common.Utilities
import vct.profitscalculate.models.MonthProfitModel


class TwoFragment : Fragment(), View.OnClickListener {


    private lateinit var activity: MainTabActivty

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

    private var listUserForMonth: ArrayList<ItemUserForMonth> = ArrayList()

    private var listMonthProfit: ArrayList<MonthProfitModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.two_fragment_layout, container, false)
        activity = getActivity() as MainTabActivty
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
    }

    private fun initData() {

//        Utilities.saveObJectToFile(ArrayList<MonthProfitModel>(), activity, Constants.LIST_REPORT_FILENAME)

        edPercentCoincapo.setText("35")
        edPercentRelayers.setText("35")
        edPercentAffiliates.setText("15")
        edPercentHolders.setText("15")
        btnAddMonth.setOnClickListener(this)

        listMonthProfit = Utilities.getListDataFromFile(activity, Constants.LIST_REPORT_FILENAME)

        for (monthProfitModel in listMonthProfit) {
            reportDataFormonth(monthProfitModel)
        }

    }

    private fun setDataMonth() {
        listUserForMonth = ArrayList()
        lnAffiliateProfit.removeAllViews()
        lnRelayerProfit.removeAllViews()
        edPercentFromAffiliates.setText("0")
        for (itemUserController in activity.listItemUserController) {
            if (itemUserController.userModel.type == UserModel.UserModel.TYPE_RELAYER) {
                listUserForMonth.add(ItemUserForMonth(activity, itemUserController.userModel, lnRelayerProfit, dataCallback))
            } else if (itemUserController.userModel.type == UserModel.UserModel.TYPE_AFFILIATE) {
                listUserForMonth.add(ItemUserForMonth(activity, itemUserController.userModel, lnAffiliateProfit, dataCallback))
            } else if (itemUserController.userModel.type == UserModel.UserModel.TYPE_HOLDER) {
                listUserForMonth.add(ItemUserForMonth(activity, itemUserController.userModel, lnHolderProfit, dataCallback))
            }
        }
    }

    private fun reportDataFormonth(monthProfitModel: MonthProfitModel) {
        var result = tvResult.text.toString()
        result += "\n\n------" + "Dữ liệu " + monthProfitModel.name + "------\n"

        result += "\n--> Lợi nhuận của Affiliate: \n"

        for (userModel in monthProfitModel.calculateProfitAffiliate()) {
            if (userModel.type == UserModel.UserModel.TYPE_AFFILIATE) {
                result += userModel.name + ": " + Utilities.getDecimalCurrency(userModel.getTotalProfit()) + "$\n"
            }
        }

        result += "\n--> Lợi nhuận của Relayer: \n"
        for (userModel in monthProfitModel.calculateProfitRelayer()) {
            if (userModel.type == UserModel.UserModel.TYPE_RELAYER) {
                result += userModel.name + ": " + Utilities.getDecimalCurrency(userModel.getTotalProfit()) + "$\n"
            }
        }


        var currMonth = Utilities.getNumberFromString(monthProfitModel.name).toInt()
        if (currMonth % 3 == 0) {
            result += "\n--> Lợi nhuận của Holder: \n"
            for (userModel in MonthProfitModel.calculateProfitHolder(listMonthProfit[listMonthProfit.size - 1],
                    listMonthProfit[listMonthProfit.size - 2], listMonthProfit[listMonthProfit.size - 3])) {
                if (userModel.type == UserModel.UserModel.TYPE_RELAYER || userModel.type == UserModel.UserModel.TYPE_HOLDER) {
                    result += userModel.name + ": " + Utilities.getDecimalCurrency(userModel.profitOfHolder) + "$\n"
                }
            }
        }

        tvResult.text = result
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

            var nameMonth = ""
            nameMonth = if (listMonthProfit.size > 0) {
                var lastMonth = listMonthProfit[listMonthProfit.size - 1]
                "Tháng " + (Utilities.getNumberFromString(lastMonth.name).toInt() + 1).toString()
            } else {
                "Tháng 1"
            }

            var monthProfitModel = MonthProfitModel(nameMonth)
            monthProfitModel.percentFromAffiliate = edPercentFromAffiliates.text.toString().toDouble()

            var totalProfitOfMonth = 0.0
            for (itemUserForMonth in listUserForMonth) {
                if (itemUserForMonth.userModel.type == UserModel.UserModel.TYPE_RELAYER) {
                    itemUserForMonth.userModel.discountValue = itemUserForMonth.discountValue
                    itemUserForMonth.userModel.profitTakeMonth = itemUserForMonth.dolarProfit
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
                    itemUserForMonth.userModel.profitTakeMonth = itemUserForMonth.userModel.percentTakeMonth * totalProfitOfMonth / 100
                }

                monthProfitModel.listUser.add(itemUserForMonth.userModel)
            }

            monthProfitModel.totalProfitOfMonth = totalProfitOfMonth


            listMonthProfit.add(monthProfitModel)

            Utilities.saveObJectToFile(listMonthProfit, activity, Constants.LIST_REPORT_FILENAME)
            reportDataFormonth(monthProfitModel)

        }
    }

}
