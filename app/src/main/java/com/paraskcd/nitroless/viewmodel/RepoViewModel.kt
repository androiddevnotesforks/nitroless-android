package com.paraskcd.nitroless.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.nitroless.data.DataOrException
import com.paraskcd.nitroless.model.*
import com.paraskcd.nitroless.network.ReposApi
import com.paraskcd.nitroless.repository.CommunityReposRepository
import com.paraskcd.nitroless.repository.RepoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(private val repository: RepoRepository, private val communityReposRepository: CommunityReposRepository) : ViewModel() {
    val communityReposData: MutableState<DataOrException<CommunityRepos, Boolean, Exception>> = mutableStateOf(DataOrException(null, true, Exception("")))

    private val _communityRepos = MutableLiveData<List<Repo>>()
    private val _loadingCommunityRepos = MutableLiveData<Boolean>()

    val communityRepos: LiveData<List<Repo>>
        get() = _communityRepos
    val loadingCommunityRepos: LiveData<Boolean>
        get() = _loadingCommunityRepos

    private val _repoURLS = MutableStateFlow<List<RepoTable>>(emptyList())
    private val _repos = MutableStateFlow<List<Repo>>(emptyList())
    private val _frequentlyUsedEmotes = MutableStateFlow<List<FrequentlyUsedEmotesTable>>(emptyList())
    private val _favouriteEmotes = MutableStateFlow<List<FavouriteEmotesTable>>(emptyList())

    val repoURLS = _repoURLS.asStateFlow()
    val repos = _repos.asStateFlow()
    val frequentlyUsedEmotes = _frequentlyUsedEmotes.asStateFlow()
    val favouriteEmotes = _favouriteEmotes.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCommunityRepos()
            repository.getAllRepos().distinctUntilChanged().collect {
                listOfRepos ->
                if (listOfRepos.isEmpty()) {
                    _repoURLS.value = emptyList()
                    _repos.value = emptyList()
                } else {
                    _repoURLS.value = listOfRepos
                }
            }
            repository.getAllFrequentlyUsedEmotes().distinctUntilChanged().collect {
                listOfFrequentlyUsedemotes ->
                if (listOfFrequentlyUsedemotes.isEmpty()) {
                    _frequentlyUsedEmotes.value = emptyList()
                } else {
                    _frequentlyUsedEmotes.value = listOfFrequentlyUsedemotes
                }
            }
            repository.getAllFavouriteEmotes().distinctUntilChanged().collect {
                listOfFavouriteEmotes ->
                if (listOfFavouriteEmotes.isEmpty()) {
                    _favouriteEmotes.value = emptyList()
                } else {
                    _favouriteEmotes.value = listOfFavouriteEmotes
                }
            }
        }
    }

    fun getCommunityRepos() {
        viewModelScope.launch() {
            communityReposData.value.loading = false
            communityReposData.value = communityReposRepository.getCommunityRepos()
            var commRepos = mutableListOf<Repo>()

            if(communityReposData.value.data.toString().isNotEmpty()) {
                communityReposData.value.loading = false
                _loadingCommunityRepos.value = true
                val data = communityReposData.value.data?.toMutableList()
                data?.forEachIndexed { index, dataItem ->
                    val retrofit: Retrofit = Retrofit.Builder().baseUrl(dataItem).addConverterFactory(GsonConverterFactory.create()).build()
                    val api: ReposApi = retrofit.create(ReposApi::class.java)
                    val dataOrException = DataOrException<Repo, Boolean, Exception>()
                    var repoData = repository.getRepoData(dataOrException, api)

                    if (repoData.loading == false) {
                        repoData.data?.url = dataItem
                        commRepos.add(repoData.data!!)
                    }
                }
            }
            _loadingCommunityRepos.value = false
            _communityRepos.value = commRepos
        }
    }

    fun addRepo(repo: RepoTable) = viewModelScope.launch { repository.addRepo(repo) }
    fun deleteRepo(repo: RepoTable) = viewModelScope.launch { repository.deleteRepo(repo) }
    fun addFrequentlyUsedEmote(emote: FrequentlyUsedEmotesTable) = viewModelScope.launch { repository.addFrequentlyUsedEmote(emote) }
    fun deleteFrequentlyUsedEmote(emote: FrequentlyUsedEmotesTable) = viewModelScope.launch { repository.deleteFrequentlyUsedEmote(emote) }
    fun addFavouriteEmote(emote: FavouriteEmotesTable) = viewModelScope.launch { repository.addFavouriteEmote(emote) }
    fun deleteFavouriteEmote(emote: FavouriteEmotesTable) = viewModelScope.launch { repository.deleteFavouriteEmote(emote) }
}