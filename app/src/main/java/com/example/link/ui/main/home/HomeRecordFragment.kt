package com.example.link.ui.main.home

import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.link.R
import com.example.link.databinding.FragmentHomeRecordBinding
import com.example.link.model.PetModel
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.MainToolbarViewModel
import com.example.link.util.lifecycle.SingleLiveEvent
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeRecordFragment : BaseFragment<FragmentHomeRecordBinding, HomeRecordViewModel>() {

    @Inject
    lateinit var sharedViewModel: MainSharedViewModel

    @Inject
    lateinit var toolbarViewModel: MainToolbarViewModel


    override val viewModel: HomeRecordViewModel by viewModels()

    override fun getViewBinding(): FragmentHomeRecordBinding =
        FragmentHomeRecordBinding.inflate(layoutInflater)

    private val buttonEvent = SingleLiveEvent<String>(300)


    override fun initViews() {
        initActionBar()
    }


    override fun bindViews() {
        bindButton()
    }


    override fun observeData() {
        sharedViewModel.petModel.observe(viewLifecycleOwner) {
            setData(it)
        }

        buttonEvent.observe(viewLifecycleOwner) { type ->
            type?.let { recordData(it) }
        }
    }


    fun initActionBar() {
        toolbarViewModel.setTitle(getString(R.string.app_name))
            .setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            .setTitleColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setNavIconRes(
                R.drawable.ic_baseline_keyboard_arrow_left_24,
                ContextCompat.getColor(requireContext(), R.color.white)
            )
            .onChange()

        toolbarViewModel.onChangeBottomNavigation(true)
    }


    private fun bindButton() {
        binding.eatingContainer.setOnClickListener { buttonEvent.value = EAT }
        binding.walkContainer.setOnClickListener { buttonEvent.value = WALK }
        binding.showerContainer.setOnClickListener { buttonEvent.value = SHOWER }
    }

    private fun setData(it: PetModel) {
        binding.petNameTextView.text = it.name
    }

    private fun recordData(it: String) {
        when (it) {
            EAT -> {
                showRecordEatAlertDialog()
            }
            WALK -> {
                showRecordWalkAlertDialog()
            }
            SHOWER -> {
                showRecordShowerAlertDialog()
            }
        }
    }



    private fun showRecordEatAlertDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()

        val getEatingAlertDialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialog_get_eating, null)

        var isEating = true
        getEatingAlertDialog.findViewById<RadioGroup>(R.id.radioGroup)
            .setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.eatingRadioButton -> { isEating = true }
                    R.id.snackRadioButton -> { isEating = false }
                }
            }

        val eatingEditText =    getEatingAlertDialog.findViewById<TextInputEditText>(R.id.eatingEditText)

        getEatingAlertDialog.findViewById<TextView>(R.id.positiveButton).setOnClickListener {
            val amount = eatingEditText.text.toString().toInt()
            sharedViewModel.saveEating( isEating,  amount)
            dialog.dismiss()
        }

        getEatingAlertDialog.findViewById<TextView>(R.id.negativeButton).setOnClickListener {
            dialog.dismiss()
        }

        dialog.apply {
            setView(getEatingAlertDialog)
        }.show()
    }

    private fun showRecordWalkAlertDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()

        val getWalkAlertDialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialog_get_walk, null)


        val timeEditText =  getWalkAlertDialog.findViewById<TextInputEditText>(R.id.timeEditText)
        val lengthEditText =  getWalkAlertDialog.findViewById<TextInputEditText>(R.id.lengthEditText)

        getWalkAlertDialog.findViewById<TextView>(R.id.positiveButton).setOnClickListener {
            val length = lengthEditText.text.toString().toDouble()
            val time = timeEditText.text.toString().toInt()
            sharedViewModel.saveWalk( 0, length,  time)
            dialog.dismiss()
        }

        getWalkAlertDialog.findViewById<TextView>(R.id.negativeButton).setOnClickListener {
            dialog.dismiss()
        }

        dialog.apply {
            setView(getWalkAlertDialog)
        }.show()
    }



    private fun showRecordShowerAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("목욕을 기록 하시겠어요?")
            .setPositiveButton("확인") { dialog, _ ->
                sharedViewModel.updateShower()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }




    companion object {
        fun newInstance() = HomeRecordFragment()
        const val TAG = "HomeRecordFragment"

        const val EAT = "eat"
        const val WALK = "walk"
        const val SHOWER = "shower"
    }
}