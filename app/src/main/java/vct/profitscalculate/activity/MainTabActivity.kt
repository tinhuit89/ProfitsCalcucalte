package vct.profitscalculate.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.checkSelfPermission
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import io.realm.Realm
import io.realm.kotlin.where
import vct.profitscalculate.AppController
import vct.profitscalculate.R
import vct.profitscalculate.common.Constants
import vct.profitscalculate.common.Utilities
import vct.profitscalculate.controller.ItemAddUserController
import vct.profitscalculate.fragment.OneFragment
import vct.profitscalculate.fragment.ThreeFragment
import vct.profitscalculate.fragment.TwoFragment
import vct.profitscalculate.models.UserModel
import java.util.*


@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
class MainTabActivity internal constructor() : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    var listItemUserController: ArrayList<ItemAddUserController> = ArrayList()
    var listUsers: ArrayList<UserModel> = ArrayList()
//
//    fun getTotalCapoHold(): Double {
//        var sum = AppController.realmInstance().where<UserModel>().equalTo("isReport", false).findAll().sum("capoVolume")
//        Log.d(Constants.TAG, "Sum: $sum")
//        return sum.toDouble()
//    }

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

        checkStoragePermissions(this)
    }

    // Storage Permissions
    private val requestExternalStorage = 1
    private val permissionStorage = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun checkStoragePermissions(activity: Activity) {
        // Check if we have write permission
        val permission = checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    permissionStorage,
                    requestExternalStorage
            )
        } else {
            Utilities.backupDatabase(AppController.realmInstance())
        }
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


        val tabThree = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null) as TextView
        tabThree.text = "Báo cáo"
        tabLayout!!.getTabAt(2)!!.customView = tabThree
    }

    fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(OneFragment(), "Vốn")
        adapter.addFragment(TwoFragment(), "Doanh Thu")
        adapter.addFragment(ThreeFragment(), "Báo cáo")
        viewPager.offscreenPageLimit = 3
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
