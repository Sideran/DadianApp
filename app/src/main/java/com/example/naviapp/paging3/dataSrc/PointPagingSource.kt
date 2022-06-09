package com.example.naviapp.paging3.dataSrc

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.naviapp.paging3.api.GetPointPage
import com.example.naviapp.paging3.model.PointInfo
import java.lang.Exception

class PointPagingSource (private val getPointPage:  GetPointPage,private val pointInfo: HashMap<String,String>,private  val token:String) : PagingSource<Int, PointInfo>(){
    override fun getRefreshKey(state: PagingState<Int, PointInfo>): Int? =null


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PointInfo> {
        return try {
            val page = params.key ?: 1 // set page 1 as default
            val pageSize = params.loadSize
            val pointInfoRespo = getPointPage.searchRepos(page, pageSize,pointInfo,token)
            val repoItems = pointInfoRespo.items
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (repoItems.isNotEmpty()&&pointInfoRespo.total>=page*pageSize+repoItems.size) page + 1 else null
            LoadResult.Page(repoItems, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}