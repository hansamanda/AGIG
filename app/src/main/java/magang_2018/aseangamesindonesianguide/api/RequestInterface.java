package magang_2018.aseangamesindonesianguide.api;

import retrofit2.Call;
import retrofit2.http.GET;
import magang_2018.aseangamesindonesianguide.network.JSONResponse;

/**
 * Created by Hans on 8/16/2018.
 */

public interface RequestInterface {

    @GET("Api/Medals")
    Call<JSONResponse> getJSON();

    @GET("Api/Medals/Top/5")
    Call<JSONResponse> getJSON5();
}
