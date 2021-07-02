package com.example.whereareu.helpers

import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class JSONHelper {
    companion object {

        fun getJsonObjFromString(string: String): JSONObject {
            return JSONObject(string)
        }

        fun getJsonArrayromString(string: String): JSONArray {
            try {
                return JSONArray(string)
            } catch (ioException: IOException) {
                return JSONArray("[]")
            }
        }

        @JvmStatic
        fun getFieldSafely(obj: JSONObject?, fieldName: String): String {
            if (obj == null) return ""
            return if (obj.has(fieldName)) obj.getString(fieldName) else "";
        }

    }
}