package net.cat_ears.kanmusuviewer.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.cat_ears.kanmusuviewer.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        if (savedInstanceState != null)
            return

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, ShipListFragment())
                .commit()
    }

    override fun onResume() {
        super.onResume()

        // 回転すると supportActionBar が消滅したので……
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}
