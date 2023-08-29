package ru.gb.android.workshop2.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.gb.android.workshop2.marketsample.databinding.ActivityProductListBinding

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}