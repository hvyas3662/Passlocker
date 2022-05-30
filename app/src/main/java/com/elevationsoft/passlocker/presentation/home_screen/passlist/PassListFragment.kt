package com.elevationsoft.passlocker.presentation.home_screen.passlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elevationsoft.passlocker.databinding.FragmentPassListBinding

class PassListFragment : Fragment() {
    private lateinit var binding: FragmentPassListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPassListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = PassListFragment()
    }
}