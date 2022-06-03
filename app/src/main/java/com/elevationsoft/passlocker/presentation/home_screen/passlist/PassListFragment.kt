package com.elevationsoft.passlocker.presentation.home_screen.passlist

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.elevationsoft.easydialog.EasyDialog
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.FragmentPassListBinding
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.domain.models.Credential
import com.elevationsoft.passlocker.presentation.add_credentials.AddCredentialsActivity
import com.elevationsoft.passlocker.presentation.add_credentials.AddCredentialsActivity.Companion.KEY_CREDENTIAL_MODE
import com.elevationsoft.passlocker.presentation.add_credentials.AddCredentialsActivity.Companion.KEY_CREDENTIAL_OBJ
import com.elevationsoft.passlocker.presentation.home_screen.HomeActivity
import com.elevationsoft.passlocker.utils.ButtonList
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import com.elevationsoft.passlocker.utils.CustomLoader
import com.elevationsoft.passlocker.utils.FragmentUtils.startActivityForResult
import com.elevationsoft.passlocker.utils.SearchQueryUtils
import com.elevationsoft.passlocker.utils.SearchQueryUtils.setSearchQueryListener
import com.elevationsoft.passlocker.utils.ViewUtils.hide
import com.elevationsoft.passlocker.utils.ViewUtils.invisible
import com.elevationsoft.passlocker.utils.ViewUtils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PassListFragment : Fragment(), HomeActivity.OnAddClickedCallBack {
    private val passListVm by viewModels<PasslistFragmentViewModel>()
    private lateinit var binding: FragmentPassListBinding
    private lateinit var openAddCredentialActivity: ActivityResultLauncher<Intent>
    private var selectedCategoryId = -1L
    private var passListAdapter: PassListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HomeActivity.addClickedCallBack = this
        openAddCredentialActivity = startActivityForResult {
            it?.let { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.let { resultIntent ->
                        if (resultIntent.hasExtra(KEY_CREDENTIAL_MODE) && resultIntent.hasExtra(
                                KEY_CREDENTIAL_OBJ
                            )
                        ) {
                            val credential: Credential? =
                                resultIntent.getParcelableExtra(KEY_CREDENTIAL_OBJ)

                            if (credential != null) {
                                if (resultIntent.getStringExtra(KEY_CREDENTIAL_MODE) == AddCredentialsActivity.MODE_ADD) {
                                    if (selectedCategoryId == credential.categoryId) {
                                        if (binding.rvPasslist.layoutManager!!.itemCount <= 0) {
                                            submitAdapter()
                                        } else {
                                            updateItemInPaging(
                                                PagingItemEvent.InsertUpdateItem(
                                                    credential
                                                )
                                            )
                                            binding.rvPasslist.scrollToPosition(0)
                                        }
                                    }
                                } else {
                                    when (selectedCategoryId) {
                                        -1L -> {
                                            updateItemInPaging(PagingItemEvent.EditItem(credential))
                                        }
                                        credential.categoryId -> {
                                            updateItemInPaging(PagingItemEvent.EditItem(credential))
                                        }
                                        else -> {
                                            updateItemInPaging(
                                                PagingItemEvent.RemoveItem(
                                                    credential
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPassListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        passListVm.passlistFragState.removeObservers(viewLifecycleOwner)
        passListVm.passlistFragState.observe(viewLifecycleOwner) {
            updateUi(it)
        }

        passListVm.isItemDeleted.observe(viewLifecycleOwner) {
            CustomLoader.getInstance().hideLoader(requireActivity())
            updateItemInPaging(PagingItemEvent.RemoveItem(it))
            requireContext().toast(getString(R.string.text_credential_deleted_msg))
            if (binding.rvPasslist.layoutManager!!.itemCount <= 0) {
                binding.layoutLoadingView.root.hide()
                binding.layoutEmptyView.root.show()
                binding.rvPasslist.invisible()
                binding.layoutEmptyView.tvError.text = getString(R.string.text_no_data)
                stopSearch()
            }
        }

        passListVm.isItemMarkedFav.observe(viewLifecycleOwner) {
            CustomLoader.getInstance().hideLoader(requireActivity())
            if (it.isFavourite) {
                updateItemInPaging(PagingItemEvent.EditItem(it))
                requireContext().toast(getString(R.string.text_item_marked_fav))
            } else {
                if (selectedCategoryId > 0L) {
                    updateItemInPaging(PagingItemEvent.EditItem(it))
                } else {
                    updateItemInPaging(PagingItemEvent.RemoveItem(it))
                }
                requireContext().toast(getString(R.string.text_item_unmarked_fav))
            }

            if (binding.rvPasslist.layoutManager!!.itemCount <= 0) {
                binding.layoutLoadingView.root.hide()
                binding.layoutEmptyView.root.show()
                binding.rvPasslist.invisible()
                binding.layoutEmptyView.tvError.text = getString(R.string.text_no_data)
                stopSearch()
            }
        }

        passListVm.getCategoryList()
    }

    override fun onAddClicked() {
        if (passListVm.passlistFragState.value != null && passListVm.passlistFragState.value!!.categoryList.size > 1) {
            val intent = Intent(requireActivity(), AddCredentialsActivity::class.java)
            openAddCredentialActivity.launch(intent)
        } else {
            requireContext().toast(getString(R.string.text_please_add_category), Toast.LENGTH_LONG)
        }
    }

    private fun updateUi(state: PassListFragmentState) {
        if (state.isCategoryLoaded) {
            if (state.isLoading) {
                CustomLoader.getInstance().showLoader(requireActivity())
            } else {
                CustomLoader.getInstance().hideLoader(requireActivity())
            }

            if (state.hasError.asString(requireContext()).isNotEmpty()) {
                requireContext().toast(state.hasError.asString(requireContext()))
            }

        } else {
            binding.rvPasslist.invisible()
            if (state.isLoading) {
                binding.layoutLoadingView.root.show()
                binding.layoutEmptyView.root.hide()
            }

            if (state.hasError.asString(requireContext()).isNotEmpty()) {
                binding.llSearch.hide()
                binding.layoutEmptyView.root.show()
                binding.layoutEmptyView.tvError.text =
                    state.hasError.asString(requireContext())
            } else if (state.categoryList.isNotEmpty()) {
                binding.layoutEmptyView.root.hide()
                setUpCategoryTabs(state.categoryList)
                passListVm.setCategoryLoaded()
                submitAdapter()
            }

        }
    }

    private fun setUpCategoryTabs(categoryList: List<Category>) {
        var selectedCategoryIndex =
            categoryList.indexOfFirst { it.id == passListVm.getSavedSelectedCategoryId() }
        if (selectedCategoryIndex < 0) {
            selectedCategoryIndex = 0
            passListVm.saveSelectedCategoryId(categoryList[selectedCategoryIndex].id)
        }
        selectedCategoryId = categoryList[selectedCategoryIndex].id
        val catList = ArrayList<String>()
        categoryList.forEach {
            catList.add(it.categoryName)
        }
        val btnList = ButtonList.with(requireContext())
            .setLayoutManager(ButtonList.LINEAR_LAYOUT_MANAGER)
            .setOrientation(ButtonList.ORIENTATION_HORIZONTAL)
            .setRecyclerViewHeight(48)
            .setDataList(catList)
            .setSelectedIndex(selectedCategoryIndex)
            .setSelectListener(object : ButtonList.ItemListener {
                override fun onItemClick(item: String, index: Int) {
                    onCategoryChanged(categoryList, index)
                }
            })
        binding.llCatTabs.removeAllViews()
        binding.llCatTabs.addView(btnList.createView())

    }

    private fun onCategoryChanged(categoryList: List<Category>, index: Int) {
        //stop and reset search
        stopSearch()
        binding.etSearch.setText("")

        //save selected category
        selectedCategoryId = categoryList[index].id
        passListVm.saveSelectedCategoryId(categoryList[index].id)

        //remove old item
        updateItemInPaging(PagingItemEvent.RemoveAllItem())

        //restart paging for selected category
        submitAdapter()
    }

    private fun setUpRecyclerView() {
        binding.rvPasslist.setHasFixedSize(true)
        val llm = LinearLayoutManager(requireContext())
        binding.rvPasslist.layoutManager = llm
        passListAdapter =
            PassListAdapter(requireContext(), object : PassListAdapter.PassListItemCallBacks {
                override fun onItemClicked(credential: Credential) {
                    val intent = Intent(requireActivity(), AddCredentialsActivity::class.java)
                    intent.putExtra(KEY_CREDENTIAL_OBJ, credential)
                    openAddCredentialActivity.launch(intent)
                }

                override fun onDeleteClicked(credential: Credential) {
                    openDeleteCredentialDialog(credential)
                }

                override fun onFavouriteClicked(credential: Credential) {
                    passListVm.markUnMarkFavourite(credential.id, !credential.isFavourite)
                }
            })
        binding.rvPasslist.adapter = passListAdapter

        startPagingStateUpdate()
    }

    private fun submitAdapter(query: String = binding.etSearch.text.toString()) {
        lifecycleScope.launchWhenResumed {
            passListVm.getCredentialList(query, selectedCategoryId)
                .collectLatest {
                    passListAdapter?.submitData(lifecycle, it)
                }
        }
    }

    private fun updateItemInPaging(pagingItemEvent: PagingItemEvent<Credential>) {
        lifecycleScope.launchWhenResumed {
            passListVm.updateItemInPaging(pagingItemEvent)?.collectLatest {
                passListAdapter?.submitData(lifecycle, it)
            }
        }
    }

    private fun startPagingStateUpdate() {
        lifecycleScope.launchWhenResumed {
            passListAdapter?.loadStateFlow?.collectLatest {
                when (it.refresh) {
                    is LoadState.Loading -> {
                        binding.layoutLoadingView.root.show()
                        binding.layoutEmptyView.root.hide()
                        binding.rvPasslist.invisible()
                        stopSearch()
                    }
                    is LoadState.NotLoading -> {
                        binding.layoutLoadingView.root.hide()
                        binding.layoutEmptyView.root.hide()
                        binding.rvPasslist.show()
                        startSearch()
                    }
                    is LoadState.Error -> {
                        binding.layoutLoadingView.root.hide()
                        binding.layoutEmptyView.root.show()
                        binding.rvPasslist.invisible()
                        binding.layoutEmptyView.tvError.text =
                            (it.refresh as LoadState.Error).error.localizedMessage
                        stopSearch()
                    }
                }
            }
        }
    }

    private fun openDeleteCredentialDialog(credential: Credential) {
        EasyDialog.with(requireContext())
            .setCancelable(false)
            .setTitle(true, getString(R.string.text_alert))
            .setMessage(
                true,
                getString(
                    R.string.text_credential_delete_dialog_message,
                    credential.title
                )
            )
            .setPrimaryButton(true, getString(R.string.text_yes))
            .setSecondaryButton(true, getString(R.string.text_no))
            .setOnButtonClickListener(object :
                EasyDialog.OnButtonClickListener {
                override fun onPrimaryButtonClick() {
                    passListVm.deleteCredential(credential.id)
                }

                override fun onSecondaryButtonClick() {

                }

            }).show()
    }

    private fun startSearch() {
        binding.etSearch.setSearchQueryListener(lifecycle) {
            submitAdapter(it)
        }
    }

    private fun stopSearch() {
        SearchQueryUtils.cancelJob()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopSearch()
    }

    companion object {
        @JvmStatic
        fun newInstance() = PassListFragment()
    }


}