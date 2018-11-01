package net.cat_ears.kanmusuviewer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_ship_image.view.*
import net.cat_ears.kanmusuviewer.R
import net.cat_ears.kanmusuviewer.showAlert
import net.cat_ears.kanmusuviewer.view.Adapter.ShipImagePagerAdapter
import net.cat_ears.kanmusuviewer.viewmodel.ShipViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ShipImageFragment : Fragment() {

    private val vm: ShipViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.errorMessageId.observe({ lifecycle }) {
            if (it != null) {
                showAlert(context, it)
                vm.errorMessageId.postValue(null)
                requireFragmentManager().popBackStack()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_ship_image, container, false)
        if (view.shipImagePager != null) {
            // この程度 Fragment/Adapter を使わず XML と DataBinding だけで表現できるようになってほしい……
            view.shipImagePager.adapter = ShipImagePagerAdapter(childFragmentManager, vm.id.value?:0)
        }

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
        setHasOptionsMenu(true)

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                fragmentManager?.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setHomeButtonEnabled(false)
    }
}
