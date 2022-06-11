package com.example.link.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.link.R
import com.example.link.databinding.FragmentStartBinding
import com.example.link.ui.main.MainActivity
import com.example.link.util.lifecycle.SingleLiveEvent
import com.example.link.util.lifecycle.SystemUIType
import com.example.yourchoice.data.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : Fragment(){

    private var _binding  : FragmentStartBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var auth : FirebaseAuth


    @Inject
    lateinit var systemUIEvent: SingleLiveEvent<SystemUIType>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViews()
    }

    private fun initViews() {
        systemUIEvent.value = SystemUIType.FULLSCREEN

        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)

            startApp()
        }
    }

    private fun bindViews() {
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun startApp() {
        val id = preferenceManager.getUserId()

        if (id == null) {
            findNavController().navigate(R.id.action_startFragment_to_loginSelectFragment)
        } else {
            startActivity(MainActivity.newIntent(requireContext()))
            requireActivity().finish()
        }
    }
}