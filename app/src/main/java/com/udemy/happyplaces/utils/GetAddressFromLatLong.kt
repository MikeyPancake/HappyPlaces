package com.udemy.happyplaces.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import java.lang.StringBuilder
import java.util.Locale

class GetAddressFromLatLong(
    context : Context,
    private val latitude : Double,
    private val longitude : Double):
    AsyncTask<Void, String, String>() {

    // Class that takes care of creating a location from a lat long
    private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
    private lateinit var mAddressListener : AddressListener

    override fun doInBackground(vararg params: Void?): String {
        try {
            val addressList: List<Address>? =
                geocoder.getFromLocation(latitude, longitude, 1)

            if(!addressList.isNullOrEmpty()){
                val address : Address = addressList[0]
                val sb = StringBuilder()
                for(i in 0..address.maxAddressLineIndex){
                    sb.append(address.getAddressLine(i)).append(" ")
                }
                sb.deleteCharAt(sb.length - 1)
                return sb.toString()
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        return " "
    }

    override fun onPostExecute(resultString: String?){
        if(resultString == null ){
            mAddressListener.onError()
        }else{
            mAddressListener.onAddressFound(resultString)
        }
        super.onPostExecute(resultString)
    }

    fun setAddressListener(addressListener : AddressListener){
        mAddressListener = addressListener
    }

    fun getAddress(){
        execute()
    }

    interface AddressListener{
        fun onAddressFound(address: String?)
        fun onError()
    }

}