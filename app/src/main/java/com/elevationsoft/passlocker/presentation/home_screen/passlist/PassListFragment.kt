package com.elevationsoft.passlocker.presentation.home_screen.passlist

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.elevationsoft.passlocker.presentation.home_screen.HomeActivity
import com.elevationsoft.passlocker.utils.ButtonList
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import com.elevationsoft.passlocker.utils.CustomLoader
import com.elevationsoft.passlocker.utils.FragmentUtils.startActivityForResult
import com.elevationsoft.passlocker.utils.SearchQueryUtils
import com.elevationsoft.passlocker.utils.SearchQueryUtils.setSearchQueryListener
import com.elevationsoft.passlocker.utils.ViewUtils.hide
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
                    passListAdapter?.refresh()
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

        binding.etSearch.setSearchQueryListener(lifecycle) {
            submitAdapter(it)
        }

        passListVm.isItemDeleted.observe(viewLifecycleOwner) {
            requireContext().toast(it.asString(requireContext()))
        }

        passListVm.isItemMarkedFav.observe(viewLifecycleOwner) {
            requireContext().toast(it.asString(requireContext()))
        }

        passListVm.getCategoryList()
    }

    override fun onAddClicked() {
        val intent = Intent(requireActivity(), AddCredentialsActivity::class.java)
        openAddCredentialActivity.launch(intent)
    }

    private fun updateUi(state: PassListFragmentState) {
        if (state.isCategoryLoaded) {

            if (state.isLoading) {
                CustomLoader.getInstance().showLoader(requireActivity())
            } else {
                CustomLoader.getInstance().hideLoader(requireActivity())
            }

            if (state.hasError.asString(requireContext()).isNotEmpty()) {
                binding.llSearch.hide()
                binding.layoutEmptyView.root.show()
                binding.layoutEmptyView.tvError.text =
                    state.hasError.asString(requireContext())
            }
        } else {
            binding.rvPasslist.hide()
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
                    selectedCategoryId = categoryList[index].id
                    passListVm.saveSelectedCategoryId(categoryList[index].id)
                    submitAdapter()
                }
            })
        binding.llCatTabs.removeAllViews()
        binding.llCatTabs.addView(btnList.createView())

    }

    private fun setUpRecyclerView() {
        binding.rvPasslist.setHasFixedSize(true)
        val llm = LinearLayoutManager(requireContext())
        binding.rvPasslist.layoutManager = llm
        passListAdapter =
            PassListAdapter(requireContext(), object : PassListAdapter.PassListItemCallBacks {
                override fun onItemClicked(credential: Credential) {
                    val intent = Intent(requireActivity(), AddCredentialsActivity::class.java)
                    intent.putExtra(AddCredentialsActivity.KEY_CREDENTIAL_OBJ, credential)
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
                    passListAdapter?.submitData(it)
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
                        binding.rvPasslist.hide()
                    }
                    is LoadState.NotLoading -> {
                        binding.layoutLoadingView.root.hide()
                        binding.layoutEmptyView.root.hide()
                        binding.rvPasslist.show()
                    }
                    is LoadState.Error -> {
                        binding.layoutLoadingView.root.hide()
                        binding.layoutEmptyView.root.show()
                        binding.rvPasslist.hide()
                        binding.layoutEmptyView.tvError.text =
                            (it.refresh as LoadState.Error).error.localizedMessage
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

    override fun onDestroy() {
        super.onDestroy()
        SearchQueryUtils.cancelJob()
    }

    companion object {
        @JvmStatic
        fun newInstance() = PassListFragment()
    }


}