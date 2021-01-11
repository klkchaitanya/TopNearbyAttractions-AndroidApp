package siri.project.topnearby;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.ImageViewHolder> {


    Context context;
    List<Place_Id_Name_and_Image> list_images;
    int list_images_size;
    ListItemClickListener itemClickListener;

    public interface ListItemClickListener
    {
        void onItemClick(int position);
    }

    public PlaceListAdapter(Context context, List<Place_Id_Name_and_Image> list_images, int list_images_size, ListItemClickListener itemClickListener)
    {
        this.context = context;
        this.list_images = list_images;
        this.list_images_size = list_images_size;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_item,parent,false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list_images_size;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        ImageView imgViewPlace;
        TextView tvPlaceTile;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imgViewPlace = (ImageView)itemView.findViewById(R.id.recycler_item_imageViewPlace);
            tvPlaceTile = (TextView)itemView.findViewById(R.id.recycler_item_tvPlaceName);

            itemView.setOnClickListener(this);
        }

        void bind(int index)
        {
            tvPlaceTile.setText(list_images.get(index).placeTitle);

            if(list_images.get(index).placeImage!=null)
                Picasso.with(context).load(list_images.get(index).placeImage).placeholder(R.drawable.brokenimage).into(imgViewPlace);

           /* Log.d("Binding",list_images.get(index).placeTitle.toString() +"\n"
            +list_images.get(index).placeId.toString()+"\n"
            +list_images.get(index).placeImage.toString());*/
        }


        @Override
        public void onClick(View v) {
            Log.d("PlaceListAdapter", "Onclick");
            itemClickListener.onItemClick(getAdapterPosition());
        }
    }

}