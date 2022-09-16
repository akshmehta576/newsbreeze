package com.practice.greedygame.presentation.fragment

import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.greedygame.R
import com.practice.greedygame.databinding.FragmentSavedNewsBinding
import com.practice.greedygame.presentation.adapter.SaveNewsAdapter
import com.practice.greedygame.presentation.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SavedNewsFragment : Fragment() {
    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var saveNewsAdapter: SaveNewsAdapter
    private lateinit var navController: NavController
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        binding.backbtnSve.setOnClickListener{

            val value = arguments?.getBoolean("internetcheck")
            if(value == false)
            {
                activity?.finish()
                activity?.finishAffinity()
            }
            else
            {
                navController.navigate(R.id.action_savedNewsFragment_to_newsFragment)
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setUpRecyclerView()

        //placeholder if no data
        newsViewModel.getSavedNews().observe(viewLifecycleOwner, Observer {articles ->
            Log.i("offlinedata", articles.toString())
            if(articles.size != 0)
            {

                binding.savenewslistRecyclerview.visibility = View.VISIBLE
                binding.placeholderText.visibility = View.GONE
                saveNewsAdapter.differ.submitList(articles)
            }
            else
            {

                binding.savenewslistRecyclerview.visibility = View.GONE
                binding.placeholderText.visibility = View.VISIBLE
            }
        })

        searchFilter()
    }

    private fun searchFilter() {
        binding.editextSearchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                newsViewModel.searchArticle(s.toString()).observe(viewLifecycleOwner, Observer {articles ->
                    Log.i("offlinedata", articles.toString())
                    saveNewsAdapter.differ.submitList(articles)
                })
                setUpRecyclerView()

            }

        })
    }

    private fun setUpRecyclerView() {
        saveNewsAdapter = SaveNewsAdapter(context!!, navController)
        binding.savenewslistRecyclerview.apply {
            adapter = saveNewsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }


}