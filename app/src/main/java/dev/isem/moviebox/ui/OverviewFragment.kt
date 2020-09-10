package dev.isem.moviebox.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import dev.isem.moviebox.R
import dev.isem.moviebox.databinding.OverviewFragmentBinding
import dev.isem.moviebox.ui.adapters.MoviesAdapter
import dev.isem.moviebox.ui.adapters.PostersGridAdapter

class OverviewFragment : Fragment() {

    private val overviewViewModel: OverviewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = OverviewFragmentBinding.inflate(inflater)

        binding.apply {
            lifecycleOwner = this@OverviewFragment
            viewModel = overviewViewModel
        }

        binding.nowPlayingGrid.adapter = PostersGridAdapter(PostersGridAdapter.OnClickListener {
            overviewViewModel.displayMovieDetails(it)
        })

        binding.popularList.adapter = MoviesAdapter(MoviesAdapter.OnClickListener {
            overviewViewModel.displayMovieDetails(it)
        })

        binding.popularList.apply {
            val dividerItemDecoration =
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            dividerItemDecoration.setDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.recyclerview_item_divider
                )!!
            )
            addItemDecoration(dividerItemDecoration)
        }

        overviewViewModel.navigateToSelectedMovie.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(it))
                overviewViewModel.displayMovieDetailsComplete()
            }
        })

        return binding.root
    }

}
