package sastra.panji.dhimas.proggeres.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import sastra.panji.dhimas.proggeres.Model.Kandang;
import sastra.panji.dhimas.proggeres.R;

public class ListKandangAdapter extends RecyclerView.Adapter<ListKandangAdapter.ListViewHolder> {
    private ArrayList<Kandang> listKandang;
    private OnItemClickCallback onItemClickCallback;

    public ListKandangAdapter(ArrayList<Kandang> list) {


        this.listKandang = list;


        notifyDataSetChanged();


    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListKandangAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kandang, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListKandangAdapter.ListViewHolder holder, int position) {
        Kandang kandang = listKandang.get(position);
        holder.namaKandang.setText(kandang.getName());
        Glide.with(holder.itemView.getContext())
                .load("http://ta.poliwangi.ac.id/~ti17183/laravel/public/" + kandang.getFoto())
                .apply(new RequestOptions().override(500, 500))
                .into(holder.foto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listKandang.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listKandang.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Kandang kandang);
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView namaKandang;
        CircleImageView foto;


        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.foto_candang);
            namaKandang = itemView.findViewById(R.id.list_kandang_nama);

        }
    }

}
