package me.metropanties.fileserver.util;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnvUtils {

    private static final Dotenv DOTENV = Dotenv.load();

    public static String get(String key) {
        return DOTENV.get(key);
    }

}
