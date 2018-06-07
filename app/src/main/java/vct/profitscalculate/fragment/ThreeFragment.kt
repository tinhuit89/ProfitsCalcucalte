package vct.profitscalculate.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import io.realm.kotlin.where
import vct.profitscalculate.AppController
import vct.profitscalculate.R
import vct.profitscalculate.activity.MainTabActivity
import vct.profitscalculate.adapter.MonthAdapter
import vct.profitscalculate.common.Constants
import vct.profitscalculate.interfaces.DataCallback
import vct.profitscalculate.models.ReportModel


class ThreeFragment : Fragment(), View.OnClickListener {


    private lateinit var activity: MainTabActivity

    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var lvItem: ListView

    private val realmInstance = AppController.realmInstance()

    private lateinit var monthAdapter: MonthAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.three_fragment_layout, container, false)
        activity = getActivity() as MainTabActivity
        initView(view)
        initData()
        return view
    }

    private fun initView(view: View) {
        swipeLayout = view.findViewById(R.id.swipeLayout)
        lvItem = view.findViewById(R.id.lvItem)
    }

    private fun initData() {

        swipeLayout.setOnRefreshListener {
            swipeLayout.isRefreshing = false
            resetData()
        }
    }

    private fun setAdapter() {
        monthAdapter = MonthAdapter(activity, dataCallback)
        lvItem.adapter = monthAdapter
    }

    private fun resetData() {
        setAdapter()
        val listItemDb = realmInstance.where<ReportModel>().findAll()
        monthAdapter.addListItems(listItemDb)
    }

    private val dataCallback: DataCallback = object : DataCallback {
        override fun callback(action: String, pos: Int, objects: Any) {
            if (action == Constants.VIEW_REPORT) {
//                var monthModel = (objects as ReportModel)


            }

        }
    }

    override fun onResume() {
        super.onResume()
        resetData()
    }

    override fun onClick(v: View?) {

    }
}