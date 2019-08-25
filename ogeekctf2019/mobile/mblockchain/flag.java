import java.security.MessageDigest;
import java.io.OutputStream;
import javax.crypto.CipherOutputStream;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

class flag{
    public static void main(String[] args) {
        try{
            byte[][] hash_array = new byte[10][];
            byte[] key = new byte[3];
            for(int i=0; i < 256; i++)
            {
                key[0] = (byte)i;
                System.err.println(i);
                for(int ii =0;ii < 256; ii++)
                {
                    key[1] = (byte)ii;

                    for(int iii=0; iii< 256;iii++)
                    {
                        key[2] = (byte)iii;
                        hash_array[0] = hash(key);
                        byte[] answer = new byte[] {(byte)0x74,(byte)0xf0,(byte)0xb1,(byte)0x65,(byte)0xdb,(byte)0x8a,(byte)0x62,(byte)0x87,(byte)0x16,(byte)0xb5,(byte)0x3a,(byte)0x9d,(byte)0x4f,(byte)0x64,(byte)0x05,(byte)0x98,(byte)0x0d,(byte)0xb2,(byte)0xf8,(byte)0x33,(byte)0xaf,(byte)0xa1,(byte)0xed,(byte)0x5e,(byte)0xeb,(byte)0x43,(byte)0x04,(byte)0xc5,(byte)0x22,(byte)0x0b,(byte)0xdc,(byte)0x0b,(byte)0x54,(byte)0x1f,(byte)0x85,(byte)0x7a,(byte)0x73,(byte)0x48,(byte)0x07,(byte)0x4b,(byte)0x2a,(byte)0x77,(byte)0x75,(byte)0xd6,(byte)0x91,(byte)0xe7,(byte)0x1b,(byte)0x49,(byte)0x04,(byte)0x02,(byte)0x62,(byte)0x1e,(byte)0x8a,(byte)0x53,(byte)0xba,(byte)0xd4,(byte)0xcf,(byte)0x7a,(byte)0xd4,(byte)0xfc,(byte)0xc1,(byte)0x5f,(byte)0x20,(byte)0xa8,(byte)0x06,(byte)0x6e,(byte)0x08,(byte)0x7f,(byte)0xc1,(byte)0xb2,(byte)0xff,(byte)0xb2,(byte)0x1c,(byte)0x27,(byte)0x46,(byte)0x3b,(byte)0x57,(byte)0x37,(byte)0xe3,(byte)0x47,(byte)0x38,(byte)0xa6,(byte)0x24,(byte)0x4e,(byte)0x16,(byte)0x30,(byte)0xd8,(byte)0xfa,(byte)0x1b,(byte)0xf4,(byte)0xf3,(byte)0x8b,(byte)0x7e,(byte)0x71,(byte)0xd7,(byte)0x07,(byte)0x42,(byte)0x5c,(byte)0x82,(byte)0x25,(byte)0xf2,(byte)0x40,(byte)0xf4,(byte)0xbd,(byte)0x2b,(byte)0x03,(byte)0xd6,(byte)0xc2,(byte)0x47,(byte)0x1e,(byte)0x90,(byte)0x0b,(byte)0x75,(byte)0x15,(byte)0x4e,(byte)0xb6,(byte)0xf9,(byte)0xdf,(byte)0xbd,(byte)0xf5,(byte)0xa4,(byte)0xec,(byte)0xa9,(byte)0xde,(byte)0x51,(byte)0x63,(byte)0xf9,(byte)0xb3,(byte)0xee,(byte)0x82,(byte)0x95,(byte)0x9f,(byte)0x16,(byte)0x69,(byte)0x24,(byte)0xe8,(byte)0xad,(byte)0x5f,(byte)0x1d,(byte)0x74,(byte)0x4c,(byte)0x51,(byte)0x41,(byte)0x6a,(byte)0x1d,(byte)0xb8,(byte)0x96,(byte)0x38,(byte)0xbb,(byte)0x4d,(byte)0x14,(byte)0x11,(byte)0xaa,(byte)0x1b,(byte)0x13,(byte)0x07,(byte)0xd8,(byte)0x8c,(byte)0x1f,(byte)0xb5};
                        for(int j=0; j < 9; j++)
                        {
                            hash_array[j + 1] = hash(hash_array[j]);
                        }


                        for(int j=9; j>=0; j--)
                        {
                            answer = decrypt(answer, hash_array[j]);
                        }

                        if(answer.length > 6){
                            System.out.println(new String(answer));
                        }
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }

    public static byte[] encrypt(final byte[] array, final byte[] array2) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(array2, "AES");
        final Cipher instance = Cipher.getInstance("AES/ECB/PKCS5Padding");
        instance.init(1, secretKeySpec);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, instance);
        cipherOutputStream.write(array);
        cipherOutputStream.flush();
        cipherOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] decrypt(final byte[] array, final byte[] array2) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(array2, "AES");
        final Cipher instance = Cipher.getInstance("AES/ECB/PKCS5Padding");
        instance.init(2, secretKeySpec);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, instance);
        cipherOutputStream.write(array);
        cipherOutputStream.flush();
        cipherOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
    
    public static byte[] hash(final byte[] array) throws Exception {
        final MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.update(array);
        return instance.digest();
    }
    
    public static String toHex(final byte[] array) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            final String hexString = Integer.toHexString(array[i] & 0xFF);
            if (hexString.length() == 1) {
                sb.append('0');
            }
            sb.append(hexString);
        }
        return sb.toString();
    }
}