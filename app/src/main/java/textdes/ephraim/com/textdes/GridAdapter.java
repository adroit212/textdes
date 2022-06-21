package textdes.ephraim.com.textdes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private final String[] labels;
    private final int[] images;

    public GridAdapter(Context c, String[] labels, int[] Images ) {
        mContext = c;
        this.images = Images;
        this.labels = labels;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return labels.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_layout, null);
            TextView textView = (TextView) grid.findViewById(R.id.ma_tv_grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.ma_iv_grid_image);
            textView.setText(labels[position]);
            imageView.setImageResource(images[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
