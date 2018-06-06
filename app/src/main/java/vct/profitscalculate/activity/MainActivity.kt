package vct.profitscalculate.activity

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import vct.profitscalculate.R
import vct.profitscalculate.models.UserModel
import vct.profitscalculate.common.Constants
import vct.profitscalculate.common.Utilities
import vct.profitscalculate.controller.ItemAddUserController
import vct.profitscalculate.interfaces.DataCallback


class MainActivity : AppCompatActivity(), OnClickListener {

    private var activity: Activity = this
    private lateinit var tvTitle: TextView
    private lateinit var lnScrollRoot: LinearLayout
    private lateinit var rlAddRelayer: LinearLayout
    private lateinit var edAddName: EditText
    private lateinit var edAddCapo: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var rbRelayer: RadioButton
    private lateinit var rbAffiliate: RadioButton
    private lateinit var rbHolder: RadioButton
    private lateinit var btnAddRelayer: Button
    private lateinit var lnAddItem: LinearLayout
    private lateinit var tvTotalCapo: TextView

    private var listItemUserController: ArrayList<ItemAddUserController> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.one_fragment_layout)
        initView()
        initData()
    }

    private fun initView() {
        tvTitle = findViewById(R.id.tvTitle)
        lnScrollRoot = findViewById(R.id.lnScrollRoot)
        rlAddRelayer = findViewById(R.id.rlAddRelayer)
        edAddName = findViewById(R.id.edAddName)
        edAddCapo = findViewById(R.id.edAddCapo)
        radioGroup = findViewById(R.id.radioGroup)
        rbRelayer = findViewById(R.id.rbRelayer)
        rbAffiliate = findViewById(R.id.rbAffiliate)
        rbHolder = findViewById(R.id.rbHolder)
        btnAddRelayer = findViewById(R.id.btnAddRelayer)
        lnAddItem = findViewById(R.id.lnAddItem)
        tvTotalCapo = findViewById(R.id.tvTotalCapo)
    }

    private fun initData() {
        recalTotal()
        btnAddRelayer.setOnClickListener(this)
    }

    private val dataCallback: DataCallback = object : DataCallback {
        override fun callback(action: String, pos: Int, objects: Any) {
            if (action == Constants.DELETE_USER_ACTION) {
                Toast.makeText(activity, (objects as UserModel).name, Toast.LENGTH_SHORT).show()
                recalTotal()
            }

        }
    }

    private fun addUser() {
        var typeUser = 0
        if (edAddName.text.toString() == "") {
            return Utilities.showToast(activity, "Vui lòng nhập tên")
        }

        when {
            radioGroup.checkedRadioButtonId == rbRelayer.id -> typeUser = UserModel.UserModel.TYPE_RELAYER
            radioGroup.checkedRadioButtonId == rbAffiliate.id -> typeUser = UserModel.UserModel.TYPE_AFFILIATE
            radioGroup.checkedRadioButtonId == rbHolder.id -> typeUser = UserModel.UserModel.TYPE_HOLDER
        }

        var capoVolume = 0.0
        if (typeUser == UserModel.UserModel.TYPE_RELAYER || typeUser == UserModel.UserModel.TYPE_HOLDER) {
            if (edAddCapo.text.toString() == "" || edAddCapo.text.toString().toDouble() <= 0) {
                return Utilities.showToast(activity, "Vui lòng nhập số lượng CAPO")
            } else {
                capoVolume = edAddCapo.text.toString().toDouble()
            }
        }

        Utilities.showHideKeyBoard(activity, false, edAddName)

        listItemUserController.add(ItemAddUserController(activity, UserModel(edAddName.text.toString(), capoVolume, 1), lnAddItem, dataCallback))

        recalTotal()
    }

    private fun recalTotal() {
        tvTotalCapo.text = "CAPO \n" + Utilities.getDecimalCurrency(getTotalCapo())
        updatePercent()
    }

    private fun getTotalCapo(): Double {
        var totalHold = 0.0
        for (itemAddUserController in listItemUserController) {
            totalHold += itemAddUserController.userModel.capoVolume
        }
        return totalHold
    }

    private fun updatePercent() {
        for (itemAddUserController in listItemUserController) {
            itemAddUserController.userModel.holdPercent(getTotalCapo())
            itemAddUserController.setViewData()
        }
    }


    override fun onClick(v: View?) {
        if (v == btnAddRelayer) {
            addUser()
        }
    }
}
