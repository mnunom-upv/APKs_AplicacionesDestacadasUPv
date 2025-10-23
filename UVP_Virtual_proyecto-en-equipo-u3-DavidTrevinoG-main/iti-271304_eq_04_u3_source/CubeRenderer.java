package com.upv.pm_2022.sep_dic.capitulo3_vrcardboard;


import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class CubeRenderer implements GLSurfaceView.Renderer, SensorEventListener {

    SensorManager sensorManager;
    Sensor rotationVectorSensor;

    /**
     * Rotation increment per frame.
     */
    public static final float CUBE_ROTATION_INCREMENT = 0.1f;
    //private static final float CUBE_ROTATION_INCREMENT = 0.6f;

    /**
     * The refresh rate, in frames per second.
     */
    private static final int REFRESH_RATE_FPS = 60;

    /**
     * The duration, in milliseconds, of one frame.
     */
    private static final float FRAME_TIME_MILLIS = TimeUnit.SECONDS.toMillis(1) / REFRESH_RATE_FPS;

    private final Context mActivityContext;

    //Matrices Necesarios
    private float[] inicioMatrix;
    float[] g_viewMatrix;
    float[] g_projectionMatrix;
    private float[] g_mvpMatrix;
    private float[] g_modelMatrix;

    //Metodos para hacer push y pop Matrix
    Stack<float[]> pila = new Stack<>();

    public void pushMatrix(float [] m){
        float[] m2 = new float[16];
        System.arraycopy(m,0,m2,0,16);
        pila.push(m2);
    }



    public float[] popMatrix(){
        return pila.pop();
    }

    //Buffers para las posiciones y texturas
    private FloatBuffer mPositions;
    private FloatBuffer mTextureCoordinates;

    /** This will be used to pass in the transformation matrix. */
    private int mMvpMatrixHandle;

    /** This will be used to pass in the texture. */
    private int mTextureUniformHandle;

    /** This will be used to pass in model position information. */
    private int mPositionHandle;

    /** This will be used to pass in model texture coordinate information. */
    private int mTextureCoordinateHandle;

    /** How many bytes per float. */
    private final int mBytesPerFloat = 4;

    /** Size of the position data in elements. */
    private final int mPositionDataSize = 3;

    /** Size of the texture coordinate data in elements. */
    private final int mTextureCoordinateDataSize = 2;

    /** This is a handle to our cube shading program. */
    private int mProgramHandle;

    /** Texturas */
    private int imagen_techoiti = 0;
    private int imagen_logo_ext = 0;
    private int textura_logo_ext = 0;
    private int imagen_loby_pared = 0;
    private int textura_loby_pared = 0;
    private int textura_techoiti = 0;
    private int imagen_puertaiti = 0;
    private int textura_loby_planta = 0;
    private int imagen_loby_planta = 0;
    private int textura_puertaiti = 0;
    private int imagen_tutorias = 0;
    private int textura_tutorias = 0;
    private int imagen_ventanaiti2 = 0;
    private int textura_ventanaiti2 = 0;
    private int imagen_cesped = 0;
    private int textura_cesped = 0;
    private int imagen_cielo = 0;
    private int textura_cielo = 0;
    private int textura_cielo_enfrente = 0;
    private int textura_cielo_atras = 0;
    private int textura_cielo_derecha = 0;
    private int textura_cielo_izquierda = 0;
    private int imagen_amarillo = 0;
    private int textura_amarillo = 0;
    private int imagen_azul = 0;
    private int textura_azul = 0;
    private int imagen_azulindigo = 0;
    private int textura_azulindigo = 0;
    private int imagen_blanco = 0;
    private int textura_blanco = 0;
    private int imagen_cafe = 0;
    private int textura_cafe = 0;
    private int imagen_crema = 0;
    private int textura_crema = 0;
    private int imagen_cyan = 0;
    private int textura_cyan = 0;
    private int imagen_entradaPrincipal = 0;
    private int textura_entradaPrincipal = 0;
    private int imagen_fucsia = 0;
    private int textura_fucsia = 0;
    private int imagen_gindo = 0;
    private int textura_gindo = 0;
    private int imagen_gris = 0;
    private int textura_gris = 0;
    private int imagen_grisclaro = 0;
    private int textura_grisclaro = 0;
    private int imagen_morado = 0;
    private int textura_morado = 0;
    private int imagen_naranja = 0;
    private int textura_naranja = 0;
    private int imagen_negro = 0;
    private int textura_negro = 0;
    private int imagen_oro = 0;
    private int textura_oro = 0;
    private int imagen_pisoupv = 0;
    private int textura_pisoupv = 0;
    private int imagen_puerta1 = 0;
    private int textura_puerta1 = 0;
    private int imagen_purpura = 0;
    private int textura_purpura = 0;
    private int imagen_rojo = 0;
    private int textura_rojo = 0;
    private int imagen_verde = 0;
    private int textura_verde = 0;
    private int imagen_verdefosfo = 0;
    private int textura_verdefosfo = 0;
    private int imagen_pisoupv2 = 0;
    private int textura_pisoupv2 = 0;
    private int imagen_ventanaISA = 0;
    private int textura_ventanaISA = 0;
    private int imagen_puertage = 0;
    private int textura_puertage = 0;
    private int imagen_pared5 = 0;
    private int textura_pared5 = 0;
    private int imagen_madera = 0;
    private int textura_madera = 0;
    private int imagen_puertaPymes = 0;
    private int textura_puertaPymes = 0;
    private int imagen_cristal = 0;
    private int textura_cristal = 0;
    private int imagen_puertabano = 0;
    private int textura_puertabano = 0;
    private int imagen_escolares = 0;
    private int textura_escolares = 0;
    private int imagen_cai = 0;
    private int textura_cai = 0;
    private int imagen_puertas = 0;
    private int textura_puertas = 0;
    private int imagen_pasillooficina = 0;
    private int textura_pasillooficina = 0;
    private int imagen_puertasalida = 0;
    private int textura_puertasalida = 0;
    private int imagen_biblioteca = 0;
    private int textura_biblioteca = 0;
    private int imagen_porbiblioteca = 0;
    private int textura_porbiblioteca = 0;
    private int imagen_salamaestros = 0;
    private int textura_salamaestros = 0;
    private int imagen_ventanapymes = 0;
    private int textura_ventanapymesve = 0;
    private int imagen_maquina = 0;
    private int textura_maquina = 0;
    private int imagen_grispymes = 0;
    private int textura_grispymes = 0;
    private int imagen_blancopymes = 0;
    private int textura_blancopymes = 0;
    private int gll = 0;
    private int u_MvpMatrix = 0;
    private int imagen_paredoficina = 0;
    private int textura_paredoficina = 0;

    // Variables para hacer la vista dual
    public static float screenWidth;
    public static float screenHeight;

    float PI = 3.1415926535897932384626433832795f;
    float PIdiv180 = (PI/180.0f);

    float RotatedX;
    float RotatedY;
    float RotatedZ;

    public float[] Position;
    public float[] ViewDir;
    public float[] RightVector;
    public float[] UpVector;

    private float[] rotationMatrix = new float[16];


    private float[] calcularCoordenadasMinimapa(float[] posicion, float anchoMapa, float altoMapa) {
        float xRelativo = (posicion[0] / anchoMapa) * 2.0f - 1.0f; // Normalizar a [-1, 1]
        float zRelativo = (posicion[2] / altoMapa) * 2.0f - 1.0f; // Normalizar a [-1, 1]
        return new float[]{xRelativo, zRelativo}; // Nota: usamos Z porque Y es la altura
    }

    private void dibujarPuntero(float x, float y) {
        // Coordenadas para un triángulo (flecha)
        final float[] vertices = {
                x, y + 0.05f, 0.0f,   // Punto superior
                x - 0.05f, y - 0.05f, 0.0f, // Esquina inferior izquierda
                x + 0.05f, y - 0.05f, 0.0f  // Esquina inferior derecha
        };

        // Sin textura (solo color sólido)
        GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0,
                ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertices).position(0));
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Color del puntero (rojo, por ejemplo)
        GLES20.glUniform4f(GLES20.glGetUniformLocation(mProgramHandle, "u_Color"), 1.0f, 0.0f, 0.0f, 1.0f);

        // Dibujar el triángulo (flecha)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        // Restaurar configuración de textura
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
    }


    private int texturaMinimapa = 0;
    private int texturaMinimapaa = 0;
    private int texturaMinimapab = 0;
    private int texturaMinimapah = 0;
    private int texturaMinimapai = 0;

    public CubeRenderer(final Context activityContext) {
        mActivityContext = activityContext;

        sensorManager = (SensorManager) activityContext.getSystemService(Context.SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        Log.d("Purito","Incializacion Cube Renderer");

        //Matrices
        inicioMatrix = new float[16];
        g_modelMatrix = new float[16];
        g_mvpMatrix = new float[16];
        g_viewMatrix = new float[16];
        g_projectionMatrix = new float[16];

        Matrix.setLookAtM(g_viewMatrix, 0, 0.0f, 0.0f, -3.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);

        RotatedX = 0.0f;
        RotatedY = 0.0f;
        RotatedZ = 0.0f;

        Position = new float[3];
        Position[0] = 0.0f;
        Position[1] = 0.0f;
        Position[2] = 0.0f;

        ViewDir = new float[3];
        ViewDir[0] = 0.0f;
        ViewDir[1] = 0.0f;
        ViewDir[2] = -1.0f;

        RightVector =  new float[3];
        RightVector[0] = 1.0f;
        RightVector[1] = 0.0f;
        RightVector[2] = 0.0f;

        UpVector =  new float[3];
        UpVector[0] = 0.0f;
        UpVector[1] = 1.0f;
        UpVector[2] = 0.0f;



        MoveForward(500.0f);
        MoveUpward(10.0f);
        StrafeRight(450.0f);


    }


    // Lista de ubicaciones predefinidas
    private List<float[]> ubicaciones = Arrays.asList(
            new float[]{446.94974f, 19.0f, 192.60959f},    // Edificio A
            new float[]{1359.5217f, 14.0f, -94.37073f}, // Edificio B
            new float[]{-3.0502605f, 29.0f, -427.3904f}, // Edificio H
            new float[]{ 1252.8394f,  24.0f, -432.67877f} // Edificio I
    );

    // Lista de direcciones para cada ubicación
    private List<float[]> direcciones = Arrays.asList(
            new float[]{0.0f, 0.0f, -1.0f}, // Norte (Ubicación 1)
            new float[]{-2.9802322E-8f, 0.0f, 1.0f},  // Sur (Ubicación 3)
            new float[]{0.0f, 0.0f, -1.0f}, // Norte (Ubicación 4)
            new float[]{0.0f, 0.0f, -1.0f}  // Norte (Ubicación 5)
    );

    public void ajustarVistaInicial() {
        float[] rotationMatrix = new float[16];
        Matrix.setIdentityM(rotationMatrix, 0);

        // Rotar 90 grados en el eje X para mirar al horizonte
        ViewDir[0] = 0.98f;
        ViewDir[1] = 0.0f;
        ViewDir[2] = 0.0f;


        // Aplica la rotación inicial
        actualizar_vista_con_matriz(rotationMatrix);
    }

    public void mostrarToastConDuracion(Context context, String mensaje, int duracionEnMilisegundos) {
        // Crear el Toast
        Toast toast = new Toast(context);
        TextView textView = new TextView(context);
        textView.setText(mensaje);
        textView.setBackgroundColor(Color.BLACK);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(20, 10, 20, 10);
        textView.setGravity(Gravity.CENTER);

        toast.setView(textView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();

        // Ocultar el Toast manualmente después de la duración
        new Handler(Looper.getMainLooper()).postDelayed(toast::cancel, duracionEnMilisegundos);
    }



    // Índice para rastrear la ubicación actual
    private int indiceUbicacion = 1;

    public void NextPosicion() {
        RotatedX = 0.0f;
        RotatedY = 0.0f;
        RotatedZ = 0.0f;

        Position = new float[3];

        // Selecciona la ubicación actual de la lista
        float[] nuevaPosicion = ubicaciones.get(indiceUbicacion);

        // Asigna la nueva posición
        Position[0] = nuevaPosicion[0];
        Position[1] = nuevaPosicion[1];
        Position[2] = nuevaPosicion[2];

        float[] nuevaDireccion = direcciones.get(indiceUbicacion);
        ViewDir[0] = nuevaDireccion[0];
        ViewDir[1] = nuevaDireccion[1];
        ViewDir[2] = nuevaDireccion[2];

        switch (indiceUbicacion){
            case 0:
                mostrarToastConDuracion(mActivityContext, "Estas en el edificio A", 1000);
                break;
            case 1:
                mostrarToastConDuracion(mActivityContext, "Estas en el edificio B", 1000);
                break;
            case 2:
                mostrarToastConDuracion(mActivityContext, "Estas en el edificio H", 1000);
                break;
            case 3:
                mostrarToastConDuracion(mActivityContext, "Estas en xel edificio I", 1000);
                break;
        }
        // Incrementa el índice para la próxima vez
        indiceUbicacion++;
        if (indiceUbicacion >= ubicaciones.size()) {
            indiceUbicacion = 0;
        }

        RightVector = new float[3];
        RightVector[0] = 1.0f;
        RightVector[1] = 0.0f;
        RightVector[2] = 0.0f;

        UpVector = new float[3];
        UpVector[0] = 0.0f;
        UpVector[1] = 1.0f;
        UpVector[2] = 0.0f;
    }

    public void ReiniciarPosiciones () {
        RotatedX = 0.0f;
        RotatedY = 0.0f;
        RotatedZ = 0.0f;

        Position = new float[3];
        Position[0] = 446.94974f;
        Position[1] = 19.0f;
        Position[2] = 192.60959f;
        mostrarToastConDuracion(mActivityContext, "Estas en el edificio A", 1000);

        ViewDir = new float[3];
        ViewDir[0] = 0.0f;
        ViewDir[1] = 0.0f;
        ViewDir[2] = -1.0f;

        RightVector =  new float[3];
        RightVector[0] = 1.0f;
        RightVector[1] = 0.0f;
        RightVector[2] = 0.0f;

/*        LeftVector = new float[3];
        LeftVector[0]= -1.0f;
        LeftVector[1]= 0.0f;
        LeftVector[2]= 0.0f;*/

        UpVector =  new float[3];
        UpVector[0] = 0.0f;
        UpVector[1] = 1.0f;
        UpVector[2] = 0.0f;

    }

    private void dibujarMinimapa() {
        // Renderizar el minimapa (visor izquierdo)
        GLES20.glViewport(40, (int) screenHeight - 250, 200, 200);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Fondo negro
        Matrix.setIdentityM(g_mvpMatrix, 0);
        renderizarMinimapa();

        // Calcular la posición del usuario en el minimapa
        float[] coordMinimapa = calcularCoordenadasMinimapa(Position, 1000.0f, 1000.0f); // Ajusta el tamaño del mundo
        dibujarPuntero(coordMinimapa[0], coordMinimapa[1]);

        // Renderizar el minimapa (visor derecho)
        GLES20.glViewport((int) screenWidth + 80, (int) screenHeight - 250, 200, 200);
        Matrix.setIdentityM(g_mvpMatrix, 0);
        renderizarMinimapa();

        // Dibujar el puntero en el visor derecho
        dibujarPuntero(coordMinimapa[0], coordMinimapa[1]);

        Log.d("CoordenadasMinimapa", String.valueOf(Position[0]) + " " + String.valueOf(Position[1]) + " " + String.valueOf(Position[2]));
        Log.d("DireccionMinimapa", String.valueOf(ViewDir[0]) + " " + String.valueOf(ViewDir[1]) + " " + String.valueOf(ViewDir[2]));
    }


    // Método que renderiza el minimapa
    private void renderizarMinimapa() {
        // Coordenadas para un cuadrado 2D
        final float[] vertices = {
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                -1.0f,  1.0f, 0.0f,
                1.0f,  1.0f, 0.0f
        };

        final float[] texturas = {
                1.0f, 0.0f, // Parte superior derecha
                0.0f, 0.0f, // Parte superior izquierda
                1.0f, 1.0f, // Parte inferior derecha
                0.0f, 1.0f  // Parte inferior izquierda
        };

        switch (indiceUbicacion){
            case 0:
                texturaMinimapa = texturaMinimapai;
                break;
            case 1:
                texturaMinimapa = texturaMinimapaa;
                break;
            case 2:
                texturaMinimapa = texturaMinimapab;
                break;
            case 3:
                texturaMinimapa = texturaMinimapah;
                break;
        }
        cargarPosicion_Textura(vertices, texturas, texturaMinimapa);
        GLES20.glUniformMatrix4fv(mMvpMatrixHandle, 1, false, g_mvpMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    private void dibujarContornoLados() {
        // Coordenadas para el borde izquierdo
        final float[] verticesIzquierda = {
                -1.0f, -1.0f, 0.0f,  // Inferior izquierdo
                -0.8f, -1.0f, 0.0f,  // Inferior derecho (lado izquierdo)
                -1.0f,  1.0f, 0.0f,  // Superior izquierdo
                -0.8f,  1.0f, 0.0f   // Superior derecho (lado izquierdo)
        };

        // Coordenadas para el borde derecho
        final float[] verticesDerecha = {
                0.8f, -1.0f, 0.0f,   // Inferior izquierdo (lado derecho)
                1.0f, -1.0f, 0.0f,   // Inferior derecho
                0.8f,  1.0f, 0.0f,   // Superior izquierdo (lado derecho)
                1.0f,  1.0f, 0.0f    // Superior derecho
        };

        // Dibujar el borde izquierdo
        dibujarBorde(verticesIzquierda);

        // Dibujar el borde derecho
        dibujarBorde(verticesDerecha);
    }

    private void dibujarBorde(float[] vertices) {
        // Buffer para los vértices
        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(vertices).position(0);

        GLES20.glUseProgram(mProgramHandle); // Usa el programa adecuado

        // Establecer color negro
        GLES20.glUniform4f(GLES20.glGetUniformLocation(mProgramHandle, "u_Color"), 0.0f, 0.0f, 0.0f, 1.0f);

        // Configura la posición de los vértices
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Dibujar el rectángulo (borde negro)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }




    private void dibujarBordeNegro() {
        // Coordenadas para un borde negro alrededor de la vista
        final float[] vertices = {
                -1.0f, -1.0f, 0.0f,  // Inferior izquierdo
                1.0f, -1.0f, 0.0f,  // Inferior derecho
                -1.0f,  1.0f, 0.0f,  // Superior izquierdo
                1.0f,  1.0f, 0.0f   // Superior derecho
        };

        // Buffer para los vértices
        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(vertices).position(0);

        GLES20.glUseProgram(mProgramHandle); // Usa el programa adecuado

        // Establecer color negro
        GLES20.glUniform4f(GLES20.glGetUniformLocation(mProgramHandle, "u_Color"), 0.0f, 0.0f, 0.0f, 1.0f);

        // Configura la posición de los vértices
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Dibujar el rectángulo (borde negro)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d("Purito","Creando Superficie");

        texturaMinimapa = generarTextura(R.drawable.map);
        texturaMinimapaa = generarTextura(R.drawable.mapaa);
        texturaMinimapab = generarTextura(R.drawable.mapab);
        texturaMinimapah = generarTextura(R.drawable.mapah);
        texturaMinimapai = generarTextura(R.drawable.mapai);

        // Set the background frame color
        //GLES20.glClearColor(5.0f, 0.5f, 0.7f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        final String vertexShader = RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_vertex_shader_tex_and_light);
        final String fragmentShader = RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_fragment_shader_tex_and_light);

        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        mProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[] {"a_Position", "a_TexCoord",  "a_Color"});

        // Load the texture
        textura_techoiti = generarTextura(R.drawable.techo1);
        //textura_techoiti = generarTextura(R.drawable.amarillo);
        textura_logo_ext = generarTextura(R.drawable.logo_ext);
        textura_loby_pared = generarTextura(R.drawable.loby_pared);
        textura_puertaiti = generarTextura(R.drawable.puertaiti);
        textura_loby_planta = generarTextura(R.drawable.loby_planta);
        textura_tutorias = generarTextura(R.drawable.tutorias);
        textura_ventanaiti2 = generarTextura(R.drawable.ventanaiti2);
        textura_cesped = generarTextura(R.drawable.cesped);
        textura_cielo = generarTextura(R.drawable.cielo);
        textura_cielo_enfrente = generarTextura(R.drawable.cielo_enfrente);
        textura_cielo_atras = generarTextura(R.drawable.cielo_atras);
        textura_cielo_derecha = generarTextura(R.drawable.cielo_derecha);
        textura_cielo_izquierda = generarTextura(R.drawable.cielo_izquierda);
        textura_amarillo = generarTextura(R.drawable.amarillo);
        textura_azul = generarTextura(R.drawable.azul);
        textura_azulindigo = generarTextura(R.drawable.blanco);
        textura_blanco = generarTextura(R.drawable.blanco);
        textura_cafe = generarTextura(R.drawable.cafe);
        textura_crema = generarTextura(R.drawable.crema);
        textura_cyan = generarTextura(R.drawable.cyan);
        textura_entradaPrincipal = generarTextura(R.drawable.entradaprincipal);
        textura_fucsia = generarTextura(R.drawable.fucsia);
        textura_gindo = generarTextura(R.drawable.gindo);
        textura_gris = generarTextura(R.drawable.gris);
        textura_grisclaro = generarTextura(R.drawable.grisclaro);
        textura_morado  = generarTextura(R.drawable.morado);
        textura_naranja  = generarTextura(R.drawable.naranja);
        textura_negro = generarTextura(R.drawable.negro);
        textura_oro = generarTextura(R.drawable.oro);
        textura_pisoupv = generarTextura(R.drawable.pisoupv);
        textura_puerta1 = generarTextura(R.drawable.puerta1);
        textura_purpura = generarTextura(R.drawable.purpura);
        textura_rojo = generarTextura(R.drawable.rojo);
        textura_verde = generarTextura(R.drawable.verde);
        textura_verdefosfo = generarTextura(R.drawable.verdefosfo);
        textura_pisoupv2 = generarTextura(R.drawable.pisoupv2);
        textura_ventanaISA = generarTextura(R.drawable.ventanaisa);
        textura_puertage = generarTextura(R.drawable.puertage);
        textura_pared5 = generarTextura(R.drawable.pared5);
        textura_madera = generarTextura(R.drawable.madera);
        textura_puertaPymes = generarTextura(R.drawable.pymes);
        textura_cristal = generarTextura(R.drawable.ventanapymes);
        textura_puertabano = generarTextura(R.drawable.puertabano);
        textura_escolares = generarTextura(R.drawable.escolares);
        textura_cai = generarTextura(R.drawable.grisclaro);
        textura_puertas = generarTextura(R.drawable.puertas);
        textura_pasillooficina = generarTextura(R.drawable.pasillooficina);
        textura_puertasalida = generarTextura(R.drawable.puertasalida);
        textura_biblioteca = generarTextura(R.drawable.biblioteca);
        textura_porbiblioteca = generarTextura(R.drawable.porbiblioteca);
        textura_salamaestros = generarTextura(R.drawable.salamaestros);
        textura_ventanapymesve = generarTextura(R.drawable.ventanapymes);
        textura_maquina = generarTextura(R.drawable.maquina);
        textura_grispymes = generarTextura(R.drawable.grispymes);
        textura_blancopymes = generarTextura(R.drawable.blancopymes);
        textura_paredoficina = generarTextura(R.drawable.paredoficinas);

        Matrix.setIdentityM(g_mvpMatrix, 0);
        Matrix.setIdentityM(g_viewMatrix, 0);
        Matrix.setIdentityM(g_projectionMatrix, 0);
        actualizar_vista();

    }

    private int generarTextura(int textura){
        int t = TextureHelper.loadTexture(mActivityContext, textura);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        return t;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);
        screenHeight = height;
        screenWidth = width;
        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 5000.0f;

        Matrix.frustumM(g_projectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // Set our per-vertex lighting program.
        GLES20.glUseProgram(mProgramHandle);

        // Set program handles for cube drawing.
        mMvpMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MvpMatrix");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Sampler");
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoord");

        actualizar_vista();

        Matrix.multiplyMM(g_modelMatrix, 0, g_projectionMatrix, 0, g_viewMatrix, 0);

        System.arraycopy(g_modelMatrix,0, inicioMatrix,0,16);

        // Se llaman los metodos para mostrar el cielo
        cielo();
        cielo_enfrente();
        cielo_atras();
        cielo_derecha();
        cielo_izquierda();

        base_cesped();

        edificioA();

        Matrix.setIdentityM(g_modelMatrix, 0);
        System.arraycopy(inicioMatrix,0, g_modelMatrix,0,16);

        edificioB();

        Matrix.setIdentityM(g_modelMatrix, 0);
        System.arraycopy(inicioMatrix,0, g_modelMatrix,0,16);

        edificioH();

        Matrix.setIdentityM(g_modelMatrix, 0);
        System.arraycopy(inicioMatrix,0, g_modelMatrix,0,16);


        edificioquefaltaba();

        dibujarMinimapa();

    }

    public void MoveForward(float Distance )
    {
        Position = suma(Position,multiplicacion(ViewDir,-Distance));
    }

    public void StrafeRight(float Distance )
    {
        Position = suma(Position,multiplicacion(RightVector,Distance));
    }

    public void MoveUpward( float Distance )
    {
        Position = suma(Position,multiplicacion(UpVector,Distance));
    }

    public void RotateX (float Angle)
    {
        RotatedX = Angle;

        //Rotate viewdir around the right vector:
        ViewDir = Normalize(suma(multiplicacion(ViewDir,(float)Math.cos(Angle*PIdiv180)),
                multiplicacion(UpVector,(float)Math.sin(Angle*PIdiv180))));

        //now compute the new UpVector (by cross product)
        UpVector = multiplicacion(CrossProduct(ViewDir, RightVector),-1);

    }

    public void RotateY (float Angle)
    {
        RotatedY = Angle;

        //Rotate viewdir around the up vector:
        ViewDir = Normalize(resta(multiplicacion(ViewDir,(float)Math.cos(Angle*PIdiv180)),
                multiplicacion(RightVector,(float)Math.sin(Angle*PIdiv180))));

        //now compute the new RightVector (by cross product)
        RightVector = CrossProduct(ViewDir, UpVector);
    }

    private float[] CrossProduct (float[] u, float[] v)
    {
        float[] resVector = new float[3];
        resVector[0] = u[1]*v[2] - u[2]*v[1];
        resVector[1] = u[2]*v[0] - u[0]*v[2];
        resVector[2] = u[0]*v[1] - u[1]*v[0];

        return resVector;
    }

    float GetF3dVectorLength(float[] v)
    {
        return (float)(Math.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]));
    }

    private float[] Normalize( float[] v)
    {
        float[] res = new float[3];
        float l = GetF3dVectorLength(v);
        if (l == 0.0f){
            res[0] = 0.0f;
            res[1] = 0.0f;
            res[2] = 0.0f;
        }else {
            res[0] = v[0] / l;
            res[1] = v[1] / l;
            res[2] = v[2] / l;
        }
        return res;
    }

    private float[] multiplicacion(float[] v,float r){
        float[] res = new float[3];
        res[0] = v[0]*r;
        res[1] = v[1]*r;
        res[2] = v[2]*r;
        return res;
    }

    private float[] suma(float[] v,float [] u){
        float[] res = new float[3];
        res[0] = v[0]+u[0];
        res[1] = v[1]+u[1];
        res[2] = v[2]+u[2];
        return res;
    }

    private float[] resta(float[] v,float [] u){
        float[] res = new float[3];
        res[0] = v[0]-u[0];
        res[1] = v[1]-u[1];
        res[2] = v[2]-u[2];
        return res;
    }


    public void Move(float[]  Direction)
    {
        Position = suma(Position, Direction);
    }

    public void actualizar_vista(){
        float[] ViewPoint = suma(Position,ViewDir);
        Matrix.setLookAtM(g_viewMatrix, 0,Position[0],Position[1],Position[2], ViewPoint[0],ViewPoint[1],ViewPoint[2], UpVector[0],UpVector[1],UpVector[2]);
    }

    public void cargarPosicion_Textura(float[] v, float[] vt, int textura){

        //Posiciones---------------------------------------------------------------------------------

        mPositions = ByteBuffer.allocateDirect(v.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPositions.put(v).position(0);

        // Pass in the position information
        mPositions.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, 0, mPositions);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        //Textura-----------------------------------------------------------------------------------
        mTextureCoordinates = ByteBuffer.allocateDirect(vt.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextureCoordinates.put(vt).position(0);

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textura);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);

        // Pass in the texture coordinate information
        mTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 0, mTextureCoordinates);

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

        //----------------------------------------------------------------------------------------------

    }

    //Cuadrilatero parado en el eje de los X
    private void pared_X(int nosirve1, int nosirve2, double largo, double alto, int nosirve3, int texturaHandle) {
        System.arraycopy(g_modelMatrix, 0, g_mvpMatrix, 0, 16);

        //Coordenadas de la pared
        final float[] vertices = {
                0,              0,               0,
                0,              (float)alto,     0,
                (float)largo,   0,               0,
                (float)largo,   (float)alto,     0
        };

        //Coordenadas de la textura
        final float[] vertices_textura = {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };

        cargarPosicion_Textura(vertices, vertices_textura, texturaHandle);


        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMvpMatrixHandle, 1, false, g_mvpMatrix, 0);

        int n = vertices.length / 3;

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, n);
    }


    //Cuadrilatero parado en el eje de los X
    private void pared_Z(int nosirve1, int nosirve2, double largo, double alto, int nosirve3, int texturaHandle) {
        System.arraycopy(g_modelMatrix, 0, g_mvpMatrix, 0, 16);

        //Coordenadas de la pared
        final float[] vertices = {
                0,     0,    0,
                0,     (float)alto, 0,
                0,     0,    (float)-largo,
                0,     (float)alto, (float)-largo
        };

        //Coordenadas de la textura
        final float[] vertices_textura = {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };


        cargarPosicion_Textura(vertices, vertices_textura, texturaHandle);


        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMvpMatrixHandle, 1, false, g_mvpMatrix, 0);

        int n = vertices.length / 3;

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, n);
    }


    //Piso o techo
    private void piso_o_techo(int nosirve1, int nosirve2, double largo, double ancho, int nosirve3, int texturaHandle) {
        System.arraycopy(g_modelMatrix, 0, g_mvpMatrix, 0, 16);

        //Coordenadas de la pared
        final float[] vertices = {
                0,     0,    0,
                0,     0,    (float)-ancho,
                (float)largo, 0,    0,
                (float)largo, 0,    (float)-ancho
        };

        //Coordenadas de la textura
        final float[] vertices_textura = {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };


        cargarPosicion_Textura(vertices, vertices_textura, texturaHandle);


        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMvpMatrixHandle, 1, false, g_mvpMatrix, 0);

        int n = vertices.length / 3;

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, n);
    }


    //Piso o techo
    private void piso_o_techo_especial(int nosirve1, int nosirve2, double x1, double z1, double x2, double z2, double x3, double z3, int nosirve3, int texturaHandle) {
        System.arraycopy(g_modelMatrix, 0, g_mvpMatrix, 0, 16);

        //Coordenadas de la pared
        final float[] vertices = {
                0,     0,    0,
                (float)x1,    0,   (float)z1,
                (float)x2,    0,   (float)z2,
                (float)x3,    0,   (float)z3
        };

        //Coordenadas de la textura
        final float[] vertices_textura = {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };


        cargarPosicion_Textura(vertices, vertices_textura, texturaHandle);


        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMvpMatrixHandle, 1, false, g_mvpMatrix, 0);

        int n = vertices.length / 3;

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, n);
    }

    private void prisma_cuadrangularLOBY(int nosirve1, int nosirve2, double largo, double ancho, double alto, int nosirve3, int texturaHandle){

        //Cara frontal y trasera
        pared_X(nosirve1, nosirve2, largo, alto, nosirve3, texturaHandle);
        Matrix.translateM(g_modelMatrix, 0, 0,0, (float)-ancho);
        pared_X(nosirve1, nosirve2, largo, alto, nosirve3, texturaHandle);
        Matrix.translateM(g_modelMatrix, 0, 0,0, (float)ancho);

        //Cara izquierda
        pared_Z(nosirve1, nosirve2, ancho, alto, nosirve3, textura_loby_pared);

        //Piso
        piso_o_techo(nosirve1, nosirve2, largo, ancho, nosirve3, texturaHandle);
        Matrix.translateM(g_modelMatrix, 0, (float)largo,0, 0);

        //Cara derecha
        pared_Z(nosirve1, nosirve2, ancho, alto, nosirve3, texturaHandle);
        Matrix.translateM(g_modelMatrix, 0, (float)-largo, (float)alto, 0);

        //Techo
        piso_o_techo(nosirve1, nosirve2, largo, ancho, nosirve3, texturaHandle);
        Matrix.translateM(g_modelMatrix, 0, 0, (float)-alto, 0);
    }


    private void prisma_cuadrangular(int nosirve1, int nosirve2, double largo, double ancho, double alto, int nosirve3, int texturaHandle) {
        //System.arraycopy(g_modelMatrix, 0, g_mvpMatrix, 0, 16);

        //Cara frontal y trasera
        pared_X(nosirve1, nosirve2, largo, alto, nosirve3, texturaHandle);
        Matrix.translateM(g_modelMatrix, 0, 0,0, (float)-ancho);
        pared_X(nosirve1, nosirve2, largo, alto, nosirve3, texturaHandle);
        Matrix.translateM(g_modelMatrix, 0, 0,0, (float)ancho);

        //Cara izquierda
        pared_Z(nosirve1, nosirve2, ancho, alto, nosirve3, texturaHandle);

        //Piso
        piso_o_techo(nosirve1, nosirve2, largo, ancho, nosirve3, texturaHandle);
        Matrix.translateM(g_modelMatrix, 0, (float)largo,0, 0);

        //Cara derecha
        pared_Z(nosirve1, nosirve2, ancho, alto, nosirve3, texturaHandle);
        Matrix.translateM(g_modelMatrix, 0, (float)-largo, (float)alto, 0);

        //Techo
        piso_o_techo(nosirve1, nosirve2, largo, ancho, nosirve3, texturaHandle);
        Matrix.translateM(g_modelMatrix, 0, 0, (float)-alto, 0);
    }


    //Para no escribir todoo de nuevo:v
    private void g_modelMatrix_translate(double x, double y, double z){
        Matrix.translateM(g_modelMatrix, 0, (float)x, (float)y, (float)z);
    }

    private void g_modelMatrix_rotate(double angulo, double x, double y, double z){
        Matrix.rotateM(g_modelMatrix, 0, (float)angulo, (float)x, (float)y ,(float)z);
    }


    //Edificios
    private void base_cesped(){
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,-0.1f,700);
        piso_o_techo(gll, u_MvpMatrix, 3000, 3000, imagen_cesped, textura_cesped);
        g_modelMatrix = popMatrix();
    }
    private void edificioquefaltaba(){
        g_modelMatrix_translate(1700,0,-500);
        //parte delantera del edificio
        for (int i=0;i<9;i++){
            if(i!=4){
                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100+(-100*i),0,0);
                prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_paredoficina);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100+(-100*i),0,-300);
                prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro,  textura_paredoficina);
                g_modelMatrix = popMatrix();
            }
        }
        for (int i=0;i<9;i++) {
            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-100+(-100*i),0,-100);
            piso_o_techo(gll, u_MvpMatrix, 100, 50, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-100+(-100*i),0,-250);
            piso_o_techo(gll, u_MvpMatrix, 100, 50, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-100+(-100*i),50.1,-100);
            piso_o_techo(gll, u_MvpMatrix, 100, 50, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-100+(-100*i),50.1,-250);
            piso_o_techo(gll, u_MvpMatrix, 100, 50, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();

            if(i==4){
                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100+(-100*i),0,-300);
                piso_o_techo(gll, u_MvpMatrix, 100, 100, imagen_pisoupv, textura_pisoupv);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-120+(-100*i),0,-100);
                piso_o_techo(gll, u_MvpMatrix, 160, 200, imagen_pisoupv, textura_pisoupv);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100+(-100*i),0,0);
                piso_o_techo(gll, u_MvpMatrix, 100, 100, imagen_pisoupv, textura_pisoupv);
                g_modelMatrix = popMatrix();

                //
                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100+(-100*i),50.1,-300);
                piso_o_techo(gll, u_MvpMatrix, 100, 100, imagen_pisoupv, textura_pisoupv);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-120+(-100*i),50.1,-100);
                piso_o_techo(gll, u_MvpMatrix, 160, 200, imagen_pisoupv, textura_pisoupv);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100+(-100*i),50.1,0);
                piso_o_techo(gll, u_MvpMatrix, 100, 100, imagen_pisoupv, textura_pisoupv);
                g_modelMatrix = popMatrix();
            }
            if(i==0||i==8){
                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100+(-100*i),0,-100);
                piso_o_techo(gll, u_MvpMatrix, 100, 200, imagen_pisoupv, textura_pisoupv);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100+(-100*i),50.1,-100);
                piso_o_techo(gll, u_MvpMatrix, 100, 200, imagen_pisoupv, textura_pisoupv);
                g_modelMatrix = popMatrix();
            }
        }
        //parte trazera del edificio
        for (int i=0;i<9;i++){



            if(i==1||i==7 ){
                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100+(-100*i),0,-500);
                prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100+(-100*i),0,-1300);
                prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-50+(-100*i),0,-1299.9);
                pared_X(gll,u_MvpMatrix,50,50,imagen_blanco,textura_puertaiti);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-50+(-100*i),0,-600.1);
                pared_X(gll,u_MvpMatrix,50,50,imagen_blanco,textura_puertabano);
                g_modelMatrix = popMatrix();
            }

            if(i<8){
                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100.1,0,-600+(-100*i));
                pared_Z(gll,u_MvpMatrix,50,50,imagen_blanco,textura_puertaiti);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-799.9,0,-600+(-100*i));
                pared_Z(gll,u_MvpMatrix,50,50,imagen_blanco,textura_puertaiti);
                g_modelMatrix = popMatrix();

                if(i!=0 && i!=2 && i!=4 && i<6){
                    pushMatrix(g_modelMatrix);
                    g_modelMatrix_translate(-700.1,0,-600+(-100*i));
                    pared_Z(gll,u_MvpMatrix,50,50,imagen_blanco,textura_puertaiti);
                    g_modelMatrix = popMatrix();

                    pushMatrix(g_modelMatrix);
                    g_modelMatrix_translate(-199.9,0,-600+(-100*i));
                    pared_Z(gll,u_MvpMatrix,50,50,imagen_blanco,textura_puertaiti);
                    g_modelMatrix = popMatrix();
                }
            }
            if(i!=1&&i!=7 ) {




                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100 + (-100 * i), 0, -700);
                prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100 + (-100 * i), 0, -900);
                prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100 + (-100 * i), 0, -1100);
                prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
                g_modelMatrix = popMatrix();
            }

        }
        //salones
        for (int i=0;i<7;i++){
            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-900,0,-600+(-100*i));
            prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
            g_modelMatrix = popMatrix();
            pushMatrix(g_modelMatrix);

            g_modelMatrix_translate(-100,0,-600+(-100*i));
            prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
            g_modelMatrix = popMatrix();
        }
        //pisos de la parte trera de este edificio
        for (int i=0;i<9;i++){
            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-100+(-100*i),0,-600);
            piso_o_techo(gll, u_MvpMatrix, 100, 200, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-100+(-100*i),0,-1200);
            piso_o_techo(gll, u_MvpMatrix, 100, 200, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-200,0,-600+(-100*i));
            piso_o_techo(gll, u_MvpMatrix, 100, 200, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-800,0,-600+(-100*i));
            piso_o_techo(gll, u_MvpMatrix, 100, 200, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();
            ////
            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-100+(-100*i),50.1,-600);
            piso_o_techo(gll, u_MvpMatrix, 100, 200, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-100+(-100*i),50.1,-1200);
            piso_o_techo(gll, u_MvpMatrix, 100, 200, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-200,50.1,-600+(-100*i));
            piso_o_techo(gll, u_MvpMatrix, 100, 200, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-800,50.1,-600+(-100*i));
            piso_o_techo(gll, u_MvpMatrix, 100, 200, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();
        }
        ///Seccion de paredes en Z
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-900,0,-100);
        pared_Z(gll,u_MvpMatrix,50,50,imagen_blanco,textura_grispymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-900,40,-50);
        pared_Z(gll,u_MvpMatrix,200,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-900,0,-250);
        pared_Z(gll,u_MvpMatrix,50,50,imagen_blanco,textura_grispymes);
        g_modelMatrix = popMatrix();
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-0,0,-100);
        pared_Z(gll,u_MvpMatrix,50,50,imagen_blanco,textura_grispymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-0,40,-50);
        pared_Z(gll,u_MvpMatrix,200,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-0,0,-250);
        pared_Z(gll,u_MvpMatrix,50,50,imagen_blanco,textura_grispymes);
        g_modelMatrix = popMatrix();
        ///



        //pasillo de la entrada
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-400,0,-100);
        pared_Z(gll,u_MvpMatrix,50,50,imagen_blanco,textura_cafe);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-400,40,-100);
        pared_Z(gll,u_MvpMatrix,200,10,imagen_blanco,textura_cafe);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-400,0,-250);
        pared_Z(gll,u_MvpMatrix,50,50,imagen_blanco,textura_cafe);
        g_modelMatrix = popMatrix();



        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,0,-100);
        pared_Z(gll,u_MvpMatrix,50,50,imagen_blanco,textura_cafe);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,40,-100);
        pared_Z(gll,u_MvpMatrix,200,10,imagen_blanco,textura_cafe);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,0,-250);
        pared_Z(gll,u_MvpMatrix,50,50,imagen_blanco,textura_cafe);
        g_modelMatrix = popMatrix();


        ///
        ///pilares de los cubos y pasillo
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-110,0,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-360,0,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
///////////////////////////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-360,0,-150);
        pared_Z(gll,u_MvpMatrix,100,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-100,0,-150);
        pared_Z(gll,u_MvpMatrix,100,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-360,0,-150);
        pared_X(gll,u_MvpMatrix,250,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-360,0,-250);
        pared_X(gll,u_MvpMatrix,250,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();
        //////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-360,40,-150);
        pared_Z(gll,u_MvpMatrix,100,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-100,40,-150);
        pared_Z(gll,u_MvpMatrix,100,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-360,40,-150);
        pared_X(gll,u_MvpMatrix,250,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-360,40,-250);
        pared_X(gll,u_MvpMatrix,250,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();
/////

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-360,0.1,-150);
        piso_o_techo(gll, u_MvpMatrix, 260, 100, imagen_pisoupv, textura_gindo);
        g_modelMatrix = popMatrix();



        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-110,0,-240);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-360,0,-240);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();



        ///pilares de los cubos y pasillo
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-530,0,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-800,0,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-800,40,-150);
        pared_Z(gll,u_MvpMatrix,100,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-520,40,-150);
        pared_Z(gll,u_MvpMatrix,100,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-800,40,-150);
        pared_X(gll,u_MvpMatrix,280,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-800,40,-250);
        pared_X(gll,u_MvpMatrix,280,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();
        ////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-800,0,-150);
        pared_Z(gll,u_MvpMatrix,100,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-520,0,-150);
        pared_Z(gll,u_MvpMatrix,100,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-800,0,-150);
        pared_X(gll,u_MvpMatrix,280,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-800,0,-250);
        pared_X(gll,u_MvpMatrix,280,10,imagen_blanco,textura_azulindigo);
        g_modelMatrix = popMatrix();
///////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-800,0.1,-150);
        piso_o_techo(gll, u_MvpMatrix, 280, 100, imagen_pisoupv, textura_gindo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-530,0,-240);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-800,0,-240);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        /////////////////////////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-400,0,-600);
        pared_X(gll,u_MvpMatrix,300,50,imagen_blanco,textura_grispymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,0.1,-400);
        piso_o_techo(gll, u_MvpMatrix, 100, 200, imagen_pisoupv, textura_gindo);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,50.1,-400);
        piso_o_techo(gll, u_MvpMatrix, 100, 200, imagen_pisoupv, textura_grispymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-800,0,-600);
        pared_X(gll,u_MvpMatrix,300,50,imagen_blanco,textura_grispymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,0,-600);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,0,-550);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,0,-450);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-400,0,-400);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-400,0,-450);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-400,0,-550);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,0,-400);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-400,0,-600);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        //pilares para mas estetica
        for (int i=0;i<9;i++){
            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-100+(-100*i),0,-100);
            prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-100+(-100*i),0,-290);
            prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
            g_modelMatrix = popMatrix();

            if(i<7){
                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-110,0,-600+(-100*i));
                prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-700,0,-590+(-100*i));
                prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
                g_modelMatrix = popMatrix();



                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-200,0,-600+(-100*i));
                prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-300,0,-590+(-100*i));
                prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
                g_modelMatrix = popMatrix();

                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-400,0,-600+(-100*i));
                prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
                g_modelMatrix = popMatrix();
                pushMatrix(g_modelMatrix);

                g_modelMatrix_translate(-500,0,-590+(-100*i));
                prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
                g_modelMatrix = popMatrix();
                pushMatrix(g_modelMatrix);

                g_modelMatrix_translate(-600,0,-600+(-100*i));
                prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
                g_modelMatrix = popMatrix();
                pushMatrix(g_modelMatrix);

                g_modelMatrix_translate(0,0,-590+(-100*i));
                prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
                g_modelMatrix = popMatrix();
                pushMatrix(g_modelMatrix);

                g_modelMatrix_translate(-800,0,-600+(-100*i));
                prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
                g_modelMatrix = popMatrix();
                pushMatrix(g_modelMatrix);
            }

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-800,0,-1300);
            pared_X(gll,u_MvpMatrix,800,10,imagen_blanco,textura_grispymes);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-800,40,-1300);
            pared_X(gll,u_MvpMatrix,800,10,imagen_blanco,textura_grispymes);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-700,0.2,-800);
            piso_o_techo(gll, u_MvpMatrix, 500, 100, imagen_pisoupv, textura_gindo);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(-700,0.2,-1000);
            piso_o_techo(gll, u_MvpMatrix, 500, 100, imagen_pisoupv, textura_gindo);
            g_modelMatrix = popMatrix();

        }

        for(int i=0;i<9;i++){
            if(i!=4){
                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100+(-100*i),0,-100.1);
                pared_X(gll,u_MvpMatrix,50,50,imagen_blanco,textura_puertaiti);
                g_modelMatrix = popMatrix();


                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(-100+(-100*i),0,-299.9);
                pared_X(gll,u_MvpMatrix,50,50,imagen_blanco,textura_puertaiti);
                g_modelMatrix = popMatrix();
            }

        }

    }
    private void edificioA(){

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,0,0);
        pared_X(gll,u_MvpMatrix,50,50,imagen_blanco,textura_blanco);
        g_modelMatrix = popMatrix();
        ///

        ////Torre de escaleras
        //peredes
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-50,0,-50);
        //pared fuera de las escaleras oficinas
        pared_X(gll,u_MvpMatrix,50,100,imagen_blanco,textura_blanco);
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-50,0,-100);
        pared_X(gll,u_MvpMatrix,50,100,imagen_blanco,textura_blanco);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-50,0,-50);
        pared_Z(gll,u_MvpMatrix,50,100,imagen_gris,textura_gris);
        g_modelMatrix = popMatrix();
        //piso
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-50,25,-50);
        piso_o_techo(gll, u_MvpMatrix, 25, 50, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-25,0,-75);
        pared_X(gll,u_MvpMatrix,25,100,imagen_grisclaro,textura_grisclaro);
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,0,-75);
        pared_Z(gll,u_MvpMatrix,25,50,imagen_gris ,textura_gris );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,50,-50);
        pared_Z(gll,u_MvpMatrix,25,50,imagen_gris ,textura_gris );
        g_modelMatrix = popMatrix();
        //escaleras
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,0,-50);
        pared_Z(gll,u_MvpMatrix,25,8,imagen_grisclaro ,textura_grisclaro );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-8,8,-50);
        piso_o_techo(gll, u_MvpMatrix,  8, 25, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        //2da
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-8,8,-50);
        pared_Z(gll,u_MvpMatrix,25,8,imagen_gris  ,textura_gris  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-16,16,-50);
        piso_o_techo(gll, u_MvpMatrix,  8, 25, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        //3ra
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-16,16,-50);
        pared_Z(gll,u_MvpMatrix,25,9,imagen_gris  ,textura_gris  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-25,25,-50);
        piso_o_techo(gll, u_MvpMatrix,  9, 25, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        //4ta
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-25,25,-75);
        pared_Z(gll,u_MvpMatrix,25,9,imagen_gris  ,textura_gris  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-25,33,-75);
        piso_o_techo(gll, u_MvpMatrix,  9, 25, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        //5ta
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-16,32,-75);
        pared_Z(gll,u_MvpMatrix,25,9,imagen_gris  ,textura_gris  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-16,41,-75);
        piso_o_techo(gll, u_MvpMatrix,  8, 25, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        //6ta
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-8,41,-75);
        pared_Z(gll,u_MvpMatrix,25,9.5,imagen_grisclaro ,textura_grisclaro );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-8,50.5,-75);
        piso_o_techo(gll, u_MvpMatrix,  9, 25, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        ////////Fin de Torre de escaleras
        /////////////////////////////////////////////////////////////
        //Oficina del Rector
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-100,0,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
        g_modelMatrix = popMatrix();
        //cubiculos de arriba del rector
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-100,50,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_blanco, textura_blanco);
        g_modelMatrix = popMatrix();
        //cuartos
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(50,0,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 50, 50, 50, imagen_blanco, textura_blanco);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100,0,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 110, 110, 50, imagen_grisclaro, textura_grisclaro);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200,0,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_blanco, textura_blanco);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300,0,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,0,0);
        prisma_cuadrangularLOBY(gll, u_MvpMatrix, 100, 100, 50, imagen_loby_pared, textura_blanco);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600,0,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700,0,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_blanco, textura_blanco);
        g_modelMatrix = popMatrix();
        //////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,0,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_blanco, textura_blanco);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600,0,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700,0,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_blanco, textura_blanco);
        g_modelMatrix = popMatrix();
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,0,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_gris, textura_gris);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(150,0,-140);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 30, 50, imagen_blanco, textura_blanco);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300,0,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
        g_modelMatrix = popMatrix();
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100,0,-220);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 30, 50, imagen_blanco, textura_blanco);
        g_modelMatrix = popMatrix();
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200,0,-220);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 30, 50, imagen_gris, textura_gris);
        g_modelMatrix = popMatrix();
        ///PISO DEL PRIMER PISO
        for (int i = 0; i <= 800; i++){
            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(i,0.25,0);
            piso_o_techo(gll, u_MvpMatrix, 100, 70, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(i,0.25,-70);
            piso_o_techo(gll, u_MvpMatrix, 100, 70, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(i,0.25,-120);
            piso_o_techo(gll, u_MvpMatrix, 100, 130, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();
            //parte del suelo superior
            if(i<400||(i>500 && i<800 )){
                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(i,50.5,0);
                piso_o_techo(gll, u_MvpMatrix, 100, 100, imagen_pisoupv, textura_pisoupv);
                g_modelMatrix = popMatrix();
                //
                if (i>400 &&i<500) {
                }

            }
            if(i<800){
                pushMatrix(g_modelMatrix);
                g_modelMatrix_translate(i,100.1,0);
                piso_o_techo(gll, u_MvpMatrix, 100, 100, imagen_pisoupv, textura_pisoupv);
                g_modelMatrix = popMatrix();

            }

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(i,50.5,-100);
            piso_o_techo(gll, u_MvpMatrix, 100, 150, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();
            //
            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(i,100.2,-100);
            piso_o_techo(gll, u_MvpMatrix, 100, 150, imagen_pisoupv, textura_pisoupv);
            g_modelMatrix = popMatrix();
            i=i+99;

            //techo que cubre el segundo piso
        }
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-50,0.1,-50);
        piso_o_techo(gll, u_MvpMatrix, 50, 50, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();




        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400,50.5,-60);
        piso_o_techo(gll,u_MvpMatrix, 100, 40, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400,50,-60);
        pared_X(gll,u_MvpMatrix, 100, 10, imagen_grisclaro, textura_grisclaro);
        g_modelMatrix = popMatrix();
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,50,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 50, 50, imagen_gris , textura_gris );
        g_modelMatrix = popMatrix();
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100,50,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 110, 100, 50, imagen_gris, textura_gris );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200,50,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300,50,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_blanco, textura_blanco);
        g_modelMatrix = popMatrix();
        //
        //se omite esta para el balcon de iti
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,50,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_gris, textura_gris);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600,50,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_blanco, textura_blanco);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700,50,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
        g_modelMatrix = popMatrix();
        /////////////////////////////////////////////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,50,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 200, 100, 50, imagen_grisclaro, textura_grisclaro );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200,50,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_gris, textura_gris );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300,50,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_blanco, textura_blanco);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400,50,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_grisclaro, textura_grisclaro);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,50,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_blanco, textura_blanco);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600,50,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_gris, textura_gris);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700,50,-150);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 100, 50, imagen_blanco, textura_blanco);
        g_modelMatrix = popMatrix();
        //
        ////Torre de escaleras2//////////////////////////////////////////////////////////////////////////////////////////////////////////
        //peredes
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(900,0,-150);
        pared_X(gll,u_MvpMatrix,50,100,imagen_blanco,textura_blanco);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(900,0,-200);
        pared_X(gll,u_MvpMatrix,50,100,imagen_blanco,textura_blanco);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(950,0,-150);
        pared_Z(gll,u_MvpMatrix,50,100,imagen_gris,textura_gris);
        g_modelMatrix = popMatrix();
        //piso
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(925,25,-150);
        piso_o_techo(gll, u_MvpMatrix, 25, 50, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(900,0,-175);
        pared_X(gll,u_MvpMatrix,25,100,imagen_grisclaro,textura_grisclaro);
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(900,0,-150);
        pared_Z(gll,u_MvpMatrix,25,50,imagen_gris ,textura_gris );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(900,50,-175);
        pared_Z(gll,u_MvpMatrix,25,50,imagen_gris ,textura_gris );
        g_modelMatrix = popMatrix();
        //escaleras
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(900,0,-175);
        pared_Z(gll,u_MvpMatrix,25,8,imagen_grisclaro ,textura_grisclaro );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(900,8,-175);
        piso_o_techo(gll, u_MvpMatrix,  8, 25, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        //2da
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(908,8,-175);
        pared_Z(gll,u_MvpMatrix,25,8,imagen_gris  ,textura_gris  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(908,16,-175);
        piso_o_techo(gll, u_MvpMatrix,  8, 25, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        //3ra
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(916,16,-175);
        pared_Z(gll,u_MvpMatrix,25,9,imagen_gris  ,textura_gris  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(916,25,-175);
        piso_o_techo(gll, u_MvpMatrix,  9, 25, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        //4ta
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(925,25,-150);
        pared_Z(gll,u_MvpMatrix,25,9,imagen_gris  ,textura_gris  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(916,33,-150);
        piso_o_techo(gll, u_MvpMatrix,  9, 25, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        //5ta
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(916,32,-150);
        pared_Z(gll,u_MvpMatrix,25,9,imagen_gris  ,textura_gris  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(908,41,-150);
        piso_o_techo(gll, u_MvpMatrix,  8, 25, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        //6ta
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(908,41,-150);
        pared_Z(gll,u_MvpMatrix,25,9.5,imagen_grisclaro ,textura_grisclaro );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(900,50.5,-150);
        piso_o_techo(gll, u_MvpMatrix,  9, 25, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();
        ////////Fin de Torre de escaleras
        /////////////////////////////////////////////////////////////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(800,50,-100);
        pared_X(gll,u_MvpMatrix,100,50,imagen_gris  ,textura_gris  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(800,50,-180);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 70, 50, imagen_gris, textura_gris );
        g_modelMatrix = popMatrix();
        ///Parte del elevador
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(900.5,0,-99.9);
        pared_X(gll,u_MvpMatrix,10,100,imagen_grisclaro  ,textura_grisclaro  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(940.5,0,-99.9);
        pared_X(gll,u_MvpMatrix,10,100,imagen_grisclaro  ,textura_grisclaro  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(950.5,0,-100);
        pared_Z(gll,u_MvpMatrix,10,100,imagen_grisclaro  ,textura_grisclaro  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(950.5,0,-140);
        pared_Z(gll,u_MvpMatrix,10,100,imagen_grisclaro  ,textura_grisclaro  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(900,0,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 50, 50, 50, imagen_gris, textura_gris );
        g_modelMatrix = popMatrix();
        ////techos faltantes
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(900,100,-100);
        piso_o_techo(gll, u_MvpMatrix, 50, 100, imagen_grisclaro, textura_grisclaro);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-50,100,-50);
        piso_o_techo(gll, u_MvpMatrix, 50, 50, imagen_grisclaro, textura_grisclaro);
        g_modelMatrix = popMatrix();
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,0,100);
        pared_X(gll,u_MvpMatrix,90,100,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(170,0,100);
        pared_X(gll,u_MvpMatrix,60,100,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(310,0,100);
        pared_X(gll,u_MvpMatrix,90,100,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        /////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,0,100);

        // PARED DEL LOGO
        pared_X(gll,u_MvpMatrix,90,100,imagen_azulindigo  ,textura_logo_ext  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(670,0,100);
        pared_X(gll,u_MvpMatrix,60,100,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(810,0,100);
        pared_X(gll,u_MvpMatrix,90,100,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,0,-340);
        pared_X(gll,u_MvpMatrix,90,100,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(170,0,-340);
        pared_X(gll,u_MvpMatrix,60,100,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(310,0,-340);
        pared_X(gll,u_MvpMatrix,90,100,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,0,-340);
        pared_X(gll,u_MvpMatrix,90,100,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(670,0,-340);
        pared_X(gll,u_MvpMatrix,60,100,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(810,0,-340);
        pared_X(gll,u_MvpMatrix,90,100,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        ////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,90,100);
        pared_X(gll,u_MvpMatrix,400,10,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,70,100);
        pared_X(gll,u_MvpMatrix,400,10,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,90,100);
        pared_X(gll,u_MvpMatrix,400,10,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,70,100);
        pared_X(gll,u_MvpMatrix,400,10,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,90,-340);
        pared_X(gll,u_MvpMatrix,400,10,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,70,-340);
        pared_X(gll,u_MvpMatrix,400,10,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,90,-340);
        pared_X(gll,u_MvpMatrix,400,10,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,70,-340);
        pared_X(gll,u_MvpMatrix,400,10,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,70,-340);
        pared_X(gll,u_MvpMatrix,400,10,imagen_entradaPrincipal  ,textura_entradaPrincipal  );
        g_modelMatrix = popMatrix();
        ///

        for (int i = 0; i <800; i++) {

            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(i,100,50);
            piso_o_techo(gll, u_MvpMatrix, 99, 50, imagen_techoiti, textura_techoiti);
            g_modelMatrix = popMatrix();
            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(i,100,100);
            piso_o_techo(gll, u_MvpMatrix, 100, 50, imagen_techoiti, textura_techoiti);
            g_modelMatrix = popMatrix();
            ////////////
            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(i,100,-290);
            piso_o_techo(gll, u_MvpMatrix, 99, 50, imagen_techoiti, textura_techoiti);
            g_modelMatrix = popMatrix();
            pushMatrix(g_modelMatrix);
            g_modelMatrix_translate(i,100,-240);
            piso_o_techo(gll, u_MvpMatrix, 100, 50, imagen_techoiti, textura_techoiti);
            g_modelMatrix = popMatrix();
            i=i+99;
        }

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,70,-340);
        pared_X(gll,u_MvpMatrix,400,10,imagen_azulindigo  ,textura_azulindigo  );
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0.1,0,-110);
        pared_Z(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0.1,50,-110);
        pared_Z(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,50,-51);
        pared_X(gll,u_MvpMatrix,100,50,imagen_tutorias  ,textura_tutorias  );
        g_modelMatrix = popMatrix();
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100,50,-100.2);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200,50,-100.2);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300,50,-100.2);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        ////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,50,-100.2);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600,50,-100.2);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700,50,-100.2);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        ////////////////////////////////////////////////////////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100,50,-149.9);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200,50,-149.9);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300,50,-149.9);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        ///

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400,50,-149.9);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300,0,-100.2);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        ////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,50,-149.9);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600,50,-149.9);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700,50,-149.9);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(800,50,-179);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        /////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(60,0,-149.9);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(160,0,-219.9);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(260,0,-219.9);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(299.8,0,-150);
        pared_Z(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        //////

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600,0,-100.2);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700,0,-100.2);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500, 0,-149.9);
        pared_X(gll,u_MvpMatrix,40,50,imagen_loby_planta  ,textura_loby_planta  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600, 0,-149.9);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700, 0,-149.9);
        pared_X(gll,u_MvpMatrix,40,50,imagen_puertaiti  ,textura_puertaiti  );
        g_modelMatrix = popMatrix();
        ///imagen_ventanaiti2
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0, 80,-149.99);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100, 80,-149.99);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200, 80,-149.99);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300, 80,-149.99);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400, 80,-149.99);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500, 80,-149.99);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600, 80,-149.99);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700, 80,-149.99);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        //////////////////////////////////////////////////////////////////////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0, 80,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100, 80,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200, 80,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300, 80,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400, 80,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500, 80,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600, 80,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700, 80,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        ////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0, 30,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100, 30,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200, 30,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300, 30,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400, 30,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500, 30,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600, 30,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700, 30,-250.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        ////////////////////////////////////////////////////////////////////////////////////////////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0, 80,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100, 80,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200, 80,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300, 80,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500, 80,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600, 80,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700, 80,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        ////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0, 30,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100, 30,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200, 30,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300, 30,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400, 30,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500, 30,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600, 30,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700, 30,0.1);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        ////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400, 0,0);
        pared_X(gll,u_MvpMatrix,100,50,imagen_entradaPrincipal  ,textura_entradaPrincipal  );
        g_modelMatrix = popMatrix();
        ////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100, 80,-100.001);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200, 80,-100.001);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300, 80,-100.001);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
//
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500, 80,-100.001);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600, 80,-100.001);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700, 80,-100.001);
        pared_X(gll,u_MvpMatrix,100,20,imagen_ventanaiti2  ,textura_ventanaiti2  );
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,50,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100,50,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200,50,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300,50,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,50,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        /////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600,50,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700,50,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        //
        g_modelMatrix_translate(100,50,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200,50,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300,50,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400,50,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,50,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600,50,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700,50,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        ///////////////////////////////////////////////////////////////
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100,0,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200,0,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300,0,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400,0,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,0,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600,0,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700,0,-145);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100,0,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200,0,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300,0,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400,0,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,0,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600,0,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700,0,-100);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        ///
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0,0,10);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100,0,10);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200,0,10);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300,0,10);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400,0,10);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,0,10);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600,0,10);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700,0,10);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(800,0,10);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100,0,-250);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200,0,-250);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(300,0,-250);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(400,0,-250);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(500,0,-250);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(600,0,-250);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(700,0,-250);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();
        //
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(800,0,-250);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        // pushMatrix(g_modelMatrix); imagen_entradaPrincipal
        //g_modelMatrix_translate(0,-0.15,0);
        //piso_o_techo(gll, u_MvpMatrix, 800, 300, imagen_pisoupv, textura_pisoupv);
        //g_modelMatrix = popMatrix();
        ////AQUI TERMINA EL PRIMER PISO
    }

    private void edificioB(){
        g_modelMatrix_translate(1500,0,0);
        g_modelMatrix_rotate(180,0,1,0);

        //edificio
        pushMatrix(g_modelMatrix);
        prisma_cuadrangular(gll, u_MvpMatrix, 300, 400, 100, imagen_grispymes, textura_grispymes);
        g_modelMatrix = popMatrix();

        //PUERTA PRINCIPAL
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100.0, 0.0, 0.1);
        pared_X(gll, u_MvpMatrix, 100, 80, imagen_puertaPymes, textura_puertaPymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(30.0, 10.0,0.1);
        pared_X(gll, u_MvpMatrix, 50,50, imagen_ventanapymes, textura_ventanapymesve);
        g_modelMatrix = popMatrix();

        //SALITA PRIMERO A LA IZQUIERDA
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(50.0, 0.0,0.0);
        pared_Z(gll, u_MvpMatrix, 80, 50, imagen_cristal, textura_cristal);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0.0, 0.0, -80.0);
        pared_X(gll, u_MvpMatrix, 50, 50, imagen_blancopymes, textura_blancopymes);
        g_modelMatrix = popMatrix();

        //BAÑOS
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(250.0, 0.0,0.0);
        pared_Z(gll, u_MvpMatrix, 15, 50, imagen_blancopymes, textura_blancopymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(250.0, 0.0, -15.0);
        pared_X(gll, u_MvpMatrix, 50, 50, imagen_puertabano, textura_puertabano);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(250.0, 0.0,-30.0);
        pared_Z(gll, u_MvpMatrix, 15, 50, imagen_puertabano, textura_puertabano);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(250.0, 0.0, -30.0);
        pared_X(gll, u_MvpMatrix, 50, 50, imagen_puertabano, textura_puertabano);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(250.0, 0.0, -45.0);
        pared_X(gll, u_MvpMatrix, 50, 50, imagen_crema, textura_crema);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(280.0, 0.0,-15.0);
        pared_Z(gll, u_MvpMatrix, 15, 50, imagen_blancopymes, textura_blancopymes);
        g_modelMatrix = popMatrix();

        //Oficina ESCOLARES
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200.0, 0.0,-45.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 80, 50, imagen_pared5, textura_pared5);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(199.9, 0.0,-45.0);
        pared_Z(gll, u_MvpMatrix, 80, 50, imagen_escolares, textura_escolares);
        g_modelMatrix = popMatrix();

        //Oficina alado de CAI
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100.0, 0.0,-199.9);
        prisma_cuadrangular(gll, u_MvpMatrix, 50, 200, 50, imagen_grispymes, textura_grispymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100.0, 0.0,-199.8);
        pared_X(gll, u_MvpMatrix, 50, 50, imagen_morado, textura_morado);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100.0, 0.0,-155.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 30, 2, 10, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(100.0, 10.0,-149.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 35, 10, 1, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(150.5, 0.0,-210.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertas, textura_puertas);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(150.5, 0.0,-245.0);
        pared_Z(gll, u_MvpMatrix, 65, 35, imagen_pasillooficina, textura_pasillooficina);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(150.9, 0.0,-399.0);
        pared_X(gll, u_MvpMatrix, 50, 50, imagen_puertasalida, textura_puertasalida);
        g_modelMatrix = popMatrix();


        //BARANDAL
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(50.0, 50.0,-80.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 200, 5, 5, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        //CAI
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0.1, 0.0,-149.9);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 250, 50, imagen_pared5, textura_pared5);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0.0, 0.0,-149.8);
        pared_X(gll, u_MvpMatrix, 80, 50, imagen_cai, textura_cai);
        g_modelMatrix = popMatrix();

        //Dentro de cai
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0.1, 0.0,-280.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 35, 120, 50, imagen_grispymes, textura_grispymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix); ///////////////
        g_modelMatrix_translate(35.9, 0.0,-365.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertabano, textura_puertabano);
        g_modelMatrix = popMatrix();



        //ESCALERAS
        //pared
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0.0, 0.0,-130.0);
        pared_X(gll, u_MvpMatrix, 50, 50, imagen_blancopymes, textura_blancopymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(50.0, 0.0,-105.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(48.0, 2.0,-105.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(48.0, 2.0,-105.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(46.0, 4.0,-105.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(46.0, 4.0,-105.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(44.0, 6.0,-105.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(44.0, 6.0,-105.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(42.0, 8.0,-105.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(42.0, 8.0,-105.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(40.0, 10.0,-105.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(40.0, 10.0,-105.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(38.0, 12.0,-105.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(38.0, 12.0,-105.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(36.0, 14.0,-105.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(36.0, 14.0,-105.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(34.0, 16.0,-105.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(34.0, 16.0,-105.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(32.0, 18.0,-105.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(32.0, 18.0,-105.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(30.0, 20.0,-105.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(30.0, 20.0,-105.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(28.0, 22.0,-105.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(28.0, 22.0,-105.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(26.0, 24.0,-105.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(26.0, 24.0,-105.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0.0, 26.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 26, 45, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(26.0, 26.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(26.0, 28.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(28.0, 28.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(28.0, 30.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(30.0, 30.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(30.0, 32.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(32.0, 32.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(32.0, 34.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(34.0, 34.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(34.0, 36.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(36.0, 36.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(36.0, 38.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(38.0, 38.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(38.0, 40.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(40.0, 40.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(40.0, 42.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(42.0, 42.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(42.0, 42.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(44.0, 42.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(44.0, 44.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(46.0, 44.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(46.0, 46.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(48.0, 46.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(48.0, 48.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(50.0, 48.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 25, 2, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(50.0, 50.0,-80.0);
        piso_o_techo(gll, u_MvpMatrix, 2, 25, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        //MESA OFICINA
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200.0, 0.0,-125.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 2, 25, 10, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(197.0, 10.0,-125.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 5, 30, 1, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200.0, 0.0,-150.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 20, 2, 10, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200.0, 10.0,-150.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 20, 5, 1, imagen_madera, textura_madera);
        g_modelMatrix = popMatrix();

        //Oficina camelina
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(200.0, 0.0,-220.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 180, 50, imagen_grispymes, textura_grispymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(210.0, 15.0, -219.9);
        pared_X(gll, u_MvpMatrix, 15,15, imagen_ventanapymes, textura_ventanapymesve);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix); ///////////////
        g_modelMatrix_translate(230.0, 0.0,-219.9);
        pared_X(gll, u_MvpMatrix, 15, 35, imagen_puertas, textura_puertas);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix); ///////////////
        g_modelMatrix_translate(275.0, 0.0,-219.9);
        pared_X(gll, u_MvpMatrix, 15, 35, imagen_puertas, textura_puertas);
        g_modelMatrix = popMatrix();

        //baño camelina
        pushMatrix(g_modelMatrix); ///////////////
        g_modelMatrix_translate(299.9, 0.0,-175.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertabano, textura_puertabano);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix); ///////////////
        g_modelMatrix_translate(299.9, 0.0,-140.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertabano, textura_puertabano);
        g_modelMatrix = popMatrix();

        //Maquina
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(45.0, 0.0,-80.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 20, imagen_cafe, textura_cafe);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(55.2, 0.0,-80.0);
        pared_Z(gll, u_MvpMatrix, 10, 20, imagen_maquina, textura_maquina);
        g_modelMatrix = popMatrix();

        //PISO
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0.0, 0.1,0.0);
        piso_o_techo(gll, u_MvpMatrix, 300, 400, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();

        //2do PISO
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(50.0, 50.5, -80.0);
        piso_o_techo(gll, u_MvpMatrix, 250, 320, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0.0, 50.5, -350.0);
        piso_o_techo(gll, u_MvpMatrix, 50, 50, imagen_pisoupv, textura_pisoupv);
        g_modelMatrix = popMatrix();

        //Sala maestros 2do piso
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(250.0, 51.0,-80.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 50, 320, 50, imagen_grispymes, textura_grispymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(249.0, 51.0,-83.0);
        pared_Z(gll, u_MvpMatrix, 80, 50, imagen_salamaestros, textura_porbiblioteca);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(249.0, 51.0,-170.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertas, textura_puertas);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(249.0, 51.0,-200.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertas, textura_puertas);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(249.0, 51.0,-240.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertas, textura_puertas);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(249.0, 51.0,-280.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertas, textura_puertas);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(249.0, 51.0,-315.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertas, textura_puertas);
        g_modelMatrix = popMatrix();


        //Salones 2do piso
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(150.0, 51.0,-150.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 50, 250, 50, imagen_grispymes, textura_grispymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(201.0, 51.0,-160.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertas, textura_puertas);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(201.0, 51.0,-195.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertas, textura_puertas);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(201.0, 51.0,-230.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertas, textura_puertas);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(201.0, 51.0,-265.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertas, textura_puertas);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(201.0, 51.0,-300.0);
        pared_Z(gll, u_MvpMatrix, 15, 35, imagen_puertas, textura_puertas);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(201.0, 51.0,-399.0);
        pared_X(gll, u_MvpMatrix, 50, 50, imagen_puertasalida, textura_puertasalida);
        g_modelMatrix = popMatrix();

        //Oficinas 2do piso
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(80.0, 51.0,-210.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 70, 180, 50, imagen_grispymes, textura_grispymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(80.0, 51.0,-209.0);
        pared_X(gll, u_MvpMatrix, 80, 50, imagen_porbiblioteca, textura_porbiblioteca);
        g_modelMatrix = popMatrix();

        //Biblioteca
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0.0, 51.0,-200.0);
        prisma_cuadrangular(gll, u_MvpMatrix, 80, 200, 50, imagen_grispymes, textura_grispymes);
        g_modelMatrix = popMatrix();

        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(0.0, 51.0,-199.0);
        pared_X(gll, u_MvpMatrix, 80, 50, imagen_biblioteca, textura_biblioteca);
        g_modelMatrix = popMatrix();
    }

    private void edificioH(){
        g_modelMatrix_translate(0,0,-500);

//Piso gris
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-175,0,25);
        piso_o_techo(gll, u_MvpMatrix, 350, 150, imagen_gris, textura_gris);

//Prisma azul izquierda
        g_modelMatrix_translate(25,0,-12.5);
        prisma_cuadrangular(gll, u_MvpMatrix, 12.5, 137.5, 50, imagen_azulindigo, textura_azulindigo);

//Cuartote blanco izquierda
        g_modelMatrix_translate(12.5,0,-12.5);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 125, 50, imagen_blanco, textura_blanco);
        g_modelMatrix_translate(0,50.1,0);
        piso_o_techo(gll, u_MvpMatrix, 100, 125, imagen_pisoupv2, textura_pisoupv2);
        g_modelMatrix_translate(0,-50.1,0);

//La pared azul del cuartote blanco izquierda
        g_modelMatrix_translate(100.1,0,0);
        pared_Z(gll, u_MvpMatrix, 125, 50, imagen_azulindigo, textura_azulindigo);
        g_modelMatrix_translate(-100.1,0,0);

//Cuartote blanco derecha
        g_modelMatrix_translate(175,0,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 100, 125, 50, imagen_blanco, textura_blanco);
        g_modelMatrix_translate(0,50.1,0);
        piso_o_techo(gll, u_MvpMatrix, 100, 125, imagen_pisoupv2, textura_pisoupv2);
        g_modelMatrix_translate(0,-50.1,0);

//Prisma azul derecha
        g_modelMatrix_translate(100,0,12.5);
        prisma_cuadrangular(gll, u_MvpMatrix, 12.5, 137.5, 50, imagen_azulindigo, textura_azulindigo);

        g_modelMatrix = popMatrix();



        pushMatrix(g_modelMatrix);
//Base gris 2
        g_modelMatrix_translate(-300,0,-100);
        piso_o_techo(gll, u_MvpMatrix, 600, 75*5.5, imagen_gris, textura_gris);

//Almacen izquierda
        g_modelMatrix_translate(25,0,-25);
        prisma_cuadrangular(gll, u_MvpMatrix, 75, 50, 100, imagen_blanco, textura_blanco);

//Primer salon izquierda
        g_modelMatrix_translate(75,0,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 75, 150, 100, imagen_blanco, textura_blanco);
        g_modelMatrix_translate(0,100.1,0);
        piso_o_techo(gll, u_MvpMatrix, 75, 150, imagen_pisoupv2, textura_pisoupv2);
        g_modelMatrix_translate(0,-100.1,0);



        g_modelMatrix_translate(75,0,-150);

//Pilar 1
        g_modelMatrix_translate(0,0,150);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);

//Ventana1
        g_modelMatrix_translate(0.1,60,-10);
        pared_Z(gll, u_MvpMatrix, 140, 20, imagen_ventanaISA, textura_ventanaISA);
        g_modelMatrix_translate(-0.1,-60,10);

//Puerta1
        g_modelMatrix_translate(0.1,0,0);
        pared_Z(gll, u_MvpMatrix, 25, 35, imagen_puertage, textura_puertage);
        g_modelMatrix_translate(-0.1,0,0);

        g_modelMatrix_translate(0,0,-150);

//Pilar 2
        g_modelMatrix_translate(0,0,10);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix_translate(0,0,-10);

//Segundo salon izquierda---------------------------------------------------
        g_modelMatrix_rotate(-20,0,1,0);
//Pared enfrente
        pared_Z(gll, u_MvpMatrix, 150, 100, imagen_blanco, textura_blanco);

//Ventana2
        g_modelMatrix_translate(0.1,60,-10);
        pared_Z(gll, u_MvpMatrix, 140, 20, imagen_ventanaISA, textura_ventanaISA);
        g_modelMatrix_translate(-0.1,-60,10);

//Puerta 2
        g_modelMatrix_translate(0.1,0,0);
        pared_Z(gll, u_MvpMatrix, 25, 35, imagen_puertage, textura_puertage);
        g_modelMatrix_translate(-0.1,0,0);

        g_modelMatrix_rotate(20,0,1,0);

//Pared atras
        g_modelMatrix_translate(-75,0,0);
        g_modelMatrix_rotate(-30.7,0,1,0);
        pared_Z(gll, u_MvpMatrix, 245, 100, imagen_oro, textura_oro);
        g_modelMatrix_rotate(30.7,0,1,0);

//Techo
        g_modelMatrix_translate(0,100,0);
        piso_o_techo_especial(gll, u_MvpMatrix, 75, 0, 75+50, -136.35-75, 75+50, -136.35, imagen_pisoupv2, textura_pisoupv2);
        g_modelMatrix_translate(0,-100,0);

        g_modelMatrix_translate(75,0,0);

        g_modelMatrix_translate(-75,0,150);

//--------------------------------------------------------------------------

        g_modelMatrix_translate(-75,0,0);
//Primer salon derecha
        g_modelMatrix_translate(400,0,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 75, 150, 100, imagen_blanco, textura_blanco);

//Ventana5
        g_modelMatrix_translate(-0.1,60,-10);
        pared_Z(gll, u_MvpMatrix, 140, 20, imagen_ventanaISA, textura_ventanaISA);
        g_modelMatrix_translate(0.1,-60,10);

//Puerta 5
        g_modelMatrix_translate(-0.1,0,0);
        pared_Z(gll, u_MvpMatrix, 25, 35, imagen_puertage, textura_puertage);
        g_modelMatrix_translate(0.1,0,0);

        g_modelMatrix_translate(0,100.1,0);
        piso_o_techo(gll, u_MvpMatrix, 75, 150, imagen_pisoupv2, textura_pisoupv2);
        g_modelMatrix_translate(0,-100.1,0);

//Pilar 5
        g_modelMatrix_translate(-10,0,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix_translate(10,0,0);

//Segundo salon derecha---------------------------------------------------
        g_modelMatrix_translate(0,0,-150);

//Pilar 6
        g_modelMatrix_translate(-10,0,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix_translate(10,0,0);

//Pared enfrente
        g_modelMatrix_rotate(20, 0,1,0);
        pared_Z(gll, u_MvpMatrix, 150, 100, imagen_blanco, textura_blanco);

//Ventana1
        g_modelMatrix_translate(-0.1,60,-10);
        pared_Z(gll, u_MvpMatrix, 140, 20, imagen_ventanaISA, textura_ventanaISA);
        g_modelMatrix_translate(0.1,-60,10);

//Puerta 6
        g_modelMatrix_translate(-0.1,0,0);
        pared_Z(gll, u_MvpMatrix, 25, 35, imagen_puertage, textura_puertage);
        g_modelMatrix_translate(0.1,0,0);

        g_modelMatrix_rotate(-20,0,1,0);

//Techo
        g_modelMatrix_translate(75,100,0);
        piso_o_techo_especial(gll, u_MvpMatrix, -50-75, -136.35-75, -75, 0, -50-75, -136.35, imagen_pisoupv2, textura_pisoupv2);
        g_modelMatrix_translate(-75,-100,0);

//Pared atras
        g_modelMatrix_translate(75,0,0);
        g_modelMatrix_rotate(30.7,0,1,0);
        pared_Z(gll, u_MvpMatrix, 245, 100, imagen_oro, textura_oro);
        g_modelMatrix_rotate(-30.7,0,1,0);


        g_modelMatrix_translate(-75,0,0);

        g_modelMatrix_translate(0,0,150);

//--------------------------------------------------------------------------

//Almacen derecha
        g_modelMatrix_translate(75,0,0);
        prisma_cuadrangular(gll, u_MvpMatrix, 75, 50, 100, imagen_blanco, textura_blanco);

        g_modelMatrix = popMatrix();

//Salon central
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-75,0,-125-150-136.35);
        prisma_cuadrangular(gll, u_MvpMatrix, 150, 75, 100, imagen_blanco, textura_blanco);

//Ventana3
        g_modelMatrix_translate(10,60,0.2);
        pared_X(gll, u_MvpMatrix, 140, 20, imagen_ventanaISA, textura_ventanaISA);
        g_modelMatrix_translate(-10,-60,-0.2);

//Puerta 3
        g_modelMatrix_translate(0,0,0.1);
        pared_X(gll, u_MvpMatrix, 25, 35, imagen_puertage, textura_puertage);
        g_modelMatrix_translate(0,0,-0.1);

        g_modelMatrix_translate(0,100.1,0);
        piso_o_techo(gll, u_MvpMatrix, 150, 75, imagen_pisoupv2, textura_pisoupv2);
        g_modelMatrix_translate(0,-100.1,0);

//Pilar 3
        g_modelMatrix_translate(0,0,10);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix_translate(0,0,-10);

        g_modelMatrix_translate(150,0,0);

//Pilar 4
        g_modelMatrix_translate(-10,0,10);
        prisma_cuadrangular(gll, u_MvpMatrix, 10, 10, 100, imagen_morado, textura_morado);
        g_modelMatrix_translate(10,0,-10);

        g_modelMatrix = popMatrix();

    }

    // Se crea y posiciona el cielo
    private void cielo(){
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,700,700);
        piso_o_techo(gll, u_MvpMatrix, 3000, 3000, imagen_cielo, textura_cielo);
        g_modelMatrix = popMatrix();
    }

    private void cielo_enfrente(){
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,-200,700);
        pared_X(gll,u_MvpMatrix,3000,1000,imagen_cielo,textura_cielo_enfrente);
        g_modelMatrix = popMatrix();
    }

    private void cielo_atras(){
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,-200,-2300);
        pared_X(gll,u_MvpMatrix,3000,1000,imagen_cielo,textura_cielo_atras);
        g_modelMatrix = popMatrix();
    }

    private void cielo_derecha(){
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(-500,-200,700);
        pared_Z(gll, u_MvpMatrix, 3000, 1000, imagen_cielo, textura_cielo_derecha);
        g_modelMatrix = popMatrix();
    }

    private void cielo_izquierda(){
        pushMatrix(g_modelMatrix);
        g_modelMatrix_translate(2500,-200,700);
        pared_Z(gll, u_MvpMatrix, 3000, 1000, imagen_cielo, textura_cielo_izquierda);
        g_modelMatrix = popMatrix();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            actualizar_vista_con_matriz(rotationMatrix);

            float[] rotationMatrix = new float[16];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            Log.d("SensorData", "Rotation Matrix: " + Arrays.toString(rotationMatrix));
        }
    }

    private void actualizar_vista_con_matriz(float[] rotationMatrix) {
        // Dirección inicial hacia adelante (eje Z negativo)
        float[] initialView = {0, 1, 0, 0}; // La cámara mira hacia adelante
        float[] transformedView = new float[4];

        // Multiplicamos la matriz de rotación por el vector de vista inicial
        Matrix.multiplyMV(transformedView, 0, rotationMatrix, 0, initialView, 0);

        // Ahora asignamos los valores correctamente:
        // transformedView[0] = nuevo valor en el eje X
        // transformedView[1] = nuevo valor en el eje Y
        // transformedView[2] = nuevo valor en el eje Z// Ajusta esta sensibilidad para que la cámara suba/baje más suavemente


        // Corregimos la asignación de ViewDir según el sistema de coordenadas
        ViewDir[0] = transformedView[1];  // Eje X: dirección horizontal
        ViewDir[1] = -transformedView[0]; // Eje Y: dirección vertical
        ViewDir[2] = transformedView[2];  // Eje Z: dirección hacia adelante/atrás

        Log.d("Valores de Victa", -transformedView[0] + " .");
        // Actualizamos la vista
        actualizar_vista();
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void startSensors() {
        if (rotationVectorSensor != null) {
            sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void stopSensors() {
        sensorManager.unregisterListener(this);
    }


}