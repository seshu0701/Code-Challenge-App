package com.example.codechallengeex.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.codechallengeex.GlideApp
import com.example.codechallengeex.R
import com.example.codechallengeex.data.interfaces.CallBackListener
import com.example.codechallengeex.databinding.ImagesListByDateItemsBinding
import com.example.codechallengeex.model.ContentModel
import com.example.codechallengeex.model.ResponseData
import com.example.codechallengeex.utils.Constants

/**
 * Created by Satya Seshu on 10/04/22.
 */
class ImagesListByDateAdapter(
    private val context: Context,
    private var listData: MutableList<ResponseData>,
    private val callBackListener: CallBackListener
) :
    RecyclerView.Adapter<ImagesListByDateAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)
    private val youtubeBaseUrl = Constants.KEY_YOU_TUBE_BASE_URL

    fun updateItems(list: MutableList<ResponseData>) {
        listData = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ImagesListByDateItemsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataObject = listData[position]
        holder.bind(dataObject)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ViewHolder(private val binding: ImagesListByDateItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataObject: ResponseData) {
            binding.tvTitleField.text = dataObject.title
            binding.tvDateField.text = dataObject.date
            binding.tvDescField.text = dataObject.explanation
            val mediaType = dataObject.mediaType
            binding.tvMediaType.text = mediaType
            val imageUrl = dataObject.url
            if (!TextUtils.isEmpty(imageUrl)) {
                if (!TextUtils.isEmpty(imageUrl)) {
                    if (mediaType == Constants.KEY_IMAGE_MEDIA_TYPE) {
                        GlideApp.with(context)
                            .asBitmap()
                            .load(imageUrl)
                            .error(R.drawable.ic_default_icon)
                            .into(object : BitmapImageViewTarget(binding.ivImageField) {
                                override fun setResource(resource: Bitmap?) {
                                    binding.progressBar.visibility = View.GONE
                                    if (resource != null) {
                                        binding.ivImageField.setImageBitmap(resource)
                                    } else {
                                        binding.ivImageField.setImageResource(R.drawable.ic_default_icon)
                                    }
                                }
                            })
                    } else {
                        var youtubeCode = imageUrl?.replace(youtubeBaseUrl, "")
                        youtubeCode = youtubeCode?.replace("?rel=0", "")
                        val youtubeImageUrl = "http://img.youtube.com/vi/$youtubeCode/0.jpg"
                        GlideApp.with(context)
                            .asBitmap()
                            .load(youtubeImageUrl)
                            .into(object : BitmapImageViewTarget(binding.ivImageField) {
                                override fun setResource(resource: Bitmap?) {
                                    binding.progressBar.visibility = View.GONE
                                    if (resource != null) {
                                        binding.ivImageField.setImageBitmap(resource)
                                    } else {
                                        binding.ivImageField.setImageResource(R.drawable.ic_default_icon)
                                    }
                                }
                            })
                    }
                }
            }

            val isFavourite = dataObject.isFavourites
            if (isFavourite) {
                binding.ivFavouriteImageField.setImageResource(R.drawable.ic_favourite_checked_icon)
            } else {
                binding.ivFavouriteImageField.setImageResource(R.drawable.ic_favourite_unchecked_icon)
            }
            binding.ivFavouriteImageField.setOnClickListener {
                val selectedItem = listData[adapterPosition]
                val contentModel = ContentModel(Constants.KEY_FAVOURITES, selectedItem)
                callBackListener.callBackSelected(contentModel)
            }
        }
    }
}