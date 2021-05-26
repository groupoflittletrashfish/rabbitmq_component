package com.noname.common.serializer;

/**
 * 自定义序列化接口，即对象的序列化与反序列化
 */
public interface Serializer {

    /**
     * 将对象转换成为一个字节数组
     *
     * @param data
     * @return
     */
    byte[] serializeRaw(Object data);

    String serialize(Object data);

    <T> T deserialize(String conent);

    <T> T deserialize(byte[] conent);
}
