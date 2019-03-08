package magang_2018.aseangamesindonesianguide.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.model.Schedule;

public class ScheduleAdapter  extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{

    private ArrayList<Schedule> data;

    public ScheduleAdapter (ArrayList<Schedule> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_schedule, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvSport.setText(data.get(i).getSports());
        viewHolder.Date.setText(data.get(i).getTime());
        viewHolder.tvNameSchedule.setText(data.get(i).getName());
        viewHolder.ScheduleDes.setText(data.get(i).getDescription());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvSport,Date,tvNameSchedule,ScheduleDes;
        public ViewHolder(View view) {
            super(view);

            tvSport = (TextView)view.findViewById(R.id.tvSport);
            Date = (TextView)view.findViewById(R.id.Date);
            tvNameSchedule = (TextView) view.findViewById(R.id.tvNameSchedule);
            ScheduleDes = (TextView) view.findViewById(R.id.ScheduleDes);
        }
    }
}
