package br.com.i9algo.autaz.pdv.data.remote.service;


import br.com.i9algo.autaz.pdv.data.remote.ResultDefault;
import br.com.i9algo.autaz.pdv.domain.constants.AuthAttr;
import br.com.i9algo.autaz.pdv.domain.models.inbound.ProductWrapper;
import br.com.i9algo.autaz.pdv.domain.models.inbound.ProductsArraylistWrapper;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by aStraube on 30/06/2017.
 */

public interface ApiService {

    //@FormUrlEncoded
    //@POST("media/{id}/sidebar/reserve")
    //Observable<Object> postReserve(@Path("id") String token, @Body ReserveActionModel reserve);

    @POST("authorization")
    @FormUrlEncoded
    Observable<ResultDefault> authorization(@Field(AuthAttr.USERNAME) String username, @Field(AuthAttr.PASSWORD) String password);

    @POST("authorization/validate")
    @FormUrlEncoded
    Observable<ResultDefault> authorizationValidate(@Field(AuthAttr.USER_API_TOKEN) String api_token, @Field(AuthAttr.PUBLIC_TOKEN) String String);

    ///api/v1/stock?api_token=fc8425fdf0350941c9bff17a4b4e42bbaf45189df25b1142000c47f9e4752663&public_token=19dc8dd3cfe1cf9a08624a74c2de19f2
    @GET("stock")
    Observable<ProductsArraylistWrapper> getProducts(@Query(AuthAttr.USER_API_TOKEN) String api_token, @Query(AuthAttr.PUBLIC_TOKEN) String public_token);

    ///api/v1/stock/item/111?api_token=fc8425fdf0350941c9bff17a4b4e42bbaf45189df25b1142000c47f9e4752663&public_token=19dc8dd3cfe1cf9a08624a74c2de19f2
    @GET("stock/item/{id}")
    Observable<ProductWrapper> getProductItem(@Query(AuthAttr.USER_API_TOKEN) String api_token, @Query(AuthAttr.PUBLIC_TOKEN) String public_token, @Path("id") String token);
}
