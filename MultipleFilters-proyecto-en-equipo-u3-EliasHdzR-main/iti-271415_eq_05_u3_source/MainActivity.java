package upvictoria.pm_may_ago_2025.iti_271415.pg1u3_eq05;

/* Comentario Agreagado por Marco Nuno:
** Se requiere crear la carpeta assets dentro de main. Dentro crear dos carpetas (models y textures). Dentro de models, pegar el archivo jaguar_upv.glb y dentro de textures el archivo freckles.png
* De hecho el proyecto original es el sig. enlace
* https://github.com/SceneView/sceneform-android/tree/master/samples/augmented-faces
* aportacion fundamental de la generacion anterior: La cabeza del Jaguar
 */

// Importaciones de librerías necesarias
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

// Importaciones del SDK de ARCore y Sceneform
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.ar.core.AugmentedFace;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Sceneform;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.RenderableInstance;
import com.google.ar.sceneform.ux.ArFrontFacingFragment;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

// Clase principal de la aplicación
public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;

    // Variables para gestionar la carga asíncrona de modelos y texturas
    private final Set<CompletableFuture<?>> loaders = new HashSet<>();

    // Fragmento y vista para AR
    private ArFrontFacingFragment arFragment;
    private ArSceneView arSceneView;

    // Textura y modelo para el rostro aumentado
    private ModelRenderable faceModel;

    // Mapa para rastrear los nodos de rostro aumentado
    private final HashMap<AugmentedFace, AugmentedFaceNode> facesNodes = new HashMap<>();

    // Boton para cambiar de actividad
    private FloatingActionButton btnChangeModel;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 69;

    // modelo seleccionado
    private String selectedModel = "cabeza_jaguar";

    // Métod que se llama al crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configura el layout de la actividad
        setContentView(R.layout.activity_main);
        btnChangeModel = findViewById(R.id.choose_model_button);
        btnChangeModel.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ModelSelectionMenu.class);
            startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
        });

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.halo_theme_song);

        // Agrega un oyente para el manejo de fragmentos
        getSupportFragmentManager().addFragmentOnAttachListener(this::onAttachFragment);

        // Inicializa el fragmento AR si es necesario
        if (savedInstanceState == null) {
            if (Sceneform.isSupported(this)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.arFragment, ArFrontFacingFragment.class, null)
                        .commit();
            }
        }

        // Carga los modelos
        loadModels();
    }

    // Maneja el evento de adjuntar un fragmento
    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        if (fragment.getId() == R.id.arFragment) {
            arFragment = (ArFrontFacingFragment) fragment;
            arFragment.setOnViewCreatedListener(this::onViewCreated);
        }
    }

    // Configura la vista AR cuando se crea
    public void onViewCreated(ArSceneView arSceneView) {
        this.arSceneView = arSceneView;

        // Establece la prioridad de renderizado para el flujo de la cámara
        arSceneView.setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        // Configura un oyente para actualizaciones de rostros aumentados
        arFragment.setOnAugmentedFaceUpdateListener(this::onAugmentedFaceTrackingUpdate);
    }

    // Limpia los recursos al destruir la actividad
    @Override
    protected void onDestroy() {
        super.onDestroy();
    
        // Cancela operaciones asíncronas pendientes
        for (CompletableFuture<?> loader : loaders) {
            if (!loader.isDone()) {
                loader.cancel(true);
            } 
        }
    }

    // Carga los modelos 3D de manera asíncrona
    private void loadModels() {
        if (Objects.equals(selectedModel, "master_chief")) {
            if (!mediaPlayer.isPlaying()) {
                try {
                    mediaPlayer.reset();
                    Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.halo_theme_song);
                    mediaPlayer.setDataSource(getApplicationContext(), mediaPath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }

        loaders.add(ModelRenderable.builder()
                .setSource(this, Uri.parse("models/"+ selectedModel + ".glb"))
                .setIsFilamentGltf(true)
                .build()
                .thenAccept(model -> {
                    faceModel = model;

                    if (arSceneView != null) {
                        for (AugmentedFaceNode node : facesNodes.values()) {
                            RenderableInstance modelInstance = node.setFaceRegionsRenderable(faceModel);
                            modelInstance.setShadowCaster(false);
                            modelInstance.setShadowReceiver(true);
                        }
                    }
                })
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load renderable", Toast.LENGTH_LONG).show();
                    return null;
                }));
    }

    // Gestiona las actualizaciones en el seguimiento de rostros aumentados
    public void onAugmentedFaceTrackingUpdate(AugmentedFace augmentedFace) {
        // Retorna si los modelos o texturas no están cargados
        if (faceModel == null) return;

        // Obtiene el nodo de cara aumentada existente
        AugmentedFaceNode existingFaceNode = facesNodes.get(augmentedFace);

        // Gestiona el estado de seguimiento del rostro
        switch (augmentedFace.getTrackingState()) {
            case TRACKING:
                // Si no existe un nodo, lo crea y lo añade a la escena
                if (existingFaceNode == null) {
                    AugmentedFaceNode faceNode = new AugmentedFaceNode(augmentedFace);

                    RenderableInstance modelInstance = faceNode.setFaceRegionsRenderable(faceModel);
                    modelInstance.setShadowCaster(false);
                    modelInstance.setShadowReceiver(true);

                    arSceneView.getScene().addChild(faceNode);
                    facesNodes.put(augmentedFace, faceNode);
                }
                break;
            case STOPPED:
                // Elimina el nodo si el seguimiento se detiene
                if (existingFaceNode != null) {
                    arSceneView.getScene().removeChild(existingFaceNode);
                }
                facesNodes.remove(augmentedFace);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();

                selectedModel = extras.getString("modelFileName");
                loadModels();
            }
        }
    }
}
