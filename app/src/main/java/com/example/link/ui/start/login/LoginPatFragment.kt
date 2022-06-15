package com.example.link.ui.start.login

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.link.R
import com.example.link.databinding.FragmentLoginPatBinding
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.start.StartSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginPatFragment : BaseFragment<FragmentLoginPatBinding, LoginPatViewModel>() {

    @Inject
    lateinit var sharedViewModel: StartSharedViewModel

    override val viewModel: LoginPatViewModel by viewModels()

    override fun getViewBinding(): FragmentLoginPatBinding =
        FragmentLoginPatBinding.inflate(layoutInflater)


    override fun initViews() {
        setData()
    }


    override fun bindViews() {
        binding.nextButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.clickNext()
        }

        binding.mailButton.setOnClickListener {
            viewModel.setMail(true)
        }

        binding.femailButton.setOnClickListener {
            viewModel.setMail(false)
        }

        binding.patNameEditText.addTextChangedListener {
            viewModel.setName(binding.patNameEditText.text.toString())
        }

        binding.typeEditText.addTextChangedListener {
            viewModel.setType(binding.typeEditText.text.toString())
        }

        binding.birthdayEditText.addTextChangedListener {
            viewModel.setBirthday(binding.birthdayEditText.text.toString())
        }

        //Set Text YYYY-MM-DD
        DateInputMask(binding.birthdayEditText).listen()

        binding.weightEditText.addTextChangedListener {
            //todo 뒤에 kg 을 붙이기
            viewModel.setWeight(binding.weightEditText.text.toString())
        }
    }


    override fun observeData() {
        viewModel.nextClickEvent.observe(viewLifecycleOwner){ goNext->
            goNext?.let {
                if(it) {
                    findNavController().navigate(
                        LoginPatFragmentDirections.actionLoginPatFragmentToLoginProfileFragment(
                            viewModel.name.value.toString()
                        )
                    )
                }else{
                    binding.progressBar.isGone = true
                    Toast.makeText(requireContext(), getString(R.string.check_input_please), Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.isMail.observe(viewLifecycleOwner) { isMail ->
            isMail?.let {
                if (it) {
                    setMail()
                } else {
                    setFemail()
                }
            }
        }

        viewModel.patAge.observe(viewLifecycleOwner) { patAge ->
            patAge?.let {
                if(it.first < 0) {
                    //error
                    binding.patAgeTextView.text = getString(R.string.day_is_error)
                }else{
                    //todo 년이 0인경우 개월만 보여주기
                    binding.patAgeTextView.text = getString(R.string.pet_age, it.first, it.second)
                }
            } ?: kotlin.run {
                    binding.patAgeTextView.text = ""
            }
        }
    }


    /**
     * Set Mail Button color
     */

    private fun setMail() = with(binding) {
        mailButton.apply {
            setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
            setBackgroundResource(R.drawable.bg_corner_green_18)
        }


        femailButton.apply {
            setColorFilter(ContextCompat.getColor(context, R.color.gray_e0), android.graphics.PorterDuff.Mode.MULTIPLY);
            setBackgroundResource(R.drawable.bg_corner_white_18)
        }
    }


    private fun setFemail() = with(binding) {
        mailButton.apply {
            setColorFilter(ContextCompat.getColor(context, R.color.gray_e0), android.graphics.PorterDuff.Mode.MULTIPLY);
            setBackgroundResource(R.drawable.bg_corner_white_18)
        }


        femailButton.apply {
            setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
            setBackgroundResource(R.drawable.bg_corner_green_18)
        }
    }

    private fun setData() {
        viewModel.isMail.value?.let { if (it) setMail() else setFemail() }
        viewModel.name.value?.let { binding.patNameEditText.setText(it) }
        viewModel.type.value?.let { binding.typeEditText.setText(it) }
        viewModel.birthday.value?.let { binding.birthdayEditText.setText(it) }
        viewModel.weight.value?.let { if(it == 0f) binding.weightEditText.setText("") else binding.weightEditText.setText(it.toString()) }
    }


    class DateInputMask(val input : EditText) {
        fun listen() {
            input.addTextChangedListener(mDateEntryWatcher)
        }

        private val mDateEntryWatcher = object : TextWatcher {

            var edited = false
            val dividerCharacter = "-"

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (edited) {
                    edited = false
                    return
                }

                var working = getEditText()
                working = manageDateDivider(working, 4, start, before)
                working = manageDateDivider(working, 7, start, before)

                edited = true
                input.setText(working)
                input.setSelection(input.text.length)
            }

            private fun manageDateDivider(working: String, position : Int, start: Int, before: Int) : String{
                if (working.length == position) {
                    return if (before <= position && start < position)
                        working + dividerCharacter
                    else
                        working.dropLast(1)
                }
                return working
            }

            private fun getEditText() : String {
                return if (input.text.length >= 10)
                    input.text.toString().substring(0,10)
                else
                    input.text.toString()
            }

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        }
    }
}