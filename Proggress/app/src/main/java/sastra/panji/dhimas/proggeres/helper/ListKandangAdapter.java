package sastra.panji.dhimas.proggeres.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sastra.panji.dhimas.proggeres.Model.Kandang;
import sastra.panji.dhimas.proggeres.R;

public class ListKandangAdapter extends RecyclerView.Adapter<ListKandangAdapter.ListViewHolder> {
    private ArrayList<Kandang> listKandang;

    public ListKandangAdapter(ArrayList<Kandang> list) {


        this.listKandang = list;


        notifyDataSetChanged();


    }

    @NonNull
    @Override
    public ListKandangAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kandang, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListKandangAdapter.ListViewHolder holder, int position) {
        Kandang kandang = listKandang.get(position);
        holder.namaKandang.setText(kandang.getName());
    }

    @Override
    public int getItemCount() {
        return listKandang.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView namaKandang;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            namaKandang = itemView.findViewById(R.id.list_kandang_nama);
        }
    }
}
