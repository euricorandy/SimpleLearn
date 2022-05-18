package umn.ac.simplelearn;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductIndoAdapter extends RecyclerView.Adapter<ProductViewHolder>{

    private Context mtx;
    private List<ProductIndo> productIndoList;

    public ProductIndoAdapter(Context mtx, List<ProductIndo> productIndoList) {
        this.mtx = mtx;
        this.productIndoList = productIndoList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mtx);
        View view = inflater.inflate(R.layout.layout_products1, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {

        final ProductIndo productIndo = productIndoList.get(position);

        holder.textViewTitle.setText(productIndo.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PdfIndo.class);
                i.putExtra("title",productIndoList.get(position).getTitle());
                i.putExtra("product",productIndoList.get(position).getTitle());
                i.putExtra("link",productIndoList.get(position).getLink());
                mtx.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productIndoList.size();
    }
}

class ProductViewHolder extends RecyclerView.ViewHolder{

    CardView cardView;
    TextView textViewTitle;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.indoCardView);
        textViewTitle = itemView.findViewById(R.id.indoTextViewTitle);
    }
}
