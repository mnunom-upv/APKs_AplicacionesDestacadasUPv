package com.z_iti_271304_u3_e05

import com.google.android.filament.utils.Float3
import com.google.android.filament.utils.GestureDetector
import com.google.android.filament.utils.Manipulator
import com.google.android.filament.utils.max
import com.google.android.filament.utils.scale
import com.google.android.filament.utils.translation
import com.google.android.filament.utils.transpose

import android.view.MotionEvent
import android.view.Surface
import android.view.SurfaceView
import android.view.TextureView
import com.google.android.filament.*
import com.google.android.filament.android.DisplayHelper
import com.google.android.filament.android.UiHelper
import com.google.android.filament.gltfio.*
import java.nio.Buffer

/**
 * CLASE PREDEFINIDA POR FILAMENT 1.6.0
 * SOLO SE MODIFICÓ PARA ADECUDARLO A LAS NECESIDADES DEL PROYECTO
 * LA DOCUMENTACIÓN ESTÁ EN INGLÉS, PERO LO QUE SE CAMBIÓ SI ESTÁ EN ESPAÑOL
 */

/**
 * Helps render glTF models into a [SurfaceView] or [TextureView] with an orbit controller.
 *
 * `ModelViewer` owns a Filament engine, renderer, swapchain, view, and scene. It allows clients
 * to access these objects via read-only properties. The viewer can display only one glTF scene
 * at a time, which can be scaled and translated into the viewing frustum by calling
 * [transformToUnitCube]. All ECS entities can be accessed and modified via the [asset] property.
 *
 * For GLB files, clients can call [loadModelGlb] and pass in a [Buffer] with the contents of the
 * GLB file. For glTF files, clients can call [loadModelGltf] and pass in a [Buffer] with the JSON
 * contents, as well as a callback for loading external resources.
 *
 * `ModelViewer` reduces much of the boilerplate required for simple Filament applications, but
 * clients still have the responsibility of adding an [IndirectLight] and [Skybox] to the scene.
 * Additionally, clients should:
 *
 * 1. Pass the model viewer into [SurfaceView.setOnTouchListener] or call its [onTouchEvent]
 *    method from your touch handler.
 * 2. Call [render] and [Animator.applyAnimation] from a `Choreographer` frame callback.
 *
 * NOTE: if its associated SurfaceView or TextureView has become detached from its window, the
 * ModelViewer becomes invalid and must be recreated.
 *
 * See `sample-gltf-viewer` for a usage example.
 */
class MyModelViewer : android.view.View.OnTouchListener {

    var asset: FilamentAsset? = null
        private set

    var animator: Animator? = null
        private set

    @Suppress("unused")
    val progress
        get() = resourceLoader.asyncGetLoadProgress()

    val engine: Engine
    val scene: Scene
    val view: View
    val camera: Camera
    @Entity
    val light: Int

