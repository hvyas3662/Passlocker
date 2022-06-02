package com.elevationsoft.passlocker.presentation.home_screen.passlist

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.LayoutPasslistItemBinding
import com.elevationsoft.passlocker.domain.models.Credential
import com.elevationsoft.passlocker.utils.ViewUtils.hide
import com.elevationsoft.passlocker.utils.ViewUtils.invisible
import com.elevationsoft.passlocker.utils.ViewUtils.show

class PassListAdapter(
    private var ctx: Context,
    private val callback: PassListItemCallBacks
) : PagingDataAdapter<Credential, PassListAdapter.ViewMaker>(Diff()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewMaker {
        val binding =
            LayoutPasslistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewMaker(binding)
    }

    override fun onBindViewHolder(holder: ViewMaker, position: Int) {
        val credential = getItem(position)
        credential?.let {
            holder.bind(it)
        }
    }

    inner class ViewMaker(private val binding: LayoutPasslistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.clMain.setOnClickListener { _ ->
                val credential = getItem(bindingAdapterPosition)
                credential?.let {
                    callback.onItemClicked(it)
                }
            }

            binding.ivDelete.setOnClickListener { _ ->
                val credential = getItem(bindingAdapterPosition)
                credential?.let {
                    callback.onDeleteClicked(it)
                }
            }

            binding.ivFav.setOnClickListener { _ ->
                val credential = getItem(bindingAdapterPosition)
                credential?.let {
                    callback.onFavouriteClicked(it)
                }
            }
        }

        fun bind(credential: Credential) {
            binding.tvTitle.text = credential.title
            binding.tvUsername.text = getLabelValueSpannableString("Username", credential.userName)
            if (credential.password.isNotEmpty()) {
                binding.tvPassword.text =
                    getLabelValueSpannableString("Password", credential.password)
                binding.tvPassword.show()
            } else {
                binding.tvPassword.invisible()
            }

            if (credential.remark.isNotEmpty()) {
                binding.tvRemark.text = credential.remark
                binding.tvRemark.show()
            } else {
                binding.tvRemark.hide()
            }

            if (credential.isFavourite) {
                binding.ivFav.setImageResource(R.drawable.ic_star_filed)
            } else {
                binding.ivFav.setImageResource(R.drawable.ic_star)
            }
        }
    }

    private fun getLabelValueSpannableString(label: String, value: String): SpannableString {
        val newLabel = "$label: "
        val spannable = SpannableString(newLabel + value)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.textSecondary)),
            0,
            label.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.textPrimary)),
            label.length,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannable
    }

    class Diff : DiffUtil.ItemCallback<Credential>() {
        override fun areItemsTheSame(oldItem: Credential, newItem: Credential): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Credential, newItem: Credential): Boolean {
            return oldItem == newItem
        }
    }

    interface PassListItemCallBacks {
        fun onItemClicked(credential: Credential)

        fun onDeleteClicked(credential: Credential)

        fun onFavouriteClicked(credential: Credential)
    }

}