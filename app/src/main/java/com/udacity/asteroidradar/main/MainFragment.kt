package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this,
            Factory(requireActivity().application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.pictureOfDay = viewModel.pictureOfDay.value

        val adapter = MainAdapter(OnClickListener {
            viewModel.displayAsteroidDetail(it)
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
            displayImage(it, binding.activityMainImageOfTheDay)
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.doneDisplayingAsteroidDetail()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun displayImage(img: PictureOfDay, imgView: ImageView) {
        if (img.mediaType == "image") {
            Picasso.get()
                .load(img.url)
                .into(imgView)

            // update content description
            imgView.contentDescription = img.title
        }
    }
}
