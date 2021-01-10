package id.co.arya.posq.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import id.co.arya.posq.data.response.LoginResponse
import id.co.arya.posq.utils.Constant
import javax.inject.Inject

class SharedPreferenceManager
@Inject
constructor(@ApplicationContext val context: Context)
{

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


}