package dev.movecax.models.helpers;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;


public class ErrorFormatter {

    private static final String UNKNOWN_ERROR = "Error desconocido";
    private static  final String FORMAT_ERROR = "Error al formatear mensaje de error";

    public static String parseError(ResponseBody responseBody){

        if (responseBody != null) {

            try {
                String errorMessage = responseBody.string();
                Log.d("error_parser", "parseError: " + errorMessage);
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(errorMessage).getAsJsonObject();

                if (jsonObject.has("error")){
                    return "ERROR: " + jsonObject.get("error").getAsString();
                }

                throw new IOException("Error has not a description");

            } catch (IOException ioException){
                return FORMAT_ERROR;
            }
        }
        return UNKNOWN_ERROR;
    }
}
