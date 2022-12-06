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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(private val repository: RepoRepository, private val communityReposRepository: CommunityReposRepository) : ViewModel() {
    val communityReposData: MutableState<DataOrException<CommunityRepos, Boolean, Exception>> = mutableStateOf(DataOrException(null, true, Exception("")))

    private val _communityRepos = MutableLiveData<List<Repo>>()
    private val _loadingCommunityRepos = MutableLiveData<Boolean>(true)

    val communityRepos: LiveData<List<Repo>>
        get() = _communityRepos
    val loadingCommunityRepos: LiveData<Boolean>
        get() = _loadingCommunityRepos

    private val _repoURLS = MutableStateFlow<List<RepoTable>>(emptyList())
    val repoURLS = _repoURLS.asStateFlow()

    private val _repos = MutableLiveData<List<Repo>>()
    val repos: LiveData<List<Repo>>
        get() = _repos
    private val _loadingRepos = MutableLiveData(true)
    val loadingRepos: LiveData<Boolean>
        get() = _loadingRepos

    private val _frequentlyUsedEmotes = MutableStateFlow<List<FrequentlyUsedEmotesTable>>(emptyList())
    val frequentlyUsedEmotes = _frequentlyUsedEmotes.asStateFlow()

    private val _favouriteEmotes = MutableStateFlow<List<FavouriteEmotesTable>>(emptyList())
    val favouriteEmotes = _favouriteEmotes.asStateFlow()

    private val _selectedRepo = MutableLiveData<Repo>(
        Repo(
            id = null,
            selected = false,
            url = null,
            author = null,
            description = null,
            emotes = emptyList(),
            icon = "",
            keywords = null,
            name = "",
            path = "",
            favouriteEmotes = null
        )
    )
    val selectedRepo: LiveData<Repo>
        get() = _selectedRepo

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCommunityRepos()
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllRepos().distinctUntilChanged().collect {
                listOfRepos ->
                if (listOfRepos.isEmpty()) {
                    _repoURLS.value = emptyList()
                    _repos.value = emptyList()
                } else {
                    Log.d("ListOfRepos: ", listOfRepos.toString())
                    _repoURLS.value = listOfRepos
                }
            }
        }
        viewModelScope.launch (Dispatchers.IO){
            repository.getAllFrequentlyUsedEmotes().distinctUntilChanged().collect {
                    listOfFrequentlyUsedemotes ->
                if (listOfFrequentlyUsedemotes.isEmpty()) {
                    _frequentlyUsedEmotes.value = kotlin.collections.emptyList()
                } else {
                    _frequentlyUsedEmotes.value = listOfFrequentlyUsedemotes
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllFavouriteEmotes().distinctUntilChanged().collect {
                    listOfFavouriteEmotes ->
                if (listOfFavouriteEmotes.isEmpty()) {
                    _favouriteEmotes.value = kotlin.collections.emptyList()
                } else {
                    _favouriteEmotes.value = listOfFavouriteEmotes
                }
            }
        }
    }

    suspend fun getReposData() {
        _loadingRepos.value = true
        val repoList = mutableListOf<Repo>()
        repoURLS.value.forEachIndexed() { index, repo ->
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(repo.repoURL).addConverterFactory(GsonConverterFactory.create()).build()
            val api: ReposApi = retrofit.create(ReposApi::class.java)
            val dataOrException = DataOrException<Repo, Boolean, Exception>()
            val repoData = repository.getRepoData(dataOrException, api)

            if (repoData.loading == false) {
                repoData.data?.id = repo.id
                repoData.data?.url = repo.repoURL
                repoList.add(repoData.data!!)
            }
        }
        _loadingRepos.value = false
        _repos.value = repoList
    }

    fun getCommunityRepos() {
        viewModelScope.launch() {
            communityReposData.value.loading = false
            communityReposData.value = communityReposRepository.getCommunityRepos()
            val commRepos = mutableListOf<Repo>()

            if(communityReposData.value.data.toString().isNotEmpty()) {
                communityReposData.value.loading = false
                _loadingCommunityRepos.value = true
                val data = communityReposData.value.data?.toMutableList()
                data?.forEachIndexed { index, dataItem ->
                    val retrofit: Retrofit = Retrofit.Builder().baseUrl(dataItem).addConverterFactory(GsonConverterFactory.create()).build()
                    val api: ReposApi = retrofit.create(ReposApi::class.java)
                    val dataOrException = DataOrException<Repo, Boolean, Exception>()
                    val repoData = repository.getRepoData(dataOrException, api)

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

    fun selectRepo(repo: Repo) {
        viewModelScope.launch {
            val newRepoList = mutableListOf<Repo>()
            _repos.value?.forEach { rep ->
                rep.selected = false
                if (rep == repo) {
                    rep.selected = true
                    _selectedRepo.value = rep
                }
                newRepoList.add(rep)
            }
            _repos.value = emptyList()
            _repos.value = newRepoList
        }
    }

    fun deselectAllRepos() {
        viewModelScope.launch {
            val newRepoList = mutableListOf<Repo>()
            _repos.value?.forEach { rep ->
                rep.selected = false
                newRepoList.add(rep)
            }
            _repos.value = newRepoList
        }
    }

    fun addRepo(repo: RepoTable) = viewModelScope.launch {
        repository.addRepo(repo)
    }
    fun deleteRepo(repo: RepoTable) = viewModelScope.launch {
        repository.deleteRepo(repo)
    }
    fun addFrequentlyUsedEmote(emote: FrequentlyUsedEmotesTable) = viewModelScope.launch {
        Log.d("Emote -", emote.toString())
        _frequentlyUsedEmotes.value!!.forEach { frequentlyUsedEmote ->
            if (frequentlyUsedEmote.emoteURL == emote.emoteURL) {
                deleteFrequentlyUsedEmote(frequentlyUsedEmote)
            }
        }

        if (_frequentlyUsedEmotes.value!!.size == 25) {
            deleteFrequentlyUsedEmote(_frequentlyUsedEmotes.value!![0])
        }

        repository.addFrequentlyUsedEmote(emote)
    }
    fun deleteFrequentlyUsedEmote(emote: FrequentlyUsedEmotesTable) = viewModelScope.launch { repository.deleteFrequentlyUsedEmote(emote) }

    fun addFavouriteEmote(emote: FavouriteEmotesTable) = viewModelScope.launch {
        _favouriteEmotes.value.forEach { favouriteEmote ->
            if (favouriteEmote.emoteURL == emote.emoteURL && favouriteEmote.repoURL == emote.emoteURL) {
                deleteFavouriteEmote(favouriteEmote)
            }
        }

        repository.addFavouriteEmote(emote)
    }
    fun deleteFavouriteEmote(emote: FavouriteEmotesTable) = viewModelScope.launch { repository.deleteFavouriteEmote(emote) }
}