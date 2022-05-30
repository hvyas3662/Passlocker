package com.elevationsoft.passlocker.presentation.home_screen.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elevationsoft.passlocker.databinding.FragmentCategoryBinding

class CategoryListFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance() = CategoryListFragment()
    }
}