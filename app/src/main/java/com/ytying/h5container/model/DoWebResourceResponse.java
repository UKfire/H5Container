package com.ytying.h5container.model;

import java.io.InputStream;

/**
 * 自己构造的WebResource资源类,可与WebResourceResponse互相转换
 * Created by kefan.wkf on 16/7/11.
 */
public class DoWebResourceResponse {
    // Accessed by jni, do not rename without modifying the jni code.
    private String mMimeType;
    private String mEncoding;
    private InputStream mInputStream;

    /**
     * Constructs a resource response with the given MIME type, encoding, and
     * input stream. Callers must implement
     * {@link InputStream#read(byte[]) InputStream.read(byte[])} for the input
     * stream.
     *
     * @param mimeType the resource response's MIME type, for example text/html
     * @param encoding the resource response's encoding
     * @param data the input stream that provides the resource response's data
     */
    public DoWebResourceResponse(String mimeType, String encoding, InputStream data) {
        mMimeType = mimeType;
        mEncoding = encoding;
        mInputStream = data;
    }

    /**
     * Sets the resource response's MIME type, for example text/html.
     *
     * @param mimeType the resource response's MIME type
     */
    public void setMimeType(String mimeType) {
        mMimeType = mimeType;
    }

    /**
     * Gets the resource response's MIME type.
     *
     * @return the resource response's MIME type
     */
    public String getMimeType() {
        return mMimeType;
    }

    /**
     * Sets the resource response's encoding, for example UTF-8. This is used
     * to decode the data from the input stream.
     *
     * @param encoding the resource response's encoding
     */
    public void setEncoding(String encoding) {
        mEncoding = encoding;
    }

    /**
     * Gets the resource response's encoding.
     *
     * @return the resource response's encoding
     */
    public String getEncoding() {
        return mEncoding;
    }

    /**
     * Sets the input stream that provides the resource respone's data. Callers
     * must implement {@link InputStream#read(byte[]) InputStream.read(byte[])}.
     *
     * @param data the input stream that provides the resource response's data
     */
    public void setData(InputStream data) {
        mInputStream = data;
    }

    /**
     * Gets the input stream that provides the resource respone's data.
     *
     * @return the input stream that provides the resource response's data
     */
    public InputStream getData() {
        return mInputStream;
    }
}
