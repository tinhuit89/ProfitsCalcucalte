package vct.profitscalculate.interfaces

import java.util.*

public interface DataCallback {
    fun callback(action: String, pos: Int, objects: Any)
}