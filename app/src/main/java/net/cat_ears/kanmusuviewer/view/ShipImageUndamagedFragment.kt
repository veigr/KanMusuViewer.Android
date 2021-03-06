package net.cat_ears.kanmusuviewer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.cat_ears.kanmusuviewer.databinding.FragmentShipImageUndamagedBinding
import net.cat_ears.kanmusuviewer.viewmodel.ShipViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ShipImageUndamagedFragment : Fragment() {

    private val vm: ShipViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentShipImageUndamagedBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this
        return binding.root
    }
}
