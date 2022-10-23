package com.dicoding.submission.imam.storyapp.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dicoding.submission.imam.storyapp.data.local.StoryAppDatabase
import com.dicoding.submission.imam.storyapp.data.local.entity.RemoteKeysEntity
import com.dicoding.submission.imam.storyapp.data.local.entity.StoryEntity
import com.dicoding.submission.imam.storyapp.data.remote.story.StoryService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryAppDatabase,
    private val apiStoryService: StoryService,
    private val token: String
) : RemoteMediator<Int, StoryEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        return try {
            val responseData = apiStoryService.getAllStoryPaging(
                "Bearer $token",
                page,
                state.config.pageSize
            ).listStory
            val endOfPaginationReached = responseData.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.getRemoteKeysDao().deleteRemoteKeys()
                    database.getStoryDao().deleteAllStories()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = responseData.map {
                    RemoteKeysEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                database.getRemoteKeysDao().insertAll(keys)
                database.getStoryDao().insertStories(responseData)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            database.getRemoteKeysDao().getRemoteKeysId(it.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let {
            database.getRemoteKeysDao().getRemoteKeysId(it.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let {
                database.getRemoteKeysDao().getRemoteKeysId(it)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}