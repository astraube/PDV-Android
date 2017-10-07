package br.com.i9algo.autaz.pdv.data.remote.service;


import br.com.i9algo.autaz.pdv.domain.constants.AuthAttr;
import br.com.i9algo.autaz.pdv.domain.models.Device;
import br.com.i9algo.autaz.pdv.domain.models.inbound.CorporateWrapper;
import br.com.i9algo.autaz.pdv.domain.models.inbound.DeviceWrapper;
import br.com.i9algo.autaz.pdv.domain.models.inbound.ProductWrapper;
import br.com.i9algo.autaz.pdv.domain.models.inbound.ProductsArraylistWrapper;
import br.com.i9algo.autaz.pdv.domain.models.inbound.ResultDefault;
import br.com.i9algo.autaz.pdv.domain.models.inbound.UserWrapper;
import br.com.i9algo.autaz.pdv.domain.models.outbound.SaleApi;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * https://guides.codepath.com/android/Consuming-APIs-with-Retrofit
 */
public interface ApiService {

    //@FormUrlEncoded
    //@POST("media/{id}/sidebar/reserve")
    //Observable<ResultDefault> postReserve(@Path("id") String token, @Body ReserveActionModel reserve);

    //@Headers({"Cache-Control: max-age=640000", "User-Agent: My-App-Name"})
    //@GET("/some/endpoint")

    /**
     * Auth
     */
    @POST("authorization")
    @FormUrlEncoded
    Observable<UserWrapper> authorization(
            @Field(AuthAttr.USERNAME) String username,
            @Field(AuthAttr.PASSWORD) String password);

    @POST("authorization/validate")
    @FormUrlEncoded
    Observable<UserWrapper> authorizationValidate(
            @Field(AuthAttr.USER_API_TOKEN) String api_token,
            @Field(AuthAttr.USER_PUBLIC_TOKEN) String String);


    /**
     * User
     */
    @GET("users/self")
    Observable<ResultDefault> getUserSelfProfile(
            @Query(AuthAttr.USER_API_TOKEN) String api_token,
            @Query(AuthAttr.USER_PUBLIC_TOKEN) String String);


    /**
     * Corporate
     */
    @GET("corporate")
    Observable<CorporateWrapper> getCorporateAll(
            @Query(AuthAttr.USER_API_TOKEN) String api_token,
            @Query(AuthAttr.USER_PUBLIC_TOKEN) String String);


    /**
     * Device
     */
    @GET("device/by-token/{token}")
    Observable<DeviceWrapper> getByToken(
            @Query(AuthAttr.USER_API_TOKEN) String api_token,
            @Query(AuthAttr.USER_PUBLIC_TOKEN) String USER_PUBLIC_TOKEN,
            @Path("token") String token);

    @POST("device/store")
    Observable<DeviceWrapper> storeDevice(
            @Query(AuthAttr.USER_API_TOKEN) String api_token,
            @Query(AuthAttr.USER_PUBLIC_TOKEN) String USER_PUBLIC_TOKEN,
            @Body Device device);


    /**
     * Stock
     */
    ///api/v1/stock?api_token=fc8425fdf0350941c9bff17a4b4e42bbaf45189df25b1142000c47f9e4752663&user_public_token=19dc8dd3cfe1cf9a08624a74c2de19f2
    @POST("stock")
    @FormUrlEncoded
    Observable<ProductsArraylistWrapper> getProducts(
            @Query(AuthAttr.USER_API_TOKEN) String api_token,
            @Query(AuthAttr.USER_PUBLIC_TOKEN) String USER_PUBLIC_TOKEN,
            @Field("lastUpdateAt") String lastUpdateAt);

    ///api/v1/stock/item/111?api_token=fc8425fdf0350941c9bff17a4b4e42bbaf45189df25b1142000c47f9e4752663&user_public_token=19dc8dd3cfe1cf9a08624a74c2de19f2
    @GET("stock/item/{id}")
    Observable<ProductWrapper> getProductItem(
            @Query(AuthAttr.USER_API_TOKEN) String api_token,
            @Query(AuthAttr.USER_PUBLIC_TOKEN) String USER_PUBLIC_TOKEN,
            @Path("id") String token);


    /**
     * Sale
     */
    @PUT("sales/store")
    Observable<ResultDefault> storeSale(
            @Query(AuthAttr.USER_API_TOKEN) String api_token,
            @Query(AuthAttr.USER_PUBLIC_TOKEN) String USER_PUBLIC_TOKEN,
            @Body SaleApi sale);
}
