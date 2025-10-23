    package upvictoria.pm_may_ago_2025.iti_271415.pg1u3_eq05;

    import android.content.Context;
    import android.graphics.drawable.Drawable;
    import android.net.Uri;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.ar.sceneform.Node;
    import com.google.ar.sceneform.SceneView;
    import com.google.ar.sceneform.math.Vector3;
    import com.google.ar.sceneform.rendering.ModelRenderable;

    import java.util.LinkedHashMap;

    public class ModelMapAdapter extends BaseAdapter {
        private final Context context;
        private final LinkedHashMap<String, String> mData;
        private final String[] mKeys;

        public ModelMapAdapter(Context context, LinkedHashMap<String, String> data){
            this.context = context;
            this.mData = data;
            this.mKeys = mData.keySet().toArray(new String[data.size()]);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(mKeys[position]);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent) {
            if(view == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.model_item, null);
            }

            ImageView imageView = view.findViewById(R.id.imageView);
            TextView textView = view.findViewById(R.id.textView);

            String modelName = mKeys[pos];
            textView.setText(modelName);

            String drawableName = mData.get(modelName);
            int imageResourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
            imageView.setImageResource(imageResourceId);

            return view;
        }
    }