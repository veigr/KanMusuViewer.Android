package net.cat_ears.kanmusuviewer.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.dialog_downloading.view.*
import kotlinx.android.synthetic.main.fragment_shiplist.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.cat_ears.kanmusuviewer.R
import net.cat_ears.kanmusuviewer.databinding.DialogDownloadingBinding
import net.cat_ears.kanmusuviewer.databinding.FragmentShiplistBinding
import net.cat_ears.kanmusuviewer.model.Ship
import net.cat_ears.kanmusuviewer.model.isNullOrEmpty
import net.cat_ears.kanmusuviewer.showAlert
import net.cat_ears.kanmusuviewer.view.Controller.ShipEpoxyController
import net.cat_ears.kanmusuviewer.viewmodel.ShipListViewModel
import net.cat_ears.kanmusuviewer.viewmodel.ShipViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ShipListFragment : Fragment() {

    private val listVm: ShipListViewModel by sharedViewModel()
    private val shipVm: ShipViewModel by sharedViewModel()
    private val epoxyController: ShipEpoxyController = ShipEpoxyController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBind()

        if (listVm.ships.value.isNullOrEmpty())
            listVm.loadStart2FromLocal()
    }

    private fun dataBind() {
        // こういうのって全部 DataBinding でできればいいのに……
        listVm.ships.observe({ lifecycle }) { list ->
            setDataToEpoxyController(list)
        }
        listVm.filterText.observe({ lifecycle }) {
            setDataToEpoxyController(listVm.ships.value)
        }
        listVm.errorMessageId.observe({ lifecycle }) {
            if (it != null) {
                showAlert(context, it)
                listVm.errorMessageId.postValue(null)
            }
        }
    }

    private fun setDataToEpoxyController(list: List<Ship>?) {
        epoxyController.setData(list?.filter {
            // フィルタとか本来変更通知コレクションの役割なんだけども……
            val filterText = listVm.filterText.value ?: ""
            if (!filterText.isEmpty())
                it.name.toLowerCase().contains(filterText.toLowerCase())
                        || it.kana.toLowerCase().contains(filterText.toLowerCase())
            else
                true
        }) { id ->
            //isClosing = true
            shipVm.selectShip(id)
            requireFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, ShipImageFragment())
                    .commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_shiplist, container, false)
        val binding = FragmentShiplistBinding.bind(view)
        binding.vm = listVm
        binding.setLifecycleOwner(this)

        view.shipList.adapter = epoxyController.adapter

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main, menu)

        menu?.findItem(R.id.item_search)?.actionView?.let {
            it as SearchView
            it.setIconifiedByDefault(false)
            if (listVm.filterText.value?.isEmpty() == false) {
                it.setQuery(listVm.filterText.value, true)
            }
            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    listVm.filter(newText)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    listVm.filter(query)
                    return false
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_load_start2 -> {
                if (checkPermissionForReadExternalStorage())
                    openDocumentIntentForLoadStart2()
            }
            R.id.item_download_all_images -> {
                showDownloadingDialog()
                listVm.downloadAllImages()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPermissionForReadExternalStorage(): Boolean {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder(context)
                    .setMessage(R.string.require_permission_to_load_start2)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                RESULT_CODE_REQUEST_PERMISSION_FOR_READ_EXTERNAL_STORAGE)
                    }
                    .show()
            return false
        }
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                RESULT_CODE_REQUEST_PERMISSION_FOR_READ_EXTERNAL_STORAGE)
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RESULT_CODE_REQUEST_PERMISSION_FOR_READ_EXTERNAL_STORAGE -> {
                if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
                    return
                }
                openDocumentIntentForLoadStart2()
            }
        }
    }

    private fun openDocumentIntentForLoadStart2() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(intent, RESULT_CODE_LOAD_START_2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_CODE_LOAD_START_2 -> {
                if (resultCode != Activity.RESULT_OK) return
                if (data?.data == null) return
                if (context == null) return
                GlobalScope.launch {
                    val text = context!!.contentResolver.openInputStream(data.data!!)?.bufferedReader()?.readText()
                    if (text != null) listVm.loadAndSaveStart2(text)
                }
            }
        }
    }

    private fun showDownloadingDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_downloading, null)

        val bind = DialogDownloadingBinding.bind(view)
        bind.vm = listVm
        bind.setLifecycleOwner(this)

        val dialog = AlertDialog.Builder(context)
                .setTitle("Downloading")
                .setCancelable(false)
                .setView(view)
                .create()

        view.buttonCancelDownload.setOnClickListener {
            listVm.cancelDownload()
            dialog.dismiss()
        }

        val dismiss = Observer<Boolean> { if (it != true) dialog.dismiss() }
        listVm.isLoading.removeObserver(dismiss)
        listVm.isLoading.observe(this, dismiss)

        dialog.show()
    }

    companion object {
        private const val RESULT_CODE_LOAD_START_2 = 1
        private const val RESULT_CODE_REQUEST_PERMISSION_FOR_READ_EXTERNAL_STORAGE = 2
    }
}
