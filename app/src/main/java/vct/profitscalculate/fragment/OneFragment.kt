package vct.profitscalculate.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.realm.Realm
import io.realm.kotlin.where
import vct.profitscalculate.AppController
import vct.profitscalculate.R
import vct.profitscalculate.activity.MainTabActivity
import vct.profitscalculate.models.UserModel
import vct.profitscalculate.common.Constants
import vct.profitscalculate.common.Utilities
import vct.profitscalculate.controller.ItemAddUserController
import vct.profitscalculate.interfaces.DataCallback
import vct.profitscalculate.interfaces.UserInterface

class OneFragment : Fragment(), View.OnClickListener {


    private lateinit var activity: MainTabActivity
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

    private val realmInstance = AppController.realmInstance()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.one_fragment_layout, container, false)
        activity = getActivity() as MainTabActivity
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

        var listUserFromdb = realmInstance.where<UserModel>().findAll()

        Log.d(Constants.TAG, listUserFromdb.size.toString())
        if (listUserFromdb.size > 0) {
            for (userModel in listUserFromdb) {
                addItemUserController(userModel)
            }
        } else {
            var listUserDefault = ArrayList<UserModel>()

            // Bài tập 1
//            listUserDefault.add(UserModel(name = "capodex.com", capoVolume = 10000.0, type = UserModel.UserModel.TYPE_RELAYER, discountValue = 35.0))
//            listUserDefault.add(UserModel(name = "dex.traderviet.com", capoVolume = 10000.0, type = UserModel.UserModel.TYPE_RELAYER, discountValue = 35.0))
//            listUserDefault.add(UserModel(name = "dex.blogtienao.com", capoVolume = 50000.0, type = UserModel.UserModel.TYPE_RELAYER, discountValue = 35.0))
//
//            listUserDefault.add(UserModel(name = "A1", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "A2", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "A3", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "A4", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "A5", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 15.0))
//
//            listUserDefault.add(UserModel(name = "H1", capoVolume = 4000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H2", capoVolume = 4000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H3", capoVolume = 4000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H4", capoVolume = 4000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H5", capoVolume = 4000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H6", capoVolume = 4000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H7", capoVolume = 4000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))

            // Bài tập 2
//            listUserDefault.add(UserModel(name = "capodex.com", capoVolume = 20000.0, type = UserModel.UserModel.TYPE_RELAYER, discountValue = 35.0))
//            listUserDefault.add(UserModel(name = "dex.traderviet.com", capoVolume = 60000.0, type = UserModel.UserModel.TYPE_RELAYER, discountValue = 25.0))
//            listUserDefault.add(UserModel(name = "dex.blogtienao.com", capoVolume = 500000.0, type = UserModel.UserModel.TYPE_RELAYER, discountValue = 45.0))
//
//            listUserDefault.add(UserModel(name = "A1", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "A2", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "A3", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "A4", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "A5", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 15.0))
//
//            listUserDefault.add(UserModel(name = "H1", capoVolume = 8000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H2", capoVolume = 8000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H3", capoVolume = 8000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H4", capoVolume = 10000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H5", capoVolume = 10000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H6", capoVolume = 10000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H7", capoVolume = 15000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H8", capoVolume = 15000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H9", capoVolume = 15000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H10", capoVolume = 2000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H11", capoVolume = 2000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
//            listUserDefault.add(UserModel(name = "H12", capoVolume = 2000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))

            // Bài tập 3
            listUserDefault.add(UserModel(name = "capodex.com", capoVolume = 80000.0, type = UserModel.UserModel.TYPE_RELAYER, discountValue = 35.0))
            listUserDefault.add(UserModel(name = "dex.traderviet.com", capoVolume = 120000.0, type = UserModel.UserModel.TYPE_RELAYER, discountValue = 25.0))
            listUserDefault.add(UserModel(name = "dex.blogtienao.com", capoVolume = 120000.0, type = UserModel.UserModel.TYPE_RELAYER, discountValue = 40.0))
            listUserDefault.add(UserModel(name = "dex.nami.trade", capoVolume = 180000.0, type = UserModel.UserModel.TYPE_RELAYER, discountValue = 30.0))

            listUserDefault.add(UserModel(name = "A1", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 0.0))
            listUserDefault.add(UserModel(name = "A2", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 0.0))
            listUserDefault.add(UserModel(name = "A3", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 0.0))
            listUserDefault.add(UserModel(name = "A4", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 0.0))
            listUserDefault.add(UserModel(name = "A5", capoVolume = 0.0, type = UserModel.UserModel.TYPE_AFFILIATE, discountValue = 0.0))

            listUserDefault.add(UserModel(name = "H1", capoVolume = 8000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H2", capoVolume = 8000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H3", capoVolume = 8000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H4", capoVolume = 10500.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H5", capoVolume = 10500.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H6", capoVolume = 10500.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H7", capoVolume = 15000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H8", capoVolume = 15000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H9", capoVolume = 15000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H10", capoVolume = 20000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H11", capoVolume = 20000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H12", capoVolume = 20000.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H13", capoVolume = 23500.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H14", capoVolume = 23500.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H15", capoVolume = 23500.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H16", capoVolume = 29700.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H17", capoVolume = 29700.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))
            listUserDefault.add(UserModel(name = "H18", capoVolume = 29700.0, type = UserModel.UserModel.TYPE_HOLDER, discountValue = 15.0))

            for (userModel in listUserDefault) {
                var userAdded = UserInterface.addUser(realmInstance, userModel)
                if (userAdded != null) {
                    Log.d(Constants.TAG, "Add user ${userAdded.name} - ${userAdded.id}")
                    addItemUserController(userAdded)
                }
            }

            recalTotal()
        }
    }

    private val dataCallback: DataCallback = object : DataCallback {
        override fun callback(action: String, pos: Int, objects: Any) {
            if (action == Constants.DELETE_USER_ACTION) {
                var userDeleted = (objects as UserModel)

                Utilities.showToast(activity, userDeleted.name)

                for (itemAddUserController in activity.listItemUserController) {
                    if (itemAddUserController.userModel.id == userDeleted.id) {
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

        var userModel = UserInterface.addUser(realmInstance, UserModel(name = edAddName.text.toString(), capoVolume = capoVolume, type = typeUser))

        if (userModel != null) {
            addItemUserController(userModel)
        }
    }

    private fun addItemUserController(userModel: UserModel) {
        activity.listItemUserController.add(ItemAddUserController(activity, userModel.copy(), lnAddItem, dataCallback))
        edAddName.setText("")
        edAddCapo.setText("")
        recalTotal()
    }

    private fun recalTotal() {
        tvTotalCapo.text = "CAPO \n" + Utilities.getDecimalCurrency(getTotalCapo())
        updatePercent()
    }

    private fun getTotalCapo(): Double {
        return UserInterface.getTotalHold(realmInstance)
    }

    private fun updatePercent() {
        activity.listUsers = arrayListOf()
        for (itemAddUserController in activity.listItemUserController) {
            itemAddUserController.setViewData()
            activity.listUsers.add(itemAddUserController.userModel)
        }
    }


    override fun onClick(v: View?) {
        if (v == btnAddRelayer) {
            addUser()
        }
    }
}
