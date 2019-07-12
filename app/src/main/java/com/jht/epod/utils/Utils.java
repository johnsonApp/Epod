/*
 * Copyright (c) 2010 - 2017, Nordic Semiconductor ASA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form, except as embedded into a Nordic
 *    Semiconductor ASA integrated circuit in a product or a software update for
 *    such product, must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. Neither the name of Nordic Semiconductor ASA nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * 4. This software, with or without modification, must only be used with a
 *    Nordic Semiconductor ASA integrated circuit.
 *
 * 5. Any software provided in binary form under this license must not be reverse
 *    engineered, decompiled, modified and/or disassembled.
 *
 * THIS SOFTWARE IS PROVIDED BY NORDIC SEMICONDUCTOR ASA "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY, NONINFRINGEMENT, AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL NORDIC SEMICONDUCTOR ASA OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jht.epod.utils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.webkit.URLUtil;
import android.widget.Toast;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class Utils {

    public static final String TAG                                                              = "Epod";

    public static final UUID EPOD_BASE_UUID                                                     = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");// service
    // UUID
    public static final UUID EPOD_READ_UUID                                                     = UUID.fromString("0000FFF4-0000-1000-8000-00805f9b34fb");// read

    public static final UUID EPOD_WRITE_UUID                                                    = UUID.fromString("0000FFF3-0000-1000-8000-00805f9b34fb");// write

    public static final String ACTION_DEVICE_CONNECTED                                          = "ACTION_DEVICE_CONNECTED_";
    public static final String ACTION_DEVICE_DISCONNECTED                                       = "ACTION_DEVICE_DISCONNECTED_";
    public static final String ACTION_SERVICE_DISCOVERY_COMPLETED                               = "ACTION_SERVICE_DISCOVERY_COMPLETED_";
    public static final String ACTION_DATA_RECEIVED                                             = "ACTION_DATA_RECEIVED_";
    public static final String CONNECTION_STATE                                                 = "READING_CONFIGURATION";


    public static final String EXTRA_DATA_SPEAKER_STATUS_NOTITIFCATION                          = "EXTRA_DATA_SPEAKER_STATUS_NOTITIFCATION";
    public static final String EXTRA_DATA_SPEAKER_MODE                                          = "EXTRA_DATA_SPEAKER_MODE";


    public static final int FORMAT_UINT24                                                       = 0x13;
    public static final int FORMAT_SINT24                                                       = 0x23;
    public static final int FORMAT_UINT16_BIG_INDIAN                                            = 0x62;
    public static final int FORMAT_UINT32_BIG_INDIAN                                            = 0x64;

    public static final int MAX_VISISBLE_GRAPH_ENTRIES                                          = 300;

    public static final SimpleDateFormat TIME_FORMAT                                            = new SimpleDateFormat("HH:mm:ss:SSS");
    public static final SimpleDateFormat TIME_FORMAT_PEDOMETER                                  = new SimpleDateFormat("mm:ss:SS");
    public static final DecimalFormat GRAVITY_VECTOR_DECIMAL_FORMAT                             = new DecimalFormat("#.##");
    public static final int CLOUD_TOKEN_LENGTH                                                  = 250;



    public static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;

    public static final String TABLE_NAME = "class";

    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String TIME = "TIME";
    public static final String CALORIE = "CALORIE";
    public static final String DEGREE = "DEGREE";
    public static final String CLASSTYPE = "CLASSTYPE";
    public static final String ICONNAME = "ICONNAME";
    public static final String EXERCISETIME = "EXERCISETIME";
    public static final String SELECTED = "SELECTED";

    public static final int CLASS_TYPE_BODY = 1;
    public static final int CLASS_TYPE_LEVEL = 2;
    public static final int CLASS_TYPE_ALL = 3;

    public static final int TYPE_CORE = 1;
    public static final int TYPE_ARM = 2;
    public static final int TYPE_HIP = 3;

    public static final int DEGREE_JUNIOR = 1;
    public static final int DEGREE_MEDIUM = 2;
    public static final int DEGREE_SENIOR = 3;

    public static final int SELECT = 1;
    public static final int UNSELECT = 0;

    /**
     * URI Scheme maps a byte code into the scheme and an optional scheme specific prefix.
     */
    private static final SparseArray<String> URI_SCHEMES = new SparseArray<String>() {
        {
            put((byte) 0, "http://www.");
            put((byte) 1, "https://www.");
            put((byte) 2, "http://");
            put((byte) 3, "https://");
            put((byte) 4, "urn:uuid:"); // RFC 2141 and RFC 4122};
        }
    };

    /**
     * Expansion strings for "http" and "https" schemes. These contain strings appearing anywhere in a
     * URL. Restricted to Generic TLDs.
     * <p/>
     * Note: this is a scheme specific encoding.
     */
    private static final SparseArray<String> URL_CODES = new SparseArray<String>() {
        {
            put((byte) 0, ".com/");
            put((byte) 1, ".org/");
            put((byte) 2, ".edu/");
            put((byte) 3, ".net/");
            put((byte) 4, ".info/");
            put((byte) 5, ".biz/");
            put((byte) 6, ".gov/");
            put((byte) 7, ".com");
            put((byte) 8, ".org");
            put((byte) 9, ".edu");
            put((byte) 10, ".net");
            put((byte) 11, ".info");
            put((byte) 12, ".biz");
            put((byte) 13, ".gov");
        }
    };

    public static IntentFilter createSpeakerStatusChangeReceiver(final String address) {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SERVICE_DISCOVERY_COMPLETED);
        intentFilter.addAction(ACTION_DEVICE_DISCONNECTED);
        intentFilter.addAction(EXTRA_DATA_SPEAKER_STATUS_NOTITIFCATION);
        return intentFilter;
    }

    public static int setValue(final byte[] dest, int offset, byte value, int formatType) {
        int intValue = value & 0xFF;
        return setValue(dest, offset, intValue, formatType);
    }

    public static int setValue(final byte[] dest, int offset, int value, int formatType) {
        int len = offset + getTypeLen(formatType);
        if (len > dest.length)
            return offset;

        switch (formatType) {
            case BluetoothGattCharacteristic.FORMAT_SINT8:
                value = intToSignedBits(value, 8);
                // Fall-through intended
            case BluetoothGattCharacteristic.FORMAT_UINT8:
                dest[offset] = (byte) (value & 0xFF);
                break;

            case BluetoothGattCharacteristic.FORMAT_SINT16:
                value = intToSignedBits(value, 16);
                // Fall-through intended
            case BluetoothGattCharacteristic.FORMAT_UINT16:
                dest[offset++] = (byte) (value & 0xFF);
                dest[offset] = (byte) ((value >> 8) & 0xFF);
                break;

            case FORMAT_SINT24:
                value = intToSignedBits(value, 24);
                // Fall-through intended
            case FORMAT_UINT24:
                dest[offset++] = (byte) (value & 0xFF);
                dest[offset++] = (byte) ((value >> 8) & 0xFF);
                dest[offset] = (byte) ((value >> 16) & 0xFF);
                break;

            case FORMAT_UINT16_BIG_INDIAN:
                dest[offset++] = (byte) ((value >> 8) & 0xFF);
                dest[offset] = (byte) (value & 0xFF);
                break;

            case BluetoothGattCharacteristic.FORMAT_SINT32:
                value = intToSignedBits(value, 32);
                // Fall-through intended
            case BluetoothGattCharacteristic.FORMAT_UINT32:
                dest[offset++] = (byte) (value & 0xFF);
                dest[offset++] = (byte) ((value >> 8) & 0xFF);
                dest[offset++] = (byte) ((value >> 16) & 0xFF);
                dest[offset] = (byte) ((value >> 24) & 0xFF);
                break;

            case FORMAT_UINT32_BIG_INDIAN:
                dest[offset++] = (byte) ((value >> 24) & 0xFF);
                dest[offset++] = (byte) ((value >> 16) & 0xFF);
                dest[offset++] = (byte) ((value >> 8) & 0xFF);
                dest[offset] = (byte) (value & 0xFF);
                break;

            default:
                return offset;
        }
        return len;
    }

    private static int getTypeLen(int formatType) {
        return formatType & 0xF;
    }

    private static int intToSignedBits(int i, int size) {
        if (i < 0) {
            i = (1 << size - 1) + (i & ((1 << size - 1) - 1));
        }
        return i;
    }

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String decodeUri(final byte[] serviceData, final int start, final int length) {
        if (start < 0 || serviceData.length < start + length)
            return null;

        final StringBuilder uriBuilder = new StringBuilder();
        int offset = 0;
        if (offset < length) {
            byte b = serviceData[start + offset++];
            String scheme = URI_SCHEMES.get(b);
            if (scheme != null) {
                uriBuilder.append(scheme);
                if (URLUtil.isNetworkUrl(scheme)) {
                    return decodeUrl(serviceData, start + offset, length - 1, uriBuilder);
                } else if ("urn:uuid:".equals(scheme)) {
                    return decodeUrnUuid(serviceData, start + offset, uriBuilder);
                }
            }
            Log.w(TAG, "decodeUri unknown Uri scheme code=" + b);
        }
        return null;
    }

    private static String decodeUrl(final byte[] serviceData, final int start, final int length, final StringBuilder urlBuilder) {
        int offset = 0;
        while (offset < length) {
            byte b = serviceData[start + offset++];
            String code = URL_CODES.get(b);
            if (code != null) {
                urlBuilder.append(code);
            } else {
                urlBuilder.append((char) b);
            }
        }
        return urlBuilder.toString();
    }

    /**
     * Creates the Uri string with embedded expansion codes.
     *
     * @param uri to be encoded
     * @return the Uri string with expansion codes.
     */
    public static byte[] encodeUri(String uri) {
        if (uri.length() == 0) {
            return new byte[0];
        }
        ByteBuffer bb = ByteBuffer.allocate(uri.length());
        // UUIDs are ordered as byte array, which means most significant first
        bb.order(ByteOrder.BIG_ENDIAN);
        int position = 0;

        // Add the byte code for the scheme or return null if none
        Byte schemeCode = encodeUriScheme(uri);
        if (schemeCode == null) {
            return null;
        }
        String scheme = URI_SCHEMES.get(schemeCode);
        bb.put(schemeCode);
        position += scheme.length();

        if (URLUtil.isNetworkUrl(scheme)) {
            return encodeUrl(uri, position, bb);
        } else if ("urn:uuid:".equals(scheme)) {
            return encodeUrnUuid(uri, position, bb);
        }
        return null;
    }

    private static Byte encodeUriScheme(String uri) {
        String lowerCaseUri = uri.toLowerCase(Locale.ENGLISH);
        for (int i = 0; i < URI_SCHEMES.size(); i++) {
            // get the key and value.
            int key = URI_SCHEMES.keyAt(i);
            String value = URI_SCHEMES.valueAt(i);
            if (lowerCaseUri.startsWith(value)) {
                return (byte) key;
            }
        }
        return null;
    }

    private static byte[] encodeUrl(String url, int position, ByteBuffer bb) {
        while (position < url.length()) {
            byte expansion = findLongestExpansion(url, position);
            if (expansion >= 0) {
                bb.put(expansion);
                position += URL_CODES.get(expansion).length();
            } else {
                bb.put((byte) url.charAt(position++));
            }
        }
        return byteBufferToArray(bb);
    }

    private static byte[] byteBufferToArray(ByteBuffer bb) {
        byte[] bytes = new byte[bb.position()];
        bb.rewind();
        bb.get(bytes, 0, bytes.length);
        return bytes;
    }

    /**
     * Finds the longest expansion from the uri at the current position.
     *
     * @param uriString the Uri
     * @param pos start position
     * @return an index in URI_MAP or 0 if none.
     */
    private static byte findLongestExpansion(String uriString, int pos) {
        byte expansion = -1;
        int expansionLength = 0;
        for (int i = 0; i < URL_CODES.size(); i++) {
            // get the key and value.
            int key = URL_CODES.keyAt(i);
            String value = URL_CODES.valueAt(i);
            if (value.length() > expansionLength && uriString.startsWith(value, pos)) {
                expansion = (byte) key;
                expansionLength = value.length();
            }
        }
        return expansion;
    }

    private static byte[] encodeUrnUuid(String urn, int position, ByteBuffer bb) {
        String uuidString = urn.substring(position, urn.length());
        UUID uuid;
        try {
            uuid = UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "encodeUrnUuid invalid urn:uuid format - " + urn);
            return null;
        }
        // UUIDs are ordered as byte array, which means most significant first
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return byteBufferToArray(bb);
    }

    private static String decodeUrnUuid(final byte[] serviceData, final int offset, final StringBuilder urnBuilder) {
        ByteBuffer bb = ByteBuffer.wrap(serviceData);
        // UUIDs are ordered as byte array, which means most significant first
        bb.order(ByteOrder.BIG_ENDIAN);
        long mostSignificantBytes, leastSignificantBytes;
        try {
            bb.position(offset);
            mostSignificantBytes = bb.getLong();
            leastSignificantBytes = bb.getLong();
        } catch (BufferUnderflowException e) {
            Log.w(TAG, "decodeUrnUuid BufferUnderflowException!");
            return null;
        }
        UUID uuid = new UUID(mostSignificantBytes, leastSignificantBytes);
        urnBuilder.append(uuid.toString());
        return urnBuilder.toString();
    }

    /**
     * Convert a signed byte to an unsigned int.
     */
    private static int unsignedByteToInt(byte b) {
        return b & 0xFF;
    }

    /**
     * Convert signed bytes to a 16-bit unsigned int.
     */
    private static int unsignedBytesToInt(byte b0, byte b1) {
        return (unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8));
    }

    /**
     * Convert an unsigned integer value to a two's-complement encoded signed value.
     */

    private static int unsignedToSigned(int unsigned, int size) {
        if ((unsigned & (1 << size - 1)) != 0) {
            unsigned = -1 * ((1 << size - 1) - (unsigned & ((1 << size - 1) - 1)));
        }
        return unsigned;
    }

    public static byte[] base64Decode(String s) {
        return Base64.decode(s, Base64.DEFAULT);
    }

    public static boolean checkIfVersionIsLollipopOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    public static boolean checkIfVersionIsOreoOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean validateSlaveLatency(final int slaveLatency, final int maxConIntervalUnits, final int supervisionTimeoutUnits){
        final double maxConInterval = maxConIntervalUnits;
        final int superVisionTimeout = supervisionTimeoutUnits;
        if(slaveLatency < (((superVisionTimeout * 4) / maxConInterval) - 1)) {
            return true;
        }
        return false;
    }

    public static boolean validateSupervisionTimeout(final int slaveLatency, final int maxConIntervalUnits, final int supervisionTimeoutUnits){
        final double maxConInterval = maxConIntervalUnits;
        final int superVisionTimeout = supervisionTimeoutUnits;
        if(superVisionTimeout > (((1 + slaveLatency) * maxConInterval) / 4)) {
            return true;
        }
        return false;
    }

    public static boolean validateMaxConnectionInterval(final int slaveLatency, final int maxConIntervalUnits, final int supervisionTimeoutUnits){
        final double maxConInterval = maxConIntervalUnits;
        final int superVisionTimeout = supervisionTimeoutUnits;
        if(maxConInterval < ((superVisionTimeout * 4) / (1 + slaveLatency))) {
            return true;
        }
        return false;
    }

    public static void removeOldDataForGraphs(final LinkedHashMap linkedHashMap) {
        if(linkedHashMap.size() > MAX_VISISBLE_GRAPH_ENTRIES) {
            Set keys = linkedHashMap.keySet();
            for(Object key : keys) {
                linkedHashMap.remove(key);
                if(linkedHashMap.size()  == MAX_VISISBLE_GRAPH_ENTRIES){
                    break;
                }
            }
        }
    }

    public static boolean handleVersionPermission(Context context) {
        if(context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= 23) {
                Log.i(TAG, "onCreate: checkSelfPermission");

                int checkSelfPermission = ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION);
                Log.i(TAG, "handleVersionPermission: checkSelfPermission = " + checkSelfPermission);
                if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onCreate: Android 6.0 动态申请权限");

                    boolean showRequestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                            Manifest.permission.ACCESS_COARSE_LOCATION);
                    Log.i(TAG, "handleVersionPermission: showRequestPermissionRationale = " + showRequestPermissionRationale);
                    if (showRequestPermissionRationale) {
                        Log.i(TAG, "*********onCreate: shouldShowRequestPermissionRationale**********");
                        Toast.makeText(context, "只有允许访问位置才能搜索到蓝牙设备", Toast.LENGTH_SHORT).show();
                    }
                    ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_CODE_ACCESS_COARSE_LOCATION);
                    return false;
                } else {
                    Log.i(TAG, "handleVersionPermission: scanning...");
                    //showDialog(getResources().getString(R.string.scanning));
                    return true;
                    //mBleService.scanLeDevice(true);
                }
            } else {
                Log.i(TAG, "handleVersionPermission: scanning...");
                //showDialog(getResources().getString(R.string.scanning));
                return true;
                //mBleService.scanLeDevice(true);
            }
        }
        return  false;
    }
}
