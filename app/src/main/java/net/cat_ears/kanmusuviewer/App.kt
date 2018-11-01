package net.cat_ears.kanmusuviewer

import android.app.AlertDialog
import android.content.Context
import net.cat_ears.kanmusuviewer.model.Model
import net.cat_ears.kanmusuviewer.viewmodel.ShipListViewModel
import net.cat_ears.kanmusuviewer.viewmodel.ShipViewModel
import org.koin.android.ext.android.startKoin
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

class App : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(
                this.appModule
        ))
    }

    private val appModule = module {
        single { Model(getExternalFilesDir(null), externalCacheDir) }
        viewModel { ShipListViewModel(get()) }
        viewModel { ShipViewModel(get()) }
    }
}

fun showAlert(context: Context?, id: Int?) {
    if (id != null) {
        AlertDialog.Builder(context)
                .setMessage(id)
                .show()
    }
}