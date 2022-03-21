package com.example.weatherapp.data.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavRemoteDataSource(
    private val favDao: FavDao,
    private val dispatcher:CoroutineDispatcher = Dispatchers.IO)
    : LocalInterface {

    override suspend fun insertFavLocation(favLocation : FavModel)
    = withContext(dispatcher) {
        favDao.insertTask(favLocation)
    }

}