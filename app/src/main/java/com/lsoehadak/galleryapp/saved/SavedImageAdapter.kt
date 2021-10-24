package com.lsoehadak.galleryapp.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lsoehadak.galleryapp.R
import com.lsoehadak.galleryapp.data.ImageEntity
import com.lsoehadak.galleryapp.databinding.ItemSavedImageBinding

class SavedImageAdapter(
    private var items: ArrayList<ImageEntity>,
    private val onItemSelectedListener: ((ImageEntity) -> Unit)? = null,
    private val onItemRemovedListener: ((ImageEntity) -> Unit)? = null
) : RecyclerView.Adapter<SavedImageAdapter.SavedImageViewHolder>() {

    inner class SavedImageViewHolder(private val binding: ItemSavedImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: ImageEntity) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(image.url)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    ).into(ivImage)
                tvTitle.text = image.title
                tvCreditLine.text = "Credit: ${image.creditLine}"

                btnDelete.setOnClickListener {
                    onItemRemovedListener?.invoke(image)
                    removeData(absoluteAdapterPosition)
                }
            }

            itemView.setOnClickListener {
                onItemSelectedListener?.invoke(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImageViewHolder {
        val binding =
            ItemSavedImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addNewData(items: List<ImageEntity>) {
        val size = this.items.size
        this.items.addAll(items)
        val newSize = this.items.size
        notifyItemRangeChanged(size, newSize)
    }

    fun removeData(pos: Int) {
        this.items.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun clearData() {
        val size = items.size
        this.items.clear()
        notifyItemRangeRemoved(0, size);
    }
}