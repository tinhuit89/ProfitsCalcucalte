package vct.profitscalculate.controller

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import vct.profitscalculate.R
import vct.profitscalculate.interfaces.DataCallback
import vct.profitscalculate.models.UserModel


class ItemUserForMonth(private val activity: Activity, var userModel: UserModel, private var lnRoot: LinearLayout, private var callback: DataCallback) : View.OnClickListener {
    private lateinit var viewChild: View

    private lateinit var lnItem: LinearLayout
    private lateinit var tvName: TextView
    lateinit var edVol: EditText
    private lateinit var tvUnit: TextView
    private lateinit var tvDiscount: TextView
    lateinit var edDiscount: EditText
    private lateinit var tvDiscountUnit: TextView
    private lateinit var rlDiscount: RelativeLayout
    lateinit var cbViolate: CheckBox

    var revenueMonthly: Double = 0.0
    var percentMonthly: Double = 0.0

    var discountValue: Double = 15.0

    init {
        this.initView()
        this.initData()
    }

    fun initView() {
        viewChild = View.inflate(activity, R.layout.item_profit_month_layout, null)

        lnItem = viewChild.findViewById(R.id.lnItem)
        tvName = viewChild.findViewById(R.id.tvName)
        edVol = viewChild.findViewById(R.id.edVol)
        tvUnit = viewChild.findViewById(R.id.tvUnit)
        tvDiscount = viewChild.findViewById(R.id.tvDiscount)
        edDiscount = viewChild.findViewById(R.id.edDiscount)
        tvDiscountUnit = viewChild.findViewById(R.id.tvDiscountUnit)
        rlDiscount = viewChild.findViewById(R.id.rlDiscount)
        cbViolate = viewChild.findViewById(R.id.cbViolate)
    }

    fun initData() {
        if (userModel.type != UserModel.TYPE_HOLDER) {
            lnRoot.post({ lnRoot.addView(viewChild) })
            setViewData()
        }
    }

    fun setViewData() {
        tvName.text = userModel.name

        if (userModel.type == UserModel.TYPE_RELAYER) {
            tvUnit.text = "($)"
        } else {
            tvUnit.text = "(%)"
        }



        edVol.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (userModel.type == UserModel.TYPE_RELAYER) {
                    revenueMonthly = if (edVol.text.toString() == "") {
                        0.0
                    } else {
                        edVol.text.toString().toDouble()
                    }
                } else if (userModel.type == UserModel.TYPE_AFFILIATE) {
                    percentMonthly = if (edVol.text.toString() == "") {
                        0.0
                    } else {
                        edVol.text.toString().toDouble()
                    }
                }
            }
        })

        edDiscount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                discountValue = if (edDiscount.text.toString() == "") {
                    0.0
                } else {
                    edDiscount.text.toString().toDouble()
                }
//                if (userModel.type == UserModel.TYPE_RELAYER || userModel.type == UserModel.TYPE_AFFILIATE) {
//
//                }
            }
        })

        edVol.setText("")

        if (userModel.discountValue > 0) {
            edDiscount.setText(userModel.discountValue.toString())
        }
    }

    override fun onClick(v: View?) {

    }
}