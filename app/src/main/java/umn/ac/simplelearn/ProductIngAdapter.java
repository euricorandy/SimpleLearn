package umn.ac.simplelearn;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductIngAdapter extends RecyclerView.Adapter<ProductIngViewHolder>{

    private Context mtx;
    private List<ProductIng> productIngList;

    public ProductIngAdapter(Context mtx, List<ProductIng> productIngList) {
        this.mtx = mtx;
        this.productIngList = productIngList;
    }

    @NonNull
    @Override
    public ProductIngViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mtx);
        View view = inflater.inflate(R.layout.layout_products1, null);
        return new ProductIngViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductIngViewHolder holder, final int position) {

        final ProductIng productIng = productIngList.get(position);

        holder.textViewTitle.setText(productIng.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PdfIng.class);
                i.putExtra("title",productIngList.get(position).getTitle());
                i.putExtra("product",productIngList.get(position).getTitle());
                i.putExtra("link",productIngList.get(position).getLink());
                mtx.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productIngList.size();
    }
}

class ProductIngViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    TextView textViewTitle;

    public ProductIngViewHolder(@NonNull View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.indoCardView);
        textViewTitle = itemView.findViewById(R.id.indoTextViewTitle);
    }
}
