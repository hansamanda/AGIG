package magang_2018.aseangamesindonesianguide.api;

import magang_2018.aseangamesindonesianguide.response.JSONResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Hans on 8/16/2018.
 */

public interface RequestEventInterface {

    @GET("Api/Events/Event/")
    Call<JSONResponse> getJSON();

    @GET("Api/Events/getByDate/{time}")
    Call<JSONResponse> getJSON2(@Path("time") String time);
}
