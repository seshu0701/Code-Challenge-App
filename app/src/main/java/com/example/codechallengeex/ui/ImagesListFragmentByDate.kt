package com.example.codechallengeex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.codechallengeex.MyApplication
import com.example.codechallengeex.R
import com.example.codechallengeex.data.interfaces.CallBackListener
import com.example.codechallengeex.data.network.NetworkState
import com.example.codechallengeex.databinding.ImagesListByDateViewBinding
import com.example.codechallengeex.model.ContentModel
import com.example.codechallengeex.model.ResponseData
import com.example.codechallengeex.ui.adapter.ImagesListByDateAdapter
import com.example.codechallengeex.utils.*
import java.util.*

/**
 * Created by Satya Seshu on 10/04/22.
 */
class ImagesListFragmentByDate : Fragment(), CallBackListener {

    private lateinit var mainViewModel: MainViewModel
    private var imagesListByDateAdapter: ImagesListByDateAdapter? = null
    private lateinit var binding: ImagesListByDateViewBinding
    private val listData: MutableList<ResponseData> = ArrayList()
    private val favouritesList: MutableList<String> = ArrayList()
    private var fromDateSelected: String? = null
    private var toDateSelected: String? = null
    private var isFabButtonsVisible = false

    companion object {
        fun getInstance(): ImagesListFragmentByDate {
            return ImagesListFragmentByDate()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = ImagesListByDateViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUIComponents()
        initData()
        setUpObservers()
        currentDaySelected()
    }

    private fun loadUIComponents() {
        binding.fab.setOnClickListener {
            fabSelected()
        }
        binding.btnCurrentDayPicture.setOnClickListener {
            binding.fabButtonsLayout.visibility = View.GONE
            isFabButtonsVisible = !isFabButtonsVisible
            currentDaySelected()
        }
        binding.btnCustomDaysPicture.setOnClickListener {
            isFabButtonsVisible = !isFabButtonsVisible
            customDatePickerSelected()
        }
        binding.btnMyFavouritePicture.setOnClickListener {
            isFabButtonsVisible = !isFabButtonsVisible
            myFavouritesSelected()
        }
    }

    private fun currentDaySelected() {
        fromDateSelected = DateUtilities.getStringDate(Calendar.getInstance())
        getImagesListByCurrentDate()
    }

    private fun myFavouritesSelected() {
        binding.fabButtonsLayout.visibility = View.GONE
        val lFavouritesList = listData.filter {
            it.isFavourites
        }
        val favouritesListDialog = FavouritesListDialog.getInstance(
            lFavouritesList,
            favouritesList
        )
        favouritesListDialog.setCallBackListener(this)
        if (!requireActivity().isFinishing) {
            favouritesListDialog.show(
                requireActivity().supportFragmentManager,
                FavouritesListDialog::class.java.name
            )
        }
    }

    private fun customDatePickerSelected() {
        binding.fabButtonsLayout.visibility = View.GONE
        val customDateSelectionDialog = CustomDateSelectionDialog.getInstance()
        customDateSelectionDialog.setCallBackListener(object : CallBackListener {
            override fun callBackSelected(pObject: Any?) {
                if (pObject is ContentModel) {
                    fromDateSelected = pObject.status
                    toDateSelected = pObject.value as String
                    getImagesListByDate()
                }
            }
        })
        if (!requireActivity().isFinishing) {
            customDateSelectionDialog.show(
                requireActivity().supportFragmentManager,
                CustomDateSelectionDialog::class.java.name
            )
        }
    }

    private fun fabSelected() {
        isFabButtonsVisible = !isFabButtonsVisible
        if (isFabButtonsVisible) {
            binding.fabButtonsLayout.visibility = View.VISIBLE
        } else {
            binding.fabButtonsLayout.visibility = View.GONE
        }
    }

    private fun initData() {
        //initialization of view model instance,
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(MyApplication.getApiHelperInstance(requireActivity()))
        )[MainViewModel::class.java]

        val divider = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        divider.setDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.recycler_divider_decoration
            )!!
        )
        binding.recyclerListField.addItemDecoration(divider)

        imagesListByDateAdapter = ImagesListByDateAdapter(requireActivity(), listData, this)
        binding.recyclerListField.setHasFixedSize(false)
        binding.recyclerListField.adapter = imagesListByDateAdapter
    }

    private fun setUpObservers() {
        mainViewModel.errorMessageLiveData.observe(requireActivity()) {
            showErrorMessage(it)
        }

        mainViewModel.networkState.observe(requireActivity()) {
            binding.progressBar.visibility =
                if (it.state == NetworkState.Status.RUNNING) View.VISIBLE else View.GONE
        }

        mainViewModel.itemsListDate.observe(requireActivity()) {
            if (it != null) {
                if (NetworkUtils.isNetworkConnected(requireActivity())) {
                    val lastUpdatedTime =
                        DateUtilities.getDateTimeStampFromCalendar(Calendar.getInstance())
                    CodeChallengeAppCache.putData(
                        Constants.CACHE_LAST_UPDATED_TIME__BY_DATE,
                        requireActivity(),
                        lastUpdatedTime
                    )
                    binding.tvLastUpdatedDateField.text = lastUpdatedTime
                } else {
                    val lObject = CodeChallengeAppCache.getData(
                        Constants.CACHE_LAST_UPDATED_TIME__BY_DATE,
                        requireActivity()
                    )
                    binding.tvLastUpdatedDateField.text = lObject.toString()
                }
                listData.clear()
                listData.addAll(it)
                checkFavouritesList()
                imagesListByDateAdapter?.updateItems(listData)
            }
        }
    }

    private fun checkFavouritesList() {
        val lObject =
            CodeChallengeAppCache.getData(Constants.CACHE_FAVOURITES_LIST, requireActivity())
        if (favouritesList.isEmpty() && lObject != null) {
            if (lObject is MutableList<*>) {
                lObject.forEach {
                    if (it is String) {
                        favouritesList.add(it)
                    }
                }
            }
            listData.forEachIndexed { index, responseData ->
                val indexList = favouritesList.indexOf(responseData.title)
                if (indexList > -1) {
                    responseData.isFavourites = !responseData.isFavourites
                    listData[index] = responseData
                }
            }
        }
    }

    private fun showErrorMessage(message: String?) {
        if (!requireActivity().isFinishing) {
            val alertDialogBuilder = AlertDialog.Builder(requireActivity())
            alertDialogBuilder.setTitle(getString(R.string.dialog_title))
            alertDialogBuilder.setMessage(message)
            alertDialogBuilder.setNeutralButton(getString(R.string.dialog_ok), null)
            alertDialogBuilder.show()
        }
    }

    private fun getImagesListByCurrentDate() {
        listData.clear()
        imagesListByDateAdapter?.updateItems(listData)
        mainViewModel.getAllImagesByCurrentDate(
            fromDateSelected
        )
    }

    private fun getImagesListByDate() {
        listData.clear()
        imagesListByDateAdapter?.updateItems(listData)
        mainViewModel.getAllImagesByDate(
            fromDateSelected,
            toDateSelected
        )
    }

    override fun callBackSelected(pObject: Any?) {
        if (pObject is ContentModel) {
            val status = pObject.status
            if (status == Constants.KEY_FAVOURITES) {
                val value = pObject.value as ResponseData
                value.isFavourites = !value.isFavourites
                val index = listData.indexOf(value)
                if (index > -1) {
                    if (value.isFavourites) value.title?.let { favouritesList.add(it) } else
                        favouritesList.remove(value.title)
                    listData[index] = value
                    imagesListByDateAdapter?.notifyItemChanged(index)
                    LoggerInfo.printLog("pObject Hashcode", pObject.hashCode())
                }
            } else if (status == Constants.KEY_UPDATE_FAVOURITES) {
                val value = pObject.value as ResponseData
                val index = listData.indexOf(value)
                if (index > -1) {
                    favouritesList.remove(value.title)
                    listData[index].isFavourites = value.isFavourites
                    imagesListByDateAdapter?.notifyItemChanged(index)
                }
            }
        }
    }

    fun updateCache() {
        CodeChallengeAppCache.putData(
            Constants.CACHE_FAVOURITES_LIST,
            requireActivity(),
            favouritesList
        )
    }
}