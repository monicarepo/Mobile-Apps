package com.ms.trackify.authentication


fun main() {

}

fun binarySearch(array: IntArray, key: Int): Int {
    var low = 0
    var high = array.size - 1
    while (low <= high) {
        var mid = low - (high - low) / 2
        if (key < array[mid]) {
            high = mid - 1
        } else if ( key > array[mid]) {
            low = mid + 1
        } else {
            return mid
        }
    }
    return -1
}

// Selection Sort
// Insertion Sort
// Shell Sort
// Shuffling, Convex hull

// Merge Sort
// Bottom up merge sort
