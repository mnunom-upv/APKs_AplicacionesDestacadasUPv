package com.z_iti_271304_u3_e05

import android.os.Bundle
import android.view.Choreographer
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.filament.Camera
import com.google.android.filament.Engine
import com.google.android.filament.Skybox
import com.google.android.filament.utils.Float4
import com.google.android.filament.utils.KtxLoader
import com.google.android.filament.utils.Mat4
import com.google.android.filament.utils.Utils
import com.google.android.material.navigation.NavigationView
import com.google.android.material.slider.Slider
import java.nio.ByteBuffer
import java.util.Locale

class MainActivity : AppCompatActivity() {

    /*
    LOS MODELOS (el directorio "assets") van en:
        /app/src/main
     */

    // Iniciar Filament
    // Esto cambia en versiones más recientes
    companion object {
        init { Utils.init() }
    }
    //boton para centrar
    private lateinit var btnCenter: Button

    private lateinit var surfaceView: SurfaceView
    private lateinit var choreographer: Choreographer
    //private lateinit var modelViewer: ModelViewer
    private lateinit var modelViewer: MyModelViewer

    //Elementos visuales
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var lytAnimation: RelativeLayout
    private lateinit var lytSlider: LinearLayout
    private lateinit var radioGroup: RadioGroup
    private lateinit var slider: Slider

    private var modelName = "Vampire"       //Nombre del modelo en uso
    private var normalizedTime = 0.1f       //Tiempo normalizado de la animación
    private var animationIndex = 0          //Indice de la animación
    private var animationNumber = 0         //Número de animaciones del modelo
    private var animationDuration = 8.0f    //Duración de la animación (8 por defecto, pero varía por animación)
    private var scaleFactor = 1.0f          //Valor de escalada del modelo

    private lateinit var engine: Engine
    private lateinit var myCamera: Camera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar elementos
        surfaceView = findViewById(R.id.surfaceView)
        choreographer = Choreographer.getInstance()
        modelViewer = MyModelViewer(surfaceView)

        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        slider = findViewById(R.id.slider)
        lytAnimation = findViewById(R.id.lytAnimation)
        lytSlider = findViewById(R.id.layoutSlider)
        radioGroup = findViewById(R.id.radioGpAnimation)
        drawerLayout = findViewById(R.id.drawer_layout)
        btnCenter = findViewById(R.id.btnCenter)

        // Habilitar el movimiento de la "cámara" en el surfaceView
        surfaceView.setOnTouchListener(modelViewer)

        modelViewer.scene.skybox = Skybox.Builder().build(modelViewer.engine)

        // Configuración el Toolbar (Barra lateral (menu))
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Toggle del menu: Abrir y cerrar
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Al seleccionar la opción se cargan los modelos
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.our_team -> showAboutOurTeam()
                R.id.model1 -> loadModel("Vampire", 1.0f)
                R.id.model2 -> loadModel("Skeleton", 2.0f)
                R.id.model3 -> loadModel("Butterfly", 3.25f)
                R.id.model4 -> loadModel("Raton", 1.25f)
                R.id.model5 -> loadModel("Jaguar", 5.0f)
                R.id.model5a -> loadModel("Jaguar_full", 2.50f)
                R.id.model6 -> loadModel("Spider", 0.75f)
                R.id.modelW -> loadModel("Wasp", 0.75f)
                R.id.model7 -> loadModel("Mammoth", 0.60f)
                R.id.model8 -> loadModel("SabreTooth", 1.75f)
                R.id.model9 -> loadModel("Velociraptor", 0.50f)
                R.id.model10 -> loadModel("Trex", 0.20f)
                R.id.model11 -> loadModel("Triceratops", 0.30f)
                R.id.model12 -> loadModel("Ankylosaurus", 1.25f)
                R.id.model13 -> loadModel("Apatosaurus", 0.15f)
                R.id.model14 -> loadModel("Parasaurolophus", 0.50f)
                R.id.model15 -> loadModel("Stegosaurus", 0.35f)
                R.id.model16 -> loadModel("BusterDrone", 0.02f)
                R.id.model17 -> loadModel("Breakdance", 2.0f)
                R.id.menu_exit -> finish()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Cargar modelo y animar "primer frame" solo por primera vez
        loadModel("Vampire", scaleFactor)

        // Cargar ambiente (fondo)
        loadEnvironment("hall_of_mammals_2k")