    private val uiHelper: UiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK)
    private lateinit var displayHelper: DisplayHelper
    lateinit var cameraManipulator: Manipulator
    private lateinit var gestureDetector: GestureDetector
    private val renderer: Renderer
    private lateinit var surfaceView: SurfaceView
    private var swapChain: SwapChain? = null
    private var assetLoader: AssetLoader
    private var resourceLoader: ResourceLoader

    private val readyRenderables = IntArray(128) // add up to 128 entities at a time

    //private var eyePos = DoubleArray(3)
    private var target = DoubleArray(3)
    //private var upward = DoubleArray(3)

    private var eyePos = doubleArrayOf(0.0, 6.0, 7.0)
    private var upward = doubleArrayOf(0.0, 8.0, 10.0)
    private val kNearPlane = 0.1    // Cambiado de 0.5 a 0.1 para ver objetos más cercanos
    private val kFarPlane = 100.0   // Ajustado para ver objetos más lejanos
    private val kFovDegrees = 60.0  // Campo de visión más amplio
    private val kAperture = 16f
    private val kShutterSpeed = 1f / 125f
    private val kSensitivity = 100f

    private var previousX = 0f

    init {
        engine = Engine.create()
        renderer = engine.createRenderer()
        scene = engine.createScene()
        camera = engine.createCamera().apply { setExposure(kAperture, kShutterSpeed, kSensitivity) }
        view = engine.createView()
        view.scene = scene
        view.camera = camera

        assetLoader = AssetLoader(engine, MaterialProvider(engine), EntityManager.get())
        resourceLoader = ResourceLoader(engine)

        // Always add a direct light source since it is required for shadowing.
        // We highly recommend adding an indirect light as well.

        light = EntityManager.get().create()

        val (r, g, b) = Colors.cct(6_500.0f)
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(r, g, b)
            .intensity(300_000.0f)
            .direction(0.0f, -1.0f, 0.0f)
            .castShadows(true)
            .build(engine, light)

        scene.addEntity(light)
    }

    //Constructor
    constructor(surfaceView: SurfaceView) {
        //Iniciar manipulador
        setupCameraManipulator(surfaceView)
    }

    @Suppress("unused")
    constructor(textureView: TextureView) {
        cameraManipulator = Manipulator.Builder()
            .targetPosition(0.0f, 0.0f, -4.0f)
            .viewport(textureView.width, textureView.height)
            .build(Manipulator.Mode.ORBIT)

        gestureDetector = GestureDetector(textureView, cameraManipulator)
        uiHelper.renderCallback = SurfaceCallback()
        uiHelper.attachTo(textureView)
        addDetachListener(textureView)
    }

    /**
     * Loads a monolithic binary glTF and populates the Filament scene.
     */
    fun loadModelGlb(buffer: Buffer) {
        destroyModel()
        asset = assetLoader.createAssetFromBinary(buffer)
        asset?.let { asset ->
            resourceLoader.asyncBeginLoad(asset)
            animator = asset.animator
            asset.releaseSourceData()
        }
    }

    /**
     * Loads a JSON-style glTF file and populates the Filament scene.
     */
    fun loadModelGltf(buffer: Buffer, callback: (String) -> Buffer) {
        destroyModel()
        asset = assetLoader.createAssetFromJson(buffer)
        asset?.let { asset ->
            for (uri in asset.resourceUris) {
                resourceLoader.addResourceData(uri, callback(uri))
            }
            resourceLoader.asyncBeginLoad(asset)
            animator = asset.animator
            asset.releaseSourceData()
        }
    }

    private fun setupCameraManipulator(surfaceView: SurfaceView) {
        /*
        Inicializa el manipulador de la camara
        Define la posición y el área de interés de la camara para mirar al modelo
         */
        cameraManipulator = Manipulator.Builder()
            // Posición del objetivo (el centro donde está el modelo)
            .targetPosition(0.0f, 1.0f, 0.0f)
            // Posición inicial de la cámara (más alejada para ver mejor el modelo)
            .orbitHomePosition(0.0f, 1.0f, 5.0f)
            // Viewport
            .viewport(surfaceView.width, surfaceView.height)
            // Usar modo ORBIT para la rotación
            .build(Manipulator.Mode.ORBIT)

        this.surfaceView = surfaceView
        gestureDetector = GestureDetector(surfaceView, cameraManipulator)
        displayHelper = DisplayHelper(surfaceView.context)
        uiHelper.renderCallback = SurfaceCallback()
        uiHelper.attachTo(surfaceView)
        addDetachListener(surfaceView)
    }

    fun resetCamera() {
        /*
        Reinicia el Manipulator (cameraManipulator)
        Crea/restablece el manipulador y ajusta los datos
        A efectos practicos sirve, aunque podría haber perdida de rendimiendo
         */
        this.setupCameraManipulator(surfaceView)
    }

    /**
     * Sets up a root transform on the current model to make it fit into the viewing frustum.
     */
    fun transformToUnitCube() {
        asset?.let { asset ->
            val tm = engine.transformManager
            val center = asset.boundingBox.center.let { v-> Float3(v[0], v[1], v[2]) }
            val halfExtent = asset.boundingBox.halfExtent.let { v-> Float3(v[0], v[1], v[2]) }
            val maxExtent = 2.0f * max(halfExtent)
            val scaleFactor = 2.0f / maxExtent
            // Centramos el modelo
            val transform = scale(Float3(scaleFactor)) * translation(Float3(-center))
            tm.setTransform(tm.getInstance(asset.root), transpose(transform).toFloatArray())
        }
    }

    /**
     * Frees all entities associated with the most recently-loaded model.
     */
    fun destroyModel() {
        asset?.let { asset ->
            assetLoader.destroyAsset(asset)
            this.asset = null
            this.animator = null
        }
    }

    /**
     * Renders the model and updates the Filament camera.
     */
    fun render(frameTimeNanos: Long) {
        if (!uiHelper.isReadyToRender) {
            return
        }

        // Allow the resource loader to finalize textures that have become ready.
        resourceLoader.asyncUpdateLoad()

        // Add renderable entities to the scene as they become ready.
        asset?.let { populateScene(it) }

        // Extract the camera basis from the helper and push it to the Filament camera.
        cameraManipulator.getLookAt(eyePos, target, upward)

        // Set the camera look at
        camera.lookAt(
            eyePos[0], eyePos[1], eyePos[2],
            target[0], target[1], target[2],
            upward[0], upward[1], upward[2])

        // Render the scene, unless the renderer wants to skip the frame.
        if (renderer.beginFrame(swapChain!!, frameTimeNanos)) {
            renderer.render(view)
            renderer.endFrame()
        }
    }

    private fun populateScene(asset: FilamentAsset) {
        var count = 0
        val popRenderables = {count = asset.popRenderables(readyRenderables); count != 0}
        while (popRenderables()) {
            scene.addEntities(readyRenderables.take(count).toIntArray())
        }
    }

    private fun addDetachListener(view: android.view.View) {
        view.addOnAttachStateChangeListener(object : android.view.View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: android.view.View) {}
            override fun onViewDetachedFromWindow(v: android.view.View) {
                uiHelper.detach()

                destroyModel()
                assetLoader.destroy()
                resourceLoader.destroy()

                engine.destroyEntity(light)
                engine.destroyRenderer(renderer)
                engine.destroyView(this@MyModelViewer.view)
                engine.destroyScene(scene)
                engine.destroyCamera(camera)

                EntityManager.get().destroy(light)

                engine.destroy()
            }
        })
    }

    /**
     * Handles a [MotionEvent] to enable one-finger orbit, two-finger pan, and pinch-to-zoom.
     */
    fun onTouchEvent(event: MotionEvent) {
        gestureDetector.onTouchEvent(event)
    }

    @SuppressWarnings("ClickableViewAccessibility")
    override fun onTouch(view: android.view.View, event: MotionEvent): Boolean {
        onTouchEvent(event)
        return true
    }

    inner class SurfaceCallback : UiHelper.RendererCallback {
        override fun onNativeWindowChanged(surface: Surface) {
            swapChain?.let { engine.destroySwapChain(it) }
            swapChain = engine.createSwapChain(surface)
            displayHelper.attach(renderer, surfaceView.display)
        }

        override fun onDetachedFromSurface() {
            displayHelper.detach()
            swapChain?.let {
                engine.destroySwapChain(it)
                engine.flushAndWait()
                swapChain = null
            }
        }

        override fun onResized(width: Int, height: Int) {
            view.viewport = Viewport(0, 0, width, height)
            val aspect = width.toDouble() / height.toDouble()
            camera.setProjection(kFovDegrees, aspect, kNearPlane, kFarPlane, Camera.Fov.VERTICAL)
            cameraManipulator.setViewport(width, height)
        }
    }
}