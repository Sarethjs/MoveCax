package dev.movecax.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

import dev.movecax.R;

public class FavoritoFragment extends Fragment {

    private ViewFlipper viewFlipper;
    private ImageView[] dots;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private int currentIndex = 0;

    private int[] imageResIds = {R.drawable.anun1, R.drawable.anun2, R.drawable.anun3};
    private String[] titles = {"Título 1", "Título 2", "Título 3"};
    private String[] descriptions = {"Descripción 1", "Descripción 2", "Descripción 3"};

    public FavoritoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        viewFlipper = view.findViewById(R.id.viewFlipper);

        // Agrega las tres imágenes al ViewFlipper
        for (int i = 0; i < imageResIds.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(imageResIds[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewFlipper.addView(imageView);
        }

        titleTextView = view.findViewById(R.id.titleTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);

        // Inicializa los puntos
        initializeDots(view);

        // Configura la animación del ViewFlipper
        viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
        viewFlipper.setFlipInterval(4000); // Cambia a la duración deseada en milisegundos
        viewFlipper.setAutoStart(true);

        // Inicia la rotación de imágenes
        startImageRotation();

        return view;
    }


    private void initializeDots(View view) {
        ViewGroup dotsContainer = view.findViewById(R.id.dotsContainer);

        dots = new ImageView[imageResIds.length];

        for (int i = 0; i < imageResIds.length; i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageResource(R.drawable.dot_selector);
            dots[i].setPadding(8, 0, 8, 0); // Ajusta según sea necesario
            dotsContainer.addView(dots[i]);
        }

        dots[currentIndex].setSelected(true);
    }

    private void startImageRotation() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Actualiza el índice para la siguiente imagen
                currentIndex = (currentIndex + 1) % imageResIds.length;

                // Cambia la imagen en el ViewFlipper
                viewFlipper.showNext();

                // Actualiza el título y la descripción
                titleTextView.setText(titles[currentIndex]);
                descriptionTextView.setText(descriptions[currentIndex]);

                // Actualiza el estado de los puntos
                updateDots(currentIndex);
                Log.d("DotSelector", "Selected index: " + currentIndex);

                // Programa la próxima rotación después de un tiempo
                handler.postDelayed(this, 4000); // Cambia a la duración deseada en milisegundos
            }
        }, 4000); // Inicia la rotación después de un tiempo inicial (en este caso, 1 segundo)
    }


    private void updateDots(int position) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setSelected(i == position);
        }
    }
}
