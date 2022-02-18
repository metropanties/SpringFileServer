package me.metropanties.fileserver.security;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.experimental.UtilityClass;
import me.metropanties.fileserver.util.EnvUtils;

@UtilityClass
public class AlgorithmUtil {

    public static Algorithm getHMAC256() {
        return Algorithm.HMAC256(EnvUtils.get("SECRET").getBytes());
    }

}
