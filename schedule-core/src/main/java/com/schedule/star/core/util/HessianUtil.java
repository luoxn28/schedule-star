package com.schedule.star.core.util;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author xiangnan
 * @date 2018/1/29 19:48
 */
public class HessianUtil {

    public static <T> byte[] serialize(T obj) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(os);
        ho.writeObject(obj);
        return os.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        HessianInput hi = new HessianInput(is);
        return (T) hi.readObject();
    }

}
