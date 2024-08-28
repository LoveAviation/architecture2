package ru.gb.android.workshop2.presentation.list.promo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.gb.android.workshop2.marketsample.R
import ru.gb.android.workshop2.marketsample.databinding.FragmentPromoListBinding
import ru.gb.android.workshop2.presentation.list.promo.adapter.PromoAdapter

class PromoListFragment : Fragment(){

    private var _binding: FragmentPromoListBinding? = null
    private val binding get() = _binding!!

    private val adapter = PromoAdapter()

    private lateinit var viewModel : PromoVM

    private val factory: PromoVMFactory by lazy {
        FeatureServiceLocator.providePromoVMFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPromoListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, factory)[PromoVM::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.loadPromos()
        viewModel.promo.observe(viewLifecycleOwner){ result ->
            if(result == null){
                showProgress()
                hidePromos()
            }else{
                hideProgress()
                showPromos(result)
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

    private fun showPromos(promoList: List<PromoVO>) {
        binding.recyclerView.visibility = View.VISIBLE
        adapter.submitList(promoList)
    }

    private fun hidePromos() {
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
