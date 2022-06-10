package com.example.link.ui.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.link.R
import com.example.link.databinding.FragmentMapBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MapBottomSheetFragment() : BottomSheetDialogFragment() {

    private var _binding: FragmentMapBottomsheetBinding? = null
    val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.example_Lint_BottomSheet
        )

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBottomsheetBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initViews()
        bindView()
    }


    private fun initViews() {

    }


    private fun bindView() {

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        fun newInstance() = MapBottomSheetFragment()
        const val TAG ="MapBottomSheetFragment"
    }
}