package com.practice.greedygame.presentation.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.greedygame.R
import com.practice.greedygame.databinding.FragmentNewsBinding
import com.practice.greedygame.domain.model.NewsResponse
import com.practice.greedygame.presentation.adapter.NewsAdapter
import com.practice.greedygame.presentation.viewmodel.NewsViewModel
import com.practice.greedygame.util.Constants.API_KEY
import com.practice.greedygame.util.NetworkListner
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class NewsFragment : Fragment() {
    lateinit var binding: FragmentNewsBinding
    lateinit var newsAdapter: NewsAdapter
    lateinit var navController: NavController
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkInternet()
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        newsViewModel.getSavedNews()

        //search box data
        binding.editextSearchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {

                editable.let {
                    if (it.toString().isNotEmpty()) {
                        newsViewModel.searchlistNews(editable.toString(), 1, API_KEY)
                        listSearchNews()
//                        Log.i("efitable", it.toString())
                    } else {
                        newsViewModel.listofSearchNews("us", 1, API_KEY)
                        listNews()
                    }
                }
            }

        })


        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                activity?.finish()
                activity?.finishAffinity()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        newsViewModel.listofSearchNews("us", 1, API_KEY)
        listNews()


        binding.saveFrag.setOnClickListener {
            navController.navigate(R.id.action_newsFragment_to_savedNewsFragment)
        }

        //popup menu on sorting things
        binding.sortNews.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sortbynews -> {
                        newsViewModel.listofSearchNews("us", 1, API_KEY)
                        listNews()
                        Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.sortbydate -> {
                        DatePickerDialog(
                            context!!, datePicker,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                        Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.sortbypub -> {
                        if (binding.editextSearchBox.text.isNotEmpty()) {
                            newsViewModel.sortPublishlistNews(
                                binding.editextSearchBox.text.toString(),
                                "publishedAt",
                                API_KEY
                            )
                            listPublishSearchNews()
                        } else {
                            newsViewModel.listofSearchNews("us", 1, API_KEY)
                            listNews()
                        }
                        Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> {
                        Toast.makeText(context, "else", Toast.LENGTH_SHORT).show()
                        true
                    }
                }

            }
            popupMenu.inflate(R.menu.sort_menu)
            popupMenu.show()

        }
    }

    val myCalendar = Calendar.getInstance()
    val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, day ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, month)
        myCalendar.set(Calendar.DAY_OF_MONTH, day)
        val date: String = myCalendar.get(Calendar.YEAR).toString() + "-" +
                getMonthFormat(myCalendar.get(Calendar.MONTH)) + "-" +
                myCalendar.get(Calendar.DAY_OF_MONTH).toString()

        Log.i("dateoffrom", date)
        if (binding.editextSearchBox.text.isNotEmpty()) {
            newsViewModel.sortDatelistNews(
                binding.editextSearchBox.text.toString(),
                date,
                "2022-09-16"
            )
            listDateSearchNews()
        } else {
            newsViewModel.listofSearchNews("us", 1, API_KEY)
            listNews()
        }
    }


    private fun getMonthFormat(month: Int): String {
        val ans: String =
            when (month + 1) {
                1 -> "01"
                2 -> "02"
                3 -> "03"
                4 -> "04"
                5 -> "05"
                6 -> "06"
                7 -> "07"
                8 -> "08"
                9 -> "09"
                10 -> "10"
                11 -> "11"
                12 -> "12"
                else -> "01"
            }
        return ans
    }

    //beaking top headlines
    private fun listNews() {

        lifecycleScope.launchWhenStarted {

            newsViewModel.listofNewsState.collect { response ->
                when (response) {
                    is NewsViewModel.NewsListState.Success -> {
                        hideProgressBar()
                        setUpRecyclerView(response.data)
                        Log.i("responseList", response.data.toString())
                    }
                    is NewsViewModel.NewsListState.Loading -> {
                        showProgressBar()
                    }
                    is NewsViewModel.NewsListState.Error -> {
                        Toast.makeText(requireContext(), "Error Occured.", Toast.LENGTH_SHORT)
                        Log.i("responseList", response.message)
                    }
                    else -> {
                        showProgressBar()
                        Toast.makeText(requireContext(), "Sorry for delay.", Toast.LENGTH_SHORT)
                    }
                }
            }
        }
    }

    //news by search
    private fun listSearchNews() {

        Log.i("responseList", "response.listTradesResponse.toString()")
        lifecycleScope.launchWhenStarted {

            newsViewModel.listofSearchState.collect { response ->
                when (response) {
                    is NewsViewModel.SearchNewsListState.Success -> {
                        hideProgressBar()
                        setUpRecyclerView(response.data)
                        Log.i("responseSeachList", response.data.toString())
                    }
                    is NewsViewModel.SearchNewsListState.Loading -> {
                        showProgressBar()
                    }
                    is NewsViewModel.SearchNewsListState.Error -> {
                        Toast.makeText(requireContext(), "Error Occured.", Toast.LENGTH_SHORT)
                        Log.i("responseList", response.message)
                    }
                    else -> {
                        showProgressBar()
                        Toast.makeText(requireContext(), "Sorry for delay.", Toast.LENGTH_SHORT)
                    }
                }
            }
        }


    }

    //news sorted by date
    private fun listDateSearchNews() {

        Log.i("responseList", "response.listTradesResponse.toString()")
        lifecycleScope.launchWhenStarted {

            newsViewModel.listofDateNewsState.collect { response ->
                when (response) {
                    is NewsViewModel.SearchDateNewsListState.Success -> {
                        hideProgressBar()
                        setUpRecyclerView(response.data)
                        Log.i("responseDateSeachList", response.data.toString())
                    }
                    is NewsViewModel.SearchDateNewsListState.Loading -> {
                        showProgressBar()
                    }
                    is NewsViewModel.SearchDateNewsListState.Error -> {
                        Toast.makeText(requireContext(), "Error Occured.", Toast.LENGTH_SHORT)
                        Log.i("responseList", response.message)
                    }
                    else -> {
                        showProgressBar()
                        Toast.makeText(requireContext(), "Sorry for delay.", Toast.LENGTH_SHORT)
                    }
                }
            }
        }


    }

    //news sorted by publish
    private fun listPublishSearchNews() {

        Log.i("responseList", "response.listTradesResponse.toString()")
        lifecycleScope.launchWhenStarted {

            newsViewModel.listofpublishNewsState.collect { response ->
                when (response) {
                    is NewsViewModel.SearchPublishNewsListState.Success -> {
                        hideProgressBar()
                        setUpRecyclerView(response.data)
                        Log.i("responseDateSeachList", response.data.toString())
                    }
                    is NewsViewModel.SearchPublishNewsListState.Loading -> {
                        showProgressBar()
                    }
                    is NewsViewModel.SearchPublishNewsListState.Error -> {
                        Toast.makeText(requireContext(), "Error Occured.", Toast.LENGTH_SHORT)
                        Log.i("responseList", response.message)
                    }
                    else -> {
                        showProgressBar()
                        Toast.makeText(requireContext(), "Sorry for delay.", Toast.LENGTH_SHORT)
                    }
                }
            }
        }


    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.home.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.home.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView(data: NewsResponse) {
        newsAdapter = NewsAdapter(context!!, data, navController, newsViewModel)
        binding.newslistRecyclerview.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    //internet checker
    private fun checkInternet() {
        val networkConnection = NetworkListner(requireContext())
        networkConnection.observe(viewLifecycleOwner) {
            if (!it)
            {
                val bundle  = bundleOf("internetcheck" to it)
                navController.navigate(R.id.action_newsFragment_to_savedNewsFragment, bundle)
            }
        }
    }

}



