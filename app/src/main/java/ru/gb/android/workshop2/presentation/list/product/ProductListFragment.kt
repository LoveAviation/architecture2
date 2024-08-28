package ru.gb.android.workshop2.presentation.list.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.gb.android.workshop2.marketsample.R
import ru.gb.android.workshop2.marketsample.databinding.FragmentProductListBinding
import ru.gb.android.workshop2.presentation.list.product.adapter.ProductsAdapter

class ProductListFragment : Fragment(){

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val adapter = ProductsAdapter()

    private val factory: ProductVMFactory by lazy {
        FeatureServiceLocator.provideProductVMFactory()
    }

    private lateinit var viewModel : ProductVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        viewModel =  ViewModelProvider(this, factory)[ProductVM::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.loadProduct()

        viewModel.product.observe(viewLifecycleOwner){ result ->
            if(result == null){
                showProgress()
                hideProducts()
            }else{
                hideProgress()
                hidePullToRefresh()
                showProducts(result)
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect{ isError ->
                if(isError) showError()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.dispose()
        _binding = null
    }

    private fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progress.visibility = View.GONE
    }

    private fun hidePullToRefresh() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun showProducts(productList: List<ProductVO>) {
        binding.recyclerView.visibility = View.VISIBLE
        adapter.submitList(productList)
    }

    private fun hideProducts() {
        binding.recyclerView.visibility = View.GONE
    }

    private fun showError() {
        Toast.makeText(
            requireContext(),
            getString(R.string.error_wile_loading_data),
            Toast.LENGTH_SHORT
        ).show()
    }
}
