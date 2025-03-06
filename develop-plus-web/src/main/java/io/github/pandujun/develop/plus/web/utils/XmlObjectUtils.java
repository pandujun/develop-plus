package io.github.pandujun.develop.plus.web.utils;

import com.thoughtworks.xstream.XStream;

/**
 * xml to object OR object to xml
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2024/8/21
 */
public class XmlObjectUtils {

    /**
     * xml to object
     *
     * @param clazz object 类
     * @param xml   xml 字符串
     * @param alias 对象别名
     * @return object
     */
    public static <T> T xmlToObject(Class<T> clazz, String xml, String alias) {
        XStream xStream = new XStream();
        xStream.allowTypes(new Class[]{clazz});
        xStream.alias(alias, clazz);
        xStream.autodetectAnnotations(true);
        xStream.ignoreUnknownElements();

        return (T) xStream.fromXML(xml);
    }

    /**
     * object to xml
     *
     * @param object 用于生成xml的对象
     * @param alias  根别名
     * @return xml字符串
     */
    public static <T> String ObjectToXml(T object, String alias) {
        XStream xStream = new XStream();
        xStream.alias(alias, object.getClass());
        xStream.autodetectAnnotations(true);
        return xStream.toXML(object);
    }
}
