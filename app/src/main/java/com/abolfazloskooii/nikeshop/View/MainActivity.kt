package com.abolfazloskooii.nikeshop.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.abolfazloskooii.nikeshop.Auth.LoginFragment
import com.abolfazloskooii.nikeshop.Base.Base
import com.abolfazloskooii.nikeshop.Base.convertDpToPixel
import com.abolfazloskooii.nikeshop.Model.CartItemCount
import com.abolfazloskooii.nikeshop.Model.TokenContainer
import com.abolfazloskooii.nikeshop.R
import com.abolfazloskooii.nikeshop.ViewModels.MainViewModel
import com.abolfazloskooii.nikeshop.databinding.ActivityMainBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * An activity that inflates a layout that has a [BottomNavigationView].
 */
class MainActivity : Base.NikeActivity() {

    val viewModel: MainViewModel by viewModel()

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    lateinit var bottom_nav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottom_nav = binding.bottomNav

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)


        // Setup the ActionBar with navController and 3 top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.navigation.home, R.navigation.cart, R.navigation.profile)
        )

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    @SuppressLint("ResourceType")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeCountItem(cartItemCount: CartItemCount) {
        val badge = bottom_nav.getOrCreateBadge(R.id.cart)
        badge.badgeGravity = BadgeDrawable.BOTTOM_START
        badge.backgroundColor = MaterialColors.getColor(bottom_nav, androidx.appcompat.R.attr.colorPrimary)
        badge.verticalOffset = convertDpToPixel(10f, this@MainActivity).toInt()
        badge.number = cartItemCount.count
        badge.isVisible = cartItemCount.count > 0

    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }
}