package upvictoria.pm_may_ago_2025.iti_271415.pg1u3_eq05;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;

public class ModelSelectionMenu extends AppCompatActivity {
    private GridView gridView;
    private final LinkedHashMap<String, String> modelList = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_selector);
        gridView = findViewById(R.id.gridView);

        modelList.put("Batman Mask", "batman_mask");
        modelList.put("Bowser Head", "bowser_head");
        modelList.put("Cabeza Jaguar", "cabeza_jaguar");
        modelList.put("Cubone Skull", "cubone_skull");
        modelList.put("Doge Head", "doge");
        modelList.put("Freddy Head", "freddy_head");
        modelList.put("Geisha Mask", "geisha_mask");
        modelList.put("Ghost Mask (COD)", "ghost_mask");
        modelList.put("Jack in the Box", "jack_in_the_box");
        modelList.put("JoJo Ishikamen Stone Mask", "jojo_ishikamen_stone_mask");
        modelList.put("Joker Mask", "joker_mask");
        modelList.put("Skull Kid Mask", "majoras_mask");
        modelList.put("Mario Head", "mario_mario_head");
        modelList.put("Master Chief Helmet", "master_chief");
        modelList.put("Minecraft Diamond Helmet", "mc_diamond_helmet");
        modelList.put("Medieval Helmet", "medieval_helmet");
        modelList.put("Menpo Mask", "menpo_mask");
        modelList.put("Nordic Helmet", "nordic_helmet");
        modelList.put("Paperbag Mask", "paperbag_mask");
        modelList.put("PUBG Helmet", "pubg_helmet");
        modelList.put("Robocop Helmet", "robocop_helmet");
        modelList.put("Samurai Staw Hat", "samurai_hat");
        modelList.put("Skull Mask", "skull_mask");
        modelList.put("Vintage Helmet", "vintage_helmet");

        ModelMapAdapter adapter = new ModelMapAdapter(this, modelList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            String modelName = modelList.keySet().toArray(new String[0])[position];
            String modelFileName = modelList.get(modelName);

            Intent intent = new Intent();
            intent.putExtra("modelFileName", modelFileName);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
