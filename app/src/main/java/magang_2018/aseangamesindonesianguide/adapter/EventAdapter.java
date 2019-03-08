package magang_2018.aseangamesindonesianguide.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.model.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private ArrayList<Event> data;

    public EventAdapter (ArrayList<Event> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvEventName.setText(data.get(i).getName());
        viewHolder.tvDateSchedule.setText(data.get(i).getTime());
        viewHolder.tvEventDescription.setText(data.get(i).getDescription());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvEventName,tvDateSchedule,tvEventDescription;
        public ViewHolder(View view) {
            super(view);

            tvEventName = (TextView)view.findViewById(R.id.tvEventName);
            tvDateSchedule = (TextView)view.findViewById(R.id.tvDateSchedule);
            tvEventDescription = (TextView) view.findViewById(R.id.tvEventDescription);

        }
    }
}
