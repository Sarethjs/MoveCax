package dev.movecax;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dev.movecax.Fragment.ExplorarFragment;
import dev.movecax.Fragment.PerfilFragment;
import dev.movecax.Fragment.FavoritoFragment;
import dev.movecax.Fragment.HistorialFragment;

public class MainActivity extends AppCompatActivity {

    ExplorarFragment explorarFragment = new ExplorarFragment();
    FavoritoFragment favoritoFragment = new FavoritoFragment();
    HistorialFragment historialFragment = new HistorialFragment();
    PerfilFragment perfilFragment = new PerfilFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(explorarFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.firstFragment) {
                loadFragment(explorarFragment);
                return true;
            } else if (itemId == R.id.secondFragment) {
                loadFragment(favoritoFragment);
                return true;
            } else if (itemId == R.id.thirdFragment) {
                loadFragment(historialFragment);
                return true;
            }
            else if (itemId == R.id.fourthFragment) {
                loadFragment(perfilFragment);
                return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
}