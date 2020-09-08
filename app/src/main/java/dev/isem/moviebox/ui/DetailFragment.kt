package com.backbase.assignment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.backbase.assignment.databinding.DetailFragmentBinding

class DetailFragment : Fragment() {

    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DetailFragmentBinding.inflate(inflater)

        val application = requireNotNull(activity).application
        val safeArgs: DetailFragmentArgs by navArgs()
        val factory = DetailViewModelFactory(safeArgs.selectedMovieProperty, application)

        detailViewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@DetailFragment
            viewModel = detailViewModel
        }

        return binding.root
    }

}
