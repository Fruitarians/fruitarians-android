package com.althaaf.fruitarians.core.data.network.retrofit

import com.althaaf.fruitarians.core.data.network.authentication.request.login.LoginRequest
import com.althaaf.fruitarians.core.data.network.authentication.request.login.SentTokenRequest
import com.althaaf.fruitarians.core.data.network.authentication.request.login.VerifyPasswordRequest
import com.althaaf.fruitarians.core.data.network.authentication.request.register.RegisterRequest
import com.althaaf.fruitarians.core.data.network.authentication.response.login.LoginResponse
import com.althaaf.fruitarians.core.data.network.authentication.response.login.SentTokenResponse
import com.althaaf.fruitarians.core.data.network.authentication.response.login.VerifyPasswordResponse
import com.althaaf.fruitarians.core.data.network.authentication.response.register.RegisterResponse
import com.althaaf.fruitarians.core.data.network.cart.CartRequest
import com.althaaf.fruitarians.core.data.network.cart.CartResponse
import com.althaaf.fruitarians.core.data.network.dashboard.DataUserResponse
import com.althaaf.fruitarians.core.data.network.dashboard.article.ArticleResponse
import com.althaaf.fruitarians.core.data.network.dashboard.article.DetailArticleResponse
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.DataItem
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.FruitStoreResponse
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.detail.DetailFruitStoreResponse
import com.althaaf.fruitarians.core.data.network.dashboard.fruitstore.detailfruit.DetailSpecificFruitResponse
import com.althaaf.fruitarians.core.data.network.dashboard.fruitvariant.BuahResponse
import com.althaaf.fruitarians.core.data.network.dashboard.membership.GeneralMembershipResponse
import com.althaaf.fruitarians.core.data.network.dashboard.membership.UserIsMemberResponse
import com.althaaf.fruitarians.core.data.network.fruitscan.FruitScanResponse
import com.althaaf.fruitarians.core.data.network.profile.addproduct.AddProductResponse
import com.althaaf.fruitarians.core.data.network.profile.changepassword.ChangePasswordRequest
import com.althaaf.fruitarians.core.data.network.profile.changepassword.ChangePasswordResponse
import com.althaaf.fruitarians.core.data.network.profile.deleteproduct.DeleteProductResponse
import com.althaaf.fruitarians.core.data.network.profile.editprofile.EditProfileResponse
import com.althaaf.fruitarians.core.data.network.profile.logout.LogoutResponse
import com.althaaf.fruitarians.core.data.network.profile.mystore.MyStoreResponse
import com.althaaf.fruitarians.core.data.network.profile.myvendor.SubsVendorResponse
import com.althaaf.fruitarians.core.data.network.profile.myvendor.addsubs.AddSubsRequest
import com.althaaf.fruitarians.core.data.network.profile.myvendor.addsubs.AddSubsResponse
import com.althaaf.fruitarians.core.data.network.profile.updateproduct.UpdateProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("auth/signup")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ) : RegisterResponse

    @POST("auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ) : LoginResponse

    @POST("user/forget_password")
    suspend fun sentToken(
        @Body loginRequest: SentTokenRequest
    ) : SentTokenResponse

    @PATCH("user/forget_password")
    suspend fun verifyPassword(
        @Body loginRequest: VerifyPasswordRequest
    ) : VerifyPasswordResponse

    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String,
    ) : LogoutResponse

    @GET("user/{role}")
    suspend fun getAllListRole(
        @Header("Authorization") token: String,
        @Path("role") role:String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("card") card: Boolean
    ): FruitStoreResponse<List<DataItem>>

    @GET("user/{role}")
    suspend fun getOneListRole(
        @Header("Authorization") token: String,
        @Path("role") role:String,
        @Query("card") card: Boolean
    ): FruitStoreResponse<DataItem>

    @GET("user/{role}/{id}")
    suspend fun getDetailToko(
        @Header("Authorization") token: String,
        @Path("role") role:String,
        @Path("id") id:String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): DetailFruitStoreResponse

    @GET("user/toko/{idToko}/{idBuah}")
    suspend fun getDetailTokoSpecificBuah(
        @Header("Authorization") token: String,
        @Path("idToko") idToko:String,
        @Path("idBuah") idBuah:String,
    ): DetailSpecificFruitResponse

    @GET("user/info")
    suspend fun getAllDataUser(
        @Header("Authorization") token: String,
    ) : DataUserResponse

    @Multipart
    @PATCH("user/info")
    suspend fun editDataUser(
        @Header("Authorization") token: String,
        @Part ("name") name: RequestBody,
        @Part ("negara") negara: RequestBody,
        @Part ("kota") kota: RequestBody,
        @Part ("deskripsi_alamat") deskripsi_alamat: RequestBody,
        @Part ("telepon") telepon: RequestBody,
        @Part ("deskripsi") deskripsi: RequestBody,
        @Part ("jam_buka") jam_buka: RequestBody,
        @Part ("jam_tutup") jam_tutup: RequestBody,
        @Part ("hari_buka_awal") hari_buka_awal: RequestBody,
        @Part ("hari_buka_akhir") hari_buka_akhir: RequestBody,
        @Part file: MultipartBody.Part?
    ): EditProfileResponse

    @PATCH("user/password")
    suspend fun changePasswordUser(
        @Header("Authorization") token: String,
        @Body changePasswordRequest: ChangePasswordRequest
    ): ChangePasswordResponse

    @GET("user/toko/buah/")
    suspend fun getTokoBuah(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): MyStoreResponse

    @Multipart
    @POST("user/toko/buah/")
    suspend fun addProductBuah(
        @Header("Authorization") token: String,
        @Part ("name") name: RequestBody,
        @Part ("harga") harga: Int,
        @Part ("satuan") satuan: RequestBody,
        @Part ("deskripsi") deskripsi: RequestBody,
        @Part ("stok") stok: Int,
        @Part file: MultipartBody.Part
    ): AddProductResponse

    @Multipart
    @PATCH("user/toko/buah/")
    suspend fun updateProductBuah(
        @Header("Authorization") token: String,
        @Part ("buahId") buahId: RequestBody,
        @Part ("name") name: RequestBody,
        @Part ("harga") harga: Int,
        @Part ("satuan") satuan: RequestBody,
        @Part ("stok") stok: Int,
        @Part ("deskripsi") deskripsi: RequestBody,
        @Part file: MultipartBody.Part?
    ): UpdateProductResponse

    @DELETE("user/toko/buah/{idBuah}")
    suspend fun deleteProductBuah(
        @Header("Authorization") token: String,
        @Path("idBuah") idBuah:String,
        ): DeleteProductResponse

    @Multipart
    @POST("api/prediction/")
    suspend fun uploadScanDetection(
        @Part image_data: MultipartBody.Part
    ): FruitScanResponse

    @GET("articles/")
    suspend fun getArticles(): ArticleResponse

    @GET("articles/{id}")
    suspend fun getArticleById(
        @Path("id") id:Int,
        @Query("card") card: Boolean
    ): DetailArticleResponse

    @GET("buah/")
    suspend fun getAllBuah(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("search") search: String?,
    ): BuahResponse

    @GET("vendor/")
    suspend fun getAllSubsVendor(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): SubsVendorResponse

    @DELETE("vendor/{id_subs}")
    suspend fun deleteSubVendor(
        @Header("Authorization") token: String,
        @Path("id_subs") id_subs: String,
    ): AddSubsResponse

    @POST("vendor/")
    suspend fun addSubsVendor(
        @Header("Authorization") token: String,
        @Body addSubsRequest: AddSubsRequest
    ): AddSubsResponse

    @PATCH("vendor/{id_subs}/{delivered}")
    suspend fun changeSubStatus(
        @Header("Authorization") token: String,
        @Path("id_subs") id_subs: String,
        @Path("delivered") delivered: String,
    ): AddSubsResponse

    @GET("user/bookmark")
    suspend fun getUserMembership(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): FruitStoreResponse<List<DataItem>>

    @GET("user/bookmark/{bookmark_userId}")
    suspend fun getUserIsMember(
        @Header("Authorization") token: String,
        @Path("bookmark_userId") bookmark_userId: String,
    ): UserIsMemberResponse

    @FormUrlEncoded
    @POST("user/bookmark")
    suspend fun addUserMembership(
        @Header("Authorization") token: String,
        @Field("bookmark_userId") bookmark_userId: String
    ): GeneralMembershipResponse

    @DELETE("user/bookmark/{delete_bookmark_userId}")
    suspend fun deleteUserMembership(
        @Header("Authorization") token: String,
        @Path("delete_bookmark_userId") delete_bookmark_userId: String,
    ): GeneralMembershipResponse

    @GET("user/carts")
    suspend fun getCartUser(
        @Header("Authorization") token: String,
    ): CartResponse

    @POST("user/carts")
    suspend fun addtoCart(
        @Header("Authorization") token: String,
        @Body cartRequest: CartRequest
    ): GeneralMembershipResponse

    @DELETE("user/carts/{id_cart}/{id_buah}")
    suspend fun deleteFruitCart(
        @Header("Authorization") token: String,
        @Path("id_cart") id_cart: String,
        @Path("id_buah") id_buah: String,
    ): GeneralMembershipResponse

    @DELETE("user/carts/{id_cart}")
    suspend fun deleteStoreCart(
        @Header("Authorization") token: String,
        @Path("id_cart") id_cart: String,
    ) : GeneralMembershipResponse

}