package vct.profitscalculate.controller

import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import vct.profitscalculate.R
import vct.profitscalculate.models.UserModel
import android.widget.TextView
import android.widget.EditText
import vct.profitscalculate.common.Constants
import vct.profitscalculate.common.Utilities
import vct.profitscalculate.interfaces.DataCallback


class ItemAddUserController(private val activity: Activity, var userModel: UserModel, private var lnRoot: LinearLayout, private var callback: DataCallback) : View.OnClickListener {
    private lateinit var viewChild: View

    private lateinit var edName: EditText
    private lateinit var edCapo: EditText
    private lateinit var tvPercent: TextView
    private lateinit var btnDel: Button

    init {

        this.initView()
        this.initData()

    }

    fun initView() {
        viewChild = View.inflate(activity, R.layout.item_user_layout, null)

        edName = viewChild.findViewById(R.id.edName)
        edCapo = viewChild.findViewById(R.id.edCapo)
        tvPercent = viewChild.findViewById(R.id.tvPercent)
        btnDel = viewChild.findViewById(R.id.btnDel)

        btnDel.setOnClickListener(this)
    }

    fun initData() {
        lnRoot.post({ lnRoot.addView(viewChild) })

        setViewData()
    }

    fun setViewData() {
        edName.setText(userModel.name)
        edCapo.setText(Utilities.getDecimalCurrency(userModel.capoVolume))
        tvPercent.text = "%.2f".format(userModel.percentHold) + " (%)"
    }

    override fun onClick(v: View?) {
        if (v == btnDel) {
            callback.callback(Constants.DELETE_USER_ACTION, lnRoot.indexOfChild(viewChild), userModel)
            lnRoot.removeView(viewChild)
        }
    }
}