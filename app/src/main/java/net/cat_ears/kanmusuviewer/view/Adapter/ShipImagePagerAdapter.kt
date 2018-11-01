package net.cat_ears.kanmusuviewer.view.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import net.cat_ears.kanmusuviewer.view.ShipImageDamagedFragment
import net.cat_ears.kanmusuviewer.view.ShipImageUndamagedFragment

class ShipImagePagerAdapter(fm: FragmentManager, private val id: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        if(1500 < id)
            return ShipImageUndamagedFragment()

        return when (position) {
            0 -> ShipImageUndamagedFragment()
            1 -> ShipImageDamagedFragment()
            else -> ShipImageUndamagedFragment()
        }
    }

    override fun getCount(): Int = if(1500 < id) 1 else 2
}