package textdes.ephraim.com.textdes;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Ephraim on 9/23/2021.
 */

public class TrippleDES {
    byte[] secretKey;
    SecretKeySpec secretKeySpec;
    byte[] iv;
    IvParameterSpec ivSpec;

    TrippleDES (){
        secretKey = "9mng65v8jf4lxn93nabf981m".getBytes();
        secretKeySpec = new SecretKeySpec(secretKey, "TripleDES");
        iv = "a76nb5h9".getBytes();
        ivSpec = new IvParameterSpec(iv);
    }

    public byte[] encryptData(String data) throws InvalidAlgorithmParameterException, InvalidKeyException,
            NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        String secretMessage = data;

        Cipher encryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

        byte[] secretMessagesBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessagesBytes);

        //String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);

        //return encodedMessage;
        return encryptedMessageBytes;
    }

    public String decryptData(byte[] data) throws InvalidAlgorithmParameterException,
            InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher decryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        byte[] decryptedMessageBytes = decryptCipher.doFinal(data);

        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);

        return decryptedMessage;
    }
}
