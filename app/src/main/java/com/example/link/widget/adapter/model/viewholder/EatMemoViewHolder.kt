package com.example.link.widget.adapter.model.viewholder

import android.annotation.SuppressLint
import com.bumptech.glide.Glide
import com.example.link.R
import com.example.link.databinding.ViewholderEatMemoBinding
import com.example.link.model.EatMemoModel
import com.example.link.util.ext.toReadableDateString
import com.example.link.util.ext.toReadableTimeString
import com.example.link.util.resource.ResourceProvider
import com.example.link.widget.adapter.model.listener.EditModelAdapterListener
import com.example.link.widget.adapter.model.listener.ModelAdapterListener
import java.util.*

class EatMemoViewHolder(
    private val binding: ViewholderEatMemoBinding,
    private val resourceProvider: ResourceProvider
) : ModelViewHolder<EatMemoModel>(binding, resourceProvider) {

    override fun reset() = Unit


    @SuppressLint("SetTextI18n")
    override fun bindData(model: EatMemoModel) {
        super.bindData(model)

        binding.nameTextView.text = model.userName
        binding.memoTextView.text = model.memo

        binding.timeTextView.text = getTime(model.time)
        binding.eatAmountTextView.text = model.eatAmount
        binding.eatTypeTextView.text = model.eatType

        Glide.with(binding.profileImageView)
            .load(model.userProfileImage)
            .circleCrop()
            .into(binding.profileImageView)


    }


    override fun bindViews(model: EatMemoModel, adapterListener: ModelAdapterListener) {
        if (adapterListener is EditModelAdapterListener) {
            binding.editButton.setOnClickListener {
                adapterListener.clickEditButton(model)
            }
        }
    }


    private val currentDate = Date(System.currentTimeMillis())
    private val currentDateStr = currentDate.toReadableDateString()
    private val currentDateTimeStr = currentDate.toReadableTimeString()


    private fun getTime(time: Long): String {
        val modelDate = Date(time)
        val modelDateStr = modelDate.toReadableDateString()

        return try {
            if (currentDateStr == modelDateStr) {
                val current = currentDateTimeStr.split(':')
                val model = modelDate.toReadableTimeString().split(':')

                var hour = current[0].toInt() - model[0].toInt()
                var minutes = current[1].toInt() - model[1].toInt()

                if(minutes <0){
                    hour--
                    minutes += 60
                }

                when{
                    hour > 0 ->{
                        resourceProvider.getString(R.string.hour_before, hour)
                    }
                    else ->{
                        resourceProvider.getString(R.string.minutes_before, minutes)
                    }
                }
            } else {
                //다른 날자
                val current = currentDateStr.split('.')
                val model = modelDateStr.split('.')

                var year = current[0].toInt() - model[0].toInt()
                var month = current[1].toInt() - model[1].toInt()
                var day = current[2].toInt() - model[2].toInt()


                if (day < 0) {
                    month--
                }
                if (month < 0) {
                    year--
                    month += 12
                }

                when {
                    year > 0 -> {
                        resourceProvider.getString(R.string.year_before, year, month, day)
                    }
                    month > 0 -> {
                        resourceProvider.getString(R.string.month_before, month, day)
                    }

                    else -> {
                        resourceProvider.getString(R.string.day_before, day)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

}