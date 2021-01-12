package id.co.arya.posq.local

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import id.co.arya.posq.data.model.Cart
import id.co.arya.posq.data.response.LoginResponse
import id.co.arya.posq.utils.Constant
import javax.inject.Inject


class SharedPreferenceManagerNoHilt constructor(val context: Context) {

    fun saveUserData(loginResponse: LoginResponse) {
        val sharedPreferences =
            context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val edit = sharedPreferences?.edit()
        edit?.putString(Constant.UID, loginResponse.data.us_id)
        edit?.putString(Constant.USERNAME, loginResponse.data.us_username)
        edit?.putString(Constant.PASSWORD, loginResponse.data.us_password)
        edit?.putString(Constant.NAMA, loginResponse.data.us_nama)
        edit?.putString(Constant.ALAMAT, loginResponse.data.us_alamat)
        edit?.putString(Constant.KOTA, loginResponse.data.us_kota)
        edit?.putString(Constant.PHOTO, loginResponse.data.us_photo)
        edit?.putString(Constant.HP, loginResponse.data.us_hp)
        edit?.putString(Constant.KET, loginResponse.data.us_keterangan)
        edit?.putString(Constant.EMAIL, loginResponse.data.us_email)
        edit?.putString(Constant.AKTIF, loginResponse.data.us_aktif)
        edit?.putString(Constant.CREATEDBY, loginResponse.data.us_create_by)
        edit?.putString(Constant.CREATED, loginResponse.data.us_created)
        edit?.putString(Constant.MODIFIED, loginResponse.data.us_modified)
        edit?.apply()
    }

    fun loadUserData(): LoginResponse.Data {
        val sharedPreferences =
            context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val loginResponse = LoginResponse.Data(
            sharedPreferences.getString(Constant.UID, "").toString(),
            sharedPreferences.getString(Constant.USERNAME, "").toString(),
            sharedPreferences.getString(Constant.PASSWORD, "").toString(),
            sharedPreferences.getString(Constant.NAMA, "").toString(),
            sharedPreferences.getString(Constant.ALAMAT, "").toString(),
            sharedPreferences.getString(Constant.KOTA, "").toString(),
            sharedPreferences.getString(Constant.PHOTO, "").toString(),
            sharedPreferences.getString(Constant.HP, "").toString(),
            sharedPreferences.getString(Constant.KET, "").toString(),
            sharedPreferences.getString(Constant.EMAIL, "").toString(),
            sharedPreferences.getString(Constant.AKTIF, "").toString(),
            sharedPreferences.getString(Constant.CREATEDBY, "").toString(),
            sharedPreferences.getString(Constant.CREATED, "").toString(),
            sharedPreferences.getString(Constant.MODIFIED, "").toString()
        )
        return loginResponse
    }

    fun saveProductCart(listCart: ArrayList<Cart>) {
        val sharedPreferences =
            context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(listCart)
        editor.putString(Constant.PRODUCT_CART, json)
        editor.apply()
    }

    fun getProductCart(): ArrayList<Cart> {
        val sharedPreferences =
            context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(Constant.PRODUCT_CART, null)
        val type = object : TypeToken<ArrayList<Cart?>?>() {}.type
        return gson.fromJson(json, type)
    }
    
    fun saveAddedCart(listCart: ArrayList<Cart>) {
        val sharedPreferences =
            context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(listCart)
        editor.putString(Constant.ADDED_CART, json)
        editor.apply()
    }

    fun getAddedCart(): ArrayList<Cart> {
        val sharedPreferences =
            context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(Constant.ADDED_CART, null)
        val type = object : TypeToken<ArrayList<Cart?>?>() {}.type
        return gson.fromJson(json, type)
    }
    

}


//    resource.data?.data[index].pr_id,
//    resource.data?.data[index].pr_kode,
//    resource.data?.data[index].pr_nama,
//    resource.data?.data[index].pr_qty,
//    resource.data?.data[index].pr_tpel_kode,
//    resource.data?.data[index].pr_image,
//    resource.data?.data[index].pr_st_kode,
//    resource.data?.data[index].pr_us_id,
//    resource.data?.data[index].pr_harga,
//    resource.data?.data[index].pr_keterangan,
//    resource.data?.data[index].pr_created,
//    resource.data?.data[index].pr_modified,

//    fun saveProductCart(listCart: ArrayList<Cart>, position: Int) {
//        val sharedPreferences =
//            context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        val edit = sharedPreferences.edit()
//        edit?.putString("${listCart[position].pr_kode}pr_id", listCart[position].pr_id)
//        edit?.putString("${listCart[position].pr_kode}pr_kode", listCart[position].pr_kode)
//        edit?.putString("${listCart[position].pr_kode}pr_nama", listCart[position].pr_nama)
//        edit?.putString("${listCart[position].pr_kode}pr_qty", listCart[position].pr_qty)
//        edit?.putString(
//            "${listCart[position].pr_kode}pr_tpel_kode",
//            listCart[position].pr_tpel_kode
//        )
//        edit?.putString("${listCart[position].pr_kode}pr_image", listCart[position].pr_image)
//        edit?.putString("${listCart[position].pr_kode}pr_st_kode", listCart[position].pr_st_kode)
//        edit?.putString("${listCart[position].pr_kode}pr_us_id", listCart[position].pr_us_id)
//        edit?.putString("${listCart[position].pr_kode}pr_harga", listCart[position].pr_harga)
//        edit?.putString(
//            "${listCart[position].pr_kode}pr_keterangan",
//            listCart[position].pr_keterangan
//        )
//        edit?.putString("${listCart[position].pr_kode}pr_created", listCart[position].pr_created)
//        edit?.putString("${listCart[position].pr_kode}pr_modified", listCart[position].pr_modified)
//        edit?.putInt("${listCart[position].pr_kode}total", listCart[position].total)
//        edit?.apply()
//    }