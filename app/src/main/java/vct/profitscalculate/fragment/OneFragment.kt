package vct.profitscalculate.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import vct.profitscalculate.R
import vct.profitscalculate.activity.MainTabActivty
import vct.profitscalculate.models.UserModel
import vct.profitscalculate.common.Constants
import vct.profitscalculate.common.Utilities
import vct.profitscalculate.controller.ItemAddUserController
import vct.profitscalculate.interfaces.DataCallback


class OneFragment : Fragment(), View.OnClickListener {


    private lateinit var activity: MainTabActivty
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.one_fragment_layout, container, false)
        activity = getActivity() as MainTabActivty
        initView(view)
        initData()
        return view
    }

    private fun initView(view: View) {
        lnScrollRoot = view.findViewById(R.id.lnScrollRoot)
        rlAddRelayer = view.findViewById(R.id.rlAddRelayer)
        edAddName = view.findViewById(R.id.edAddName)
        edAddCapo = view.findViewById(R.id.edAddCapo)
        radioGroup = view.findViewById(R.id.radioGroup)
        rbRelayer = view.findViewById(R.id.rbRelayer)
        rbAffiliate = view.findViewById(R.id.rbAffiliate)
        rbHolder = view.findViewById(R.id.rbHolder)
        btnAddRelayer = view.findViewById(R.id.btnAddRelayer)
        lnAddItem = view.findViewById(R.id.lnAddItem)
        tvTotalCapo = view.findViewById(R.id.tvTotalCapo)
    }

    private fun initData() {
        btnAddRelayer.setOnClickListener(this)

        var listUserFromFile = Utilities.getListUserFromFile(activity, Constants.LIST_USER_FILENAME)
        Log.d("tinhvc", listUserFromFile.size.toString())
        if (listUserFromFile.size > 0) {
            for (userModel in listUserFromFile) {
                addItemUserController(userModel)
            }
        } else {

//            listUserFromFile.add(UserModel("Capodex", 10000.0, UserModel.UserModel.TYPE_RELAYER))
//            listUserFromFile.add(UserModel("Tranderviet", 10000.0, UserModel.UserModel.TYPE_RELAYER))
//            listUserFromFile.add(UserModel("Blogtienao", 50000.0, UserModel.UserModel.TYPE_RELAYER))
//
//            listUserFromFile.add(UserModel("A1", 0.0, UserModel.UserModel.TYPE_AFFILIATE))
//            listUserFromFile.add(UserModel("A2", 0.0, UserModel.UserModel.TYPE_AFFILIATE))
//            listUserFromFile.add(UserModel("A3", 0.0, UserModel.UserModel.TYPE_AFFILIATE))
//            listUserFromFile.add(UserModel("A4", 0.0, UserModel.UserModel.TYPE_AFFILIATE))
//            listUserFromFile.add(UserModel("A5", 0.0, UserModel.UserModel.TYPE_AFFILIATE))
//
//            listUserFromFile.add(UserModel("H1", 4000.0, UserModel.UserModel.TYPE_HOLDER))
//            listUserFromFile.add(UserModel("H2", 4000.0, UserModel.UserModel.TYPE_HOLDER))
//            listUserFromFile.add(UserModel("H3", 4000.0, UserModel.UserModel.TYPE_HOLDER))
//            listUserFromFile.add(UserModel("H4", 4000.0, UserModel.UserModel.TYPE_HOLDER))
//            listUserFromFile.add(UserModel("H5", 4000.0, UserModel.UserModel.TYPE_HOLDER))
//            listUserFromFile.add(UserModel("H6", 4000.0, UserModel.UserModel.TYPE_HOLDER))
//            listUserFromFile.add(UserModel("H7", 4000.0, UserModel.UserModel.TYPE_HOLDER))

            listUserFromFile.add(UserModel("Capodex", 20000.0, UserModel.UserModel.TYPE_RELAYER))
            listUserFromFile.add(UserModel("Tranderviet", 60000.0, UserModel.UserModel.TYPE_RELAYER))
            listUserFromFile.add(UserModel("Blogtienao", 500000.0, UserModel.UserModel.TYPE_RELAYER))

            listUserFromFile.add(UserModel("A1", 0.0, UserModel.UserModel.TYPE_AFFILIATE))
            listUserFromFile.add(UserModel("A2", 0.0, UserModel.UserModel.TYPE_AFFILIATE))
            listUserFromFile.add(UserModel("A3", 0.0, UserModel.UserModel.TYPE_AFFILIATE))
            listUserFromFile.add(UserModel("A4", 0.0, UserModel.UserModel.TYPE_AFFILIATE))
            listUserFromFile.add(UserModel("A5", 0.0, UserModel.UserModel.TYPE_AFFILIATE))

            listUserFromFile.add(UserModel("H1", 8000.0, UserModel.UserModel.TYPE_HOLDER))
            listUserFromFile.add(UserModel("H2", 8000.0, UserModel.UserModel.TYPE_HOLDER))
            listUserFromFile.add(UserModel("H3", 8000.0, UserModel.UserModel.TYPE_HOLDER))
            listUserFromFile.add(UserModel("H4", 10000.0, UserModel.UserModel.TYPE_HOLDER))
            listUserFromFile.add(UserModel("H5", 10000.0, UserModel.UserModel.TYPE_HOLDER))
            listUserFromFile.add(UserModel("H6", 10000.0, UserModel.UserModel.TYPE_HOLDER))
            listUserFromFile.add(UserModel("H7", 15000.0, UserModel.UserModel.TYPE_HOLDER))
            listUserFromFile.add(UserModel("H8", 15000.0, UserModel.UserModel.TYPE_HOLDER))
            listUserFromFile.add(UserModel("H9", 15000.0, UserModel.UserModel.TYPE_HOLDER))
            listUserFromFile.add(UserModel("H10", 2000.0, UserModel.UserModel.TYPE_HOLDER))
            listUserFromFile.add(UserModel("H11", 2000.0, UserModel.UserModel.TYPE_HOLDER))
            listUserFromFile.add(UserModel("H12", 2000.0, UserModel.UserModel.TYPE_HOLDER))


            for (userModel in listUserFromFile) {
                addItemUserController(userModel)
            }

            recalTotal()
        }
    }

    private val dataCallback: DataCallback = object : DataCallback {
        override fun callback(action: String, pos: Int, objects: Any) {
            if (action == Constants.DELETE_USER_ACTION) {
                Utilities.showToast(activity, (objects as UserModel).name)
                for (itemAddUserController in activity.listItemUserController) {
                    if (itemAddUserController.userModel.id == (objects as UserModel).id) {
                        activity.listItemUserController.remove(itemAddUserController)
                        break
                    }
                }

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
        addItemUserController(UserModel(edAddName.text.toString(), capoVolume, typeUser))
    }

    private fun addItemUserController(userModel: UserModel) {
        activity.listItemUserController.add(ItemAddUserController(activity, userModel, lnAddItem, dataCallback))
        edAddName.setText("")
        edAddCapo.setText("")
        recalTotal()
    }

    private fun recalTotal() {
        tvTotalCapo.text = "CAPO \n" + Utilities.getDecimalCurrency(getTotalCapo())
        updatePercent()
    }

    private fun getTotalCapo(): Double {
        var totalHold = 0.0
        for (itemAddUserController in activity.listItemUserController) {
            totalHold += itemAddUserController.userModel.capoVolume
        }
        return totalHold
    }

    private fun updatePercent() {
        activity.listUsers = arrayListOf()
        for (itemAddUserController in activity.listItemUserController) {
            itemAddUserController.userModel.holdPercent(getTotalCapo())
            itemAddUserController.setViewData()
            activity.listUsers.add(itemAddUserController.userModel)
        }

        Utilities.saveObJectToFile(activity.listUsers, activity, Constants.LIST_USER_FILENAME)
    }


    override fun onClick(v: View?) {
        if (v == btnAddRelayer) {
            addUser()
        }
    }

}