        // Slider que anima el modelo al cambiar el valor
        slider.addOnChangeListener { _, value, _ ->
            // Ajusta la animación al progreso del slider
            // Se divide el tiempo que dura la animación entre 100
            // y al actualizar ese "frame" se renderiza ese momento
            normalizedTime = value / 100f * animationDuration

            // Animar frames
            animateOnTime()

            // Renderizar escena
            modelViewer.render(System.nanoTime())
        }

        // Modificar el formato de impresión del slider
        slider.setLabelFormatter {
            String.format(Locale.US, "%.1f segundos", normalizedTime)
        }

        // Función para cambiar el número de animación del modelo
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            slider.value = 0.0f //Reiniciar slider
            animationIndex = checkedId
        }

        // Configurar el botón de centrado
        btnCenter.setOnClickListener {
            modelViewer.resetCamera()
        }

    }

    // Función para renderizar la coreografía
    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(currentTime: Long) {
            choreographer.postFrameCallback(this)
            modelViewer.render(currentTime)
        }
    }

    //Métodos básicos
    //onResume
    override fun onResume() {
        super.onResume()
        choreographer.postFrameCallback(frameCallback)
    }

    //onPause
    override fun onPause() {
        super.onPause()
        choreographer.removeFrameCallback(frameCallback)
    }

    //onDestroy
    override fun onDestroy() {
        super.onDestroy()
        choreographer.removeFrameCallback(frameCallback)
        //engine.destroyCamera(myCamera)
    }

    private fun readAsset(assetName: String): ByteBuffer {
        /*
        Carga el directorio de los assets
        */
        val input = assets.open(assetName)
        val bytes = ByteArray(input.available())
        input.read(bytes)
        return ByteBuffer.wrap(bytes)
    }

    private fun loadEnvironment(ibl: String) {
        /*
        Carga el ambiente ("fondo")
        Carga un fondo predefinido por Filament
        */

        // Create the indirect light source and add it to the scene.
        var buffer = readAsset("envs/$ibl/${ibl}_ibl.ktx")
        KtxLoader.createIndirectLight(modelViewer.engine, buffer).apply {
            intensity = 50_000f
            modelViewer.scene.indirectLight = this
        }

        // Create the sky box and add it to the scene.
        buffer = readAsset("envs/$ibl/${ibl}_skybox.ktx")
        KtxLoader.createSkybox(modelViewer.engine, buffer).apply {
            modelViewer.scene.skybox = this
        }
    }

    private fun loadGlb(name: String) {
        /*
        Función para cargar un modelo glb sin escalar
         */
        val buffer = readAsset("models/${name}.glb")
        modelViewer.loadModelGlb(buffer)
        modelViewer.transformToUnitCube()
    }

    private fun loadGlb(name: String, desiredScale: Float) {
        /*
        Función para cargar un modelo glb escalado
         */
        modelName = name
        val buffer = readAsset("models/${name}.glb")
        modelViewer.loadModelGlb(buffer)
        modelViewer.transformToUnitCube()
        scaleModel(desiredScale)
    }

    private fun loadGltf(name: String, desiredScale: Float) {
        /*
        Función para cargar un modelo glft sin escalar
         */
        modelName = name
        val buffer = readAsset("models/${name}.gltf")
        modelViewer.loadModelGltf(buffer) { uri -> readAsset("models/$uri") }
        modelViewer.transformToUnitCube()
        scaleModel(desiredScale)
    }

    private fun animateOnTime() {
        /*
        Función para animar en x tiempo de acuerdo al valor del slider
         */
        modelViewer.animator?.apply {
            if (animationCount > 0) {
                // Aplicar animación (index de animación, tiempo o frame)
                applyAnimation(animationIndex, normalizedTime)
                updateBoneMatrices()
            }

            //Aplicar escalado
            // Es últil cuando los modelos son muy grandes
            modelViewer.asset?.apply {
                scaleRootModel(scaleFactor)
            }
        }
    }

    private fun loadModel(fileName: String, scaleFactor: Float) {
        /*
        Función para cargar un modelo
        Sirve para usarse con el menú o barra lateral
        */
        this.scaleFactor = scaleFactor
        try {
            // Cargar el modelo según su tipo
            if (fileName == "BusterDrone") {
                loadGltf(fileName, scaleFactor)
            } else {
                loadGlb(fileName, scaleFactor)
            }

            // Centrar la cámara al cargar el modelo
            modelViewer.camera.lookAt(
                0.0, 2.0, 5.0,  // Posición de la cámara
                0.0, 0.0, 0.0,  // Punto al que mira (centro)
                0.0, 1.0, 0.0   // Vector "arriba"
            )

            // Establecer primera animación como principal
            animationIndex = 0

            // Obtener duración de la animación 0
            animationDuration = getAnimationDuration()

            // Obtener número de animaciones del modelo
            animationNumber = getAnimationNumber()

            // Mostrar/ocultar visibilidad del RadioGroup
            // de acuerdo al número de animaciones del modelo
            if(animationNumber > 1) {
                lytAnimation.visibility = View.VISIBLE
            } else {
                lytAnimation.visibility = View.INVISIBLE
            }

            // Eliminar radioBtns antiguos del radioGp y agregar nuevos
            radioGroup.removeAllViews()
            for (i in 0 until animationNumber) {
                val button = RadioButton(this)
                button.text = (i + 1).toString()
                button.id = i
                radioGroup.addView(button)
            }

            // Seleccionar el primer radio button si existe
            if(radioGroup.childCount > 0) {
                radioGroup.check(radioGroup.getChildAt(0).id)
            }

            // Reiniciar slider de animación
            slider.value = 0f

            // Aplicar animación inicial
            animateOnTime()

            // Actualizar el nombre del modelo
            modelName = fileName

        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar el modelo: $fileName", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun scaleModel(scaleFactor: Float) {
        /*
        Función para aplicar un escalado
        En versiones más recientes de Filament se puede hacer más sencillo,
        pero debido a la versión utilizada en esta aplicación hay que hacerlo
        manualmente
        */
        modelViewer.asset?.apply {
            val tm = modelViewer.engine.transformManager
            val transformInstance = tm.getInstance(root)
            val currentTransform = Mat4.of(*tm.getTransform(transformInstance, null))

            // Matriz de escala usando Float4
            val scaleMatrix = Mat4(
                Float4(scaleFactor, 0f, 0f, 0f),
                Float4(0f, scaleFactor, 0f, 0f),
                Float4(0f, 0f, scaleFactor, 0f),
                Float4(0f, 0f, 0f, 1f)
            )

            // Aplicar la transformación combinada
            tm.setTransform(transformInstance, (currentTransform * scaleMatrix).toFloatArray())
        }
    }

    private fun scaleRootModel(scaleFactor: Float) {
        /*
        Función para escalar el modelo base o raíz
        */
        modelViewer.asset?.apply {
            val tm = modelViewer.engine.transformManager
            val transformInstance = tm.getInstance(root)
            val scaleMatrix = Mat4(
                Float4(scaleFactor, 0f, 0f, 0f),
                Float4(0f, scaleFactor, 0f, 0f),
                Float4(0f, 0f, scaleFactor, 0f),
                Float4(0f, 0f, 0f, 1f)
            )
            tm.setTransform(transformInstance, scaleMatrix.toFloatArray())
        }
    }

    private fun getAnimationDuration(): Float {
        /*
        Función para obtener el tiempo de animación de un modelo
        */

        // Devolver 8 segundo en caso de que no encuentre una animación
        // o ocurra un error
        val animator = modelViewer.animator ?: return 8.0f

        // Si tiene un tiempo de animación, lo devuelve
        if (animator.animationCount > 0) {
            return animator.getAnimationDuration(animationIndex)
        }

        // Devolver 8 segundo por defecto
        return 8.0f
    }

    private fun getAnimationNumber(): Int {
        /*
        Función para obtener el tiempo de animación de un modelo
        */

        // Devolver 8 segundo en caso de que no encuentre una animación
        // o ocurra un error
        val animator = modelViewer.animator ?: return 0

        // Si tiene un tiempo de animación, lo devuelve
        if (animator.animationCount > 0) {
            lytSlider.visibility = View.VISIBLE
            return animator.animationCount
        }

        // Poner invisible si no existen animaciones
        // Si no hay animaciones, entonces no hay nada que animar
        lytSlider.visibility = View.INVISIBLE
        return 0
    }

    private fun showAboutOurTeam() {
        /*
        Muestra el dialogo para los autores
         */
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Autores")
        builder.setMessage("Aplicaciones móviles - Proyecto en equipo U3\nEquipo:\n - Coyoy López Mario\n - Bocanegra Gonzalez Carlos Antonio\n - Nava López Heriberto Geovanny\n - Rivera Zamarrón Nuria Yaretzi")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

}