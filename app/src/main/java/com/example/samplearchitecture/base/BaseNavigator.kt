package com.example.samplearchitecture.base

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.samplearchitecture.R
import com.example.samplearchitecture.utils.animateExit
import com.example.samplearchitecture.utils.animateShared
import java.lang.ref.WeakReference

abstract class BaseNavigator {

    abstract var fragmentManager: WeakReference<FragmentManager?>

    fun attachFragmentManager(fragmentManager: FragmentManager?) {
        this.fragmentManager = WeakReference(fragmentManager)
    }

    fun release() {
        fragmentManager = WeakReference(null)
    }

    fun FragmentManager.goWithAnimation(targetFragment: Fragment, vararg sharedViews: View) {

        val previousFragment = findFragmentById(R.id.container) ?: return
        previousFragment.animateExit()

        targetFragment.animateShared(previousFragment.requireContext())
        //targetFragment.animateEnter()

        beginTransaction()
            .replace(R.id.container, targetFragment)
            .apply { sharedViews.forEach { addSharedElement(it, it.transitionName) } }
            .commit()

    }

    fun FragmentManager.goWithAnimationAndBack(targetFragment: Fragment, vararg sharedViews: View) {

        val previousFragment = findFragmentById(R.id.container) ?: return
        previousFragment.animateExit()

        targetFragment.animateShared(previousFragment.requireContext())
        //targetFragment.animateEnter()

        beginTransaction()
            .replace(R.id.container, targetFragment)
            .apply { sharedViews.forEach { addSharedElement(it, it.transitionName) } }
            .addToBackStack(targetFragment::class.java.name)
            .commit()

    }

}