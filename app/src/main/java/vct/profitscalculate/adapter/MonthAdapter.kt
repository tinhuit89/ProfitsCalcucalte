package vct.profitscalculate.adapter

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import vct.profitscalculate.AppController
import vct.profitscalculate.R
import vct.profitscalculate.common.Utilities
import vct.profitscalculate.interfaces.DataCallback
import vct.profitscalculate.interfaces.MonthProfitInterface
import vct.profitscalculate.models.MonthProfitModel
import java.util.*

class MonthAdapter(private val activity: Activity, private val callback: DataCallback) : ArrayAdapter<MonthProfitModel>(activity, R.layout.item_report_layout) {

    val listItems: ArrayList<MonthProfitModel>
        get() {
            val listItems = ArrayList<MonthProfitModel>()
            for (i in 0 until count) {
                listItems.add(getItem(i))
            }
            return listItems
        }

    internal inner class ViewHolder {

        lateinit var edName: EditText
        lateinit var edCapo: EditText
        lateinit var btnDel: Button
        lateinit var btnViewReport: Button
        lateinit var tvReport: TextView
        lateinit var rlItem: RelativeLayout


    }

    fun addListItems(popups: List<MonthProfitModel>) {
        for (obj in popups) {
            add(obj)
        }

        notifyDataSetChanged()
    }

    var positionReport = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView

        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            val inflater = LayoutInflater.from(activity)
            convertView = inflater.inflate(R.layout.item_report_layout, null)
            holder.edName = convertView.findViewById(R.id.edName)
            holder.edCapo = convertView.findViewById(R.id.edCapo)
            holder.rlItem = convertView.findViewById(R.id.rlItem)
            holder.btnDel = convertView.findViewById(R.id.btnDel)
            holder.btnViewReport = convertView.findViewById(R.id.btnViewReport)
            holder.tvReport = convertView.findViewById(R.id.tvReport)

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val item = getItem(position)

        holder.btnDel.visibility = View.VISIBLE
        holder.btnViewReport.visibility = View.VISIBLE


        if (position % 2 == 0) {
            holder.rlItem.setBackgroundColor(Color.parseColor("#f0fefc"))
        } else {
            holder.rlItem.setBackgroundColor(Color.parseColor("#ffffff"))
        }

        if (positionReport == position) {
            holder.tvReport.visibility = View.VISIBLE
            holder.tvReport.text = item.textReport
        } else {
            holder.tvReport.visibility = View.GONE
        }

        holder.btnDel.setOnClickListener {
            if (MonthProfitInterface.deleteMonth(AppController.realmInstance(), item.id)) {
                remove(item)
                notifyDataSetChanged()
            }
        }

        holder.btnViewReport.setOnClickListener {
            positionReport = if (positionReport == position) {
                -1
            } else {
                position
            }
            notifyDataSetChanged()
//                callback.callback(Constants.VIEW_REPORT, position, item)
        }

        holder.edName.setText(item.name)
        holder.edCapo.setText(Utilities.getDecimalCurrency(item.totalProfitOfMonth) + " ($)")
        return convertView
    }
}


