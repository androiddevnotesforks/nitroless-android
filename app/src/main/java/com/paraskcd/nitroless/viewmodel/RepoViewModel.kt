package com.paraskcd.nitroless.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
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

    private val _frequentlyUsedStickers = MutableStateFlow<List<FrequentlyUsedStickersTable>>(emptyList())
    val frequentlyUsedStickers = _frequentlyUsedStickers.asStateFlow()

    private val _favouriteEmotes = MutableStateFlow<List<FavouriteEmotesTable>>(emptyList())
    val favouriteEmotes = _favouriteEmotes.asStateFlow()

    private val _favouriteStickers = MutableStateFlow<List<FavouriteStickersTable>>(emptyList())
    val favouriteStickers = _favouriteStickers.asStateFlow()

    private val _selectedEmote = MutableLiveData<FavouriteEmotesTable>()
    val selectedEmote: LiveData<FavouriteEmotesTable>
        get() = _selectedEmote

    private val _selectedSticker = MutableLiveData<FavouriteStickersTable>()
    val selectedSticker: LiveData<FavouriteStickersTable>
        get() = _selectedSticker

    private val _selectedRepo = MutableLiveData<Repo>()
    val selectedRepo: LiveData<Repo>
        get() = _selectedRepo

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCommunityRepos()
            getReposData()
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllRepos().distinctUntilChanged().collect {
                listOfRepos ->
                if (listOfRepos.isEmpty()) {
                    _repoURLS.value = emptyList()
                } else {
                    Log.d("ListOfRepos: ", listOfRepos.toString())
                    _repoURLS.value = listOfRepos
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
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
            repository.getAllFrequentlyUsedStickers().distinctUntilChanged().collect {
                listOfFrequentlyUsedStickers ->

                if (listOfFrequentlyUsedStickers.isEmpty()) {
                    _frequentlyUsedStickers.value = emptyList()
                } else {
                    _frequentlyUsedStickers.value = listOfFrequentlyUsedStickers
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
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllFavouriteStickers().distinctUntilChanged().collect {
                listOfFavouriteStickers ->
                if (listOfFavouriteStickers.isEmpty()) {
                    _favouriteStickers.value = emptyList()
                } else {
                    _favouriteStickers.value = listOfFavouriteStickers
                }
            }
        }
    }

    suspend fun getReposData() {
        viewModelScope.launch {
            _loadingRepos.value = true
            val repoList = mutableListOf<Repo>()
            repoURLS.value.forEachIndexed() { index, repo ->
                val favEmotes = mutableListOf<FavouriteEmotesTable>()
                val favStickers = mutableListOf<FavouriteStickersTable>()
                val retrofit: Retrofit = Retrofit.Builder().baseUrl(repo.repoURL).addConverterFactory(GsonConverterFactory.create()).build()
                val api: ReposApi = retrofit.create(ReposApi::class.java)
                val dataOrException = DataOrException<Repo, Boolean, Exception>()
                val repoData = repository.getRepoData(dataOrException, api)
                favouriteEmotes.value.forEach {
                    if (it.repoURL == repo.repoURL) {
                        favEmotes.add(it)
                    }
                }
                favouriteStickers.value.forEach {
                    if (it.repoURL == repo.repoURL) {
                        favStickers.add(it)
                    }
                }

                if (repoData.loading == false) {
                    repoData.data?.id = repo.id
                    repoData.data?.url = repo.repoURL
                    repoData.data?.favouriteEmotes = favEmotes
                    repoData.data?.favouriteStickers = favStickers
                    repoList.add(repoData.data!!)
                }

                if (selectedRepo.value != null) {
                    if (selectedRepo.value!!.selected && selectedRepo.value!!.url == repo.repoURL) {
                        repoData.data?.selected = true
                        _selectedRepo.value = repoData.data
                    }
                }
            }
            _loadingRepos.value = false
            _repos.value = repoList
        }
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

    fun selectEmote(emote: FavouriteEmotesTable) {
        var selectedEmote: FavouriteEmotesTable = emote
        this.favouriteEmotes.value.forEach { favouriteEmote ->
            if (emote.repoURL == favouriteEmote.repoURL && emote.emoteURL == favouriteEmote.emoteURL) {
                selectedEmote = favouriteEmote
            }
        }
        _selectedEmote.value = selectedEmote
    }

    fun deselectEmote() {
        _selectedEmote.value = null
    }

    fun selectSticker(sticker: FavouriteStickersTable) {
        var selectedSticker: FavouriteStickersTable = sticker
        this.favouriteStickers.value.forEach {
            favouriteSticker ->
            if (sticker.repoURL == favouriteSticker.repoURL && sticker.stickerURL == favouriteSticker.stickerURL) {
                selectedSticker = favouriteSticker
            }
        }
        _selectedSticker.value = selectedSticker
    }

    fun deselectSticker() {
        _selectedSticker.value = null
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
            if (_selectedRepo.value != null) {
                _selectedRepo.value!!.selected = false
            }
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

        if (_frequentlyUsedEmotes.value!!.size > 24) {
            deleteFrequentlyUsedEmote(_frequentlyUsedEmotes.value!![0])
        }

        repository.addFrequentlyUsedEmote(emote)
    }
    fun addFrequentlyUsedSticker(sticker: FrequentlyUsedStickersTable) = viewModelScope.launch {
        Log.d("Sticker -", sticker.toString())

        _frequentlyUsedStickers.value!!.forEach {
            frequentlyUsedSticker ->
            if (frequentlyUsedSticker.stickerURL == sticker.stickerURL) {
                deleteFrequentlyUsedSticker(frequentlyUsedSticker)
            }
        }

        if (_frequentlyUsedStickers.value!!.size > 24) {
            deleteFrequentlyUsedSticker(_frequentlyUsedStickers.value!![0])
        }

        repository.addFrequentlyUsedSticker(sticker)
    }
    fun deleteFrequentlyUsedEmote(emote: FrequentlyUsedEmotesTable) = viewModelScope.launch { repository.deleteFrequentlyUsedEmote(emote) }

    fun deleteFrequentlyUsedSticker(sticker: FrequentlyUsedStickersTable) = viewModelScope.launch { repository.deleteFrequentlyUsedSticker(sticker) }

    fun addFavouriteEmote(emote: FavouriteEmotesTable) = viewModelScope.launch {
        _favouriteEmotes.value.forEach { favouriteEmote ->
            if (favouriteEmote.emoteURL == emote.emoteURL && favouriteEmote.repoURL == emote.emoteURL) {
                deleteFavouriteEmote(favouriteEmote)
            }
        }

        repository.addFavouriteEmote(emote)
    }

    fun addFavouriteSticker(sticker: FavouriteStickersTable) = viewModelScope.launch {
        _favouriteStickers.value.forEach { favouriteSticker ->
            if (favouriteSticker.stickerURL == sticker.stickerURL && favouriteSticker.repoURL == sticker.repoURL) {
                deleteFavouriteSticker(favouriteSticker)
            }
        }

        repository.addFavouriteSticker(sticker)
    }

    fun deleteFavouriteEmote(emote: FavouriteEmotesTable) = viewModelScope.launch { repository.deleteFavouriteEmote(emote) }

    fun deleteFavouriteSticker(sticker: FavouriteStickersTable) = viewModelScope.launch { repository.deleteFavouriteSticker(sticker) }
}