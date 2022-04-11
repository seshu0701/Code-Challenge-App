package com.example.codechallengeex.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.codechallengeex.data.interfaces.CallBackListener
import com.example.codechallengeex.databinding.FavouritesListDialogViewBinding
import com.example.codechallengeex.model.ContentModel
import com.example.codechallengeex.model.ResponseData
import com.example.codechallengeex.ui.adapter.ImagesListByDateAdapter
import com.example.codechallengeex.utils.Constants
import java.io.Serializable

/**
 * Created by Satya Seshu on 10/04/22.
 */
class FavouritesListDialog : DialogFragment(), CallBackListener {

    private lateinit var binding: FavouritesListDialogViewBinding
    private lateinit var imagesListByDateAdapter: ImagesListByDateAdapter
    private var favouritesList: MutableList<*>? = null
    private val updatedFavouritesList: MutableList<String> = ArrayList()
    private var listData: MutableList<*>? = null
    private val updatedListData: MutableList<ResponseData> = ArrayList()
    private var callBackListener: CallBackListener? = null

    companion object {
        fun getInstance(
            listData: List<ResponseData>,
            favouritesList: MutableList<String>
        ): FavouritesListDialog {
            val favouritesListDialog = FavouritesListDialog()
            val extras = Bundle()
            extras.apply {
                putSerializable(Constants.KEY_LIST_DATA, listData as Serializable)
                putSerializable(Constants.KEY_FAVOURITES_LIST_DATA, favouritesList as Serializable)
                favouritesListDialog.arguments = this
            }
            return favouritesListDialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val dialog = Dialog(requireActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        binding = FavouritesListDialogViewBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(binding.root)

        val extras = arguments
        if (extras != null) {
            listData = extras.getSerializable(Constants.KEY_LIST_DATA) as MutableList<*>?
            favouritesList =
                extras.getSerializable(Constants.KEY_FAVOURITES_LIST_DATA) as MutableList<*>?
        }

        val listDataSize = listData?.size ?: 0
        if (listDataSize > 0) {
            listData?.forEach {
                if (it is ResponseData) {
                    updatedListData.add(it)
                }
            }
        }

        if (updatedListData.isEmpty()) {
            binding.tvNoFavouritesFound.visibility = View.VISIBLE
        } else {
            binding.tvNoFavouritesFound.visibility = View.GONE
        }

        val favouritesListSize = favouritesList?.size ?: 0
        if (favouritesListSize > 0) {
            favouritesList?.forEach {
                if (it is String) {
                    updatedFavouritesList.add(it)
                }
            }
        }

        loadUIComponents()

        return dialog
    }

    private fun loadUIComponents() {
        imagesListByDateAdapter = ImagesListByDateAdapter(requireActivity(), updatedListData, this)
        binding.recyclerListField.setHasFixedSize(false)
        binding.recyclerListField.adapter = imagesListByDateAdapter
    }

    fun setCallBackListener(callBackListener: CallBackListener?) {
        this.callBackListener = callBackListener
    }

    override fun callBackSelected(pObject: Any?) {
        if (pObject is ContentModel) {
            val value = pObject.value as ResponseData
            val index = updatedListData.indexOf(value)
            if (index > -1) {
                value.isFavourites = !value.isFavourites
                updatedListData.removeAt(index)
                updatedFavouritesList.remove(value.title)
                imagesListByDateAdapter.notifyItemRemoved(index)
                val contentModel = ContentModel(Constants.KEY_UPDATE_FAVOURITES, value)
                callBackListener?.callBackSelected(contentModel)
            }
        }
    }
}