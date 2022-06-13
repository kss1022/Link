package com.example.link.ui.main.my

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.link.R
import com.example.link.databinding.FragmentMyBinding
import com.example.link.model.PetModel
import com.example.link.model.User
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.MainToolbarViewModel
import com.example.link.util.ext.toReadableDateString
import com.example.link.util.lifecycle.SingleLiveEvent
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MyFragment : BaseFragment<FragmentMyBinding, MyViewModel>() {

    @Inject
    lateinit var sharedViewModel : MainSharedViewModel

    @Inject
    lateinit var toolbarViewModel: MainToolbarViewModel

    override val viewModel: MyViewModel by viewModels()

    override fun getViewBinding(): FragmentMyBinding =
        FragmentMyBinding.inflate(layoutInflater)

    private val editButtonEvent = SingleLiveEvent<Unit>(300)

    override fun initViews() {
        initActionBar()
    }

    override fun bindViews() {
        binding.editButton.setOnClickListener { editButtonEvent.call() }
    }



    override fun observeData() {
        viewModel.profileImage.observe(viewLifecycleOwner){
            Glide.with(binding.profileImageView)
                .load(it)
                .circleCrop()
                .into(binding.profileImageView)
        }

        sharedViewModel.userModel.observe(viewLifecycleOwner){
            setUserData(it)
        }

        sharedViewModel.petModel.observe(viewLifecycleOwner) {
            setPetData(it)
        }

        editButtonEvent.observe(viewLifecycleOwner){
            showEditAlertDialog()
        }
    }


    fun initActionBar() {
        toolbarViewModel.setTitle(getString(R.string.app_name))
            .setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setTitleColor(ContextCompat.getColor(requireContext(), R.color.green))
            .setNavIconRes(null)
            .onChange()

        toolbarViewModel.onChangeBottomNavigation(true)
    }


    /**
     * Set Data
     */

    private fun setUserData(it: User) {
        binding.userNameTextView.text = it.name
        binding.userEmailTextView.text = it.email
    }


    @SuppressLint("SetTextI18n")
    private fun setPetData(pet: PetModel) = with(binding) {
        petNameTextView.text = pet.name
        petYearTextView.text = getYear(pet.birthDay)
        petTypeTextView.text = pet.type
        petMailTextView.text =
            if (pet.isMail) getString(R.string.character_mail) else getString(R.string.character_femail)
        petWeightTextView.text = "${pet.weight}kg"
    }


    private fun getYear(date: List<Int>): String {
        val today = Date(System.currentTimeMillis()).toReadableDateString().split(".")

        var year = today[0].toInt() - date[0]
        var month = today[1].toInt() - date[1]
        val day = today[2].toInt() - date[2]


        if (day < 0) {
            month--
        }
        if (month < 0) {
            year--
            month += 12
        }

        return getString(R.string.pet_age, year, month)
    }


    private fun showEditAlertDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val getUserNameLayout = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialog_get_user_name, null)

        val editText = getUserNameLayout.findViewById<TextInputEditText>(R.id.userNameEditText)

        editText.setText(binding.userNameTextView.text.toString())


        getUserNameLayout.findViewById<TextView>(R.id.negativeButton).setOnClickListener {
            dialog.dismiss()
        }

        getUserNameLayout.findViewById<TextView>(R.id.positiveButton).setOnClickListener {
            sharedViewModel.updateUserName(editText.text.toString())
            dialog.dismiss()
        }

        dialog.apply {
            setView(getUserNameLayout)
        }.show()
    }


    companion object{
        fun newInstance()  = MyFragment()
        const val TAG = "MyFragment"
    }
}