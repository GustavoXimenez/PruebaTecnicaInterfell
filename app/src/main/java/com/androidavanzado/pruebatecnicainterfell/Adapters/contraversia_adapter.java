package com.androidavanzado.pruebatecnicainterfell.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidavanzado.pruebatecnicainterfell.Models.Contraversia;
import com.androidavanzado.pruebatecnicainterfell.R;

import java.util.List;

public class contraversia_adapter extends RecyclerView.Adapter<contraversia_adapter.ViewHolder> {

    private Context context;
    private List<Contraversia> contraversias;

    public contraversia_adapter(Context context, List<Contraversia> contraversias) {
        this.context = context;
        this.contraversias = contraversias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.holder_contraversia, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Contraversia item = contraversias.get(position);

        TextView textViewId = viewHolder.textViewId;
        TextView textViewPlate = viewHolder.textViewPlate;
        TextView textViewDate = viewHolder.textViewDate;
        TextView textViewHour = viewHolder.textViewHour;

        int id = item.getId();

        textViewId.setText(String.valueOf(id));
        textViewPlate.setText(item.getPlate());
        textViewDate.setText(item.getDate());
        textViewHour.setText(item.getHour());
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder){
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder viewHolder){
        super.onViewAttachedToWindow(viewHolder);
        animateCircularReveal(viewHolder.itemView);
    }

    public void animateCircularReveal(View view){
        int centerX = 0;
        int centerY = 0;
        int startRadius = 0;
        int endRadius = Math.max(view.getWidth(), view.getHeight());
        //Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
        //view.setVisibility(View.VISIBLE);
        //animation.start();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemCount() {
        if(contraversias.isEmpty()){
            return 0;
        } else {
            return contraversias.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewId;
        public TextView textViewPlate;
        public TextView textViewDate;
        public TextView textViewHour;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            textViewId = (TextView) itemView.findViewById(R.id.textViewId);
            textViewPlate = (TextView) itemView.findViewById(R.id.textViewPlate);
            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            textViewHour = (TextView) itemView.findViewById(R.id.textViewHour);
        }
    }
}
