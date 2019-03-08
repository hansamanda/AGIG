package magang_2018.aseangamesindonesianguide.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RequestScheduleInterface {

    @GET("Api/Schedules/getByDate/{time}")
    Call<magang_2018.aseangamesindonesianguide.response2.JSONResponse> getJSON(@Path("time") String time);


}
