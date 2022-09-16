package com.practice.greedygame.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresPermission
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.practice.greedygame.R
import com.practice.greedygame.databinding.FragmentReadNewsBinding
import com.practice.greedygame.presentation.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadNewsFragment : Fragment() {
    lateinit var binding: FragmentReadNewsBinding
    lateinit var newsViewModel: NewsViewModel
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReadNewsBinding.inflate(inflater, container, false)
        setUpView();

        binding.backbtn.setOnClickListener {
            navController.popBackStack()
        }

        binding.saveBtn.setOnClickListener {
            Toast.makeText(context, "Saved for read later!", Toast.LENGTH_SHORT).show()
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController.navigate(R.id.action_readNewsFragment_to_newsFragment)
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }


    private fun setUpView() {
        val img = arguments?.getString("newsImg")
        val title = arguments?.getString("newsTitle")
        val date = arguments?.getString("newsDate")
        val content = arguments?.getString("newsContent")
        val sourceName = arguments?.getString("sourceName")
        val name = arguments?.getString("authorName")
        val isSave = arguments?.getBoolean("saveornot")

        if(isSave == true)
        {
            binding.saveBtn.text = "Unsave"
        }
        else
        {
            binding.saveBtn.text = "Save"
        }


        //here if author name, press name will be null than default names are putted
        binding.nameauthor.text = (name ?: "Samantha Subin").toString()
        binding.contentNews.text = content
        binding.dateofnews.text =
            date?.subSequence(8, 10).toString() + "-" + date?.subSequence(5, 7)
                .toString() + "-" + date?.subSequence(2, 4).toString()
        binding.titleofnews.text = title
        binding.pressnameauthor.text = (sourceName ?: "The Washington Post").toString()


        context?.let {
            Glide.with(it)
                .load(img)
                .into(binding.mainScreen)
        }

    }


}