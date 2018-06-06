package vct.profitscalculate.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import vct.profitscalculate.R
import vct.profitscalculate.controller.ItemAddUserController
import vct.profitscalculate.fragment.OneFragment
import vct.profitscalculate.fragment.TwoFragment
import vct.profitscalculate.models.UserModel
import java.util.ArrayList


class MainTabActivty internal constructor() : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    public var listItemUserController: ArrayList<ItemAddUserController> = ArrayList()
    public var listUsers: ArrayList<UserModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_tabs)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewPager = findViewById(R.id.viewpager)
        setupViewPager(viewPager!!)

        tabLayout = findViewById(R.id.tabs)
        tabLayout!!.setupWithViewPager(viewPager)

        tabLayout!!.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
        setupTabIcons()
    }

    /**
     * Adding custom view to tab
     */
    private fun setupTabIcons() {

        val tabOne = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null) as TextView
        tabOne.text = "Vốn"
        tabLayout!!.getTabAt(0)!!.customView = tabOne

        val tabTwo = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null) as TextView
        tabTwo.text = "Doanh Thu"
        tabLayout!!.getTabAt(1)!!.customView = tabTwo
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(OneFragment(), "Vốn")
        adapter.addFragment(TwoFragment(), "Doanh Thu")
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                adapter.getItem(position).onResume()
            }

        })
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }


}
